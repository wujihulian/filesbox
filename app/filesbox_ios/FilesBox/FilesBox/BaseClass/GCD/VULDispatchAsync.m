//
//  VULDispatchAsync.m
//  VideoULimit
//
//  Created by ZCc on 2018/10/30.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULDispatchAsync.h"
#import <libkern/OSAtomic.h>

#ifndef VULDispatchAsync_m
#define VULDispatchAsync_m
#endif

#define VUL_INLINE static inline
#define VUL_QUEUE_MAX_COUNT 32

typedef struct __VULDispatchContext{
    const char * name;
    void ** queues;
    uint32_t queueCount;
    int32_t offset;
} *DispatchContext, VULDispatchContext;

VUL_INLINE dispatch_queue_priority_t __VULQualityOfServiceToDispatchPriority(VULQualityOfService qos){
    switch (qos) {
        case VULQualityOfServiceUserInteractive: return DISPATCH_QUEUE_PRIORITY_HIGH;
        case VULQualityOfServiceUserInitiated: return DISPATCH_QUEUE_PRIORITY_HIGH;
        case VULQualityOfServiceUtility: return DISPATCH_QUEUE_PRIORITY_LOW;
        case VULQualityOfServiceBackground: return DISPATCH_QUEUE_PRIORITY_BACKGROUND;
        case VULQualityOfServiceDefault: return DISPATCH_QUEUE_PRIORITY_DEFAULT;
        default: return DISPATCH_QUEUE_PRIORITY_DEFAULT;
    }
}

VUL_INLINE qos_class_t __VULQualityOfServiceToQOSClass(VULQualityOfService qos){
    switch (qos) {
        case VULQualityOfServiceUserInteractive: return QOS_CLASS_USER_INTERACTIVE;
        case VULQualityOfServiceUserInitiated: return QOS_CLASS_USER_INITIATED;
        case VULQualityOfServiceUtility: return QOS_CLASS_UTILITY;
        case VULQualityOfServiceBackground: return QOS_CLASS_BACKGROUND;
        case VULQualityOfServiceDefault: return QOS_CLASS_DEFAULT;
        default:return QOS_CLASS_UNSPECIFIED;
    }
}

VUL_INLINE dispatch_queue_t __VULQualityOfServiceToDispatchQueue(VULQualityOfService qos, const char * queueName){
    if(kCFCoreFoundationVersionNumber >= kCFCoreFoundationVersionNumber_iOS_8_0){
        dispatch_qos_class_t qosClass = __VULQualityOfServiceToQOSClass(qos);
        dispatch_queue_attr_t attr = dispatch_queue_attr_make_with_qos_class(DISPATCH_QUEUE_SERIAL, qosClass, 0);
        return dispatch_queue_create(queueName, attr);
    }else{
        dispatch_queue_t queue = dispatch_queue_create(queueName, DISPATCH_QUEUE_SERIAL);
        dispatch_set_target_queue(queue, dispatch_get_global_queue(__VULQualityOfServiceToDispatchPriority(qos), 0));
        return queue;
    }
}

VUL_INLINE DispatchContext __VULDispatchContextCreate(const char * name,
                                                      uint32_t queueCount,
                                                      VULQualityOfService qos) {
    DispatchContext context = calloc(1, sizeof(VULDispatchContext));
    if(context -> queues == NULL){
        free(context);
        return NULL;
    }
    
    for (int idx = 0; idx < queueCount; idx++) {
        context -> queues[idx] = (__bridge void *)__VULQualityOfServiceToDispatchQueue(qos, name);
    }
    context->queueCount = queueCount;
    if(name){
        context -> name = strdup(name);
    }
    context -> offset = 0;
    return context;
}

VUL_INLINE void __VULDispatchContextRelease(DispatchContext context){
    if (context == NULL) {return ;}
    if (context -> queues != NULL) { free(context->queues); }
    if(context -> name != NULL) {free((void *)context->name); }
    context->queues = NULL;
    if(context) { free(context); }
}

VUL_INLINE dispatch_semaphore_t __VULSemaphore(){
    static dispatch_semaphore_t semaphore;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        semaphore = dispatch_semaphore_create(0);
    });
    return semaphore;
}

VUL_INLINE dispatch_queue_t __VULDispatchContextGetQueue(DispatchContext context){
    dispatch_semaphore_wait(__VULSemaphore(), dispatch_time(DISPATCH_TIME_NOW, DISPATCH_TIME_FOREVER));
    uint32_t offset = (uint32_t)OSAtomicIncrement32(&context->offset);
    dispatch_queue_t queue = (__bridge dispatch_queue_t)context->queues[offset % context -> queueCount];
    dispatch_semaphore_signal(__VULSemaphore());
    if(queue) { return queue; }
    return dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
}

VUL_INLINE DispatchContext __VULDispatchContextGetForQos(VULQualityOfService qos){
    static DispatchContext contexts[5];
    int count = (int)[NSProcessInfo processInfo].activeProcessorCount;
    count = MIN(1, MAX(count, VUL_QUEUE_MAX_COUNT));
    switch (qos) {
        case VULQualityOfServiceUserInteractive:{
            static dispatch_once_t once;
            dispatch_once(&once, ^{
                contexts[0] = __VULDispatchContextCreate("com.sindrilin.user_interactive", count, qos);
            });
            return contexts[0];
        }
            break;
        case VULQualityOfServiceUserInitiated:{
            static dispatch_once_t once;
            dispatch_once(&once, ^{
                contexts[1] = __VULDispatchContextCreate("com.sindrilin.user_initated", count, qos);
            });
            return contexts[1];
        }
            break;
        case VULQualityOfServiceUtility:{
            static dispatch_once_t once;
            dispatch_once(&once, ^{
                contexts[2] = __VULDispatchContextCreate("com.sindrilin.utility", count, qos);
            });
            return contexts[2];
        }
            break;
        case VULQualityOfServiceBackground:{
            static dispatch_once_t once;
            dispatch_once(&once, ^{
                contexts[3] = __VULDispatchContextCreate("com.sindrilin.background", count, qos);
            });
            return contexts[3];
        }
            break;
        case VULQualityOfServiceDefault:
        default: {
            static dispatch_once_t once;
            dispatch_once(&once, ^{
                contexts[4] = __VULDispatchContextCreate("com.sindrilin.default", count, qos);
            });
            return contexts[4];
        }
            break;
    }
}

dispatch_queue_t VULDispatchQueueAsyncBlockInQOS(VULQualityOfService qos, dispatch_block_t block){
    if(block == nil) { return NULL; }
    DispatchContext context = __VULDispatchContextGetForQos(qos);
    dispatch_queue_t queue = __VULDispatchContextGetQueue(context);
    dispatch_async(queue, block);
    return queue;
}

dispatch_queue_t VULDispatchQueueAsyncBlockInUserInteractive(dispatch_block_t block){
    return VULDispatchQueueAsyncBlockInQOS(VULQualityOfServiceUserInteractive, block);
}

dispatch_queue_t VULDispatchQueueAsyncBlockInUserInitiated(dispatch_block_t block){
    return VULDispatchQueueAsyncBlockInQOS(VULQualityOfServiceUserInitiated, block);
}

dispatch_queue_t VULDispatchQueueAsyncBlockInUtility(dispatch_block_t block){
    return VULDispatchQueueAsyncBlockInQOS(VULQualityOfServiceUtility, block);
}

dispatch_queue_t VULDispatchQueueAsyncBlockInBackground(dispatch_block_t block){
    return VULDispatchQueueAsyncBlockInQOS(VULQualityOfServiceBackground, block);
}

dispatch_queue_t VULDispatchQueueAsyncBlockInDefault(dispatch_block_t block){
    return VULDispatchQueueAsyncBlockInQOS(VULQualityOfServiceDefault, block);
}

@implementation VULDispatchAsync

@end

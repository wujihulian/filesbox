package com.svnlan.exception;

import com.svnlan.common.I18nUtils;

/**
 * @Author:
 * @Description: 自定义运行时异常，抛出不符合业务需求的结果异常
 * @Modified:
 */
public class SvnlanRuntimeException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * 错误编码
         */
        private String errorCode;

        /**
         * 消息是否为属性文件中的Key
         */
        private boolean propertiesKey = true;

        /**
         * 构造一个基本异常.
         *
         * @param i18eCode 信息描述
         */
        public SvnlanRuntimeException(String i18eCode) {
            // 根据资源文件的属性名以及当前语言环境，获取国际化信息
            super(I18nUtils.tryI18n(i18eCode));
            this.setErrorCode(i18eCode);
        }

        public SvnlanRuntimeException(String i18eCode, Object... args) {
            // 根据资源文件的属性名，属性值中的参数以及当前语言环境，获取国际化信息
            // args用来替换资源文件属性值中的占位符参数
            super(I18nUtils.tryI18n(i18eCode, args));
            this.setErrorCode(i18eCode);
        }
        /**
         * 构造一个基本异常.
         *
         * @param errorCode 错误编码
         * @param message 信息描述
         */
       /* public SvnlanRuntimeException(String errorCode, String message)
        {
            this(errorCode, message, true);
        }*/

        /**
         * 构造一个基本异常.
         *
         * @param errorCode 错误编码
         * @param message 信息描述
         */
        public SvnlanRuntimeException(String errorCode, String message, Throwable cause)
        {
            this(errorCode, message, cause, true);
        }

        /**
         * 构造一个基本异常.
         *
         * @param errorCode 错误编码
         * @param message 信息描述
         * @param propertiesKey 消息是否为属性文件中的Key
         */
        public SvnlanRuntimeException(String errorCode, String message, boolean propertiesKey)
        {
            super(message);
            this.setErrorCode(errorCode);
            this.setPropertiesKey(propertiesKey);
        }

        /**
         * 构造一个基本异常.
         *
         * @param errorCode 错误编码
         * @param message 信息描述
         */
        public SvnlanRuntimeException(String errorCode, String message, Throwable cause, boolean propertiesKey)
        {
            super(message, cause);
            this.setErrorCode(errorCode);
            this.setPropertiesKey(propertiesKey);
        }

        /**
         * 构造一个基本异常.
         *
         * @param message 信息描述
         * @param cause 根异常类（可以存入任何异常）
         */
        public SvnlanRuntimeException(String message, Throwable cause)
        {
            super(message, cause);
        }

    public SvnlanRuntimeException(CodeMessageEnum codeMessageEnum) {
        this(codeMessageEnum.getCode(), codeMessageEnum.getMsg());
    }

    public String getErrorCode()
        {
            return errorCode;
        }

        public void setErrorCode(String errorCode)
        {
            this.errorCode = errorCode;
        }

        public boolean isPropertiesKey()
        {
            return propertiesKey;
        }

        public void setPropertiesKey(boolean propertiesKey)
        {
            this.propertiesKey = propertiesKey;
        }
}

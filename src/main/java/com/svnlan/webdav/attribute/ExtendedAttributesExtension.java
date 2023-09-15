package com.svnlan.webdav.attribute;

import com.ithit.webdav.server.exceptions.ServerException;
import com.svnlan.webdav.impl.WebDavEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Helper extension methods for custom attributes.
 */
@Slf4j
public final class ExtendedAttributesExtension {

    private final RedisTemplate<String, Object> redisTemplate;

    public static final String EXTENDED_PREFIX = "ExtAttr:";

    public ExtendedAttributesExtension(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String generateKey(String path, Long userId) {
        if (Objects.isNull(userId)) {
            userId = WebDavEngine.userVoThreadLocal.get().getUserID();
        }
        return String.format("%s%d:%s", EXTENDED_PREFIX, userId, path);
    }

    private String generateKey(String path) {
        return generateKey(path, null);
    }

    /**
     * Reads extended attribute.
     *
     * @param path       File or folder path to read extended attribute.
     * @param attribName Attribute name.
     * @return Attribute value or null if attribute doesn't exist.
     * @throws ServerException If file is not available or read attribute was unsuccessful.
     */
    public String getExtendedAttribute(String path, String attribName) throws ServerException {
        return (String) redisTemplate.opsForHash().get(generateKey(path), attribName);
    }

    /**
     * Write the extended attribute to the file.
     *
     * @param path        File or folder path to write attribute.
     * @param attribName  Attribute name.
     * @param attribValue Attribute value.
     * @throws ServerException If file is not available or write attribute was unsuccessful.
     */
    public void setExtendedAttribute(String path, String attribName, String attribValue) throws ServerException {
        String key = generateKey(path);
        redisTemplate.opsForHash().put(key, attribName, attribValue);
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
    }

    /**
     * Checks extended attribute existence.
     *
     * @param path       File or folder path to read extended attribute.
     * @param attribName Attribute name.
     * @return True if attribute exist, false otherwise.
     * @throws ServerException If file is not available or read attribute was unsuccessful.
     */
    public boolean hasExtendedAttribute(String path, String attribName) throws ServerException {
        return redisTemplate.opsForHash().hasKey(generateKey(path), attribName);
    }

    /**
     * Deletes extended attribute.
     *
     * @param path       File or folder path to delete extended attributes.
     * @param attribName Attribute name.
     * @throws ServerException If file is not available or delete attribute was unsuccessful.
     */
    public void deleteExtendedAttribute(String path, String attribName) throws ServerException {
        redisTemplate.opsForHash().delete(generateKey(path), attribName);
    }

    /**
     * Determines whether extended attributes are supported.
     *
     * @param path File or folder path to check extended attributes support.
     * @return True if extended attributes or NTFS file alternative streams are supported, false otherwise.
     */
    public static boolean isExtendedAttributesSupported(String path) {
//        return getExtendedAttributeSupport().isExtendedAttributeSupported(path);
        return true;
    }

    private static ExtendedAttribute getExtendedAttributeSupport() {
        return ExtendedAttributeFactory.buildFileExtendedAttributeSupport();
    }

}

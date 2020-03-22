package com.taskagile.domain.model.common.security;

public interface PasswordEncryptor {
    /**
     * Encrypt a raw password
     */
    String encrypt(String rawPassword);
}

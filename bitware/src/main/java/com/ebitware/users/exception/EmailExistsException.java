package com.ebitware.users.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email) {
        super("Ya existe un usuario con el email: " + email);
    }
}

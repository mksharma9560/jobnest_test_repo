package com.jobnest.searchms.exceptions;

import lombok.Data;

@Data
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }
}
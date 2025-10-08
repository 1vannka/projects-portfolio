package com.project3.exceptions;

public class CatNotFoundException extends RuntimeException {
    public CatNotFoundException(Long id) {
        super("Cat not found with id: " + id);
    }
}

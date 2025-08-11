package br.com.styli.domain.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}

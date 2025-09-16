package com.fastcampus.ecommerce.common.errors;


//menghandler error terkait data salah yang dikirimkan client

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}

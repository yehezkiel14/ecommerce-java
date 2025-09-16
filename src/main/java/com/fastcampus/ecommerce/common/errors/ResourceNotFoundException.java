package com.fastcampus.ecommerce.common.errors;


//kalau data tidak ditemukan akan dilempar kesini
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}

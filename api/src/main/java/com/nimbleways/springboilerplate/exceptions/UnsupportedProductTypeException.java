package com.nimbleways.springboilerplate.exceptions;

public class UnsupportedProductTypeException extends RuntimeException{

    public UnsupportedProductTypeException(String productType) {
        super("Product type "+ productType +" not supported!");
    }
}

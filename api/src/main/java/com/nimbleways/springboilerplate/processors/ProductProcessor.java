package com.nimbleways.springboilerplate.processors;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductProcessor {
    boolean supports(Product product);
    void process(Product product);
}

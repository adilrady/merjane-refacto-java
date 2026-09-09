package com.nimbleways.springboilerplate.processors.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.processors.ProductProcessor;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NormalProductService;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import org.springframework.stereotype.Component;

@Component
public class NormalProductProcessor implements ProductProcessor {

    private final ProductRepository productRepository;
    private final NormalProductService normalProductService;

    public NormalProductProcessor(ProductRepository productRepository,
                                  NormalProductService normalProductService) {
        this.productRepository = productRepository;
        this.normalProductService = normalProductService;
    }

    @Override
    public boolean supports(Product product) {
        return "NORMAL".equals(product.getType());
    }

    @Override
    public void process(Product product) {
        if (product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else if (product.getLeadTime() > 0) {
            normalProductService.notifyDelay(product.getLeadTime(), product);
        }
    }
}
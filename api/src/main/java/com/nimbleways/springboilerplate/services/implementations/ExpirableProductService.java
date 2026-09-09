package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpirableProductService {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public ExpirableProductService(ProductRepository productRepository,
                                   NotificationService notificationService) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    public void handleExpiredProduct(Product product) {
        notificationService.sendExpirationNotification(
                product.getName(),
                product.getExpiryDate()
        );
        product.setAvailable(0);
        productRepository.save(product);
    }
}

package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SeasonalProductService {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;
    private final NormalProductService normalProductService;

    public SeasonalProductService(ProductRepository productRepository,
                                  NotificationService notificationService,
                                  NormalProductService normalProductService) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.normalProductService = normalProductService;
    }

    public void handleSeasonalProduct(Product product) {
        LocalDate now = LocalDate.now();

        if (isLeadTimeExceedsSeasonEnd(product, now)) {
            notificationService.sendOutOfStockNotification(product.getName());
            product.setAvailable(0);
            productRepository.save(product);

        } else if (isBeforeSeasonStart(product, now)) {
            notificationService.sendOutOfStockNotification(product.getName());
            productRepository.save(product);

        } else {
            normalProductService.notifyDelay(product.getLeadTime(), product);
        }
    }

    private boolean isLeadTimeExceedsSeasonEnd(Product product, LocalDate now) {
        return now.plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate());
    }

    private boolean isBeforeSeasonStart(Product product, LocalDate now) {
        return product.getSeasonStartDate().isAfter(now);
    }
}

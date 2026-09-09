package com.nimbleways.springboilerplate.processors.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.processors.ProductProcessor;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import com.nimbleways.springboilerplate.services.implementations.SeasonalProductService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SeasonalProductProcessor implements ProductProcessor {

    private final ProductRepository productRepository;
    private final SeasonalProductService seasonalProductService;

    public SeasonalProductProcessor(ProductRepository productRepository,
                                    SeasonalProductService seasonalProductService) {
        this.productRepository = productRepository;
        this.seasonalProductService = seasonalProductService;
    }

    @Override
    public boolean supports(Product product) {
        return "SEASONAL".equals(product.getType());
    }

    @Override
    public void process(Product product) {
        LocalDate now = LocalDate.now();
        boolean inSeason = now.isAfter(product.getSeasonStartDate())
                && now.isBefore(product.getSeasonEndDate());

        if (inSeason && product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
        } else {
            seasonalProductService.handleSeasonalProduct(product);
        }
    }
}

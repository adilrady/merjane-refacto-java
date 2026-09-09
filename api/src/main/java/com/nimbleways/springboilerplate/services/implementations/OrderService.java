package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.exceptions.UnsupportedProductTypeException;
import com.nimbleways.springboilerplate.processors.ProductProcessor;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final List<ProductProcessor> processors;

    // Spring injects all ProductProcessor implementations automatically
    public OrderService(OrderRepository orderRepository,
                                  List<ProductProcessor> processors) {
        this.orderRepository = orderRepository;
        this.processors = processors;
    }

    public ProcessOrderResponse processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.getItems().forEach(this::processProduct);

        return new ProcessOrderResponse(order.getId());
    }

    private void processProduct(Product product) {
        processors.stream()
                .filter(p -> p.supports(product))
                .findFirst()
                .ifPresentOrElse(
                        p -> p.process(product),
                        () -> { throw new UnsupportedProductTypeException(product.getType()); }
                );
    }
}

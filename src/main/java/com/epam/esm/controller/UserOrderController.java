package com.epam.esm.controller;

import com.epam.esm.model.DTO.UserWithMaxTotalCostDTO;
import com.epam.esm.model.DTO.user_order.CreateUserOrderRequest;
import com.epam.esm.model.DTO.user_order.UserOrderDTO;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.mapper.OrderMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:4200"})
public class UserOrderController {

    private final UserOrderService service;
    private final OrderMapper mapper;

    @PostMapping()
    public UserOrderDTO create(
            @Valid @RequestBody CreateUserOrderRequest request) {
        log.info("Creating a new order.");
        return mapper.toDTO(service.create(request));
    }

    @GetMapping()
    public Page<UserOrderDTO> findAll(Pageable pageable) {
        log.info("Getting all orders.");
        return service.findAll(pageable).map(mapper::toDTO);
    }

    @GetMapping("/user")
    public Page<UserOrderDTO> findByUser(
            Pageable pageable,
            @Valid @Param("user") String user) {
        log.info("Getting all orders by user: {}.", user);
        return service.findByUser(user, pageable).map(mapper::toDTO);
    }

    @GetMapping("/max")
    public UserWithMaxTotalCostDTO findUserWithMaxTotalCost() {
        log.info("Determining the most widely used tag of a user with the highest cost of all orders.");
        return service.findUserWithMaxTotalCost();
    }

}

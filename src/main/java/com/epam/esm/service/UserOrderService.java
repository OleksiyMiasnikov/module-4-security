package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.UserWithMaxTotalCostDTO;
import com.epam.esm.model.DTO.user_order.CreateUserOrderRequest;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.entity.UserOrder;
import com.epam.esm.model.entity.UserWithMaxTotalCost;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserOrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  A service to work with {@link UserOrder}.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderService {

    private final UserOrderRepository repo;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final TagRepository tagRepository;

    /**
     * Creates new record of order.
     *
     * @param request - created order request.
     * @return {@link UserOrder} created order.
     */
    @Transactional
    public UserOrder create(CreateUserOrderRequest request) {
        log.info("Creating a new users order.");
        UserOrder userOrder = orderMapper.toOrder(request);

        return repo.save(userOrder);
    }

    /**
     * Finds all orders.
     *
     * @return List of {@link UserOrder} List of all orders.
     */
    public Page<UserOrder> findAll(Pageable pageable) {
        log.info("Getting all certificates with tags.");
        return repo.findAll(pageable);
    }

    public List<UserOrder> findByUser(String name) {
        log.info("Looking for all orders by user name: {}", name);

        User user = userRepository.findByName(name)
                .orElseThrow(() -> new ApiEntityNotFoundException(
                        "No order found by user name: " + name
                ));

        return repo.findByUserId(user.getId());
    }

    public UserWithMaxTotalCostDTO findUserWithMaxTotalCost(){
        log.info("Finding the most widely used tag of a user with the highest cost of all orders.");

        UserWithMaxTotalCost userWithMaxTotalCost =
                repo.findUsersWithTotalCost();

        Long userId = userWithMaxTotalCost.getUserId();
        Long tagId = repo.findMostlyUsedTag(userId).getTagId();
        Double totalCost = userWithMaxTotalCost.getTotalCost();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiEntityNotFoundException("User is not present!"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ApiEntityNotFoundException("Tag is not present!"));

        return UserWithMaxTotalCostDTO.builder()
                .user(user.getName())
                .tag(tag.getName())
                .totalCost(totalCost)
                .build();
    }

}

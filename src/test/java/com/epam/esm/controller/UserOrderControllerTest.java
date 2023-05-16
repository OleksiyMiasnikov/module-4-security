package com.epam.esm.controller;

import com.epam.esm.controller.advice.ApiExceptionHandler;
import com.epam.esm.model.DTO.UserWithMaxTotalCostDTO;
import com.epam.esm.model.DTO.user_order.UserOrderDTO;
import com.epam.esm.model.entity.UserOrder;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserOrderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    UserOrderService service;
    @Mock
    OrderMapper mapper;
    @InjectMocks
    UserOrderController subject;

    private Pageable pageable;
    private UserOrder userOrder;
    private UserOrderDTO userOrderDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        pageable = Pageable.ofSize(3).withPage(0);
        userOrder = UserOrder.builder().id(1L).userId(2L).CertificateWithTagId(3L).build();
        userOrderDTO = UserOrderDTO.builder().id(1L).CertificateWithTagId(3L).build();
    }

    @Test
    void create() throws Exception {
        String jsonCreateUserOrder = "{\"userId\": 2, \"certificateWithTagId\": 2}";

        when(service.create(any())).thenReturn(userOrder);
        when(mapper.toDTO(userOrder)).thenReturn(userOrderDTO);

        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateUserOrder))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userOrder.getId()));

        verify(service).create(any());
    }

    @Test
    void findAll() throws Exception {
        Page<UserOrder> page = new PageImpl<>(List.of(userOrder));

        when(service.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(userOrder)).thenReturn(userOrderDTO);

        this.mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(userOrderDTO.getId()));

        verify(service).findAll(pageable);
        verify(mapper).toDTO(userOrder);
    }

    @Test
    void findByUser() throws Exception {
        Page<UserOrder> page = new PageImpl<>(List.of(userOrder));

        when(service.findByUser("user", pageable)).thenReturn(page);
        when(mapper.toDTO(userOrder)).thenReturn(userOrderDTO);

        this.mockMvc.perform(get("/orders/user")
                        .param("user", "user")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(userOrderDTO.getId()));

        verify(service).findByUser("user", pageable);
        verify(mapper).toDTO(userOrder);
    }

    @Test
    void findUserWithMaxTotalCost() throws Exception {
        UserWithMaxTotalCostDTO userWithMaxTotalCostDTO =
                UserWithMaxTotalCostDTO.builder()
                        .totalCost(100d)
                        .tag("tag")
                        .user("user")
                        .build();

        when(service.findUserWithMaxTotalCost()).thenReturn(userWithMaxTotalCostDTO);

        this.mockMvc.perform(get("/orders/max"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(userWithMaxTotalCostDTO.getTotalCost()))
                .andExpect(jsonPath("$.tag").value(userWithMaxTotalCostDTO.getTag()))
                .andExpect(jsonPath("$.user").value(userWithMaxTotalCostDTO.getUser()));

        verify(service).findUserWithMaxTotalCost();
    }
}
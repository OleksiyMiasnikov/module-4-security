package com.epam.esm.service.mapper;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagDTO;
import com.epam.esm.model.DTO.user_order.CreateUserOrderRequest;
import com.epam.esm.model.DTO.user_order.UserOrderDTO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.entity.UserOrder;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateWithTagRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private CertificateWithTagRepository certificateWithTagRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CertificateWithTagMapper certificateWithTagMapper;
    @InjectMocks
    private OrderMapper subject;


    @BeforeEach
    void setUp() {
    }

    @Test
    void toOrder() {
        CreateUserOrderRequest request = CreateUserOrderRequest.builder()
                .certificateWithTagId(1L)
                .build();
        CertificateWithTag cwt = CertificateWithTag.builder()
                .id(1L)
                .build();
        Certificate certificate = Certificate.builder().build();
        UserOrder expected = UserOrder.builder()
                .CertificateWithTagId(1L)
                .build();

        when(certificateWithTagRepository.findById(request.getCertificateWithTagId()))
                .thenReturn(Optional.of(cwt));
        when(certificateRepository.findById(cwt.getCertificateId()))
                .thenReturn(Optional.of(certificate));

        assertThat(subject.toOrder(request).getCertificateWithTagId())
                .isEqualTo(expected.getCertificateWithTagId());

        verify(certificateWithTagRepository).findById(request.getCertificateWithTagId());
        verify(certificateRepository).findById(cwt.getCertificateId());
    }

    @Test
    void toOrderThrowException() {
        CreateUserOrderRequest request = CreateUserOrderRequest.builder()
                .certificateWithTagId(1L)
                .build();

        when(certificateWithTagRepository.findById(request.getCertificateWithTagId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> subject.toOrder(request))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessage("Entity is absent");

        verify(certificateWithTagRepository).findById(request.getCertificateWithTagId());
    }

    @Test
    void toDTO() {
        UserOrder userOrder = UserOrder.builder()
                .id(5L)
                .createDate(Instant.EPOCH)
                .build();
        User user = User.builder().build();
        CertificateWithTag cwt = CertificateWithTag.builder().build();
        CertificateWithTagDTO cwtDTO = CertificateWithTagDTO.builder().build();
        UserOrderDTO expected = UserOrderDTO.builder()
                .id(5L)
                .build();

        when(userRepository.findById(userOrder.getUserId()))
                .thenReturn(Optional.of(user));
        when(certificateWithTagRepository.findById(userOrder.getCertificateWithTagId()))
                .thenReturn(Optional.of(cwt));
        when(certificateWithTagMapper.toDTO(cwt))
                .thenReturn(cwtDTO);

        assertThat(subject.toDTO(userOrder).getId()).isEqualTo(expected.getId());

        verify(userRepository).findById(userOrder.getUserId());
        verify(certificateWithTagRepository).findById(userOrder.getCertificateWithTagId());
        verify(certificateWithTagMapper).toDTO(cwt);

    }

    @Test
    void toDTOThrowException() {
        UserOrder userOrder = UserOrder.builder().build();

        when(userRepository.findById(userOrder.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subject.toDTO(userOrder))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessage("Entity is absent");

        verify(userRepository).findById(userOrder.getUserId());
    }
}
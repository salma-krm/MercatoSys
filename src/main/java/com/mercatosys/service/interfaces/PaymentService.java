package com.mercatosys.service.interfaces;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.payment.PaymentRequestDTO;
import com.mercatosys.dto.payment.PaymentResponseDTO;


public interface PaymentService {


    PaymentResponseDTO payOrder(Long orderId, PaymentRequestDTO dto);
}

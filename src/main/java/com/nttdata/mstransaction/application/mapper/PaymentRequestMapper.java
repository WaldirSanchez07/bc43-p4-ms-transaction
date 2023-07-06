package com.nttdata.mstransaction.application.mapper;

import com.nttdata.mstransaction.application.dto.request.PaymentRequest;
import com.nttdata.mstransaction.domain.model.Payment;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentRequestMapper {

  PaymentRequestMapper INSTANCE = Mappers.getMapper(PaymentRequestMapper.class);

  default Payment map(PaymentRequest request) {
    return Payment.builder()
            .accountId(request.getAccountId())
            .amount(request.getAmount())
            .payAt(request.getPayAt())
            .build();
  }

  default List<Payment> map(List<PaymentRequest> request) {
    return request.stream().map(this::map).collect(Collectors.toList());
  }

}

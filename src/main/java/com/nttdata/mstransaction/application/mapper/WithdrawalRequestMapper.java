package com.nttdata.mstransaction.application.mapper;

import com.nttdata.mstransaction.application.dto.request.WithdrawalRequest;
import com.nttdata.mstransaction.domain.model.Transaction;
import java.time.LocalDateTime;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WithdrawalRequestMapper {

  WithdrawalRequestMapper INSTANCE = Mappers.getMapper(WithdrawalRequestMapper.class);

  default Transaction map(WithdrawalRequest transaction) {
    return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .type("Retiro")
            .accountId(transaction.getAccountId())
            .amount(transaction.getAmount())
            .date(LocalDateTime.now())
            .build();
  }

}

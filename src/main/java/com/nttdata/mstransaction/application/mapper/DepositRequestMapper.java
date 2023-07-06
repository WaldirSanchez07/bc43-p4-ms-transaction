package com.nttdata.mstransaction.application.mapper;

import com.nttdata.mstransaction.application.dto.request.DepositRequest;
import com.nttdata.mstransaction.domain.model.Transaction;
import java.time.LocalDateTime;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepositRequestMapper {

  DepositRequestMapper INSTANCE = Mappers.getMapper(DepositRequestMapper.class);

  default Transaction map(DepositRequest transaction) {
    return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .type("Deposito")
            .accountId(transaction.getAccountId())
            .amount(transaction.getAmount())
            .date(LocalDateTime.now())
            .build();
  }

}

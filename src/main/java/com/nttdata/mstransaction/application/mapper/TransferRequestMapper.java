package com.nttdata.mstransaction.application.mapper;

import com.nttdata.mstransaction.application.dto.request.TransferRequest;
import com.nttdata.mstransaction.domain.model.Transaction;
import java.time.LocalDateTime;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransferRequestMapper {

  TransferRequestMapper INSTANCE = Mappers.getMapper(TransferRequestMapper.class);

  default Transaction map(TransferRequest transaction) {
    return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .type("Transferencia")
            .accountId(transaction.getAccountId())
            .anotherAccountId(transaction.getAnotherAccountId())
            .amount(transaction.getAmount())
            .date(LocalDateTime.now())
            .build();
  }

}

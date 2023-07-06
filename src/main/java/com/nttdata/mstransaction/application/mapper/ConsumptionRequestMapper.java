package com.nttdata.mstransaction.application.mapper;

import com.nttdata.mstransaction.application.dto.request.ConsumptionRequest;
import com.nttdata.mstransaction.domain.model.Transaction;
import java.time.LocalDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConsumptionRequestMapper {

  ConsumptionRequestMapper INSTANCE = Mappers.getMapper(ConsumptionRequestMapper.class);

  default Transaction map(ConsumptionRequest request, String accountId) {
    return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .type("Consumo")
            .accountId(accountId)
            .cardId(request.getCardId())
            .description(request.getDescription())
            .amount(request.getAmount())
            .date(LocalDateTime.now())
            .build();
  }

}

package com.nttdata.mstransaction.infrastructure.dao.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class TransactionEntity {

  @Id
  private String id;
  private String type; //Deposito, Retiro, Consumo, Comision, Transferencia
  private String description;
  private String accountId;
  private String cardId;
  private String anotherAccountId;
  private Double amount;
  private LocalDateTime date;

}

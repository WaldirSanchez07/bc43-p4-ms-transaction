package com.nttdata.mstransaction.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  private String id;
  private String type; //Deposito, Retiro, Consumo, Comision, Transferencia
  private String description;
  private String accountId;
  private String cardId;
  private String anotherAccountId;
  private Double amount;
  private LocalDateTime date;

}

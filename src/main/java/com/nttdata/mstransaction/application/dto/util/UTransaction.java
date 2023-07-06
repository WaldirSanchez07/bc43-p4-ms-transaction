package com.nttdata.mstransaction.application.dto.util;
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
public class UTransaction {

  private String type;
  private String cardId;
  private String accountId;
  private Double balance;
  private Double amount;
  private Double commission;

}

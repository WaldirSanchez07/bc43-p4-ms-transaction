package com.nttdata.mstransaction.infrastructure.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "passive_products")
public class PassiveProductEntity {

  private String id;
  private String name;
  private Boolean hasMaintenanceFee;
  private Integer maximumDeposits;
  private Integer maximumWithdrawals;
  private Double transactionFee;
  //Plazo fijo
  private Integer transactionDay;

}

package com.nttdata.mstransaction.infrastructure.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Account entity.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "accounts")
public class AccountEntity {

  @Id
  private String id;
  private String accountNumber;
  private String clientId;
  private String passiveProductId;
  private String activeProductId;

}

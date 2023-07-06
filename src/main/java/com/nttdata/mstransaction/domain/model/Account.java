package com.nttdata.mstransaction.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Account model.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

  private String id;
  private String accountNumber;
  private String clientId;
  private String passiveProductId;
  private String activeProductId;

  private String cardId;
  private LocalDateTime associationDate;

}

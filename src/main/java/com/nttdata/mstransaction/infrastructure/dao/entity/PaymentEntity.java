package com.nttdata.mstransaction.infrastructure.dao.entity;

import java.time.LocalDate;
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
@Document(collection = "payments")
public class PaymentEntity {

  @Id
  private String id;
  private String accountId;
  private Double amount;
  private Integer state;
  private LocalDate payAt;
  private LocalDateTime paidAt;

}

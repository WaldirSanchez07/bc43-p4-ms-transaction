package com.nttdata.mstransaction.infrastructure.dao.repository;

import com.nttdata.mstransaction.infrastructure.dao.entity.PaymentEntity;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PaymentRepositoryRM extends ReactiveMongoRepository<PaymentEntity, String> {

  @Query("{ _id: ?0 }")
  Maybe<PaymentEntity> customFindById(String id);

}

package com.nttdata.mstransaction.infrastructure.dao.repository;

import com.nttdata.mstransaction.infrastructure.dao.entity.BalanceEntity;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BalanceRepositoryRM extends ReactiveMongoRepository<BalanceEntity, String> {

  Maybe<BalanceEntity> findFirstByAccountIdOrderByDateDesc(String accountId);

}

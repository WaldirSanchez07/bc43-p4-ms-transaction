package com.nttdata.mstransaction.infrastructure.dao.repository;

import com.nttdata.mstransaction.infrastructure.dao.entity.AccountEntity;
import com.nttdata.mstransaction.infrastructure.dao.entity.CardEntity;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CardRepositoryRM extends ReactiveMongoRepository<CardEntity, String> {

  @Aggregation(value = {
    "{ $lookup: { from: 'accounts', localField: 'mainAccountId', foreignField: '_id', as: 'account' } }",
    "{ $unwind: { path: '$account', preserveNullAndEmptyArrays: false } }",
    "{ $match: { _id: ?0 } }",
    "{ $replaceRoot: { newRoot: '$account' } }"
  })
  Maybe<AccountEntity> accountByCardId(String id);

}

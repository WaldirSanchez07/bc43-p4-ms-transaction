package com.nttdata.mstransaction.infrastructure.dao.repository;

import com.nttdata.mstransaction.infrastructure.dao.entity.AccountEntity;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * AccountRepository.
 */
public interface AccountRepositoryRM extends ReactiveMongoRepository<AccountEntity, String> {

  @Query("{ '_id': ?0 }")
  Maybe<AccountEntity> findAccountById(String id);

  @Aggregation(value = {
    "{ $lookup: { from: 'cards', localField: 'cardId', foreignField: '_id', as: 'card' } }",
    "{ $unwind: { path: '$card', preserveNullAndEmptyArrays: false } }",
    "{ $match: { cardId: ?0 } }",
    "{ $match: { $expr: { $ne: ['$card.mainAccountId', '$_id'] } } }",
    "{ $unionWith: { coll: 'cards' } }",
    "{ $project: { _id: 1, associationDate: 1, mainAccountId: 1 } }",
    "{ $sort: { mainAccountId: -1, associationDate: 1 } }",
    "{ $set: { _id: { $cond: [ { $ifNull: ['$mainAccountId', false] }, '$mainAccountId', '$_id' ] } } }",
    "{ $lookup: { from: 'accounts', localField: '_id', foreignField: '_id', as: 'account' } }",
    "{ $unwind: { path: '$account', preserveNullAndEmptyArrays: false } }",
    "{ $set: { passiveProductId: '$account.passiveProductId', activeProductId: '$account.activeProductId' } }"
  })
  Flowable<AccountEntity> findAccountsByCardId(String cardId);

}

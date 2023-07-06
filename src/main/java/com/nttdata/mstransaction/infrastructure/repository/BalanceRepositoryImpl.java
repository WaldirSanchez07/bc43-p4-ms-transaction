package com.nttdata.mstransaction.infrastructure.repository;

import com.nttdata.mstransaction.domain.model.Balance;
import com.nttdata.mstransaction.domain.repository.BalanceRepository;
import com.nttdata.mstransaction.infrastructure.dao.entity.BalanceEntity;
import com.nttdata.mstransaction.infrastructure.dao.repository.BalanceRepositoryRM;
import com.nttdata.mstransaction.infrastructure.mapper.BalanceMapper;
import io.reactivex.rxjava3.core.Maybe;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class BalanceRepositoryImpl implements BalanceRepository {

  private final BalanceRepositoryRM balanceRepository;

  @Override
  public Maybe<Double> findLastBalanceByAccountId(String accountId) {
    return balanceRepository.findFirstByAccountIdOrderByDateDesc(accountId)
            .map(BalanceEntity::getAmount)
            .switchIfEmpty(Maybe.just(0.0));
  }

  @Override
  public Maybe<Balance> saveBalance(Balance balance) {
    return Maybe.fromPublisher(balanceRepository
            .save(BalanceMapper.INSTANCE.map(balance))
            .map(BalanceMapper.INSTANCE::map));
  }

}

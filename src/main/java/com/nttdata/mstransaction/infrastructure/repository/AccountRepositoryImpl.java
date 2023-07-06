package com.nttdata.mstransaction.infrastructure.repository;

import com.nttdata.mstransaction.domain.model.Account;
import com.nttdata.mstransaction.domain.repository.AccountRepository;
import com.nttdata.mstransaction.infrastructure.dao.repository.AccountRepositoryRM;
import com.nttdata.mstransaction.infrastructure.dao.repository.CardRepositoryRM;
import com.nttdata.mstransaction.infrastructure.mapper.AccountMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementation.
 */
@Repository
@AllArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

  private final AccountRepositoryRM repository;
  private final CardRepositoryRM cardRepository;

  @Override
  public Maybe<Account> findAccountById(String id) {
    return repository.findAccountById(id).map(AccountMapper.INSTANCE::map);
  }

  @Override
  public Flowable<Account> findAccountsByCardId(String cardId) {
    return repository.findAccountsByCardId(cardId).map(AccountMapper.INSTANCE::map);
  }

  @Override
  public Maybe<Account> accountByCardId(String cardId) {
    return cardRepository.accountByCardId(cardId).map(AccountMapper.INSTANCE::map);
  }


}

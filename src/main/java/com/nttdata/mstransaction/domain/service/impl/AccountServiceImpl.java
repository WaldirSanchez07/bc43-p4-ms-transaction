package com.nttdata.mstransaction.domain.service.impl;

import com.nttdata.mstransaction.domain.model.Account;
import com.nttdata.mstransaction.domain.repository.AccountRepository;
import com.nttdata.mstransaction.domain.service.AccountService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation.
 */
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  @Override
  public Maybe<Account> findAccountById(String id) {
    return accountRepository.findAccountById(id);
  }

  @Override
  public Flowable<Account> findAccountsByCardId(String cardId) {
    return accountRepository.findAccountsByCardId(cardId);
  }

  @Override
  public Maybe<Account> accountByCardId(String cardId) {
    return accountRepository.accountByCardId(cardId);
  }

}

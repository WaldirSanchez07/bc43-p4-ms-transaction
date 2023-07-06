package com.nttdata.mstransaction.domain.service;

import com.nttdata.mstransaction.domain.model.Account;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

/**
 * Interface to define methods.
 */
public interface AccountService {

  Maybe<Account> findAccountById(String id);

  Flowable<Account> findAccountsByCardId(String cardId);

  Maybe<Account> accountByCardId(String cardId);

}

package com.nttdata.mstransaction.domain.repository;

import com.nttdata.mstransaction.domain.model.Account;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

/**
 * Interface to define methods.
 */
public interface AccountRepository {

  Maybe<Account> findAccountById(String id);

  Flowable<Account> findAccountsByCardId(String cardId);

  Maybe<Account> accountByCardId(String cardId);

}

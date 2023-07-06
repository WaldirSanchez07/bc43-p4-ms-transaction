package com.nttdata.mstransaction.application.service.impl;

import com.nttdata.mstransaction.application.dto.request.ConsumptionRequest;
import com.nttdata.mstransaction.application.dto.response.ObjectResponse;
import com.nttdata.mstransaction.application.mapper.ConsumptionRequestMapper;
import com.nttdata.mstransaction.application.service.ConsumptionExternalService;
import com.nttdata.mstransaction.domain.model.Balance;
import com.nttdata.mstransaction.domain.model.Transaction;
import com.nttdata.mstransaction.domain.service.AccountService;
import com.nttdata.mstransaction.domain.service.BalanceService;
import com.nttdata.mstransaction.domain.service.TransactionService;
import io.reactivex.rxjava3.core.Maybe;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ConsumptionExternalService implementation.
 */
@Service
@AllArgsConstructor
public class ConsumptionExternalServiceImpl implements ConsumptionExternalService {

  private final AccountService accountService;
  private final TransactionService transactionService;
  private final BalanceService balanceService;

  @Override
  public Maybe<ObjectResponse> saveConsumption(ConsumptionRequest request) {
    return accountService
            .accountByCardId(request.getCardId())
            .flatMap(account -> balanceService
                    .findLastBalanceByAccountId(account.getId())
                    .filter(balance -> balance >= request.getAmount())
                    .flatMap(balance -> {
                      Double currentBalance = balance - request.getAmount();

                      var balanceObj = Balance.builder()
                              .id(UUID.randomUUID().toString())
                              .accountId(account.getId())
                              .amount(currentBalance)
                              .date(LocalDateTime.now())
                              .build();
                      Maybe<Balance> balanceMaybe = balanceService.saveBalance(balanceObj);

                      Maybe<Transaction> transactionMaybe = transactionService
                              .saveTransaction(ConsumptionRequestMapper
                                      .INSTANCE.map(request, account.getId()));

                      return transactionMaybe
                              .flatMap(transaction -> balanceMaybe
                                      .map(o -> new ObjectResponse(201, "Consumo registrado!", null)));
                    }))
            .switchIfEmpty(Maybe.error(new Throwable("No hay saldo disponible!")))
            .onErrorResumeNext(error -> Maybe.error(new Throwable(error.getMessage())));
  }

}

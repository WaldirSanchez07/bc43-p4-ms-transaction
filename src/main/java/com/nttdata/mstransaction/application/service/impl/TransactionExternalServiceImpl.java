package com.nttdata.mstransaction.application.service.impl;

import com.nttdata.mstransaction.application.dto.request.CardRequest;
import com.nttdata.mstransaction.application.dto.request.DepositRequest;
import com.nttdata.mstransaction.application.dto.request.TransferRequest;
import com.nttdata.mstransaction.application.dto.request.WithdrawalRequest;
import com.nttdata.mstransaction.application.dto.response.ObjectResponse;
import com.nttdata.mstransaction.application.dto.util.UTransaction;
import com.nttdata.mstransaction.application.mapper.TransferRequestMapper;
import com.nttdata.mstransaction.application.service.TransactionExternalService;
import com.nttdata.mstransaction.domain.model.Account;
import com.nttdata.mstransaction.domain.model.Balance;
import com.nttdata.mstransaction.domain.model.PassiveProduct;
import com.nttdata.mstransaction.domain.model.Transaction;
import com.nttdata.mstransaction.domain.service.AccountService;
import com.nttdata.mstransaction.domain.service.BalanceService;
import com.nttdata.mstransaction.domain.service.PassiveProductService;
import com.nttdata.mstransaction.domain.service.TransactionService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionExternalServiceImpl implements TransactionExternalService {

  private final TransactionService transactionService;
  private final BalanceService balanceService;
  private final AccountService accountService;
  private final PassiveProductService passiveProductService;

  private static final String TYPE_RETIRO = "Retiro";

  @Override
  public Maybe<ObjectResponse> saveDeposit(DepositRequest request) {
    return accountService
            .findAccountById(request.getAccountId())
            .flatMap(account -> validateMaxDeposits(account)
                    .flatMap(commission -> balanceService
                            .findLastBalanceByAccountId(account.getId())
                            .flatMap(balance -> {
                              Double currentBalance = balance + request.getAmount() - commission;

                              UTransaction obj = UTransaction.builder()
                                      .type("Deposito")
                                      .accountId(account.getId())
                                      .amount(request.getAmount())
                                      .commission(commission)
                                      .balance(currentBalance)
                                      .build();

                              return commission == 0 ? transactWithOutCommission(obj) : transactWithCommission(obj);
                            })
                    ));
  }

  @Override
  public Maybe<ObjectResponse> saveWithdrawalByDebitCard(CardRequest request) {
    Flowable<Account> flowable = accountService.findAccountsByCardId(request.getCardId());

    return flowable
            .concatMapMaybe(account -> validateMaxWithdrawals(account)
                    .flatMap(commission -> balanceService
                            .findLastBalanceByAccountId(account.getId())
                            .filter(balance -> balance >= request.getAmount() + commission)
                            .flatMap(balance -> {
                              Double currentBalance = balance - request.getAmount() - commission;

                              var obj = UTransaction.builder()
                                      .type(TYPE_RETIRO)
                                      .cardId(request.getCardId())
                                      .accountId(account.getId())
                                      .amount(request.getAmount())
                                      .commission(commission)
                                      .balance(currentBalance)
                                      .build();

                              return commission == 0 ? transactWithOutCommission(obj) : transactWithCommission(obj);
                            }))
            ).take(1)
            .firstElement()
            .switchIfEmpty(Maybe.error(new Throwable("Sus cuentas no tienen suficiente saldo!")))
            .onErrorResumeNext(error -> Maybe.error(new Throwable(error.getMessage())));
  }

  @Override
  public Maybe<ObjectResponse> saveWithdrawal(WithdrawalRequest request) {
    return accountService
            .findAccountById(request.getAccountId())
            .flatMap(account -> validateMaxWithdrawals(account)
                    .flatMap(commission -> balanceService
                            .findLastBalanceByAccountId(account.getId())
                            .flatMap(balance -> {
                              Double currentBalance = balance - request.getAmount() - commission;

                              var obj = UTransaction.builder()
                                      .type(TYPE_RETIRO)
                                      .accountId(account.getId())
                                      .amount(request.getAmount())
                                      .commission(commission)
                                      .balance(currentBalance)
                                      .build();

                              return commission == 0 ? transactWithOutCommission(obj) : transactWithCommission(obj);
                            })
                    ));
  }

  @Override
  public Maybe<ObjectResponse> saveTransfer(TransferRequest request) {
    return accountService
            .findAccountById(request.getAccountId())
            .flatMap(account -> balanceService
                    .findLastBalanceByAccountId(request.getAccountId())
                    .filter(balance -> balance >= request.getAmount())
                    .flatMap(balance -> {
                      Double newAmount = balance - request.getAmount();

                      Maybe<Transaction> transactionM = transactionService
                              .saveTransaction(TransferRequestMapper.INSTANCE.map(request));

                      var balanceAccount1 = Balance.builder()
                              .id(UUID.randomUUID().toString())
                              .accountId(request.getAccountId())
                              .amount(newAmount)
                              .date(LocalDateTime.now())
                              .build();
                      Maybe<Balance> balanceM1 = balanceService.saveBalance(balanceAccount1);

                      var balanceAccount2 = Balance.builder()
                              .id(UUID.randomUUID().toString())
                              .accountId(request.getAnotherAccountId())
                              .amount(request.getAmount())
                              .date(LocalDateTime.now())
                              .build();
                      Maybe<Balance> balanceM2 = balanceService.saveBalance(balanceAccount2);

                      return Maybe.zip(transactionM, balanceM1, balanceM2, (t, b1, b2) ->
                              new ObjectResponse(201, "Transferencia exitosa!", null)
                      );
                    }));
  }

  private Maybe<Double> validateMaxDeposits(Account account) {
    var currentYearMonth = YearMonth.now();
    LocalDateTime startOfMonth = currentYearMonth.atDay(1).atStartOfDay();

    var nextYearMonth = currentYearMonth.plusMonths(1);
    LocalDateTime startOfNextMonth = nextYearMonth.atDay(1).atStartOfDay();

    Maybe<PassiveProduct> productMaybe = passiveProductService
            .findProductById(account.getPassiveProductId());

    Maybe<Long> transactionsMaybe = transactionService
            .countTransactions(account.getId(), "Deposito", startOfMonth, startOfNextMonth);

    return productMaybe
            .flatMap(product -> transactionsMaybe
                    .filter(transactions -> transactions <= product.getMaximumDeposits())
                    .map(count -> count <= 20 ? 0 : product.getTransactionFee()));
  }

  private Maybe<Double> validateMaxWithdrawals(Account account) {
    var currentYearMonth = YearMonth.now();
    LocalDateTime startOfMonth = currentYearMonth.atDay(1).atStartOfDay();

    var nextYearMonth = currentYearMonth.plusMonths(1);
    LocalDateTime startOfNextMonth = nextYearMonth.atDay(1).atStartOfDay();

    Maybe<PassiveProduct> productMaybe = passiveProductService
            .findProductById(account.getPassiveProductId());

    Maybe<Long> transactionsMaybe = transactionService
            .countTransactions(account.getId(), TYPE_RETIRO, startOfMonth, startOfNextMonth);

    return productMaybe
            .flatMap(product -> transactionsMaybe
                    .filter(transactions -> transactions <= product.getMaximumWithdrawals())
                    .map(count -> count <= 20 ? 0 : product.getTransactionFee()));
  }

  private Maybe<ObjectResponse> transactWithOutCommission(UTransaction transaction) {
    var transactionObj = Transaction.builder()
            .id(UUID.randomUUID().toString())
            .type(transaction.getType())
            .cardId(transaction.getCardId())
            .accountId(transaction.getAccountId())
            .amount(transaction.getAmount())
            .date(LocalDateTime.now())
            .build();
    Maybe<Transaction> transactionMaybe = transactionService.saveTransaction(transactionObj);

    var objBalance = Balance.builder()
            .id(UUID.randomUUID().toString())
            .accountId(transaction.getAccountId())
            .amount(transaction.getBalance())
            .date(LocalDateTime.now())
            .build();
    Maybe<Balance> balanceMaybe = balanceService.saveBalance(objBalance);

    return transactionMaybe.flatMap(obj -> balanceMaybe
            .map(obj2 -> new ObjectResponse(201, "Transacción exitosa!", null)));
  }

  private Maybe<ObjectResponse> transactWithCommission(UTransaction transaction) {
    var transactionObj = Transaction.builder()
            .id(UUID.randomUUID().toString())
            .type(transaction.getType())
            .cardId(transaction.getCardId())
            .accountId(transaction.getAccountId())
            .amount(transaction.getAmount())
            .date(LocalDateTime.now())
            .build();
    Maybe<Transaction> withdrawalM = transactionService.saveTransaction(transactionObj);

    var commissionTransact = Transaction.builder()
            .id(UUID.randomUUID().toString())
            .cardId(transaction.getCardId())
            .accountId(transaction.getAccountId())
            .type("Comision")
            .amount(transaction.getCommission())
            .date(LocalDateTime.now())
            .build();
    Maybe<Transaction> commissionM = transactionService.saveTransaction(commissionTransact);

    var objBalance = Balance.builder()
            .id(UUID.randomUUID().toString())
            .accountId(transaction.getAccountId())
            .amount(transaction.getBalance())
            .date(LocalDateTime.now())
            .build();
    Maybe<Balance> balanceM = balanceService.saveBalance(objBalance);

    return Maybe.zip(withdrawalM, commissionM, balanceM, (w, c, b) ->
            new ObjectResponse(201, "Transacción exitosa!", null));
  }

}
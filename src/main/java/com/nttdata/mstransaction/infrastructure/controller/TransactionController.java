package com.nttdata.mstransaction.infrastructure.controller;

import com.nttdata.mstransaction.application.dto.request.CardRequest;
import com.nttdata.mstransaction.application.dto.request.DepositRequest;
import com.nttdata.mstransaction.application.dto.request.TransferRequest;
import com.nttdata.mstransaction.application.dto.request.WithdrawalRequest;
import com.nttdata.mstransaction.application.dto.response.ObjectResponse;
import com.nttdata.mstransaction.application.service.TransactionExternalService;
import io.reactivex.rxjava3.core.Maybe;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * API Rest Full.
 */
@AllArgsConstructor
@RestController
@RequestMapping("api/transactions")
public class TransactionController {

  private final TransactionExternalService transactionService;

  @PostMapping(
          value = "/passive-product/deposit",
          produces = MediaType.TEXT_EVENT_STREAM_VALUE
  )
  public Maybe<ObjectResponse> saveDeposit(@Valid @RequestBody DepositRequest request) {
    return transactionService.saveDeposit(request);
  }

  @PostMapping(
          value = "/passive-product/withdrawal",
          produces = MediaType.TEXT_EVENT_STREAM_VALUE
  )
  public Maybe<ObjectResponse> saveWithdrawal(@Valid @RequestBody WithdrawalRequest request) {
    return transactionService.saveWithdrawal(request);
  }

  @PostMapping(
          value = "/passive-product/transfer",
          produces = MediaType.TEXT_EVENT_STREAM_VALUE
  )
  public Maybe<ObjectResponse> saveTransfer(@Valid @RequestBody TransferRequest request) {
    return transactionService.saveTransfer(request);
  }

  @PostMapping(
          value = "/debit-card/withdrawal",
          produces = MediaType.TEXT_EVENT_STREAM_VALUE
  )
  public Maybe<ObjectResponse> saveDebitCardWithdrawal(@Valid @RequestBody CardRequest request) {
    return transactionService.saveWithdrawalByDebitCard(request);
  }

}

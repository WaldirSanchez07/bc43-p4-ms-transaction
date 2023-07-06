package com.nttdata.mstransaction.util;

import com.nttdata.mstransaction.application.dto.request.PaymentRequest;
import com.nttdata.mstransaction.application.dto.response.ObjectResponse;
import io.reactivex.rxjava3.core.Maybe;

import java.time.LocalDate;

public class PaymentTestUtil {

  private static String MESSAGE = "Consumo registrado!";

  public static Maybe<ObjectResponse> generateResponse() {
    ObjectResponse obj1 = ObjectResponse.builder()
            .status(201)
            .message(MESSAGE)
            .data(null)
            .build();

    return Maybe.just(obj1);
  }

  public static PaymentRequest generateRequest() {
    var request = PaymentRequest.builder()
            .accountId("1")
            .amount(120.00)
            .payAt(LocalDate.now())
            .build();

    return request;
  }

}

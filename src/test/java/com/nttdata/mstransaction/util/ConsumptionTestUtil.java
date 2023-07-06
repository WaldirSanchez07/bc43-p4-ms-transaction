package com.nttdata.mstransaction.util;

import com.nttdata.mstransaction.application.dto.request.ConsumptionRequest;
import com.nttdata.mstransaction.application.dto.response.ObjectResponse;
import io.reactivex.rxjava3.core.Maybe;

public class ConsumptionTestUtil {

  private static String MESSAGE = "Consumo registrado!";

  public static Maybe<ObjectResponse> generateResponse() {
    ObjectResponse obj1 = ObjectResponse.builder()
            .status(201)
            .message(MESSAGE)
            .data(null)
            .build();

    return Maybe.just(obj1);
  }

  public static ConsumptionRequest generateRequest() {
    var request = ConsumptionRequest.builder()
            .cardId("1")
            .amount(120.00)
            .description("SMarket")
            .build();

    return request;
  }

}

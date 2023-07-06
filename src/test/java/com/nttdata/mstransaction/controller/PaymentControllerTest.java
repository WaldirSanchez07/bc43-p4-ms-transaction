package com.nttdata.mstransaction.controller;

import com.nttdata.mstransaction.application.dto.response.ObjectResponse;
import com.nttdata.mstransaction.application.service.PaymentExternalService;
import com.nttdata.mstransaction.infrastructure.controller.PaymentController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.nttdata.mstransaction.util.PaymentTestUtil.generateRequest;
import static com.nttdata.mstransaction.util.PaymentTestUtil.generateResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(PaymentControllerTest.class)
public class PaymentControllerTest {

  @InjectMocks
  PaymentController controller;

  @Mock
  private PaymentExternalService paymentService;

  @Test
  public void testSave() {
    when(paymentService.savePaymentCredit(anyString())).thenReturn(generateResponse());

    WebTestClient client = WebTestClient.bindToController(controller).build();

    client.put()
            .uri("/api/transactions/save-payment?id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody(generateRequest())
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(ObjectResponse.class)
            .consumeWith(o -> {
              Assertions.assertEquals(o.getResponseBody().blockFirst().getStatus(), 201);
            });
  }

}

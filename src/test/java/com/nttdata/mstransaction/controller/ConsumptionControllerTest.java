package com.nttdata.mstransaction.controller;

import com.nttdata.mstransaction.application.dto.request.ConsumptionRequest;
import com.nttdata.mstransaction.application.dto.response.ObjectResponse;
import com.nttdata.mstransaction.application.service.ConsumptionExternalService;
import com.nttdata.mstransaction.infrastructure.controller.ConsumptionController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.nttdata.mstransaction.util.ConsumptionTestUtil.generateRequest;

import static com.nttdata.mstransaction.util.ConsumptionTestUtil.generateResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ConsumptionControllerTest.class)
public class ConsumptionControllerTest {

  @InjectMocks
  ConsumptionController controller;

  @Mock
  private ConsumptionExternalService consumptionService;

  @Test
  public void testSave() {
    when(consumptionService.saveConsumption(any(ConsumptionRequest.class))).thenReturn(generateResponse());

    WebTestClient client = WebTestClient.bindToController(controller).build();

    client.post()
            .uri("/api/transactions/active-product/consumption")
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

package org.task.controller.contract;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.task.controller.ResourceNotFoundException;
import org.task.data.service.OrderBookService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderBookControllerBase {

  @LocalServerPort
  private int port;

  @MockBean
  private OrderBookService orderBookService;

  private static UUID NOT_FOUND = UUID.fromString("e4944dab-2846-44d5-891a-0ba3a78fb1ce");
  private static UUID INTERNAL_ERROR = UUID.fromString("f4944dab-2846-44d5-891a-0ba3a78fb1ce");

  @BeforeEach
  public void clear() {
    reset(orderBookService);
    doThrow(new ResourceNotFoundException("not found")).when(orderBookService)
        .closeBook(eq(NOT_FOUND));
    doThrow(new RuntimeException("internal_error")).when(orderBookService)
        .closeBook(eq(INTERNAL_ERROR));
  }


  @BeforeAll
  public void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

}

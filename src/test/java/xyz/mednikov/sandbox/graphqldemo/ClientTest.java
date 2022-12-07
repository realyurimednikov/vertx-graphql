package xyz.mednikov.sandbox.graphqldemo;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class ClientTest {

  private WebClient client;

  @BeforeEach
  void setup(Vertx vertx, VertxTestContext context){
    client = WebClient.create(vertx);
    context.completeNow();
  }

  @Test
  void queryTest(Vertx vertx, VertxTestContext context){
//    String query = "{User (id: 15) {first_name last_name } }";
    String query = "{ allUsers(page: 0, perPage:10) {id first_name} }";
    JsonObject body = new JsonObject();
    body.put("query", query);
    context.verify(() -> {
      client.postAbs("http://localhost:5000/graphql")
        .sendJsonObject(body)
        .onFailure(err -> context.failNow(err))
        .onSuccess(response -> {
          String data = response.bodyAsString();
          System.out.println(data);
          context.completeNow();
        });
    });
  }

  @Test
  void mutationTest(Vertx vertx, VertxTestContext context){
    String query = "mutation { createUser (first_name: \"John\" last_name: \"Doe\" email: \"john.doe@email.com\" ) {id} }";
    JsonObject body = new JsonObject();
    body.put("query", query);
    context.verify(() -> {
      client.postAbs("http://localhost:5000/graphql")
        .sendJsonObject(body)
        .onFailure(err -> context.failNow(err))
        .onSuccess(response -> {
          String data = response.bodyAsString();
          System.out.println(data);
          context.completeNow();
        });
    });
  }
}

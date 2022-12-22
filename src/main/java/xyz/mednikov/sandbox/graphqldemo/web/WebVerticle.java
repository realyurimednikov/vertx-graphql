package xyz.mednikov.sandbox.graphqldemo.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;
import xyz.mednikov.sandbox.graphqldemo.data.EmployeeRepository;
import xyz.mednikov.sandbox.graphqldemo.data.EmployeeRepositoryImpl;
import xyz.mednikov.sandbox.graphqldemo.service.EmployeeService;
import xyz.mednikov.sandbox.graphqldemo.service.EmployeeServiceImpl;

public class WebVerticle extends AbstractVerticle {

  private final EmployeeService employeeService;

  public WebVerticle(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    GraphQLManager graphQLManager = new GraphQLManager(vertx, employeeService);
    graphQLManager.getGraphQL()
      .map(graphQL -> {
        GraphQLHandler graphQLHandler = GraphQLHandler.create(graphQL);
        GraphiQLHandler graphiQLHandler = GraphiQLHandler.create(new GraphiQLHandlerOptions().setEnabled(true));
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/graphql").handler(graphQLHandler);
        router.route("/graphiql/*").handler(graphiQLHandler);
        HttpServer server = vertx.createHttpServer();
        return server.requestHandler(router).listen(8080);
      })
      .onFailure(err -> startPromise.fail(err))
      .onSuccess(result -> startPromise.complete());
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    EmployeeRepository repository = new EmployeeRepositoryImpl();
    EmployeeService service = new EmployeeServiceImpl(repository);
    WebVerticle verticle = new WebVerticle(service);
    vertx.deployVerticle(verticle)
      .onFailure(err -> err.printStackTrace())
      .onSuccess(result -> System.out.println(result));
  }
}

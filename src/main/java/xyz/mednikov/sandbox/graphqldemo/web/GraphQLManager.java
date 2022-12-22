package xyz.mednikov.sandbox.graphqldemo.web;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.graphql.schema.VertxDataFetcher;
import xyz.mednikov.sandbox.graphqldemo.model.Employee;
import xyz.mednikov.sandbox.graphqldemo.service.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

record GraphQLManager(Vertx vertx, EmployeeService employeeService) {

  Future<GraphQL> getGraphQL() {

    VertxDataFetcher<Employee> createEmployee = VertxDataFetcher.create(env -> {
      String firstName = env.getArgument("firstName");
      String lastName = env.getArgument("lastName");
      Employee employee = new Employee(null, firstName, lastName);
      return employeeService().createEmployee(employee);
    });

    VertxDataFetcher<Employee> updateEmployee = VertxDataFetcher.create(env -> {
      String firstName = env.getArgument("firstName");
      String lastName = env.getArgument("lastName");
      UUID id = UUID.fromString(env.getArgument("id"));
      Employee employee = new Employee(id, firstName, lastName);
      return employeeService().updateEmployee(employee);
    });

    VertxDataFetcher<Boolean> removeEmployee = VertxDataFetcher.create(env -> {
      UUID id = UUID.fromString(env.getArgument("id"));
      return employeeService().removeEmployee(id).map(result -> Boolean.TRUE);
    });

    VertxDataFetcher<Optional<Employee>> findEmployeeById = VertxDataFetcher.create(env -> {
      UUID id = UUID.fromString(env.getArgument("id"));
      return employeeService().findEmployeeById(id);
    });

    VertxDataFetcher<List<Employee>> findAllEmployees = VertxDataFetcher.create(env -> {
      return employeeService().findAllEmployees().map(employeeList -> employeeList.employees());
    });

    return vertx().fileSystem().readFile("schemas/employees.graphql")
      .map(result -> result.toString())
      .map(result -> {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(result);
        return typeRegistry;
      })
      .map(registry -> {
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
          .type("Mutation", builder -> builder.dataFetcher("createEmployee", createEmployee))
          .type("Mutation", builder -> builder.dataFetcher("updateEmployee", updateEmployee))
          .type("Mutation", builder -> builder.dataFetcher("removeEmployee", removeEmployee))
          .type("Query", builder -> builder.dataFetcher("allEmployees", findAllEmployees))
          .type("Query", builder -> builder.dataFetcher("Employee", findEmployeeById))
          .build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(registry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
      });
  }
}

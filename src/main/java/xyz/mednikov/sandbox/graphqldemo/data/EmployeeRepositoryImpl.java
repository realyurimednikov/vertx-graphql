package xyz.mednikov.sandbox.graphqldemo.data;

import io.vertx.core.Future;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import xyz.mednikov.sandbox.graphqldemo.model.Employee;
import xyz.mednikov.sandbox.graphqldemo.model.EmployeeList;

import java.util.Optional;
import java.util.UUID;

public class EmployeeRepositoryImpl implements EmployeeRepository {

  ImmutableList<Employee> data;

  public EmployeeRepositoryImpl(){

    this.data = Lists.immutable.of(
      new Employee(UUID.randomUUID(), "Star", "Brunker"),
      new Employee(UUID.randomUUID(), "Gennie", "Brittlebank"),
      new Employee(UUID.randomUUID(), "Carl", "Humbey"),
      new Employee(UUID.randomUUID(), "Jeramie", "Edscer"),
      new Employee(UUID.randomUUID(), "Normand", "Noylund"),
      new Employee(UUID.randomUUID(), "Sapphire", "Lavallie"),
      new Employee(UUID.randomUUID(), "Worden", "Oxbie"),
      new Employee(UUID.randomUUID(), "Evangelin", "Diguid"),
      new Employee(UUID.randomUUID(), "Hyman", "Dun"),
      new Employee(UUID.randomUUID(), "Adena", "Synan")
    );

  }

  @Override
  public Future<Employee> createEmployee(Employee employee) {
    Employee e = new Employee(UUID.randomUUID(), employee.firstName(), employee.lastName());
    this.data = this.data.newWith(e);
    return Future.succeededFuture(e);
  }

  @Override
  public Future<Void> removeEmployee(UUID id) {
    this.data = this.data.reject(e -> e.id().equals(id));
    return Future.succeededFuture();
  }

  @Override
  public Future<Optional<Employee>> findEmployeeById(UUID id) {
    Optional<Employee> employee = this.data.stream().filter(p -> p.id().equals(id)).findFirst();
    return Future.succeededFuture(employee);
  }

  @Override
  public Future<Employee> updateEmployee(Employee employee) {
    this.data = this.data.reject(e -> e.id().equals(employee.id()));
    this.data = this.data.newWith(employee);
    return Future.succeededFuture(employee);
  }

  @Override
  public Future<EmployeeList> findAllEmployees() {
    EmployeeList list = new EmployeeList(this.data.castToList());
    return Future.succeededFuture(list);
  }
}

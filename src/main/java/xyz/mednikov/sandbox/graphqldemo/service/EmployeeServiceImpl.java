package xyz.mednikov.sandbox.graphqldemo.service;

import io.vertx.core.Future;
import xyz.mednikov.sandbox.graphqldemo.data.EmployeeRepository;
import xyz.mednikov.sandbox.graphqldemo.model.Employee;
import xyz.mednikov.sandbox.graphqldemo.model.EmployeeList;

import java.util.Optional;
import java.util.UUID;

public record EmployeeServiceImpl(EmployeeRepository repository) implements EmployeeService {
  @Override
  public Future<Employee> createEmployee(Employee employee) {
    return repository().createEmployee(employee);
  }

  @Override
  public Future<Employee> updateEmployee(Employee employee) {
    return repository().updateEmployee(employee);
  }

  @Override
  public Future<Void> removeEmployee(UUID id) {
    return repository().removeEmployee(id);
  }

  @Override
  public Future<Optional<Employee>> findEmployeeById(UUID id) {
    return repository().findEmployeeById(id);
  }

  @Override
  public Future<EmployeeList> findAllEmployees() {
    return repository().findAllEmployees();
  }
}

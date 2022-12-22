package xyz.mednikov.sandbox.graphqldemo.data;

import io.vertx.core.Future;
import xyz.mednikov.sandbox.graphqldemo.model.Employee;
import xyz.mednikov.sandbox.graphqldemo.model.EmployeeList;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {

  Future<Employee> createEmployee (Employee employee);

  Future<Employee> updateEmployee (Employee employee);

  Future<Void> removeEmployee (UUID id);

  Future<Optional<Employee>> findEmployeeById (UUID id);

  Future<EmployeeList> findAllEmployees ();
}

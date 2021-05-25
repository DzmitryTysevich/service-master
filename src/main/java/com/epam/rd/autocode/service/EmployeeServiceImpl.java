package com.epam.rd.autocode.service;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.epam.rd.autocode.requests.SQLRequests.*;

public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public List<Employee> getAllSortByHireDate(Paging paging) {
        List<Employee> employees = getEmployees(ALL_SORT_BY_HIREDATE);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getAllSortByLastname(Paging paging) {
        List<Employee> employees = getEmployees(ALL_SORT_BY_LASTNAME);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getAllSortBySalary(Paging paging) {
        List<Employee> employees = getEmployees(ALL_SORT_BY_SALARY);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
        List<Employee> employees = getEmployees(ALL_SORT_BY_DEPARTMENT_NAME_AND_LASTNAME);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
        String query = FROM_EMPLOYEE_WHERE_DEPARTMENT + department.getId() + ORDER_BY_HIREDATE;
        return getSublist(getEmployees(query), paging);
    }

    @Override
    public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
        String query = FROM_EMPLOYEE_WHERE_DEPARTMENT + department.getId() + ORDER_BY_SALARY;
        return getSublist(getEmployees(query), paging);
    }

    @Override
    public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
        String query = FROM_EMPLOYEE_WHERE_DEPARTMENT + department.getId() + ORDER_BY_LASTNAME;
        return getSublist(getEmployees(query), paging);
    }

    @Override
    public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
        String query = FROM_EMPLOYEE_WHERE_MANAGER + manager.getId() + ORDER_BY_LASTNAME;
        return getSublist(getEmployees(query), paging);
    }

    @Override
    public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
        String query = FROM_EMPLOYEE_WHERE_MANAGER + manager.getId() + ORDER_BY_HIREDATE;
        return getSublist(getEmployees(query), paging);
    }

    @Override
    public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
        String query = FROM_EMPLOYEE_WHERE_MANAGER + manager.getId() + ORDER_BY_SALARY;
        return getSublist(getEmployees(query), paging);
    }

    @Override
    public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
        Employee newEmployee = null;
        try {
            Connection connection = ConnectionSource.instance().createConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(FROM_EMPLOYEE_WHERE_ID + employee.getId());
            while (rs.next()) {
                newEmployee = getEmployee(rs, getFullManagerChain(rs.getInt(MANAGER)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newEmployee;
    }

    @Override
    public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
        List<Employee> employees = getEmployees(FROM_EMPLOYEE_WHERE_DEPARTMENT + department.getId() + ORDER_BY_SALARY_DESC);
        return employees.get(salaryRank - 1);
    }

    private Employee getEmployee(ResultSet rs, Employee manager) throws SQLException {
        return new Employee(
                new BigInteger(rs.getString(ID)),
                new FullName(
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME),
                        rs.getString(MIDDLE_NAME)
                ),
                Position.valueOf(rs.getString(POSITION)),
                rs.getDate(HIREDATE).toLocalDate(),
                new BigDecimal(rs.getString(SALARY)),
                manager,
                getDepartment(rs.getInt(DEPARTMENT))
        );
    }

    private Employee getFullManagerChain(int managerId) throws SQLException {
        Employee manager = null;
        Connection connection = ConnectionSource.instance().createConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(FROM_EMPLOYEE_WHERE_ID + managerId);
        while (rs.next()) {
            manager = getEmployee(rs, getFullManagerChain(rs.getInt(MANAGER)));
        }
        return manager;
    }

    private Employee getManager(int managerId) throws SQLException {
        Employee manager = null;
        Connection connection = ConnectionSource.instance().createConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(FROM_EMPLOYEE_WHERE_ID + managerId);
        while (rs.next()) {
            manager = getEmployee(rs, null);
        }
        return manager;
    }

    private Department getDepartment(int departmentId) throws SQLException {
        Department department = null;
        Connection connection = ConnectionSource.instance().createConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(FROM_DEPARTMENT_WHERE_ID + departmentId);
        while (rs.next()) {
            department = new Department(
                    new BigInteger(rs.getString(ID)),
                    rs.getString(NAME),
                    rs.getString(LOCATION)
            );
        }
        return department;
    }

    private List<Employee> getEmployees(String query) {
        List<Employee> employees = new ArrayList<>();
        try {
            Connection connection = ConnectionSource.instance().createConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                employees.add(getEmployee(rs, getManager(rs.getInt(MANAGER))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private <T> List<T> getSublist(List<T> list, Paging paging) {
        int fromIndex = getFromIndex(paging);
        int toIndex = Math.min(list.size(), getToIndex(paging));
        return list.subList(fromIndex, toIndex);
    }

    private int getFromIndex(Paging paging) {
        return paging.itemPerPage * (paging.page - 1);
    }

    private int getToIndex(Paging paging) {
        return paging.itemPerPage + getFromIndex(paging);
    }
}
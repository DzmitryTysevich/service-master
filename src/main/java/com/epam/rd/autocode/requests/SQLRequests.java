package com.epam.rd.autocode.requests;

public class SQLRequests {

    public static final String ID = "id";
    public static final String FIRST_NAME = "firstname";
    public static final String LAST_NAME = "lastname";
    public static final String MIDDLE_NAME = "middlename";
    public static final String POSITION = "position";
    public static final String HIREDATE = "hiredate";
    public static final String SALARY = "salary";
    public static final String MANAGER = "manager";
    public static final String DEPARTMENT = "department";
    public static final String NAME = "name";
    public static final String LOCATION = "location";
    public static final String ALL_SORT_BY_HIREDATE = "SELECT * FROM employee ORDER BY hiredate";
    public static final String ALL_SORT_BY_LASTNAME = "SELECT * FROM employee ORDER BY lastname";
    public static final String ALL_SORT_BY_SALARY = "SELECT * FROM employee ORDER BY salary";
    public static final String ALL_SORT_BY_DEPARTMENT_NAME_AND_LASTNAME =
            "SELECT * FROM employee LEFT JOIN department ON employee.department = department.id ORDER BY department.name, employee.lastname";
    public static final String FROM_EMPLOYEE_WHERE_DEPARTMENT = "SELECT * FROM employee WHERE department = ";
    public static final String ORDER_BY_HIREDATE = " ORDER BY hiredate";
    public static final String ORDER_BY_SALARY = " ORDER BY salary";
    public static final String ORDER_BY_LASTNAME = " ORDER BY lastname";
    public static final String FROM_EMPLOYEE_WHERE_MANAGER = "SELECT * FROM employee WHERE manager = ";
    public static final String FROM_EMPLOYEE_WHERE_ID = "SELECT * FROM employee WHERE id = ";
    public static final String FROM_DEPARTMENT_WHERE_ID = "SELECT * FROM department WHERE id = ";
    public static final String ORDER_BY_SALARY_DESC = " ORDER BY salary DESC";
}
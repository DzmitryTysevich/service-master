package com.epam.rd.autocode.service;

public class ServiceFactory {

    public EmployeeService employeeService(){
        return new EmployeeServiceImpl();
    }
}
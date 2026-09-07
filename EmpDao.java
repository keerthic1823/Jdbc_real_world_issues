package com.DAO;

import java.util.List;

import com.Model.Emp;

public interface EmpDao {

    // #1
    boolean updateSalary(int eid, float salary);

    // #2
    boolean addEmployee(Emp e);

    // #3
    Emp searchEmployee(int eid);

    // #4
    boolean transferEmployeeWithConsistency(
            int eid, String newdept);

    // #5
    void employeeDepartmentReport();

    // #6
    boolean updateEmployeeWithLock(
            int eid,
            String ename,
            float salary,
            String dept);

    // #7
    boolean processOrder(
            int productId,
            int quantity,
            boolean paymentSuccessful);

    // #8
    boolean batchUpdateEmployees(List<Emp> employees);

    // #9
    void searchEmployees(
            String name,
            String dept,
            Float minSalary,
            Float maxSalary,
            String joiningDate);

    // #10
    boolean updateEmployeeSafely(Emp e);
}
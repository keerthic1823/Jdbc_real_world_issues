package com.Utility;

import com.DAO.EmpDao;
import com.DAO.EmpDaoImpl;
import com.Model.Emp;

public class EmpTest {

    public static void main(String[] args) {

    	EmpDao dao = new EmpDaoImpl();

        Emp e = new Emp();

        e.setEid(101);
        e.setEname("Rahul");
        e.setSalary(25000);
        e.setDept("IT");


        // Add Employee
        System.out.println(
                "Add Employee: "
                + dao.addEmployee(e));


        // Get All Employees
        System.out.println("\nAll Employees:");

        for (Emp emp : dao.getEmployees()) {

            System.out.println(emp);
        }


        // Get Employee By ID
        System.out.println("\nEmployee By ID:");

        System.out.println(
                dao.getEmployeeById(101));


        // Update Salary
        System.out.println("\nUpdate Salary:");

        System.out.println(
                dao.updateSalary(101, 30000));


        // Transfer Employee
        System.out.println("\nTransfer Employee:");

        System.out.println(
                dao.transferEmployee(101, "HR"));


        // Invalid Salary
        System.out.println("\nInvalid Salary:");

        System.out.println(
                dao.updateEmployeeSalary(101, -5000));


        // Delete Employee
        // System.out.println(
        //        dao.deleteEmployee(101));
    }
}
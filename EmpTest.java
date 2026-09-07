package com.Utility;

import java.util.ArrayList;
import java.util.List;

import com.DAO.EmpDao;
import com.DAO.EmpDaoImpl;
import com.Model.Emp;

public class EmpTest {

    public static void main(String[] args) {

        EmpDao dao = new EmpDaoImpl();


        // =====================================================
        // #1 — Data Validation
        // =====================================================

        System.out.println(
                dao.updateSalary(101, 30000)
        );


        // =====================================================
        // #2 — Duplicate Data
        // =====================================================

        Emp e = new Emp();

        e.setEid(101);
        e.setEname("Rahul");
        e.setSalary(25000);
        e.setDept("IT");

        System.out.println(
                dao.addEmployee(e)
        );


        // =====================================================
        // #3 — Search Performance
        // =====================================================

        Emp result = dao.searchEmployee(101);

        if (result != null) {
            System.out.println(result);
        }


        // =====================================================
        // #4 — Data Consistency
        // =====================================================

        System.out.println(
                dao.transferEmployeeWithConsistency(
                        101,
                        "HR"
                )
        );


        // =====================================================
        // #5 — Reporting
        // =====================================================

        dao.employeeDepartmentReport();


        // =====================================================
        // #6 — Concurrency
        // =====================================================

        System.out.println(
                dao.updateEmployeeWithLock(
                        101,
                        "Rahul Kumar",
                        35000,
                        "IT"
                )
        );


        // =====================================================
        // #7 — Transaction Failure
        // =====================================================

        System.out.println(
                dao.processOrder(
                        1,
                        2,
                        true
                )
        );


        // =====================================================
        // #8 — Batch Processing
        // =====================================================

        List<Emp> employees = new ArrayList<>();

        Emp e1 = new Emp();
        e1.setEid(101);
        e1.setEname("Rahul");
        e1.setSalary(40000);
        e1.setDept("IT");

        Emp e2 = new Emp();
        e2.setEid(102);
        e2.setEname("Kiran");
        e2.setSalary(45000);
        e2.setDept("HR");

        employees.add(e1);
        employees.add(e2);

        System.out.println(
                dao.batchUpdateEmployees(employees)
        );


        // =====================================================
        // #9 — Dynamic Filtering
        // =====================================================

        dao.searchEmployees(
                "Rahul",
                "IT",
                20000f,
                50000f,
                null
        );


        // =====================================================
        // #10 — Production Bug
        // =====================================================

        Emp e3 = new Emp();

        e3.setEid(101);
        e3.setEname("Rahul Updated");
        e3.setSalary(50000);
        e3.setDept("Finance");

        System.out.println(
                dao.updateEmployeeSafely(e3)
        );
    }
}
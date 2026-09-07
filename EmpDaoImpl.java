package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.Connection.EmpConnection;
import com.Model.Emp;

public class EmpDaoImpl implements EmpDao {
	EmpConnection e = new EmpConnection();

    Connection con = e.getConnection();

//#1 data Validation 
//Invalid employee Id is being accepted
    @Override
    public boolean updateSalary(int eid, float salary) {

        int count = 0;

        try {

            PreparedStatement ps =
                    con.prepareStatement(
                            "select eid from emp where eid=?");

            ps.setInt(1, eid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                count++;
            }

            if (count > 0) {

                PreparedStatement ps1 =
                        con.prepareStatement(
                                "update emp set salary=? where eid=?");

                ps1.setFloat(1, salary);
                ps1.setInt(2, eid);

                int n = ps1.executeUpdate();

                if (n > 0) {

                    System.out.println(
                            n + " row affected");

                    return true;
                }

            } else {

                System.out.println("Invalid eid");
            }

        } catch (Exception e) {

            System.out.println(e);
        }

        return false;
    }
    // #2 — Duplicate Data
 // HR is accidentally creating duplicate employee records
  //when accidentally run code 2 times data also inserted 2 times.
     @Override
     public boolean addEmployee(Emp e) {

         int count = 0;

         try {

             PreparedStatement ps3 =
                     con.prepareStatement(
                             "select eid from emp where eid=?");

             ps3.setInt(1, e.getEid());

             ResultSet rs2 = ps3.executeQuery();

             while (rs2.next()) {
                 count++;
             }

             if (count > 0) {

                 System.out.println(
                         "Employee already inserted!");

             } else {

                 PreparedStatement ps =
                         con.prepareStatement(
                                 "insert into emp values(?,?,?,?)");

                 ps.setInt(1, e.getEid());
                 ps.setString(2, e.getEname());
                 ps.setFloat(3, e.getSalary());
                 ps.setString(4, e.getDept());

                 int n = ps.executeUpdate();

                 if (n > 0) {
                     return true;
                 }
             }

         } catch (Exception e1) {

             System.out.println(e1);
         }

         return false;
     }

    // =====================================================
    // #3 — Search Performance
    // Employee search is timing out when searching
    // through thousands of records.
    // =====================================================

    @Override
    public Emp searchEmployee(int eid) {

        try {

            PreparedStatement ps =
                    con.prepareStatement(
                            "select eid, ename, salary, dept " +
                            "from emp where eid=?");

            ps.setInt(1, eid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Emp e = new Emp();

                e.setEid(rs.getInt("eid"));
                e.setEname(rs.getString("ename"));
                e.setSalary(rs.getFloat("salary"));
                e.setDept(rs.getString("dept"));

                return e;
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }


    // =====================================================
    // #4 — Data Consistency
    // An employee transfer updates the department but
    // sometimes leaves related records inconsistent.
    // =====================================================

    @Override
    public boolean transferEmployeeWithConsistency(
            int eid, String newdept) {

        try {

            con.setAutoCommit(false);

            PreparedStatement ps1 =
                    con.prepareStatement(
                            "select eid from emp where eid=?");

            ps1.setInt(1, eid);

            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                con.rollback();
                return false;
            }

            PreparedStatement ps2 =
                    con.prepareStatement(
                            "update emp set dept=? where eid=?");

            ps2.setString(1, newdept);
            ps2.setInt(2, eid);

            int n1 = ps2.executeUpdate();

            if (n1 == 0) {
                con.rollback();
                return false;
            }

            /*
             * Update related employee records here.
             * Both operations belong to the same transaction.
             */

            con.commit();

            return true;

        } catch (Exception e) {

            try {
                con.rollback();
            } catch (Exception ex) {
                System.out.println(ex);
            }

            System.out.println(e);
            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    // =====================================================
    // #5 — Reporting
    // Management needs a report combining employee and
    // department information.
    // =====================================================

    @Override
    public void employeeDepartmentReport() {

        try {

            String sql =
                    "select e.eid, e.ename, e.salary, " +
                    "e.dept, d.dname, d.loc " +
                    "from emp e " +
                    "join dept d on e.dept=d.dname";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("eid") + " " +
                        rs.getString("ename") + " " +
                        rs.getFloat("salary") + " " +
                        rs.getString("dept") + " " +
                        rs.getString("dname") + " " +
                        rs.getString("loc")
                );
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // =====================================================
    // #6 — Concurrency
    // Two HR users can modify the same employee
    // simultaneously, causing lost updates.
    // =====================================================

    @Override
    public boolean updateEmployeeWithLock(
            int eid, String ename, float salary, String dept) {

        try {

            con.setAutoCommit(false);

            PreparedStatement ps1 =
                    con.prepareStatement(
                            "select eid from emp " +
                            "where eid=? for update");

            ps1.setInt(1, eid);

            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                con.rollback();
                return false;
            }

            PreparedStatement ps2 =
                    con.prepareStatement(
                            "update emp " +
                            "set ename=?, salary=?, dept=? " +
                            "where eid=?");

            ps2.setString(1, ename);
            ps2.setFloat(2, salary);
            ps2.setString(3, dept);
            ps2.setInt(4, eid);

            int n = ps2.executeUpdate();

            if (n > 0) {

                con.commit();
                return true;
            }

            con.rollback();

        } catch (Exception e) {

            try {
                con.rollback();
            } catch (Exception ex) {
                System.out.println(ex);
            }

            System.out.println(e);
            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return false;
    }


    // =====================================================
    // #7 — Transaction Failure
    // During order processing, stock gets reduced even
    // when payment fails.
    // =====================================================

    @Override
    public boolean processOrder(
            int productId,
            int quantity,
            boolean paymentSuccessful) {

        try {

            con.setAutoCommit(false);

            PreparedStatement ps1 =
                    con.prepareStatement(
                            "update product " +
                            "set stock=stock-? " +
                            "where product_id=? " +
                            "and stock>=?");

            ps1.setInt(1, quantity);
            ps1.setInt(2, productId);
            ps1.setInt(3, quantity);

            int n = ps1.executeUpdate();

            if (n == 0) {
                con.rollback();
                return false;
            }

            if (!paymentSuccessful) {

                con.rollback();

                System.out.println(
                        "Payment failed. Stock restored.");

                return false;
            }

            PreparedStatement ps2 =
                    con.prepareStatement(
                            "insert into orders(product_id, quantity) " +
                            "values(?,?)");

            ps2.setInt(1, productId);
            ps2.setInt(2, quantity);

            ps2.executeUpdate();

            con.commit();

            return true;

        } catch (Exception e) {

            try {
                con.rollback();
            } catch (Exception ex) {
                System.out.println(ex);
            }

            System.out.println(e);
            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    // =====================================================
    // #8 — Batch Processing
    // Company receives CSV containing 50,000 employee
    // updates and current implementation takes too long.
    // =====================================================

    @Override
    public boolean batchUpdateEmployees(
            java.util.List<Emp> employees) {

        try {

            con.setAutoCommit(false);

            PreparedStatement ps =
                    con.prepareStatement(
                            "update emp " +
                            "set ename=?, salary=?, dept=? " +
                            "where eid=?");

            for (Emp e : employees) {

                ps.setString(1, e.getEname());
                ps.setFloat(2, e.getSalary());
                ps.setString(3, e.getDept());
                ps.setInt(4, e.getEid());

                ps.addBatch();
            }

            ps.executeBatch();

            con.commit();

            return true;

        } catch (Exception e) {

            try {
                con.rollback();
            } catch (Exception ex) {
                System.out.println(ex);
            }

            System.out.println(e);
            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    // =====================================================
    // #9 — Dynamic Filtering
    // Admin needs to search employees using any combination
    // of name, department, salary range and joining date.
    // =====================================================

    @Override
    public void searchEmployees(
            String name,
            String dept,
            Float minSalary,
            Float maxSalary,
            String joiningDate) {

        try {

            StringBuilder sql =
                    new StringBuilder(
                            "select * from emp where 1=1");

            java.util.List<Object> params =
                    new java.util.ArrayList<>();


            if (name != null && !name.isEmpty()) {

                sql.append(" and ename like ?");
                params.add("%" + name + "%");
            }


            if (dept != null && !dept.isEmpty()) {

                sql.append(" and dept=?");
                params.add(dept);
            }


            if (minSalary != null) {

                sql.append(" and salary>=?");
                params.add(minSalary);
            }


            if (maxSalary != null) {

                sql.append(" and salary<=?");
                params.add(maxSalary);
            }


            if (joiningDate != null &&
                    !joiningDate.isEmpty()) {

                sql.append(" and joining_date=?");
                params.add(joiningDate);
            }


            PreparedStatement ps =
                    con.prepareStatement(sql.toString());


            for (int i = 0; i < params.size(); i++) {

                ps.setObject(i + 1, params.get(i));
            }


            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                System.out.println(
                        rs.getInt("eid") + " " +
                        rs.getString("ename") + " " +
                        rs.getFloat("salary") + " " +
                        rs.getString("dept")
                );
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // =====================================================
    // #10 — Production Bug
    // A failed database operation sometimes leaves the
    // application in an inconsistent state.
    // =====================================================

    @Override
    public boolean updateEmployeeSafely(Emp e) {

        try {

            con.setAutoCommit(false);

            PreparedStatement ps =
                    con.prepareStatement(
                            "update emp " +
                            "set ename=?, salary=?, dept=? " +
                            "where eid=?");

            ps.setString(1, e.getEname());
            ps.setFloat(2, e.getSalary());
            ps.setString(3, e.getDept());
            ps.setInt(4, e.getEid());

            int n = ps.executeUpdate();

            if (n == 0) {

                con.rollback();
                return false;
            }

            con.commit();

            return true;

        } catch (Exception ex) {

            try {
                con.rollback();
            } catch (Exception rollbackEx) {
                System.out.println(rollbackEx);
            }

            System.out.println(ex);

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
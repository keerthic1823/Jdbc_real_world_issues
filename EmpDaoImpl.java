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


    @Override
    public List<Emp> getEmployees() {

        List<Emp> list = new ArrayList<>();

        try {

            PreparedStatement ps =
                    con.prepareStatement(
                            "select * from emp");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Emp e = new Emp();

                e.setEid(rs.getInt(1));
                e.setEname(rs.getString(2));
                e.setSalary(rs.getFloat(3));
                e.setDept(rs.getString(4));

                list.add(e);
            }

        } catch (Exception e) {

            System.out.println(e);
        }

        return list;
    }


    @Override
    public Emp getEmployeeById(int eid) {

        Emp e = new Emp();

        try {

            PreparedStatement ps =
                    con.prepareStatement(
                            "select * from emp where eid=?");

            ps.setInt(1, eid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                e.setEid(rs.getInt(1));
                e.setEname(rs.getString(2));
                e.setSalary(rs.getFloat(3));
                e.setDept(rs.getString(4));

                return e;
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return e;
    }


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


    @Override
    public boolean deleteEmployee(int eid) {

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
                                "delete from emp where eid=?");

                ps1.setInt(1, eid);

                int n = ps1.executeUpdate();

                if (n > 0) {

                    System.out.println(
                            n + " row deleted");

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


    @Override
    public boolean updateEmployee(Emp ex) {

        int count = 0;

        try {

            PreparedStatement ps5 =
                    con.prepareStatement(
                            "select eid from emp where eid=?");

            ps5.setInt(1, ex.getEid());

            ResultSet rs3 = ps5.executeQuery();

            while (rs3.next()) {
                count++;
            }

            if (count > 0) {

                PreparedStatement ps7 =
                        con.prepareStatement(
                                "update emp set ename=?, salary=?, dept=? where eid=?");

                ps7.setString(1, ex.getEname());
                ps7.setFloat(2, ex.getSalary());
                ps7.setString(3, ex.getDept());
                ps7.setInt(4, ex.getEid());

                int n = ps7.executeUpdate();

                if (n > 0) {

                    System.out.println(
                            n + " rows affected");

                    return true;
                }

            } else {

                System.out.println("Invalid eid");
            }

        } catch (Exception e5) {

            System.out.println(e5);
        }

        return false;
    }


    @Override
    public boolean transferEmployee(int eid, String newdept) {

        try {

            if (newdept == null || newdept.isEmpty()) {

                System.out.println(
                        "Invalid department");

                return false;
            }

            PreparedStatement ps3 =
                    con.prepareStatement(
                            "select dept from emp where eid=?");

            ps3.setInt(1, eid);

            ResultSet rs2 = ps3.executeQuery();

            if (rs2.next()) {

                String currentDept =
                        rs2.getString("dept");

                if (newdept.equals(currentDept)) {

                    System.out.println(
                            "Same department, cannot transfer!");

                    return false;
                }

                PreparedStatement ps4 =
                        con.prepareStatement(
                                "update emp set dept=? where eid=?");

                ps4.setString(1, newdept);
                ps4.setInt(2, eid);

                int n = ps4.executeUpdate();

                if (n > 0) {

                    System.out.println(
                            n + " row affected!");

                    return true;
                }

            } else {

                System.out.println(
                        "Employee ID does not exist");
            }

        } catch (Exception e) {

            System.out.println(e);
        }

        return false;
    }


    @Override
    public boolean updateEmployeeSalary(
            int eid, float newsalary) {

        int count = 0;

        try {

            PreparedStatement ps3 =
                    con.prepareStatement(
                            "select eid from emp where eid=?");

            ps3.setInt(1, eid);

            ResultSet rs2 = ps3.executeQuery();

            while (rs2.next()) {
                count++;
            }

            if (count > 0) {

                PreparedStatement ps4 =
                        con.prepareStatement(
                                "update emp set salary=? where eid=?");

                if (newsalary > 0) {

                    ps4.setFloat(1, newsalary);

                } else {

                    System.out.println(
                            "Invalid salary entered");

                    return false;
                }

                ps4.setInt(2, eid);

                int n = ps4.executeUpdate();

                if (n > 0) {

                    System.out.println(
                            n + " rows affected!");

                    return true;
                }

            } else {

                System.out.println(
                        "eid does not exist");
            }

        } catch (Exception e) {

            System.out.println(e);
        }

        return false;
    }
}
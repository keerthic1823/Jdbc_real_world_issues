package com.DAO;

import java.util.List;

import com.Model.Emp;

public interface EmpDao {
public abstract boolean addEmployee(Emp e);
public abstract List<Emp> getEmployees();
public abstract Emp getEmployeeById(int eid);
public abstract boolean updateSalary(int eid, float salary);
public abstract boolean deleteEmployee(int eid);
public abstract boolean updateEmployee(Emp e);
public boolean transferEmployee(int eid, String newdept);
public boolean updateEmployeeSalary(int eid, float newsalary);
}

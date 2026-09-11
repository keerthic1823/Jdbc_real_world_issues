package com.Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class EmpConnection {
Connection con;
public Connection getConnection() {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/batch73","root","your_db_password");
	}
	catch(Exception e) {
		System.out.print(e);
	}
	return con;
}
}

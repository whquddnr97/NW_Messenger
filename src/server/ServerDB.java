package server;

import java.sql.*;

public class ServerDB
{
	private final String driver = "com.mysql.cj.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/messenger?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
	private final String user = "root";
	private final String password = "a1s2d3f4g5h6j7";
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	ServerDB()
	{
		connectDB();
	}
	
	public void connectDB()
	{
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			System.out.println(con);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void login(ServerDB DB, String id, String password) throws SQLException
	{
		String search = "select id, password from user where id = '" + id + "';";
		ResultSet rs = stmt.executeQuery(search);
		if (rs.next())
		{
			if (password.equals(rs.getString("password")))
			{
				System.out.println("Success");
			}
		}
	}
}

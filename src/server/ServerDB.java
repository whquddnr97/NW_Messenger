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
	
	/*DB�� ���� ����*/
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
	
	/*Ŭ���̾�Ʈ���� �α��� ��û�� ���� ��, select���� �̿��Ͽ� id�� ã��
	 * �ش� id�� ��й�ȣ�� ��ġ�� ��, true�� ��ȯ*/
	public boolean login(ServerDB DB, String id, String password) throws SQLException
	{
		String search = "select id, password from user where id = '" + id + "';";
		ResultSet rs = stmt.executeQuery(search);
		if (rs.next())
		{
			if (password.equals(rs.getString("password")))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
	public String findName(ServerDB DB, String id) throws SQLException
	{
		String name = null;
		String search = "select id, name from user where id = '" + id + "';";
		ResultSet rs = stmt.executeQuery(search);
		if (rs.next())
		{
			if (name.equals(rs.getString("name")))
			{
				return name;
			}
		}
		return name;
	}
	
	/*Ŭ���̾�Ʈ���� ��й�ȣ ã�� ��û�� ���� ��, select���� �̿��Ͽ� id�� ã��
	 * �ش� id�� �̸����� ��ġ�� ��, ��й�ȣ�� ��ȯ*/
	public String findPw(ServerDB DB, String id, String email) throws SQLException
	{
		String search = "select id, password, email from user where id = '" + id + "';";
		ResultSet rs = stmt.executeQuery(search);
		if (rs.next())
		{
			if (email.equals(rs.getString("email")))
			{
				return rs.getString("password");
			}
			else
			{
				return "";
			}
		}
		return "";
	}
	
	/*Ŭ���̾�Ʈ���� ȸ������ ��û�� ���� ��, ���ڿ��� �Ľ��Ͽ� �� �׸��� ã�� �� insert���� �̿��Ͽ� DB�� ����*/
	public void register(ServerDB DB, String register) throws SQLException
	{
		String[] registerArray = register.split(" ");
		String id = registerArray[1];
		String password = registerArray[2];
		String nickName = registerArray[3];
		String name = registerArray[4];
		String email = registerArray[5];
		String birth = registerArray[6];
		String phoneNumber = registerArray[7];
		String homepage = registerArray[8];
		String additional = registerArray[9];
		String sql = "insert into user(id, password, nickName, name, email, birth, phoneNumber, homepage, additional) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement pstmt = DB.con.prepareStatement(sql);
		
		pstmt.setString(1, id);
		pstmt.setString(2, password);
		pstmt.setString(3, nickName);
		pstmt.setString(4, name);
		pstmt.setString(5, email);
		pstmt.setString(6, birth);
		pstmt.setString(7, phoneNumber);
		pstmt.setString(8, homepage);
		pstmt.setString(9, additional);
		
		int res = pstmt.executeUpdate();
	}
}

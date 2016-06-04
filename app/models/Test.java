package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.db.DB;

public class Test {
	private final static String SELECT_TEST_BY_ID_USER = "SELECT * FROM test WHERE createby = ?";
	private final static String INSERT_TEST = "INSERT INTO test(title,id_module,id_chapter,createby,isenable) VALUES (?, ?, ?, ?, ?)";
	private final static String UPDATE_IS_ENABLE = "UPDATE test SET isenable = ? WHERE createby = ? AND id_test = ?";
	private final static String DELETE_TEST = "DELETE FROM test WHERE createby=? AND id_test=?";
	
	private String title;
	private int id_module;
	private int id_chapter;
	private int createby;
	private String isenable;
	private int id_test;
	
	public Test(String title, int id_module, int id_chapter, int createby, String isenable){
		this.title = title;
		this.id_module = id_module;
		this.id_chapter = id_chapter;
		this.createby = createby;
		this.isenable = isenable;
	}
	
	public Test(int id_test, String title, int id_module, int id_chapter, int createby, String isenable){
		this.id_test = id_test;
		this.title = title;
		this.id_module = id_module;
		this.id_chapter = id_chapter;
		this.createby = createby;
		this.isenable = isenable;
	}
	
	public String getTitle() {
		return title;
	}

	public int getId_module() {
		return id_module;
	}

	public int getId_chapter() {
		return id_chapter;
	}

	public String getIsenable() {
		return isenable;
	}

	public int getId_test() {
		return id_test;
	}
	
	public static ArrayList<Test> getTestByIduser(int id_user,int idtest) throws SQLException{
		String add_sql = "";
		if(idtest != 0){
			add_sql = " AND id_test = '"+idtest+"'";
  	  	}
		Connection connection = DB.getConnection();
		PreparedStatement statement = connection.prepareStatement(SELECT_TEST_BY_ID_USER+add_sql);;	
		ArrayList<Test> list = new ArrayList<Test>();
		statement.setInt(1,id_user);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
		        int id_module = rs.getInt("id_module");
				int id_test = rs.getInt("id_test");
				int id_chapter = rs.getInt("id_chapter");
				String title = rs.getString("title");
				String isenable = rs.getString("isenable");
				Test test = new Test(id_test,title,id_module, id_chapter, id_user, isenable);
				//add each test to the list
				list.add(test);
		}
		statement.close();
		closeConnection(connection);
		return list;
	}
	
	public int insert() throws SQLException{
		Connection connection = DB.getConnection();;
		PreparedStatement statement = connection.prepareStatement(INSERT_TEST,Statement.RETURN_GENERATED_KEYS);
		int id = -1;
		statement.setString(1,this.title);
		statement.setInt(2,this.id_module);
		statement.setInt(3,this.id_chapter);
		statement.setInt(4,this.createby);
		statement.setString(5,this.isenable);
		statement.executeUpdate();
		ResultSet resultat = statement.getGeneratedKeys();
		if(resultat.next()){
			id = resultat.getInt(1);
		}
		statement.close();
		closeConnection(connection);
		return id;
	}
	
	public static int updateIsenable(String isenable, int id_user, int id_test) throws SQLException{
		int id = 1;
		Connection connection = DB.getConnection();
		PreparedStatement statement = connection.prepareStatement(UPDATE_IS_ENABLE);
    	statement.setString(1,isenable);
    	statement.setInt(2,id_user);
    	statement.setInt(3,id_test);
    	statement.executeUpdate();
    	statement.close();
    	closeConnection(connection);
    	return id;
	}
	
	public static void closeConnection(Connection conn) throws SQLException{
		if(conn != null){
			conn.close();
		}
	}
	
	public static void delete(int id_user, int id_test) throws SQLException{
  	  Connection connection = DB.getConnection();
  	  PreparedStatement statement = connection.prepareStatement(DELETE_TEST);
  	  statement.setInt(1,id_user);
  	  statement.setInt(2,id_test);
  	  statement.executeUpdate();
  	  statement.close();
  	  closeConnection(connection);
	}
}

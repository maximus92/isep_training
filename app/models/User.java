package models;

import play.mvc.*;
import views.html.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.*;

public class User {
	private final static String INSERT_USER = "INSERT INTO user(username,firstname,lastname,token,isprof) VALUES (?, ?, ?, ?, ?)";
	private final static String GET_USER_BY_USERNAME = "SELECT * FROM user WHERE username= ? ";
	private final static String GET_USER_BY_TOKEN = "SELECT * FROM user WHERE token= ? ";
	
	private String username;
	private String firstname;
	private String lastname;
	private int isProf;
	private String token;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getIsProf() {
		return isProf;
	}

	public void setIsProf(int isProf) {
		this.isProf = isProf;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void insert() throws SQLException{
		  Connection connection = DB.getConnection();
		  PreparedStatement stmt = connection.prepareStatement(INSERT_USER);
		  stmt.setString(1, this.username);
		  stmt.setString(2, this.firstname);
		  stmt.setString(3, this.lastname);
		  stmt.setString(4, this.token);
		  stmt.setInt(5, this.isProf);
	      stmt.executeUpdate();
	      stmt.close();
	      Model.closeConnection(connection);
	}
	
	public String exist() throws SQLException{
		  String token="";
		  Connection connection = DB.getConnection();
	      PreparedStatement stmt = connection.prepareStatement(GET_USER_BY_USERNAME);
	      stmt.setString(1, this.username);
	      ResultSet rs = stmt.executeQuery();
	      while(rs.next()){
	    	  token = rs.getString("token");
	      }
	      stmt.close();
	      Model.closeConnection(connection);
	      return token;
	}
	
	public static User getUserByToken(String token) throws SQLException{
	      User u = null;
  		  Connection connection = DB.getConnection();
  		  PreparedStatement stmt = connection.prepareStatement(GET_USER_BY_TOKEN);
  		  stmt.setString(1, token);
          ResultSet rs = stmt.executeQuery();
          while(rs.next()){
        	  String username = rs.getString("username");
        	  String firstname = rs.getString("firstname");
        	  String lastname = rs.getString("lastname");
        	  int isProf = rs.getInt("isprof");
        	  u = new User();
        	  u.setUsername(username);
        	  u.setFirstname(firstname);
        	  u.setLastname(lastname);
        	  u.setIsProf(isProf);
        	  u.setToken(token);
          }
          stmt.close();
          Model.closeConnection(connection);
          return u;                
	}
	
	public static int getIdByToken(String token) throws SQLException{
		  int id_user = 0;
  		  Connection conn = DB.getConnection();
  		  PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_TOKEN);
  		  stmt.setString(1, token);
          ResultSet rs = stmt.executeQuery();
          while(rs.next()){
        	  id_user = rs.getInt("id_user");
          }
          stmt.close();
          Model.closeConnection(conn);
          return id_user;          
	}
}

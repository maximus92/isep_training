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
	public String username;
	public String firstname;
	public String lastname;
	public int isProf;
	public String token;
	public static Connection conn = null;
	public static PreparedStatement stmt = null;
	public static ResultSet rs =null;
	
	public User(String username,String firstname, String lastname,int isProf, String uuid){
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.isProf = isProf;
		this.token = uuid;
	}
	
	public String getLastname(){
		return this.lastname;
	}
	
	public String getFirstname(){
		return this.firstname;
	}
	
	public String getToken(){
		return this.token;
	}
	
	public int getIsprof(){
		return this.isProf;
	}
	
	public void setToken(String s){
		this.token = s;
	}
	
	public void insert(){
	      try{
    		  conn = DB.getConnection();
	          stmt = conn.prepareStatement("INSERT INTO user(username,firstname,lastname,token,isprof) "
	          		+ "VALUES ('"+this.username+"', '"+this.firstname+"', '"+this.lastname+"', '"+this.token+"', '"+this.isProf+"')");
	          stmt.executeUpdate();
	          stmt.close();
	      }catch(SQLException e){
	    	  e.printStackTrace();
	      }finally{
	    	  if(conn != null){
	    		  try{
	    			 conn.close(); 
	    		  }catch(SQLException ignore){
	    			  ignore.printStackTrace();
	    		  }
	    	  }
	      }
	}
	
	public String exist(){
		String token="";
	      try{
	    	  conn = DB.getConnection();
	          stmt = conn.prepareStatement("SELECT * FROM user WHERE username='"+this.username+"'");
	          rs = stmt.executeQuery();
	          while(rs.next()){
	        	  token = rs.getString("token");
	          }
	          stmt.close();
	      }catch(SQLException e){
	    	  e.printStackTrace();
	      }finally{
	    	  if(conn != null){
	    		  try{
	    			 conn.close(); 
	    		  }catch(SQLException ignore){
	    			  ignore.printStackTrace();
	    		  }
	    	  }
	      }
          return token;
	}
	
	public static User getUserByToken(String token){
	      User u = null;
	      try{
  		  conn = DB.getConnection();
  		  stmt = conn.prepareStatement("SELECT * FROM user WHERE token='"+token+"'");
          rs = stmt.executeQuery();
          while(rs.next()){
        	  String username = rs.getString("username");
        	  String firstname = rs.getString("firstname");
        	  String lastname = rs.getString("lastname");
        	  int isProf = rs.getInt("isprof");
        	  u = new User(username,firstname,lastname,isProf,token);
          }
          stmt.close();
          return u;          
	      }catch(SQLException e){
	    	  e.printStackTrace();
	    	  return u;
	      }finally{
	    	  if(conn != null){
	    		  try{
	    			 conn.close(); 
	    		  }catch(SQLException ignore){
	    			  ignore.printStackTrace();
	    		  }
	    	  }
	      }
	}
	
	public static int getIdByToken(String token){
		int id_user = 0;
		try{
	  		  conn = DB.getConnection();
	  		  stmt = conn.prepareStatement("SELECT * FROM user WHERE token='"+token+"'");
	          rs = stmt.executeQuery();
	          while(rs.next()){
	        	  id_user = rs.getInt("id_user");
	          }
	          stmt.close();
	          return id_user;          
		      }catch(SQLException e){
		    	  e.printStackTrace();
		    	  return id_user;
		      }finally{
		    	  if(conn != null){
		    		  try{
		    			 conn.close(); 
		    		  }catch(SQLException ignore){
		    			  ignore.printStackTrace();
		    		  }
		    	  }
		      }
	}
}

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
	
	public static ArrayList<Test> select(int id_user){
		Connection connection = null;
		PreparedStatement statement = null;	
		ArrayList<Test> list = new ArrayList<Test>();
	      try{
	    	  connection = DB.getConnection();
	    	  statement = connection.prepareStatement(SELECT_TEST_BY_ID_USER);
	    	  statement.setInt(1,id_user);
	    	  ResultSet rs = statement.executeQuery();
	    	  while (rs.next()) {
	                int id_module = rs.getInt("id_module");
	                int id_test = rs.getInt("id_test");
	                int id_chapter = rs.getInt("id_chapter");
	                String title = rs.getString("title");
	                String isenable = rs.getString("isenable");
	                Logger.debug(id_test+" "+title+" "+id_module+" "+ id_chapter+" "+ id_user+" "+ isenable);
	                Test test = new Test(id_test,title,id_module, id_chapter, id_user, isenable);
	                //add each test to the list
	                list.add(test);
	            }
	    	  statement.close();
	          return list;
	      }catch(SQLException e){
	    	  e.printStackTrace();
	    	  return list;
	      }finally{
	    	  if(connection != null){
	    		  try{
	    			  connection.close(); 
	    		  }catch(SQLException ignore){
	    			  ignore.printStackTrace();
	    		  }
	    	  }
	      }
	}
	
	public int insert(){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
		int id = -1;
      try{
    	  connection = DB.getConnection();
    	  statement = connection.prepareStatement(INSERT_TEST,Statement.RETURN_GENERATED_KEYS);
    	  statement.setString(1,this.title);
    	  statement.setInt(2,this.id_module);
    	  statement.setInt(3,this.id_chapter);
    	  statement.setInt(4,this.createby);
    	  statement.setString(5,this.isenable);
    	  statement.executeUpdate();
    	  resultat = statement.getGeneratedKeys();
    	  if(resultat.next()){
        	   id = resultat.getInt(1);
          }
    	  statement.close();
    	  return id;
      }catch(SQLException e){
    	  e.printStackTrace();
    	  return id;
      }finally{
    	  if(connection != null){
    		  try{
    			  connection.close(); 
    		  }catch(SQLException ignore){
    			  ignore.printStackTrace();
    		  }
    	  }
      }
	}

}

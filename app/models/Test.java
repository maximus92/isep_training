package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import play.db.DB;

public class Test {
	private String INSERT_TEST = "INSERT INTO test(title,id_module,id_chapter,createby,isenable) VALUES (?, ?, ?, ?, ?)";
	private String title;
	private int id_module;
	private int id_chapter;
	private int createby;
	private String isenable;
	
	public Test(String title, int id_module, int id_chapter, int createby, String isenable){
		this.title = title;
		this.id_module = id_module;
		this.id_chapter = id_chapter;
		this.createby = createby;
		this.isenable = isenable;
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

package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import play.db.DB;

public class JoinTestQuestion {
	private String INSERT_JOIN_TEST_QUESTION = "INSERT INTO join_test_question(id_test,id_question) VALUES (?, ?)";
	private int id_question;
	private int id_test;
	
	public JoinTestQuestion(int id_test, int id_question){
		this.id_test = id_test;
		this.id_question = id_question;
	}
	
	public void insert(){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
      try{
    	  connection = DB.getConnection();
    	  statement = connection.prepareStatement(INSERT_JOIN_TEST_QUESTION);
    	  statement.setInt(1,this.id_test);
    	  statement.setInt(2,this.id_question);
    	  statement.executeUpdate();
    	  statement.close();
      }catch(SQLException e){
    	  e.printStackTrace();
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

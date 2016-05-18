package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import play.db.DB;
import play.mvc.*;

public class Answer {
	public String answer;
	public int id_question;
	public String istrue;
	
	public Answer( String answer, int id_question, String istrue){
		this.answer = answer;
		this.id_question = id_question;
		this.istrue = istrue;
	}

	public void insertAnswer(){
		  Connection connection = null;
	      PreparedStatement stmt=null;
	      try{
  		  connection = DB.getConnection();
	          stmt = connection.prepareStatement("INSERT INTO answer(answer, id_question, istrue) "
	          		+ "VALUES ('"+this.answer+"', '"+this.id_question+"', '"+this.istrue+"')");
	          
	          stmt.executeUpdate();
	          stmt.close();
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

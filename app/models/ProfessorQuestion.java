package models;

import play.mvc.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.UUID;

import play.mvc.*;
import views.html.*;
import play.data.*;
import play.db.*;

import javax.inject.Inject;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import play.Logger;

import views.html.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.*;

public class ProfessorQuestion {
	/*public String question;
	public String correction;
	public int level;
	public int id_chapter;
	public int forexam;
	public String file;
	public String createby;
	
	public Question (String question,String correction, int level,int id_chapter, int forexam, String file, String createby){
		this.question = question;
		this.correction = correction;
		this.level = level;
		this.id_chapter = id_chapter;
		this.forexam = forexam;
		this.file = file;
		this.createby = createby;	
	}
	
	public String getQuestion(){
		return this.question;
	}
	

	public void insert(){
		
		
		  Connection conn = null;
	      PreparedStatement stmt=null;
	      try{
  		  conn = DB.getConnection();
	          stmt = conn.prepareStatement("INSERT INTO question(question,correction,level,id_chapter,forexam,file, createby) "
	          		+ "VALUES ('"+this.question+"', '"+this.correction+"', '"+this.level+"', '"+this.id_chapter+"', '"+this.forexam+"', '"+this.file+"',, '"+this.createby+"')");
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
	}*/
	public void select(){
		
		  Connection conn = null;
	      PreparedStatement stmt=null;
	      try{
		  conn = DB.getConnection();
		  stmt = conn.prepareStatement("SELECT * FROM question"); 
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

}

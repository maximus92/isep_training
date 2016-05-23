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

public class Module {
	
	private final static String GET_ALL_MODULES = "SELECT id_module, name FROM module";
	private final static String INSERT_MODULE = "INSERT INTO module(name,id_user) VALUES (?, ?)";
	private final static String DELETE_MODULE = "DELETE FROM module WHERE id_user=? AND id_module=?";
	private final static String SELECT_BY_ID_USER_MODULE = "SELECT * FROM module WHERE id_user=?";
	
	private String module_name;
	private int id_module;
	private int id_user;
	
	public Module(int id_module, String module_name){
		this.module_name = module_name;
		this.id_module = id_module;
	}
	
	public Module(String name, int id_user){
		this.module_name = name;
		this.id_user = id_user;
	}
	
	public Module(int id, String name, int id_user){
		this.id_module = id;
		this.module_name = name;
		this.id_user = id_user;
	}
	
	public String getModule_name() {
		return module_name;
	}

	public int getId_module() {
		return id_module;
	}
	
	public static ArrayList<Module> getAllModules(){
		ArrayList<Module> all_modules = new ArrayList<Module>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try{
			connection = DB.getConnection();
			statement = connection.prepareStatement(GET_ALL_MODULES);
			result = statement.executeQuery();
			while(result.next()){
				Module module = new Module(result.getInt("id_module"), result.getString("name"));
				all_modules.add(module);

			}
			statement.close();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			if(connection != null){
				try{
					connection.close();
				} catch (SQLException ignore){
					ignore.printStackTrace();
				}
			}
		}
		return all_modules;
	}
	
	public int insert(){
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultat = null;
			int id = -1;
	      try{
	    	  connection = DB.getConnection();
	    	  statement = connection.prepareStatement(INSERT_MODULE,Statement.RETURN_GENERATED_KEYS);
	    	  statement.setString(1,this.module_name);
	    	  statement.setInt(2,this.id_user);
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
	
	public static ArrayList<Module> select(int id){
		Connection connection = null;
		PreparedStatement statement = null;	
		ArrayList<Module> list = new ArrayList<Module>();
	      try{
	    	  connection = DB.getConnection();
	    	  statement = connection.prepareStatement(SELECT_BY_ID_USER_MODULE);
	    	  statement.setInt(1,id);
	    	  ResultSet rs = statement.executeQuery();
	    	  while (rs.next()) {
	                int module_id = rs.getInt("id_module");
	                String name = rs.getString("name");
	                Module m = new Module(module_id,name,id);
	                //add each employee to the list
	                list.add(m);
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
	
	public static void delete(int id_user, int id_module){
		Connection connection = null;
		PreparedStatement statement = null;
      try{
    	  connection = DB.getConnection();
    	  statement = connection.prepareStatement(DELETE_MODULE);
    	  statement.setInt(1,id_user);
    	  statement.setInt(2,id_module);
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

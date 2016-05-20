package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.Logger;
import play.db.DB;

public class Module {
	
	private final static String GET_ALL_MODULES = "SELECT id_module, name FROM module";
	
	private static String module_name;
	private static String id_module;
	
	public Module(String module_name, String id_module){
		this.module_name = module_name;
		this.id_module = id_module;
	}
	
	public static ArrayList<ArrayList<String>> getAllModules(){
		ArrayList<ArrayList<String>> all_modules = new ArrayList<ArrayList<String>>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try{
			connection = DB.getConnection();
			statement = connection.prepareStatement(GET_ALL_MODULES);
			result = statement.executeQuery();
			while(result.next()){
				ArrayList<String> array = new ArrayList<String>();
				array.add(Integer.toString(result.getInt("id_module")));
				array.add(result.getString("name"));
				all_modules.add(array);

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
}

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
	
	private String module_name;
	private int id_module;
	
	public String getModule_name() {
		return module_name;
	}

	public int getId_module() {
		return id_module;
	}

	public Module(int id_module, String module_name){
		this.module_name = module_name;
		this.id_module = id_module;
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
}

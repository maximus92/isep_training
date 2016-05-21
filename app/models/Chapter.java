package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.Logger;
import play.db.DB;

public class Chapter {
	
	private static final String GET_CHAPTERS_BY_MODULE_ID = "SELECT id_chapter, chapter_name FROM chapter WHERE id_module = ?";

	public static ArrayList<ArrayList<String>> getChaptersByModuleId(int module_id){
		ArrayList<ArrayList<String>> chapters = new ArrayList<ArrayList<String>>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		
		try{
			connection = DB.getConnection();
			statement = connection.prepareStatement(GET_CHAPTERS_BY_MODULE_ID);
			statement.setInt(1, module_id);
			result = statement.executeQuery();
			
			while(result.next()){
				ArrayList<String> array = new ArrayList<String>();
				array.add(Integer.toString(result.getInt("id_chapter")));
				array.add(result.getString("chapter_name"));
				chapters.add(array);
			}
			statement.close();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException ignore) {
					ignore.printStackTrace();
				}
			}
		}
		return chapters;	
	}
	
}

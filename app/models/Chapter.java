package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.Logger;
import play.db.DB;

public class Chapter {

	private int id_chapter;
	private String chapter_name;
	private int id_module;
	
	public int getId_module() {
		return id_module;
	}

	public int getId_chapter() {
		return id_chapter;
	}

	public String getChapter_name() {
		return chapter_name;
	}
	
	public Chapter(int id_chapter, String chapter_name, int id_module) {
		this.id_chapter = id_chapter;
		this.chapter_name = chapter_name;
		this.id_module = id_module;
	}

	private static final String GET_CHAPTERS_BY_MODULE_ID = "SELECT id_chapter, chapter_name FROM chapter WHERE id_module = ?";
	private static final String GET_ALL_CHAPTERS = "SELECT id_chapter, chapter_name, id_module FROM chapter";

	public static ArrayList<Chapter> getAllChapters() {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {
			connection = DB.getConnection();
			statement = connection.prepareStatement(GET_ALL_CHAPTERS);
			result = statement.executeQuery();

			while (result.next()) {
				Chapter chapter = new Chapter(
						result.getInt("id_chapter"),
						result.getString("chapter_name"), 
						result.getInt("id_module")
				);
				chapters.add(chapter);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException ignore) {
					ignore.printStackTrace();
				}
			}
		}
		return chapters;
	}

	// public static ArrayList<ArrayList<String>> getChaptersByModuleId(int
	// module_id){
	// ArrayList<ArrayList<String>> chapters = new
	// ArrayList<ArrayList<String>>();
	// Connection connection = null;
	// PreparedStatement statement = null;
	// ResultSet result = null;
	//
	//
	// try{
	// connection = DB.getConnection();
	// statement = connection.prepareStatement(GET_CHAPTERS_BY_MODULE_ID);
	// statement.setInt(1, module_id);
	// result = statement.executeQuery();
	//
	// while(result.next()){
	// ArrayList<String> array = new ArrayList<String>();
	// array.add(Integer.toString(result.getInt("id_chapter")));
	// array.add(result.getString("chapter_name"));
	// chapters.add(array);
	// }
	// statement.close();
	// } catch (SQLException e){
	// e.printStackTrace();
	// } finally {
	// if(connection != null){
	// try {
	// connection.close();
	// } catch (SQLException ignore) {
	// ignore.printStackTrace();
	// }
	// }
	// }
	// return chapters;
	// }
	//

}

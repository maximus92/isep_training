package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import play.Logger;
import play.db.DB;

public class Chapter {
	private static final String GET_CHAPTERS_BY_MODULE_ID = "SELECT id_chapter, chapter_name FROM chapter WHERE id_module = ?";
	private static final String GET_ALL_CHAPTERS = "SELECT id_chapter, chapter_name, id_module FROM chapter";
	private static final String INSERT_CHAPTER = "INSERT INTO chapter (chapter_name, id_module) VALUES ( ?, ?)";
	private int id_chapter;
	private String chapter_name;
	private int id_module;
	
	public Chapter(int id_chapter, String chapter_name, int id_module) {
		this.id_chapter = id_chapter;
		this.chapter_name = chapter_name;
		this.id_module = id_module;
	}
	
	public Chapter(String chapter_name, int id_module) {
		this.chapter_name = chapter_name;
		this.id_module = id_module;
	}

	public int getId_module() {
		return id_module;
	}

	public int getId_chapter() {
		return id_chapter;
	}

	public String getChapter_name() {
		return chapter_name;
	}
	
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

	 public static ArrayList<Chapter> getChaptersByModuleId(int module_id) throws SQLException{
		 ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		 Connection connection = DB.getConnection();
		 PreparedStatement statement = connection.prepareStatement(GET_CHAPTERS_BY_MODULE_ID);
		 statement.setInt(1, module_id);
		 ResultSet result = statement.executeQuery();
		
		 while(result.next()){
			 int id_chapter = result.getInt("id_chapter");
			 String chapter_name = result.getString("chapter_name");
			 Chapter chap = new Chapter(id_chapter, chapter_name, module_id);
			 chapters.add(chap);
		 }
		 statement.close();
		 Test.closeConnection(connection);
		 return chapters;
	 }
	 
	 public int insert() throws SQLException{
		 int id = -1;
		 Connection connection = DB.getConnection();
		 PreparedStatement statement = connection.prepareStatement(INSERT_CHAPTER,Statement.RETURN_GENERATED_KEYS);
    	 statement.setString(1,this.chapter_name);
    	 statement.setInt(2,this.id_module);
    	 statement.executeUpdate();
    	 ResultSet resultat = statement.getGeneratedKeys();
    	 if(resultat.next()){
           id = resultat.getInt(1);
         }
    	 statement.close();
    	 Test.closeConnection(connection);
    	 return id;
	      
	 }
	

}

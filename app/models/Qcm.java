package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.db.DB;

public class Qcm {
	
	private static final String GET_RANDOM_QUESTIONS_ID_BY_PARAM = "SELECT id_question FROM question WHERE id_chapter = ? ORDER BY RAND() LIMIT ?";
	
	public static ArrayList<Integer> getQuestionsIdArrayByParam(int id_chapter, int question_num, int question_level){
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		ArrayList<Integer> questionsIdArray = new ArrayList<Integer>();
		
		try{
			connection = DB.getConnection();
			statement = connection.prepareStatement(GET_RANDOM_QUESTIONS_ID_BY_PARAM);
			statement.setInt(1, id_chapter);
			statement.setInt(2, question_num);
			result = statement.executeQuery();
			
			while(result.next()){
				
				questionsIdArray.add(result.getInt("id_question"));
			}
			
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException ignore){
					ignore.printStackTrace();
				}
			}
		}
		
		return questionsIdArray;
	}
	
}

package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.db.DB;

public class Answer {
    private static final String GET_ANSWERS_BY_QUESTION_ID = "SELECT answer, id_answer, istrue "
                                                                   + "FROM answer "
                                                                   + "WHERE id_question = ?";
    public String               answer;
    public int                  id_question;
    public String               istrue;
	public int                  id_answer;

    public Answer( String answer, int id_answer ) {
        this.answer = answer;
        this.id_answer = id_answer;
    }

    public Answer( String answer, int id_question, String istrue ) {
        this.answer = answer;
        this.id_question = id_question;
        this.istrue = istrue;
    }
    public String getIstrue() {
		return istrue;
	}
    public void insertAnswer() {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DB.getConnection();
            stmt = connection.prepareStatement( "INSERT INTO answer(answer, id_question, istrue) "
                    + "VALUES ('" + this.answer + "', '" + this.id_question + "', '" + this.istrue + "')" );

            stmt.executeUpdate();
            stmt.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        } finally {
            if ( connection != null ) {
                try {
                    connection.close();
                } catch ( SQLException ignore ) {
                    ignore.printStackTrace();
                }
            }
        }
    }

    public static ArrayList<Answer> getAnswersByQuestionId( int id_question ) {
        ArrayList<Answer> answers_list = new ArrayList<Answer>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = DB.getConnection();
            statement = connection.prepareStatement( GET_ANSWERS_BY_QUESTION_ID );
            statement.setInt( 1, id_question );
            result = statement.executeQuery();

            while ( result.next() ) {
                Answer answer = new Answer( result.getString( "answer" ), result.getInt( "id_answer" ), result.getString( "istrue" ) );
                answers_list.add( answer );
            }

        } catch ( SQLException e ) {
            e.printStackTrace();
        } finally {
            if ( connection != null ) {
                try {
                    connection.close();
                } catch ( SQLException ignore ) {
                    ignore.printStackTrace();
                }
            }
        }

        return answers_list;
    }
    
    public void updateAnswer(int id_answer) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DB.getConnection();
            stmt = connection.prepareStatement( "UPDATE answer SET answer='"+this.answer+"', istrue ='"+this.istrue+"' WHERE id_answer?" );            
            stmt.setInt( 1, id_answer );

            
            stmt.executeUpdate();
            stmt.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        } finally {
            if ( connection != null ) {
                try {
                    connection.close();
                } catch ( SQLException ignore ) {
                    ignore.printStackTrace();
                }
            }
        }
    }
    
}

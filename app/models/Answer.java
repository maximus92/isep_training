package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.db.DB;

public class Answer {
    private static final String GET_ANSWERS_BY_QUESTION_ID = "SELECT a.answer, a.id_answer, s.isselected "
                                                                   + "FROM answer a "
                                                                   + "INNER JOIN student_qcm_answer s "
                                                                   + "ON a.id_answer = s.id_answer "
                                                                   + "WHERE a.id_question = ?";
    private static final String UPDATE_STUDENT_QCM_ANSWER  = "UPDATE student_qcm_answer "
                                                                   + "SET isselected = ? "
                                                                   + "WHERE id_qcm = ? AND id_answer = ?";
    private static final String GET_STUDENT_QCM_ANSWER     = "SELECT id_student_qcm_answer "
                                                                   + "FROM student_qcm_answer "
                                                                   + "WHERE id_qcm = ? AND id_answer = ?";
    private static final String CREATE_STUDENT_QCM_ANSWER  = "INSERT INTO student_qcm_answer (id_qcm, id_answer) "
                                                                   + "VALUES (?, ?)";

    public String               answer;
    public int                  id_question;
    public String               istrue;
    public int                  id_answer;
    public boolean              is_select;

    public Answer( String answer, int id_answer, boolean is_select ) {
        this.answer = answer;
        this.id_answer = id_answer;
        this.is_select = is_select;
    }

    public Answer( String answer, int id_question, String istrue ) {
        this.answer = answer;
        this.id_question = id_question;
        this.istrue = istrue;
    }

    public void insertAnswer() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        connection = DB.getConnection();
        stmt = connection.prepareStatement( "INSERT INTO answer(answer, id_question, istrue) "
                + "VALUES ('" + this.answer + "', '" + this.id_question + "', '" + this.istrue + "')" );

        stmt.executeUpdate();
        stmt.close();
        if ( connection != null ) {
            connection.close();
        }
    }

    public static ArrayList<Answer> getAnswersByQuestionId( int id_question ) throws SQLException {
        ArrayList<Answer> answers_list = new ArrayList<Answer>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_ANSWERS_BY_QUESTION_ID );
        statement.setInt( 1, id_question );
        result = statement.executeQuery();

        while ( result.next() ) {
            Answer answer = new Answer( result.getString( "a.answer" ), result.getInt( "a.id_answer" ),
                    result.getBoolean( "s.isselected" ) );
            answers_list.add( answer );
        }

        statement.close();

        if ( connection != null ) {
            connection.close();
        }
        return answers_list;
    }

    public static void updateStudentQcmAnswer( int id_qcm, int id_answer, boolean is_selected ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_STUDENT_QCM_ANSWER );
        statement.setInt( 1, id_qcm );
        statement.setInt( 2, id_answer );
        result = statement.executeQuery();

        if ( !result.next() ) {
            statement = connection.prepareStatement( CREATE_STUDENT_QCM_ANSWER );
            statement.setBoolean( 1, is_selected );
            statement.setInt( 2, id_qcm );
            statement.setInt( 3, id_answer );
            statement.executeUpdate();
        } else {
            statement = connection.prepareStatement( UPDATE_STUDENT_QCM_ANSWER );
            statement.setBoolean( 1, is_selected );
            statement.setInt( 2, id_qcm );
            statement.setInt( 3, id_answer );
            statement.executeUpdate();
        }

        statement.close();
        if ( connection != null ) {
            connection.close();
        }
    }
}

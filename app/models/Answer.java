package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import play.db.DB;

public class Answer {
    private static final String GET_ANSWERS_BY_QUESTION_ID = "SELECT answer, id_answer, istrue "
                                                                   + "FROM answer "
                                                                   + "WHERE id_question = ?";
    private static final String UPDATE_STUDENT_QCM_ANSWER  = "UPDATE student_qcm_answer "
                                                                   + "SET isselected = ? "
                                                                   + "WHERE id_qcm = ? AND id_answer = ?";
    private static final String GET_STUDENT_QCM_ANSWER     = "SELECT id_student_qcm_answer "
                                                                   + "FROM student_qcm_answer "
                                                                   + "WHERE id_qcm = ? AND id_answer = ?";
    private static final String CREATE_STUDENT_QCM_ANSWER  = "INSERT INTO student_qcm_answer (id_qcm, id_answer, isselected) "
                                                                   + "VALUES (?, ?, ?)";
    private static final String GET_SELECTED_ANSWERS       = "SELECT s.id_answer, s.isselected "
                                                                   + "FROM student_qcm_answer s "
                                                                   + "INNER JOIN answer a "
                                                                   + "ON a.id_answer = s.id_answer "
                                                                   + "WHERE id_qcm = ? and id_question = ?";

    private static final String INSERT_ANSWER              = "INSERT INTO answer(answer, id_question, istrue) VALUES (?, ?, ?)";

    private String              answer;
    private int                 id_question;
    private String              istrue;
    private int                 id_answer;
    private boolean             is_select;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer( String answer ) {
        this.answer = answer;
    }

    public int getId_question() {
        return id_question;
    }

    public void setId_question( int id_question ) {
        this.id_question = id_question;
    }

    public String getIstrue() {
        return istrue;
    }

    public void setIstrue( String istrue ) {
        this.istrue = istrue;
    }

    public int getId_answer() {
        return id_answer;
    }

    public void setId_answer( int id_answer ) {
        this.id_answer = id_answer;
    }

    public boolean isSelect() {
        return is_select;
    }

    public void setIs_select( boolean is_select ) {
        this.is_select = is_select;
    }

    public void insertAnswer() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        connection = DB.getConnection();
        stmt = connection.prepareStatement( INSERT_ANSWER );
        stmt.setString( 1, this.answer );
        stmt.setInt( 2, this.id_question );
        stmt.setString( 3, this.istrue );
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
            Answer answer = new Answer();
            answer.setAnswer( result.getString( "answer" ) );
            answer.setId_answer( result.getInt( "id_answer" ) );
            answer.setIstrue( result.getString( "istrue" ) );
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
            statement.setInt( 1, id_qcm );
            statement.setInt( 2, id_answer );
            statement.setBoolean( 3, is_selected );
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

    public void updateAnswer( int id_answer ) throws SQLException {

        Connection connection = DB.getConnection();
        PreparedStatement stmt = connection.prepareStatement( "UPDATE answer SET answer='" + this.answer
                + "', istrue ='"
                + this.istrue + "' WHERE id_answer=?" );
        stmt.setInt( 1, id_answer );
        stmt.executeUpdate();
        stmt.close();

        if ( connection != null ) {
            connection.close();
        }
    }

    public static List<Answer> getSelectedAnswers( int id_qcm, int id_question ) throws SQLException {

        List<Answer> questionsSelected = new ArrayList<Answer>();
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( GET_SELECTED_ANSWERS );
        statement.setInt( 1, id_qcm );
        statement.setInt( 2, id_question );
        ResultSet result = statement.executeQuery();

        while ( result.next() ) {
            Answer answer = new Answer();
            answer.setId_answer( result.getInt( "s.id_answer" ) );
            answer.setIs_select( result.getBoolean( "s.isselected" ) );
            questionsSelected.add( answer );
        }

        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return questionsSelected;

    }

}

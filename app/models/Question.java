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

public class Question {
    private static final String GET_QUESTION_BY_ID_TEST                 = "SELECT * FROM question q "
                                                                                + "LEFT JOIN join_test_question jtq ON q.id_question = jtq.id_question "
                                                                                + "WHERE jtq.id_test=? AND q.createby=?";

    private static final String GET_QCM_QUESTION_NUM                    = "SELECT q.question, q.id_question "
                                                                                + "FROM question q "
                                                                                + "INNER JOIN join_qcm_question j "
                                                                                + "ON j.id_question = q.id_question "
                                                                                + "WHERE j.id_qcm = ? ";
    private static final String INSERT_QUESTION                         = "INSERT INTO question(question,correction,level,id_chapter,forexam,file, createby) "
                                                                                + "VALUES (?,?,?,?,?,?,?)";
    private static final String SELECT_QUESTION_BY_ID_QUESTION          = "SELECT * FROM question "
                                                                                + "WHERE id_question=?";
    private static final String SELECT_QUESTION_BY_USER                 = "SELECT * FROM question "
                                                                                + "WHERE createby=?";
    private static final String DELETE_QUESTION_BY_USER_AND_ID_QUESTION = "DELETE FROM question "
                                                                                + "WHERE createby=? AND id_question=?";

    private static final String GET_QUESTIONS_BY_QCM_ID                 = "SELECT q.id_question, q.question, q.correction, j.points FROM question q "
                                                                                + "INNER JOIN join_qcm_question j "
                                                                                + "ON q.id_question = j.id_question "
                                                                                + "WHERE j.id_qcm = ?";
    private static final String UPDATE_QUESTION_BY_ID_QUESTION          = "UPDATE question "
                                                                                + "SET question=?, correction=?, level=?, id_chapter=?, forexam=?, file=? "
                                                                                + " WHERE id_question=?";

    private static final String GET_QUESTIONS_ANSWERED                  = "SELECT id_question "
                                                                                + "FROM join_qcm_question j "
                                                                                + "WHERE id_qcm = ? AND EXISTS( "
                                                                                + "SELECT * "
                                                                                + "FROM answer a "
                                                                                + "INNER JOIN student_qcm_answer s "
                                                                                + "ON s.id_answer = a.id_answer "
                                                                                + "WHERE a.id_question = j.id_question AND s.id_qcm = ?)";
    private static final String SELECT_QUESTION_BY_USER_FILTERED        = "SELECT * FROM question "
                                                                                + "WHERE createby=?";

    private static final String SELECT_QUESTION_BY_ID_CHAPTER_AND_USER  = "SELECT * FROM question WHERE id_chapter= ? AND createby = ? AND forexam = ?";

    private int                 id_question;
    private String              question;
    private String              correction;
    private String              level;
    private String              id_chapter;
    private String              forexam;
    private String              file;
    private int                 createby;
    private int                 points;
    private boolean             answered;

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered( boolean answered ) {
        this.answered = answered;
    }

    public int getId_question() {
        return id_question;
    }

    public void setId_question( int id_question ) {
        this.id_question = id_question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion( String question ) {
        this.question = question;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection( String correction ) {
        this.correction = correction;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel( String level ) {
        this.level = level;
    }

    public String getId_chapter() {
        return id_chapter;
    }

    public void setId_chapter( String id_chapter ) {
        this.id_chapter = id_chapter;
    }

    public String getForexam() {
        return forexam;
    }

    public void setForexam( String forexam ) {
        this.forexam = forexam;
    }

    public String getFile() {
        return file;
    }

    public void setFile( String file ) {
        this.file = file;
    }

    public int getCreateby() {
        return createby;
    }

    public void setCreateby( int createby ) {
        this.createby = createby;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints( int points ) {
        this.points = points;
    }

    public int insertQuestion() throws SQLException {

        int id_question = 0;

        Connection connection = DB.getConnection();
        PreparedStatement stmt = connection.prepareStatement( INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS );

        stmt.setString( 1, this.question );
        stmt.setString( 2, this.correction );
        stmt.setString( 3, this.level );
        stmt.setString( 4, this.id_chapter );
        stmt.setString( 5, this.forexam );
        stmt.setString( 6, this.file );
        stmt.setInt( 7, this.createby );
        stmt.executeUpdate();
        ResultSet resultat = stmt.getGeneratedKeys();
        if ( resultat.next() ) {
            id_question = resultat.getInt( 1 );
        }
        stmt.close();
        return id_question;
    }

    public static ArrayList<Question> selectQuestionByIdQ( int id_question ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Question> list = new ArrayList<Question>();
        connection = DB.getConnection();
        statement = connection.prepareStatement( SELECT_QUESTION_BY_ID_QUESTION );
        statement.setInt( 1, id_question );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            String question = rs.getString( "question" );
            String correction = rs.getString( "correction" );
            String level = rs.getString( "level" );
            String id_chapter = rs.getString( "id_chapter" );
            String forexam = rs.getString( "forexam" );
            String file = rs.getString( "file" );
            Question q = new Question();
            q.setId_question( id_question );
            q.setQuestion( question );
            q.setCorrection( correction );
            q.setLevel( level );
            q.setId_chapter( id_chapter );
            q.setForexam( forexam );
            q.setFile( file );
            // add each employee to the list
            list.add( q );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return list;
    }

    public static ArrayList<Question> select( int id ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Question> list = new ArrayList<Question>();

        connection = DB.getConnection();
        statement = connection.prepareStatement( SELECT_QUESTION_BY_USER );
        statement.setInt( 1, id );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_question = rs.getInt( "id_question" );
            String question = rs.getString( "question" );
            String correction = rs.getString( "correction" );
            String level = rs.getString( "level" );
            String id_chapter = rs.getString( "id_chapter" );
            String forexam = rs.getString( "forexam" );
            String file = rs.getString( "file" );

            Question q = new Question();
            q.setId_question( id_question );
            q.setQuestion( question );
            q.setCorrection( correction );
            q.setLevel( level );
            q.setId_chapter( id_chapter );
            q.setForexam( forexam );
            q.setFile( file );
            // add each employee to the list
            list.add( q );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return list;
    }

    public static void delete( int createby, int id_question ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        connection = DB.getConnection();
        statement = connection.prepareStatement( DELETE_QUESTION_BY_USER_AND_ID_QUESTION );
        statement.setInt( 1, createby );
        statement.setInt( 2, id_question );
        statement.executeUpdate();
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
    }

    public void updateQuestion( int id_question ) throws SQLException {

        Connection connection = DB.getConnection();

        PreparedStatement stmt = connection.prepareStatement( UPDATE_QUESTION_BY_ID_QUESTION );
        stmt.setString( 1, this.question );
        stmt.setString( 2, this.correction );
        stmt.setString( 3, this.level );
        stmt.setString( 4, this.id_chapter );
        stmt.setString( 5, this.forexam );
        stmt.setString( 6, this.file );
        stmt.setInt( 7, id_question );

        stmt.executeUpdate();
        stmt.close();

        if ( connection != null ) {
            connection.close();
        }
    }

    public static ArrayList<Question> selectQuestionByIdTest( int id_test, int id_user ) throws SQLException {
        ArrayList<Question> list = new ArrayList<Question>();
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( GET_QUESTION_BY_ID_TEST );
        statement.setInt( 1, id_test );
        statement.setInt( 2, id_user );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_question = rs.getInt( "id_question" );
            String question = rs.getString( "question" );
            String correction = rs.getString( "correction" );
            String level = rs.getString( "level" );
            String id_chapter = rs.getString( "id_chapter" );
            String forexam = rs.getString( "forexam" );
            String file = rs.getString( "file" );
            Question q = new Question();
            q.setId_question( id_question );
            q.setQuestion( question );
            q.setCorrection( correction );
            q.setLevel( level );
            q.setId_chapter( id_chapter );
            q.setForexam( forexam );
            q.setFile( file );
            // add each employee to the list
            list.add( q );
        }
        statement.close();
        Model.closeConnection( connection );
        return list;
    }

    public void getQcmQuestions( int id_qcm, int question_num ) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_QCM_QUESTION_NUM );
        statement.setInt( 1, id_qcm );
        result = statement.executeQuery();

        result.absolute( question_num );
        this.setQuestion( result.getString( "question" ) );
        this.setId_question( result.getInt( "id_question" ) );

        if ( connection != null ) {
            connection.close();
        }
    }

    public static List<Question> getQuestionsByQcmId( int id_qcm ) throws SQLException {
        List<Question> questions_list = new ArrayList<Question>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_QUESTIONS_BY_QCM_ID );
        statement.setInt( 1, id_qcm );
        result = statement.executeQuery();

        while ( result.next() ) {
            Question question = new Question();
            question.setId_question( result.getInt( "q.id_question" ) );
            question.setQuestion( result.getString( "q.question" ) );
            question.setCorrection( "q.correction" );
            question.setPoints( result.getInt( "j.points" ) );

            questions_list.add( question );
        }
        statement.close();

        if ( connection != null ) {
            connection.close();
        }

        return questions_list;
    }

    public void getQuestionById( int id_question ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( SELECT_QUESTION_BY_ID_QUESTION );
        statement.setInt( 1, id_question );
        result = statement.executeQuery();

        if ( result.next() ) {
            this.setId_question( result.getInt( "id_question" ) );
            this.setCorrection( result.getString( "correction" ) );
            this.setQuestion( result.getString( "question" ) );
        }
        statement.close();

        if ( connection != null ) {
            connection.close();
        }
    }

    public static List<Question> selectQuestionForReview( int id_qcm ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result1 = null;
        ResultSet result2 = null;

        List<Integer> questions_aswered = new ArrayList<Integer>();
        List<Question> questions_list = new ArrayList<Question>();

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_QUESTIONS_ANSWERED );
        statement.setInt( 1, id_qcm );
        statement.setInt( 2, id_qcm );
        result1 = statement.executeQuery();

        while ( result1.next() ) {
            questions_aswered.add( result1.getInt( "id_question" ) );
        }

        statement = connection.prepareStatement( GET_QUESTIONS_BY_QCM_ID );
        statement.setInt( 1, id_qcm );

        result2 = statement.executeQuery();

        while ( result2.next() ) {
            int id_question = result2.getInt( "id_question" );
            Question question = new Question();
            question.setId_question( id_question );
            question.setQuestion( result2.getString( "question" ) );
            if ( questions_aswered.contains( id_question ) ) {
                question.setAnswered( true );
            } else {
                question.setAnswered( false );
            }
            questions_list.add( question );
        }

        statement.close();
        if ( connection != null ) {
            connection.close();
        }

        return questions_list;
    }

    public static ArrayList<Question> filterQuestion( int id ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Question> list = new ArrayList<Question>();

        connection = DB.getConnection();
        statement = connection.prepareStatement( SELECT_QUESTION_BY_USER_FILTERED );
        statement.setInt( 1, id );

        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_question = rs.getInt( "id_question" );
            String question = rs.getString( "question" );
            String correction = rs.getString( "correction" );
            String level = rs.getString( "level" );
            String id_chapter = rs.getString( "id_chapter" );
            String forexam = rs.getString( "forexam" );
            String file = rs.getString( "file" );

            Question q = new Question();
            q.setId_question( id_question );
            q.setQuestion( question );
            q.setCorrection( correction );
            q.setLevel( level );
            q.setId_chapter( id_chapter );
            q.setForexam( forexam );
            q.setFile( file );
            list.add( q );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return list;
    }

    public List<Question> getQuestionByIdChapterAndUser( int id_chapter, int id_user ) throws SQLException {
        ArrayList<Question> list = new ArrayList<Question>();
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( SELECT_QUESTION_BY_ID_CHAPTER_AND_USER );
        statement.setInt( 1, id_chapter );
        statement.setInt( 2, id_user );
        statement.setString( 3, "0" );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_question = rs.getInt( "id_question" );
            String question = rs.getString( "question" );
            Question q = new Question();
            q.setId_question( id_question );
            q.setQuestion( question );
            list.add( q );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return list;
    }
}

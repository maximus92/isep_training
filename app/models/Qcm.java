package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.db.DB;

public class Qcm {

    private int                       id_qcm;
    private int                       time;
    private int                       level;
    private int                       number_of_questions;
    private int                       createby;
    private Integer                   score;
    private int                       good_answer;
    private int                       bad_answer;
    private int                       no_answer;
    private int                       nbanswermax;
    private String                    title;
    private boolean                   exam;
    private String                    module;
    private String                    chapter;
    private Date                      finishat;
    private Integer                   max_score;

    private static ArrayList<Integer> questions_id_array                          = new ArrayList<Integer>();

    private static final String       GET_RANDOM_QUESTIONS_ID_BY_PARAM            = "SELECT id_question "
                                                                                          + "FROM question WHERE id_chapter = ? "
                                                                                          + "ORDER BY RAND() "
                                                                                          + "LIMIT ?";

    private static final String       CREATE_STUDENT_QCM                          = "INSERT INTO qcm (createby, time, nbofquestions, good_answer, bad_answer, no_answer, id_chapter) "
                                                                                          + "VALUES (? ,?, ?, ?, ?, ?, ?)";

    private static final String       STUDENT_QCM_QUESTIONS                       = "INSERT INTO join_qcm_question (id_qcm, id_question) "
                                                                                          + "VALUES (?, ?)";

    private static final String       GET_LAST_QCM_FOR_USER                       = "SELECT id_qcm "
                                                                                          + "FROM qcm "
                                                                                          + "WHERE createby = ? "
                                                                                          + "ORDER BY id_qcm DESC "
                                                                                          + "LIMIT 1";

    private static final String       GET_QCM_INFO_BY_ID                          = "SELECT * "
                                                                                          + "FROM qcm "
                                                                                          + "WHERE id_qcm = ? ";
    private static final String       UPDATE_QCM_TIME                             = "UPDATE qcm "
                                                                                          + "SET time = ? "
                                                                                          + "WHERE id_qcm = ?";
    private static final String       GET_QCM_ID_QUESTIONS                        = "SELECT id_question "
                                                                                          + "FROM join_qcm_question "
                                                                                          + "WHERE id_qcm = ?";
    private static final String       GET_USER_ANSWERS_AND_GOOD_ANSWERS           = "SELECT isselected, istrue "
                                                                                          + "FROM answer a "
                                                                                          + "INNER JOIN student_qcm_answer s "
                                                                                          + "ON a.id_answer = s.id_answer "
                                                                                          + "WHERE id_question = ? AND id_qcm = ?";
    private static final String       UPDATE_QCM_SCORE                            = "UPDATE qcm "
                                                                                          + "SET score = ?, max_score = ?, finishat = NOW() "
                                                                                          + "WHERE id_qcm = ?";
    private static final String       STUDENT_ANSWER_IS_TRUE                      = "UPDATE join_qcm_question "
                                                                                          + "SET points = ? "
                                                                                          + "WHERE id_qcm = ? AND id_question = ?";
    private static final String       CREATE_PROFESSOR_EXAM                       = "INSERT INTO qcm (createby, nbanswermax, time, exam, nbofquestions, good_answer, bad_answer, no_answer, title) "
                                                                                          + "VALUES (? ,?, ?, ?, ?, ?, ?,?, ?)";
    private static final String       SELECT_PROFESSOR_EXAM                       = "SELECT * FROM qcm "
                                                                                          + "WHERE createby=? AND exam=1";
    private static final String       DELETE_EXAM_PROFESSOR_BY_ID_USER_AND_ID_QCM = "DELETE FROM qcm "
                                                                                          + "WHERE createby=? AND id_qcm=?";
    private static final String       GET_END_QCM_BY_USER                         = "SELECT * "
                                                                                          + "FROM qcm "
                                                                                          + "WHERE createby = ? AND finishat IS NOT NULL";
    private static final String       GET_MODULE_AND_CHAPTER_NAME                 = "SELECT name, chapter_name "
                                                                                          + "FROM chapter c "
                                                                                          + "INNER JOIN module m "
                                                                                          + "ON c.id_module = m.id_module "
                                                                                          + "WHERE id_chapter = ?";

    public int getId_qcm() {
        return id_qcm;
    }

    public void setId_qcm( int id_qcm ) {
        this.id_qcm = id_qcm;
    }

    public int getTime() {
        return time;
    }

    public void setTime( int time ) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel( int level ) {
        this.level = level;
    }

    public int getNumber_of_questions() {
        return number_of_questions;
    }

    public void setNumber_of_questions( int number_of_questions ) {
        this.number_of_questions = number_of_questions;
    }

    public int getCreateby() {
        return createby;
    }

    public void setCreateby( int createby ) {
        this.createby = createby;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore( Integer score ) {
        this.score = score;
    }

    public int getGood_answer() {
        return good_answer;
    }

    public void setGood_answer( int good_answer ) {
        this.good_answer = good_answer;
    }

    public int getBad_answer() {
        return bad_answer;
    }

    public void setBad_answer( int bad_answer ) {
        this.bad_answer = bad_answer;
    }

    public int getNo_answer() {
        return no_answer;
    }

    public void setNo_answer( int no_answer ) {
        this.no_answer = no_answer;
    }

    public int getNbanswermax( int nbanswermax ) {
        return nbanswermax;
    }

    public void setNbanswermax( int nbanswermax ) {
        this.nbanswermax = nbanswermax;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public boolean getExam() {
        return exam;
    }

    public void setExam( boolean exam ) {
        this.exam = exam;
    }

    public String getModule() {
        return module;
    }

    public void setModule( String module ) {
        this.module = module;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter( String chapter ) {
        this.chapter = chapter;
    }

    public Date getFinishat() {
        return finishat;
    }

    public void setFinishat( Date finishat ) {
        this.finishat = finishat;
    }

    public Integer getMax_score() {
        return max_score;
    }

    public void setMax_score( Integer max_score ) {
        this.max_score = max_score;
    }

    public static ArrayList<Integer> getQuestionsIdArrayByParam( int id_chapter, int question_num, int question_level )
            throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_RANDOM_QUESTIONS_ID_BY_PARAM );
        statement.setInt( 1, id_chapter );
        statement.setInt( 2, question_num );
        result = statement.executeQuery();

        while ( result.next() ) {

            questions_id_array.add( result.getInt( "id_question" ) );
        }

        statement.close();
        if ( connection != null ) {
            connection.close();
        }

        return questions_id_array;
    }

    public void createStudentQcm( ArrayList<Integer> questionsArray,
            Integer qcm_time, String token, Integer number_questions,
            Integer good_answer, Integer bad_answer, Integer no_answer, int id_chapter ) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        int id_qcm = 0;
        int userId = User.getIdByToken( token );

        connection = DB.getConnection();
        statement = connection.prepareStatement( CREATE_STUDENT_QCM, statement.RETURN_GENERATED_KEYS );
        statement.setInt( 1, userId );
        statement.setInt( 2, qcm_time );
        statement.setInt( 3, number_questions );
        statement.setInt( 4, good_answer );
        statement.setInt( 5, bad_answer );
        statement.setInt( 6, no_answer );
        statement.setInt( 7, id_chapter );
        statement.executeUpdate();

        result = statement.getGeneratedKeys();
        if ( result.next() ) {
            id_qcm = result.getInt( 1 );
        }
        for ( int i = 0; i < questionsArray.size(); i++ ) {
            statement = connection.prepareStatement( STUDENT_QCM_QUESTIONS );
            statement.setInt( 1, id_qcm );
            statement.setInt( 2, questionsArray.get( i ) );
            statement.executeUpdate();
        }

        statement.close();

        if ( connection != null ) {
            connection.close();
        }

    }

    public static int getLastQcmForUser( String token ) throws SQLException {
        int userId = User.getIdByToken( token );
        int id_qcm = -1;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_LAST_QCM_FOR_USER );
        statement.setInt( 1, userId );
        result = statement.executeQuery();

        while ( result.next() ) {
            id_qcm = result.getInt( "id_qcm" );
        }

        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return id_qcm;
    }

    public void getInfoById( int id_qcm ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_QCM_INFO_BY_ID );
        statement.setInt( 1, id_qcm );
        result = statement.executeQuery();

        while ( result.next() ) {
            this.setId_qcm( id_qcm );
            this.setNumber_of_questions( result.getInt( "nbofquestions" ) );
            this.setTime( result.getInt( "time" ) );
            this.setLevel( result.getInt( "level" ) );
            this.setScore( result.getInt( "score" ) );
            this.setGood_answer( result.getInt( "good_answer" ) );
        }

        statement.close();
        if ( connection != null ) {
            connection.close();
        }
    }

    public static void updateQcmTime( int id_qcm, int time ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( UPDATE_QCM_TIME );
        statement.setInt( 1, time );
        statement.setInt( 2, id_qcm );
        statement.executeUpdate();

        statement.close();
        if ( connection != null ) {
            connection.close();
        }
    }

    public void calculateScore() throws SQLException {
        int score = 0;
        int id_qcm = this.getId_qcm();
        Qcm qcm_info = new Qcm();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result1 = null;
        ResultSet result2 = null;
        ResultSet infoResult = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_QCM_INFO_BY_ID );
        statement.setInt( 1, id_qcm );
        infoResult = statement.executeQuery();

        if ( infoResult.next() ) {
            qcm_info.setId_qcm( id_qcm );
            qcm_info.setNumber_of_questions( infoResult.getInt( "nbofquestions" ) );
            qcm_info.setGood_answer( infoResult.getInt( "good_answer" ) );
            qcm_info.setBad_answer( infoResult.getInt( "bad_answer" ) );
            qcm_info.setNo_answer( infoResult.getInt( "no_answer" ) );
            qcm_info.setMax_score( qcm_info.getNumber_of_questions() * qcm_info.getGood_answer() );
        }

        statement = connection.prepareStatement( GET_QCM_ID_QUESTIONS );
        statement.setInt( 1, id_qcm );
        result1 = statement.executeQuery();

        while ( result1.next() ) {
            int as_answered = 0;
            int run_score = 0;
            int counter_answers = 0;
            int id_question = result1.getInt( "id_question" );
            int points_for_question = 0;
            statement = connection.prepareStatement( GET_USER_ANSWERS_AND_GOOD_ANSWERS );
            statement.setInt( 1, id_question );
            statement.setInt( 2, id_qcm );
            result2 = statement.executeQuery();

            while ( result2.next() ) {
                if ( result2.getBoolean( "isselected" ) ) {
                    as_answered++;
                }
                if ( result2.getBoolean( "isselected" ) == result2.getBoolean( "istrue" ) ) {
                    run_score++;
                }
                counter_answers++;
            }

            if ( run_score == counter_answers && counter_answers != 0 ) {
                points_for_question = qcm_info.getGood_answer();
                score += qcm_info.getGood_answer();
            } else if ( counter_answers == 0 || as_answered == 0 ) {
                points_for_question = qcm_info.getNo_answer();
                score += qcm_info.getNo_answer();
            } else {
                points_for_question = qcm_info.getBad_answer();
                score += qcm_info.getBad_answer();
            }

            statement = connection.prepareStatement( STUDENT_ANSWER_IS_TRUE );
            statement.setInt( 1, points_for_question );
            statement.setInt( 2, id_qcm );
            statement.setInt( 3, id_question );
            statement.executeUpdate();
        }

        statement = connection.prepareStatement( UPDATE_QCM_SCORE );
        statement.setInt( 1, score );
        statement.setInt( 2, qcm_info.getMax_score() );
        statement.setInt( 3, id_qcm );
        statement.executeUpdate();

        statement.close();

        if ( connection != null ) {
            connection.close();
        }

        this.setScore( score );

    }

    public void createExam() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        connection = DB.getConnection();
        stmt = connection.prepareStatement( CREATE_PROFESSOR_EXAM );
        stmt.setInt( 1, this.createby );
        stmt.setInt( 2, this.nbanswermax );
        stmt.setInt( 3, this.time );
        stmt.setBoolean( 4, this.exam );
        stmt.setInt( 5, this.number_of_questions );
        stmt.setInt( 6, this.good_answer );
        stmt.setInt( 7, this.bad_answer );
        stmt.setInt( 8, this.no_answer );
        stmt.setString( 9, this.title );

        stmt.executeUpdate();
        stmt.close();
        if ( connection != null ) {
            connection.close();
        }
    }

    public static ArrayList<Qcm> selectExam( int id ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Qcm> list = new ArrayList<Qcm>();

        connection = DB.getConnection();
        statement = connection.prepareStatement( SELECT_PROFESSOR_EXAM );
        statement.setInt( 1, id );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_qcm = rs.getInt( "id_qcm" );
            int nbanswermax = rs.getInt( "nbanswermax" );
            int time = rs.getInt( "time" );
            int good_answer = rs.getInt( "good_answer" );
            int bad_answer = rs.getInt( "bad_answer" );
            int no_answer = rs.getInt( "no_answer" );
            int number_of_question = rs.getInt( "nbofquestions" );
            String title = rs.getString( "title" );

            Qcm q = new Qcm();
            q.setNbanswermax( nbanswermax );
            q.setGood_answer( good_answer );
            q.setBad_answer( bad_answer );
            q.setNo_answer( no_answer );
            q.setNumber_of_questions( number_of_question );
            q.setTitle( title );
            q.setTime( time );
            q.setId_qcm( id_qcm );
            list.add( q );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return list;

    }

    public static void deleteExam( int createby, int id_qcm ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        connection = DB.getConnection();
        statement = connection.prepareStatement( DELETE_EXAM_PROFESSOR_BY_ID_USER_AND_ID_QCM );
        statement.setInt( 1, createby );
        statement.setInt( 2, id_qcm );
        statement.executeUpdate();
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
    }

    public static List<Qcm> getEndQcmByUser( int id_user ) throws SQLException {
        List<Qcm> qcm_list = new ArrayList<Qcm>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result1 = null;
        ResultSet result2 = null;

        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_END_QCM_BY_USER );
        statement.setInt( 1, id_user );
        result1 = statement.executeQuery();

        while ( result1.next() ) {

            Qcm qcm = new Qcm();
            Logger.debug( "idchapter : " + Integer.toString( result1.getInt( "id_chapter" ) ) );

            statement = connection.prepareStatement( GET_MODULE_AND_CHAPTER_NAME );
            statement.setInt( 1, result1.getInt( "id_chapter" ) );
            result2 = statement.executeQuery();

            if ( result2.next() ) {
                Logger.debug( "idqcm : " + Integer.toString( result1.getInt( "id_qcm" ) ) );
                qcm.setId_qcm( result1.getInt( "id_qcm" ) );
                qcm.setFinishat( result1.getDate( "finishat" ) );
                qcm.setScore( result1.getInt( "score" ) );
                qcm.setMax_score( result1.getInt( "max_score" ) );
                qcm.setModule( result2.getString( "name" ) );
                qcm.setChapter( result2.getString( "chapter_name" ) );
            }

            qcm_list.add( qcm );

        }

        return qcm_list;
    }
}

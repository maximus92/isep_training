package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import play.db.DB;

public class Question {
    private static String       GET_QUESTION_BY_ID_TEST = "SELECT * FROM question q "
                                                                + "LEFT JOIN join_test_question jtq ON q.id_question = jtq.id_question "
                                                                + "WHERE jtq.id_test=? AND q.createby=?";

    private static final String GET_QCM_QUESTION_NUM    = "SELECT q.question, q.id_question "
                                                                + "FROM question q "
                                                                + "INNER JOIN join_qcm_question j "
                                                                + "ON j.id_question = q.id_question "
                                                                + "WHERE j.id_qcm = ?";

    public int                  id_question;
    public String               question;
    public String               correction;
    public String               level;
    public String               id_chapter;
    public String               forexam;
    public String               file;
    public int                  createby;

    public Question() {
    }

    public Question( String question, String correction, String level, String id_chapter, String forexam, String file,
            int createby ) {
        this.question = question;
        this.correction = correction;
        this.level = level;
        this.id_chapter = id_chapter;
        this.forexam = forexam;
        this.file = file;
        this.createby = createby;
    }

    public Question( int id_question, String question, String correction, String level, String id_chapter,
            String forexam, String file ) {
        this.id_question = id_question;
        this.question = question;
        this.correction = correction;
        this.level = level;
        this.id_chapter = id_chapter;
        this.forexam = forexam;
        this.file = file;
    }

    public Question( String question ) {
        this.question = question;
    }

    public Question( int id_question, int createby ) {
        this.id_question = id_question;
        this.createby = createby;
    }

    public String getQuestion() {
        return this.question;
    }

    public int getId_question() {
        return id_question;
    }

    public int insertQuestion() throws SQLException {
        int id_question = 0;

        Connection connection = DB.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO question(question,correction,level,id_chapter,forexam,file, createby) "
                        + "VALUES ('" + this.question + "', '" + this.correction + "', '" + this.level + "', '"
                        + this.id_chapter + "', '" + this.forexam + "', '" + this.file + "', '" + this.createby
                        + "')", Statement.RETURN_GENERATED_KEYS );
        stmt.executeUpdate();
        ResultSet resultat = stmt.getGeneratedKeys();
        if ( resultat.next() ) {
            id_question = resultat.getInt( 1 );
        }
        stmt.close();
        return id_question;
    }

    public static ArrayList<Question> selectQuestionByIdQ( int id_question ) {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Question> list = new ArrayList<Question>();
        try {
            connection = DB.getConnection();
            statement = connection.prepareStatement( "SELECT * FROM question WHERE id_question=?" );
            statement.setInt( 1, id_question );
            ResultSet rs = statement.executeQuery();
            while ( rs.next() ) {
                String question = rs.getString( "question" );
                String correction = rs.getString( "correction" );
                String level = rs.getString( "level" );
                String id_chapter = rs.getString( "id_chapter" );
                String forexam = rs.getString( "forexam" );
                String file = rs.getString( "file" );
                Question q = new Question( id_question, question, correction, level, id_chapter, forexam, file );
                // add each employee to the list
                list.add( q );
            }
            statement.close();
            return list;
        } catch ( SQLException e ) {
            e.printStackTrace();
            return list;
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

    public static ArrayList<Question> select( int id ) {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Question> list = new ArrayList<Question>();
        try {
            connection = DB.getConnection();
            statement = connection.prepareStatement( "SELECT * FROM question WHERE createby=?" );
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
                Question q = new Question( id_question, question, correction, level, id_chapter, forexam, file );
                // add each employee to the list
                list.add( q );
            }
            statement.close();
            return list;
        } catch ( SQLException e ) {
            e.printStackTrace();
            return list;
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

    public static void delete( int createby, int id_question ) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DB.getConnection();
            statement = connection.prepareStatement( "DELETE FROM question WHERE createby=? AND id_question=?" );
            statement.setInt( 1, createby );
            statement.setInt( 2, id_question );
            statement.executeUpdate();
            statement.close();
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

    public void updateQuestion( int id_question ) throws SQLException {

        Connection connection = DB.getConnection();
        PreparedStatement stmt = connection.prepareStatement( "UPDATE question SET question='" + this.question
                + "' WHERE id_question=?" );
        stmt.setInt( 1, id_question );
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
            Question q = new Question( id_question, question, correction, level, id_chapter, forexam, file );
            // add each employee to the list
            list.add( q );
        }
        statement.close();
        Test.closeConnection( connection );
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
        this.question = result.getString( "question" );
        this.id_question = result.getInt( "id_question" );

        if ( connection != null ) {
            connection.close();
        }
    }
}

package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import play.Logger;
import play.db.DB;

public class Test {
    private final static String SELECT_TEST_BY_ID_USER   = "SELECT * FROM test "
                                                                 + "WHERE createby = ?";
    private final static String INSERT_TEST              = "INSERT INTO test(title,id_module,id_chapter,createby,isenable,password) "
                                                                 + "VALUES (?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_IS_ENABLE         = "UPDATE test "
                                                                 + "SET isenable = ? "
                                                                 + "WHERE createby = ? AND id_test = ?";
    private final static String DELETE_TEST              = "DELETE FROM test "
                                                                 + "WHERE createby=? AND id_test=?";
    private final static String SELECT_TEST_BY_ID_MODULE = "SELECT * FROM test WHERE id_module = ? AND isenable = true ";
    private final static String SELECT_TEST_BY_ID_TEST   = "SELECT * FROM test WHERE id_test = ?";

    private String              title;
    private int                 id_module;
    private int                 id_chapter;
    private int                 createby;
    private String              isenable;
    private int                 id_test;
    private String              password;

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public int getId_module() {
        return id_module;
    }

    public void setId_module( int id_module ) {
        this.id_module = id_module;
    }

    public int getId_chapter() {
        return id_chapter;
    }

    public void setId_chapter( int id_chapter ) {
        this.id_chapter = id_chapter;
    }

    public int getCreateby() {
        return createby;
    }

    public void setCreateby( int createby ) {
        this.createby = createby;
    }

    public String getIsenable() {
        return isenable;
    }

    public void setIsenable( String isenable ) {
        this.isenable = isenable;
    }

    public int getId_test() {
        return id_test;
    }

    public void setId_test( int id_test ) {
        this.id_test = id_test;
    }

    public static ArrayList<Test> getTestByIduser( int id_user, int idtest ) throws SQLException {
        String add_sql = "";
        if ( idtest != 0 ) {
            add_sql = " AND id_test = '" + idtest + "'";
        }
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( SELECT_TEST_BY_ID_USER + add_sql );
        ;
        ArrayList<Test> list = new ArrayList<Test>();
        statement.setInt( 1, id_user );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_module = rs.getInt( "id_module" );
            int id_test = rs.getInt( "id_test" );
            int id_chapter = rs.getInt( "id_chapter" );
            String title = rs.getString( "title" );
            String isenable = rs.getString( "isenable" );
            Test test = new Test();
            test.setId_test( id_test );
            test.setId_chapter( id_chapter );
            test.setId_module( id_module );
            test.setTitle( title );
            test.setIsenable( isenable );
            test.setCreateby( id_user );
            // add each test to the list
            list.add( test );
        }
        statement.close();
        Model.closeConnection( connection );
        return list;
    }

    public int insert() throws SQLException {
        Connection connection = DB.getConnection();
        ;
        PreparedStatement statement = connection.prepareStatement( INSERT_TEST, Statement.RETURN_GENERATED_KEYS );
        int id = -1;
        String pwd = BCrypt.hashpw( this.password, BCrypt.gensalt() );
        statement.setString( 1, this.title );
        statement.setInt( 2, this.id_module );
        statement.setInt( 3, this.id_chapter );
        statement.setInt( 4, this.createby );
        statement.setString( 5, this.isenable );
        statement.setString( 6, pwd );
        statement.executeUpdate();
        ResultSet resultat = statement.getGeneratedKeys();
        if ( resultat.next() ) {
            id = resultat.getInt( 1 );
        }
        statement.close();
        Model.closeConnection( connection );
        return id;
    }

    public static int updateIsenable( String isenable, int id_user, int id_test ) throws SQLException {
        int id = 1;
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( UPDATE_IS_ENABLE );
        statement.setString( 1, isenable );
        statement.setInt( 2, id_user );
        statement.setInt( 3, id_test );
        statement.executeUpdate();
        statement.close();
        Model.closeConnection( connection );
        return id;
    }

    public static void delete( int id_user, int id_test ) throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( DELETE_TEST );
        statement.setInt( 1, id_user );
        statement.setInt( 2, id_test );
        statement.executeUpdate();
        statement.close();
        Model.closeConnection( connection );
    }

    public static ArrayList<Test> getEnableTestByIdModule( int idModule ) throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( SELECT_TEST_BY_ID_MODULE );
        ;
        ArrayList<Test> list = new ArrayList<Test>();
        statement.setInt( 1, idModule );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_module = rs.getInt( "id_module" );
            int id_test = rs.getInt( "id_test" );
            int id_chapter = rs.getInt( "id_chapter" );
            String title = rs.getString( "title" );
            String isenable = rs.getString( "isenable" );
            Test test = new Test();
            test.setId_test( id_test );
            test.setId_chapter( id_chapter );
            test.setId_module( id_module );
            test.setTitle( title );
            test.setIsenable( isenable );
            // add each test to the list
            list.add( test );
        }
        statement.close();
        Model.closeConnection( connection );
        return list;
    }

    public static Test getTestByIdTest( int idTest ) throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement( SELECT_TEST_BY_ID_TEST );
        ;
        Test test = new Test();
        statement.setInt( 1, idTest );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int id_module = rs.getInt( "id_module" );
            int id_test = rs.getInt( "id_test" );
            int id_chapter = rs.getInt( "id_chapter" );
            String title = rs.getString( "title" );
            String isenable = rs.getString( "isenable" );
            String password = rs.getString( "password" );
            test.setId_test( id_test );
            test.setId_chapter( id_chapter );
            test.setId_module( id_module );
            test.setTitle( title );
            test.setPassword( password );
            test.setIsenable( isenable );
            // add each test to the list
        }
        statement.close();
        Model.closeConnection( connection );
        return test;
    }

    public boolean checkPassword( String pwd ) {
        return BCrypt.checkpw( pwd, this.password );
    }
}

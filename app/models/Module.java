package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import play.db.DB;

public class Module {

    private final static String GET_ALL_MODULES          = "SELECT id_module, name FROM module";
    private final static String INSERT_MODULE            = "INSERT INTO module(name,id_user) VALUES (?, ?)";
    private final static String DELETE_MODULE            = "DELETE FROM module WHERE id_user=? AND id_module=?";
    private final static String SELECT_BY_ID_USER_MODULE = "SELECT * FROM module WHERE id_user=?";

    private String              module_name;
    private int                 id_module;
    private int                 id_user;

    public int getId_user() {
        return id_user;
    }

    public void setId_user( int id_user ) {
        this.id_user = id_user;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name( String module_name ) {
        this.module_name = module_name;
    }

    public int getId_module() {
        return id_module;
    }

    public void setId_module( int id_module ) {
        this.id_module = id_module;
    }

    public static ArrayList<Module> getAllModules() throws SQLException {
        ArrayList<Module> all_modules = new ArrayList<Module>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        connection = DB.getConnection();
        statement = connection.prepareStatement( GET_ALL_MODULES );
        result = statement.executeQuery();
        while ( result.next() ) {
            Module module = new Module();
            module.setId_module( result.getInt( "id_module" ) );
            module.setModule_name( result.getString( "name" ) );

            all_modules.add( module );

        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return all_modules;
    }

    public int insert() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultat = null;
        int id = -1;
        connection = DB.getConnection();
        statement = connection.prepareStatement( INSERT_MODULE, Statement.RETURN_GENERATED_KEYS );
        statement.setString( 1, this.getModule_name() );
        statement.setInt( 2, this.getId_user() );
        statement.executeUpdate();
        resultat = statement.getGeneratedKeys();
        if ( resultat.next() ) {
            id = resultat.getInt( 1 );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return id;
    }

    public static ArrayList<Module> select( int id ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<Module> list = new ArrayList<Module>();
        connection = DB.getConnection();
        statement = connection.prepareStatement( SELECT_BY_ID_USER_MODULE );
        statement.setInt( 1, id );
        ResultSet rs = statement.executeQuery();
        while ( rs.next() ) {
            int module_id = rs.getInt( "id_module" );
            String name = rs.getString( "name" );
            Module m = new Module();
            m.setId_module( module_id );
            m.setModule_name( name );
            m.setId_user( id );
            // add each employee to the list
            list.add( m );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }

        return list;
    }

    public static void delete( int id_user, int id_module ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        connection = DB.getConnection();
        statement = connection.prepareStatement( DELETE_MODULE );
        statement.setInt( 1, id_user );
        statement.setInt( 2, id_module );
        statement.executeUpdate();
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
    }
}

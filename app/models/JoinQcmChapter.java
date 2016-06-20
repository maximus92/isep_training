package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import play.db.DB;

public class JoinQcmChapter {
    private String INSERT_JOIN_QCM_CHAPTER = "INSERT INTO join_qcm_chapter(id_qcm,id_chapter) "
                                                   + "VALUES (?, ?)";
    
    private static final String SELECT_JOIN_QCM_CHAPTER_BY_ID_QCM = "SELECT * FROM Join_qcm_chapter "
    												+ "WHERE id_qcm =?";
    private int    id_qcm;
    private int    id_chapter;

    public int getId_qcm() {
        return id_qcm;
    }

    public void setId_qcm( int id_qcm ) {
        this.id_qcm = id_qcm;
    }

    public int getId_chapter() {
        return id_chapter;
    }

    public void setId_chapter( int id_chapter ) {
        this.id_chapter = id_chapter;
    }

    public int insert() throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement statement = null;
        statement = connection.prepareStatement( INSERT_JOIN_QCM_CHAPTER, statement.RETURN_GENERATED_KEYS );
        int id = -1;
        statement.setInt( 1, this.id_qcm );
        statement.setInt( 2, this.id_chapter );
        statement.executeUpdate();
        ResultSet resultat = statement.getGeneratedKeys();
        if ( resultat.next() ) {
            id = resultat.getInt( 1 );
        }
        statement.close();
        Model.closeConnection( connection );
        return id;
    }
    

    public static ArrayList<JoinQcmChapter> select( int id ) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ArrayList<JoinQcmChapter> list = new ArrayList<JoinQcmChapter>();

        connection = DB.getConnection();
        statement = connection.prepareStatement( SELECT_JOIN_QCM_CHAPTER_BY_ID_QCM );
        statement.setInt( 1, id );
        ResultSet rs = statement.executeQuery();
        
        while ( rs.next() ) {
        	int id_chapter = rs.getInt("id_chapter");
        	

            JoinQcmChapter q = new JoinQcmChapter();
            q.setId_chapter(id_chapter);
            list.add( q );
        }
        statement.close();
        if ( connection != null ) {
            connection.close();
        }
        return list;

    }

}

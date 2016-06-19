package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import play.db.DB;

public class JoinQcmChapter {
    private String INSERT_JOIN_QCM_CHAPTER = "INSERT INTO join_qcm_chapter(id_qcm,id_chapter) "
                                                   + "VALUES (?, ?)";
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

}

package controllers;

import play.mvc.*;
import views.html.*;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;

import models.*;
/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class SigninController extends Controller {
	@Inject Database db;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(signin.render("ISEP Training - Connexion"));
    }
    
    public Result connexion(){
        DynamicForm signinform = Form.form().bindFromRequest();
        ConnexionLDAP c = new ConnexionLDAP();
        //User u = c.connectLDAP(signinform);
        User u = c.connect(db, signinform);
        if(u.exist(db).equals("")){
        	u.insert(db);
        }else{
        	String token = u.exist(db);
        	u.setToken(token);
        }
        if(u!= null){
        	session().clear();
            session("token", u.getToken());
        }
        return redirect("/");   
    }
    
    public Result logout(){
    	session().clear();
        return redirect("/");
    }

}

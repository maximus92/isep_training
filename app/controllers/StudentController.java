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
@With(StudentSecurity.class)
public class StudentController extends Controller {
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
      
          
	@Security.Authenticated(Secured.class)
    public Result index() {
		Logger.debug("OK3");
        return ok(home.render(""));
    }
    

}
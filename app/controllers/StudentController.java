package controllers;


import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.List;

import models.Module;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import controllers.security.Secured;
import controllers.security.StudentSecurity;

import views.html.*;

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
        return ok(student_generate_qcm.render(""));
    }

	
	public Result getAllModules(){
		ArrayList<String> modules = new ArrayList<String>();
		modules = Module.getAllModules();
		return ok(toJson(modules));	
	}
    

}
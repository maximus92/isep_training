package controllers;

import controllers.security.ProfessorSecurity;
import controllers.security.Secured;
import controllers.security.StudentSecurity;
import play.mvc.*;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@With(ProfessorSecurity.class)
public class ProfessorController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	@Security.Authenticated(Secured.class)
    public Result index() {
        return ok(home_prof.render(""));
    }

}

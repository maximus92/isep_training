package controllers;

import models.ConnexionLDAP;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.signin;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class SigninController extends Controller {
    /**
     * An action that renders an HTML page with a welcome message. The
     * configuration in the <code>routes</code> file means that this method will
     * be called when the application receives a <code>GET</code> request with a
     * path of <code>/</code>.
     */
    public Result index() {
        return ok( signin.render( "ISEP Training - Connexion" ) );
    }

    public Result connexion() {
        DynamicForm signinform = Form.form().bindFromRequest();
        ConnexionLDAP c = new ConnexionLDAP();

        // User u = c.connectLDAP(signinform);
        User u = c.connect( signinform );

        Logger.debug( "ok" );
        if ( u.exist().equals( "" ) ) {
            u.insert();
        } else {
            String token = u.exist();
            u.setToken( token );
        }
        if ( u != null ) {
            session().clear();
            session( "token", u.getToken() );
            session( "ln", u.getLastname() );
            session( "fn", u.getFirstname() );
        }
        return redirect( "/student" );
    }

    public Result logout() {
        session().clear();
        return redirect( "/signin" );
    }

}

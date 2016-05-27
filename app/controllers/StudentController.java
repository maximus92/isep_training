package controllers;

import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Chapter;
import models.Module;
import models.Qcm;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import controllers.security.Secured;
import controllers.security.StudentSecurity;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
@With( StudentSecurity.class )
public class StudentController extends Controller {
    /**
     * An action that renders an HTML page with a welcome message. The
     * configuration in the <code>routes</code> file means that this method will
     * be called when the application receives a <code>GET</code> request with a
     * path of <code>/</code>.
     */

    @Security.Authenticated( Secured.class )
    public Result index() {
        return ok( home_student.render( "" ) );
    }

    public Result studentQcmSettings() {
        ArrayList<Module> modules = new ArrayList<Module>();
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();

        modules = Module.getAllModules();
        chapters = Chapter.getAllChapters();

        return ok( student_qcm_settings.render( "", modules, chapters ) );
    }

    public Result studentPostTrainingQcmSettings() {
        DynamicForm form = Form.form().bindFromRequest();
        String id_chapter = form.get( "id_chapter" );
        String question_num = form.get( "question_num" );
        String question_level = form.get( "question_level" );
        Integer qcm_time = Integer.parseInt( form.get( "qcm_time" ) );
        String token = session().get( "token" );

        ArrayList<Integer> questionsArray = new ArrayList<Integer>();

        questionsArray = Qcm.getQuestionsIdArrayByParam(
                Integer.parseInt( id_chapter ),
                Integer.parseInt( question_num ),
                Integer.parseInt( question_level )
                );

        Qcm.createStudentQcm( questionsArray, qcm_time, token );

        JsonNode json = Json.toJson( questionsArray );
        questionsArray.clear();
        return ok( json );
    }

    public Result studentTrainingQcm() {
        
        int id_qcm = -1;
        String question = "";
        String token = session().get( "token" );

        if ( id_qcm == -1 ) {
            id_qcm = Qcm.getLastQcmForUser( token );
        }
        if ( id_qcm != -1 ) {
            question = Qcm.getQcmQuestions( id_qcm, 1 ).question;
        }
        

        return ok( student_training_qcm.render( "", question ) );
    }

}
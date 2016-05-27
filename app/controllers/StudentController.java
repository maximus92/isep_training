package controllers;

import java.util.ArrayList;

import models.Answer;
import models.Chapter;
import models.Module;
import models.Qcm;
import models.Question;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.home_student;
import views.html.student_qcm_settings;
import views.html.student_training_qcm;

import com.fasterxml.jackson.databind.JsonNode;

import controllers.security.Secured;
import controllers.security.StudentSecurity;

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

        ArrayList<Answer> answers_list = null;
        Question question = null;
        int id_qcm = -1;
        int id_question = -1;
        String questionString = "";
        String token = session().get( "token" );

        if ( id_qcm == -1 ) {
            id_qcm = Qcm.getLastQcmForUser( token );
        }
        if ( id_qcm != -1 ) {
            question = Qcm.getQcmQuestions( id_qcm, 1 );
            questionString = question.question;
            id_question = question.id_question;
        }

        if ( id_question != -1 ) {
            answers_list = Answer.getAnswersByQuestionId( id_question );
        }

        return ok( student_training_qcm.render( "", questionString, answers_list ) );
    }

}
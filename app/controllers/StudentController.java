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

        Qcm.createStudentQcm( questionsArray, qcm_time, token, questionsArray.size() );

        JsonNode json = Json.toJson( questionsArray );
        questionsArray.clear();
        return ok( json );
    }

    public Result studentTrainingQcm( Integer question_num ) {

        ArrayList<Answer> answers_list = null;
        Question question = null;
        int id_qcm = -1;
        int id_question = -1;
        String questionString = "";
        String token = session().get( "token" );
        Qcm qcm_info = new Qcm();

        if ( id_qcm == -1 ) {
            id_qcm = Qcm.getLastQcmForUser( token );
            qcm_info.getInfoById( id_qcm );
        }
        if ( id_qcm != -1 ) {
            if ( question_num <= 0 ) {
                return redirect( "/student/trainingQcm?question_num=1" );
            }
            if ( question_num > qcm_info.getNumber_of_questions() ) {
                return redirect( "/student/trainingQcm?question_num=" + qcm_info.getNumber_of_questions() );
            }
            question = Qcm.getQcmQuestions( id_qcm, question_num );
            questionString = question.question;
            id_question = question.id_question;
        }

        if ( id_question != -1 ) {
            answers_list = Answer.getAnswersByQuestionId( id_question );
        }

        return ok( student_training_qcm.render( "", questionString, answers_list, qcm_info ) );
    }

    public Result updateQcm() {

        DynamicForm form = Form.form().bindFromRequest();
        int id_qcm = Integer.parseInt( form.get( "id_qcm" ) );

        for ( int i = 0; i < Integer.parseInt( form.get( "nb-of-answers" ) ); i++ ) {
            if ( form.get( "answer" + i ) != null ) {
                Answer.updateStudentQcmAnswer( id_qcm,
                        Integer.parseInt( form.get( "answer" + i ) ), true );
            } else {
                Answer.updateStudentQcmAnswer( id_qcm,
                        Integer.parseInt( form.get( "id-answer-" + i ) ), false );
            }
        }

        Qcm.updateQcmTime( id_qcm, Integer.parseInt( form.get( "time" ) ) );
        return ok();
    }
}
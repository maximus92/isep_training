package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Answer;
import models.Chapter;
import models.Module;
import models.Qcm;
import models.Question;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.*;

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

    public Result studentPostTrainingQcmSettings() throws NumberFormatException, SQLException {
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
        for ( int i = 0; i < session().size(); i++ ) {
            session().get( "answer" );
        }
        return ok( json );
    }

    public Result studentTrainingQcm( Integer question_num ) throws SQLException {

        ArrayList<Answer> answers_list = null;
        Question question = new Question();
        int id_qcm = -1;
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
                return redirect( "/student/resultat?id_qcm=" + id_qcm );
            }
            question.getQcmQuestions( id_qcm, question_num );
        }

        if ( question.id_question != -1 ) {
            answers_list = Answer.getAnswersByQuestionId( question.id_question );
        }

        // for ( int i = 0; i < answers_list.size(); i++ ) {
        // if ( session().get( "answer" + answers_list.get( i ).id_answer ) !=
        // null &&
        // session().get( "answer" + answers_list.get( i ).id_answer ).equals(
        // "1" ) ) {
        //
        // answers_list.get( i ).is_select = true;
        // } else {
        // answers_list.get( i ).is_select = false;
        // }
        // }

        return ok( student_training_qcm.render( "", question, answers_list, qcm_info ) );
    }

    public Result updateQcm() throws NumberFormatException, SQLException {

        DynamicForm form = Form.form().bindFromRequest();
        int id_qcm = Integer.parseInt( form.get( "id_qcm" ) );

        for ( int i = 0; i < Integer.parseInt( form.get( "nb-of-answers" ) ); i++ ) {
            if ( form.get( "answer" + i ) != null ) {

                // changement depuis l'id de la checkbox
                // session().put( "answer" + form.get( "answer" + i ), "1" );
                Answer.updateStudentQcmAnswer( id_qcm,
                        Integer.parseInt( form.get( "answer" + i ) ), true );
            } else {

                // changement depuis l'id d'un input caché (ajax ne renvoie pas
                // l'id des réponses non cochées)
                // session().put( "answer" + form.get( "id-answer-" + i ), "0");
                Answer.updateStudentQcmAnswer( id_qcm,
                        Integer.parseInt( form.get( "id-answer-" + i ) ), false );
            }
        }

        Qcm.updateQcmTime( id_qcm, Integer.parseInt( form.get( "time" ) ) );
        return ok();
    }

    public Result answersSelected() throws SQLException {

        List answersSelected = new ArrayList();
        DynamicForm form = Form.form().bindFromRequest();
        int id_question = Integer.parseInt( form.get( "id_question" ) );
        int id_qcm = Integer.parseInt( form.get( "id_qcm" ) );
        answersSelected = Answer.getSelectedAnswers( id_qcm, id_question );

        JsonNode json = Json.toJson( answersSelected );
        Logger.debug( json.toString() );
        return ok( json );
    }

    public Result qcmResultat( int id_qcm ) throws SQLException {
        Qcm qcm = new Qcm();
        qcm.calculateScore( id_qcm );
        qcm.getInfoById( id_qcm );
        return ok( student_qcm_result.render( "", qcm ) );
    }

}
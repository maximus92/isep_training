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
import com.fasterxml.jackson.databind.node.ArrayNode;

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

    public Result studentQcmSettings() throws NumberFormatException, SQLException {
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
        Integer good_answer = Integer.parseInt( form.get( "good_answer" ) );
        Integer bad_answer = Integer.parseInt( form.get( "bad_answer" ) );
        Integer no_answer = Integer.parseInt( form.get( "no_answer" ) );
        String token = session().get( "token" );
        Qcm qcm = new Qcm();

        ArrayList<Integer> questionsArray = new ArrayList<Integer>();

        questionsArray = Qcm.getQuestionsIdArrayByParam(
                Integer.parseInt( id_chapter ),
                Integer.parseInt( question_num ),
                Integer.parseInt( question_level )
                );

        qcm.createStudentQcm(
                questionsArray,
                qcm_time,
                token,
                questionsArray.size(),
                good_answer,
                bad_answer,
                no_answer
                );

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
        Qcm qcm_info = new Qcm();
        int id_qcm = -1;
        String token = session().get( "token" );

        if ( id_qcm == -1 ) {
            id_qcm = Qcm.getLastQcmForUser( token );
            qcm_info.getInfoById( id_qcm );
            if ( qcm_info.getTime() == 0 ) {
                return redirect( "/student/resultat?id_qcm=" + id_qcm );
            }
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

        if ( question.getId_question() != -1 ) {
            answers_list = Answer.getAnswersByQuestionId( question.getId_question() );
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

        List<Answer> answersSelected = new ArrayList<Answer>();
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
        List<Question> questions_list = new ArrayList<Question>();
        qcm.setId_qcm( id_qcm );
        qcm.getInfoById( id_qcm );
        if ( qcm.getScore() == 0 ) {
            qcm.calculateScore();
        }

        questions_list = Question.getQuestionsByQcmId( id_qcm );
        return ok( student_qcm_result.render( "", qcm, questions_list ) );
    }

    public Result getCorrectionForQuestion() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_qcm = Integer.parseInt( form.get( "id_qcm" ) );
        int id_question = Integer.parseInt( form.get( "id_question" ) );
        List<Answer> answers_list = new ArrayList<Answer>();
        Question question = new Question();

        question.getQuestionById( id_question );
        answers_list = Answer.getSelectedAnswers( id_qcm, id_question );

        if ( answers_list.isEmpty() ) {
            answers_list = Answer.getAnswersByQuestionId( id_question );
        }

        for ( int i = 0; i < answers_list.size(); i++ ) {
            answers_list.get( i ).getAnswerParam();
        }

        JsonNode json1 = Json.toJson( question );
        JsonNode json2 = Json.toJson( answers_list );
        ArrayNode json_array = Json.newArray();

        json_array.add( json1 );
        json_array.add( json2 );

        return ok( json_array );
    }

    public Result qcmPreview( int id_qcm ) throws SQLException {

        List<Question> questions_list = new ArrayList<Question>();
        questions_list = Question.selectQuestionForReview( id_qcm );
        JsonNode json = Json.toJson( questions_list );

        return ok( json );
    }
}
package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.Answer;
import models.Chapter;
import models.Module;
import models.Qcm;
import models.Question;
import models.Test;
import models.User;
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
import com.fasterxml.jackson.databind.node.ObjectNode;

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
        Scanner scanner = new Scanner( id_chapter );
        List<Integer> id_chapter_list = new ArrayList<Integer>();
        while ( scanner.hasNextInt() ) {
            id_chapter_list.add( scanner.nextInt() );
        }
        Integer question_num = Integer.parseInt( form.get( "question_num" ) );
        Integer question_level = Integer.parseInt( form.get( "question_level" ) );
        Integer qcm_time = Integer.parseInt( form.get( "qcm_time" ) );
        Integer good_answer = Integer.parseInt( form.get( "good_answer" ) );
        Integer bad_answer = Integer.parseInt( form.get( "bad_answer" ) );
        Integer no_answer = Integer.parseInt( form.get( "no_answer" ) );
        String token = session().get( "token" );
        Qcm qcm = new Qcm();

        // empêche de charger le dernier test lancé si celui-ci est encore en
        // mémoire
        qcm.setId_test( -1 );

        ArrayList<Integer> questionsArray = new ArrayList<Integer>();

        questionsArray = Qcm.getQuestionsIdArrayByParam(
                id_chapter_list,
                question_num,
                question_level
                );

        qcm.createStudentQcm(
                questionsArray,
                qcm_time,
                token,
                questionsArray.size(),
                good_answer,
                bad_answer,
                no_answer,
                id_chapter_list
                );

        JsonNode json = Json.toJson( questionsArray );
        questionsArray.clear();
        //
        // for ( int i = 0; i < session().size(); i++ ) {
        // session().get( "answer" );
        // }
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

        return ok( student_training_qcm.render( "", question, answers_list, qcm_info ) );
    }

    public Result updateQcm() throws NumberFormatException, SQLException {
        Logger.debug( "okok" );
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

    public Result answersSelected() throws SQLException {

        List<Answer> answersSelected = new ArrayList<Answer>();
        DynamicForm form = Form.form().bindFromRequest();
        int id_question = Integer.parseInt( form.get( "id_question" ) );
        int id_qcm = Integer.parseInt( form.get( "id_qcm" ) );
        answersSelected = Answer.getSelectedAnswers( id_qcm, id_question );

        JsonNode json = Json.toJson( answersSelected );
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

    /********* TEST DE COURS ETUDIANT ***********/

    public Result courseTest() throws SQLException {
        ArrayList<Module> modules = new ArrayList<Module>();
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();

        modules = Module.getAllModules();
        chapters = Chapter.getAllChapters();
        return ok( student_course_test.render( modules, chapters ) );
    }

    public Result displayTestList() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_module = Integer.parseInt( form.get( "id_module" ) );
        List<Test> list_test = Test.getEnableTestByIdModule( id_module );
        JsonNode json = Json.toJson( list_test );
        return ok( json );
    }

    public Result displayTest() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_test = Integer.parseInt( form.get( "student_test_id_test" ) );
        String title = form.get( "student_test_title" );
        String password = form.get( "test_student_password" );
        Test test = Test.getTestByIdTest( id_test );
        ObjectNode result = Json.newObject();
        if ( test.checkPassword( password ) ) {

            int id_chapter = test.getId_chapter();
            List<Integer> id_chapter_list = new ArrayList<Integer>();
            id_chapter_list.add( id_chapter );
            String token = session().get( "token" );
            int id_user = User.getIdByToken( token );
            List<Question> questionsArray = Question.selectQuestionByIdTest( id_test, id_user );
            ArrayList<Integer> questionIdArray = new ArrayList<>();
            for ( int i = 0; i < questionsArray.size(); i++ ) {
                questionIdArray.add( questionsArray.get( i ).getId_question() );
            }
            Qcm qcm = new Qcm();
            qcm.setId_test( id_test );
            qcm.setTitle( title );
            qcm.createStudentQcm( questionIdArray, 3600, token, questionsArray.size(), 1, 0, 0, id_chapter_list );
            result.put( "password", true );
            result.put( "id_qcm", qcm.getId_qcm() );
        } else {
            result.put( "password", false );
        }
        return ok( result );
    }

    /********** Historique des qcms *************/

    public Result studentHistory() throws SQLException {
        List<Qcm> qcm_list = new ArrayList<Qcm>();
        String token = session().get( "token" );
        int id_user = User.getIdByToken( token );

        qcm_list = Qcm.getEndQcmByUser( id_user ).subList( 0, 10 );

        return ok( student_qcm_history.render( "", qcm_list ) );
    }

    /*************** Mode Examen ***********************/

    public Result examMode() throws SQLException {

        ArrayList<Module> modules = new ArrayList<Module>();

        modules = Module.getAllModules();

        return ok( student_exam_mode.render( "", modules ) );
    }

    public Result displayExamList() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_module = Integer.parseInt( form.get( "id_module" ) );

        List<Qcm> exam_list = Qcm.getExamModeByModule( id_module );
        JsonNode json = Json.toJson( exam_list );

        return ok( json );
    }

    public Result displayExam() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        Qcm qcm = new Qcm();
        List<Chapter> chapter_list = new ArrayList<Chapter>();
        ArrayList<Integer> id_question_list = new ArrayList<Integer>();
        List<Integer> id_chapter_list = new ArrayList<Integer>();
        String token = session().get( "token" );

        qcm.setId_test( -1 );

        qcm.getInfoById( Integer.parseInt( form.get( "id_exam" ) ) );

        chapter_list = Chapter.getChaptersByModuleId( qcm.getId_module() );
        for ( int i = 0; i < chapter_list.size(); i++ ) {
            id_chapter_list.add( chapter_list.get( i ).getId_chapter() );
        }
        id_question_list = Qcm.getQuestionsIdArrayByParam(
                id_chapter_list,
                qcm.getNumber_of_questions(),
                qcm.getLevel()
                );

        qcm.createStudentQcm(
                id_question_list,
                qcm.getTime(),
                token,
                qcm.getNumber_of_questions(),
                qcm.getGood_answer(),
                qcm.getBad_answer(),
                qcm.getNo_answer(),
                id_chapter_list
                );
        JsonNode json = Json.toJson( id_question_list );
        id_question_list.clear();
        return ok( json );
    }

}
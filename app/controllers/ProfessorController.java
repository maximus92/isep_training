package controllers;

import java.io.File;
import java.sql.SQLException;
import java.lang.Object;
import java.util.ArrayList;
import java.util.List;

import models.Answer;
import models.Chapter;
import models.ExcelFile;
import models.JoinTestQuestion;
import models.Module;
import models.Qcm;
import models.Question;
import models.Test;
import models.User;
import models.JoinQcmChapter;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.home_prof;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.security.ProfessorSecurity;
import controllers.security.Secured;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
// @With(ProfessorSecurity.class)
public class ProfessorController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message. The
     * configuration in the <code>routes</code> file means that this method will
     * be called when the application receives a <code>GET</code> request with a
     * path of <code>/</code>.
     */
    @Security.Authenticated( Secured.class )
    public Result index() {
        return ok( home_prof.render( "" ) );
    }

    public Result addQ() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        String question = form.get( "question" );
        String correction = form.get( "correction" );
        String level = form.get( "level" );
        String id_chapter = form.get( "question_chapter" );
        String forexam = form.get( "forexam" );
        String file = form.get( "file" );
        String token = session().get( "token" );
        int reponse_counter = Integer.parseInt( form.get( "reponse_counter" ) );
        int createby = User.getIdByToken( token );

        Question q = new Question();

        q.setQuestion( question );
        q.setCorrection( correction );
        q.setLevel( level );
        q.setId_chapter( id_chapter );
        q.setForexam( forexam );
        q.setCreateby( createby );
        q.setFile( file );
        int id_question = q.insertQuestion();

        for ( int i = 0; i <= reponse_counter; i++ ) {
            String answer = form.get( "reponse" + i + "" );

            if ( answer != null && answer != "" ) {
                boolean istrue;
                if ( form.get( "goodA" + i + "" ) == null ) {
                    istrue = false;
                } else {
                    istrue = true;
                }
                Answer a = new Answer();
                a.setAnswer( answer );
                a.setId_question( id_question );
                a.setIstrue( istrue );
                a.insertAnswer();
            }
        }
        return redirect( "/prof" );
    }

    public Result addModule() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.get( "module_name" );
        String token = session().get( "token" );
        int id_user = User.getIdByToken( token );
        Module mod = new Module();
        mod.setModule_name( name );
        mod.setId_user( id_user );
        int id_module = mod.insert();
        ObjectNode result = Json.newObject();
        result.put( "name", name );
        result.put( "id", id_module );
        return ok( result );
    }

    public Result selectModule() throws SQLException {
        String token = session().get( "token" );
        int id = User.getIdByToken( token );
        ArrayList<Module> list = Module.select( id );
        JsonNode json = Json.toJson( list );
        return ok( json );
    }

    public Result deleteModule() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_module = Integer.parseInt( form.get( "id" ) );
        String token = session().get( "token" );
        int id_user = User.getIdByToken( token );
        Module.delete( id_user, id_module );
        ObjectNode result = Json.newObject();
        result.put( "id_module", id_module );
        return ok( result );
    }

    public Result selectQuestion() throws SQLException {
        String token = session().get( "token" );
        int id = User.getIdByToken( token );
        ArrayList<Question> list = Question.select( id );
        JsonNode json = Json.toJson( list );
        return ok( json );
    }

    public Result deleteQuestion() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_question = Integer.parseInt( form.get( "id" ) );
        String token = session().get( "token" );
        int createby = User.getIdByToken( token );
        Question.delete( createby, id_question );
        ObjectNode result = Json.newObject();
        result.put( "id_question", id_question );
        return ok( result );
    }

    public Result selectAnswerWithQuestion() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_question = Integer.parseInt( form.get( "id" ) );
        ArrayList<Answer> list = Answer.getAnswersByQuestionId( id_question );
        ArrayList<Question> list1 = Question.selectQuestionByIdQ( id_question );
        JsonNode json = Json.toJson( list );
        JsonNode json1 = Json.toJson( list1 );
        ArrayNode result = Json.newArray();
        result.add( json );
        result.add( json1 );
        return ok( result );

    }

    public Result addTest() throws SQLException {
        // Get form from view
        DynamicForm form = Form.form().bindFromRequest();
        // Get current user id
        int createby = getUserID();
        // Create Test in DB and catch the created id
        Test test = new Test();
        test.setTitle( form.get( "test_title" ) );
        test.setId_module( Integer.parseInt( form.get( "test_module" ) ) );
        test.setId_chapter( 0 );
        test.setCreateby( createby );
        test.setPassword(form.get("test_password"));
        test.setIsenable( "0" );

        int id_test = test.insert();
        // Insert questions and answers in DB
        insertQandAFromTest( form, createby, id_test );
        return redirect( "/prof" );
    }

    public void insertQandAFromTest( DynamicForm form, int createby, int id_test ) throws SQLException {
        int answer_test_counter = Integer.parseInt( form.get( "answer_test_counter" ) );
        int question_test_counter = Integer.parseInt( form.get( "question_test_counter" ) );
        String id_chapter = form.get( "test_chapter" );
        for ( int i = 0; i <= question_test_counter; i++ ) {
            String question = form.get( "question" + i );

            if ( question != null && question != "" ) {
                Question q = new Question();
                q.setQuestion( question );
                q.setCorrection( "" );
                q.setLevel( "0" );
                q.setId_chapter( id_chapter );
                q.setForexam( "0" );
                q.setFile( "" );
                q.setCreateby( createby );
                int id_question = q.insertQuestion();
                JoinTestQuestion join_test_question = new JoinTestQuestion( id_test, id_question );
                join_test_question.insert();
                for ( int j = 0; j <= answer_test_counter; j++ ) {
                    String answer = form.get( "question" + i + "_answer" + j );
                    if ( answer != null && answer != "" ) {
                        boolean istrue;

                        if ( form.get( "question" + i + "_goodA" + j ) == null ) {
                            istrue = false;
                        } else {
                            istrue = true;
                        }
                        Answer a = new Answer();
                        a.setAnswer( answer );
                        a.setId_question( id_question );
                        a.setIstrue( istrue );
                        a.insertAnswer();
                    }
                }
            }
        }
    }

    public static int getUserID() throws SQLException {
        String token = session().get( "token" );
        return User.getIdByToken( token );
    }

    public Result selectTest() throws SQLException {
        int id = getUserID();
        DynamicForm form = Form.form().bindFromRequest();
        int id_test = 0;
        if ( form.get( "id_test" ) != null && form.get( "id_test" ) != "" ) {
            id_test = Integer.parseInt( form.get( "id_test" ) );
        }
        ArrayList<Test> list = Test.getTestByIduser( id, id_test );
        JsonNode json = Json.toJson( list );
        return ok( json );
    }

    public Result enableTest() throws SQLException {
        int id_user = getUserID();
        DynamicForm form = Form.form().bindFromRequest();
        int id_test = Integer.parseInt( form.get( "id" ) );
        String isenable = form.get( "isenable" );
        int res = Test.updateIsenable( isenable, id_user, id_test );
        ObjectNode result = Json.newObject();
        result.put( "res", res );
        return ok( result );
    }

    public Result updateQuestionWithAnswer() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_question = Integer.parseInt( form.get( "id_question" ) );
        String question = form.get( "question" );
        String correction = form.get( "correction" );
        String level = form.get( "level" );
        String id_chapter = form.get( "id_chapter" );
        String forexam = form.get( "forexam" );
        String file = form.get( "file" );

        Question q = new Question();
        q.setQuestion( question );
        q.setCorrection( correction );
        q.setLevel( level );
        q.setId_chapter( id_chapter );
        q.setForexam( forexam );
        q.setFile( file );
        q.updateQuestion( id_question );

        int reponse_counter_modify = Integer.parseInt( form.get( "reponse_counter_modify" ) );

        for ( int i = 0; i < reponse_counter_modify; i++ ) {
            int id_answer = Integer.parseInt( form.get( "id_answer" + i ) );
            String answer = form.get( "reponse" + i + "" );

            if ( id_answer != 0 ) {

                if ( answer != null && answer != "" ) {
                    boolean istrue;
                    if ( form.get( "goodA" + id_answer + i + "" ) == null ) {
                        istrue = false;
                    } else {
                        istrue = true;
                    }
                    Answer a = new Answer();
                    a.setAnswer( answer );
                    a.setIstrue( istrue );
                    a.updateAnswer( id_answer );
                }
            }

            else {
                if ( answer != null && answer != "" ) {
                    boolean istrue;
                    if ( form.get( "goodA" + i + "" ) == null ) {
                        istrue = false;
                    } else {
                        istrue = true;
                    }
                    Answer a = new Answer();
                    a.setAnswer( answer );
                    a.setId_question( id_question );
                    a.setIstrue( istrue );
                    a.insertAnswer();
                }
            }
        }
        return redirect( "/prof" );

    }

    public Result selectAnswerWithQuestionByIdTest() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_test = Integer.parseInt( form.get( "id_test" ) );
        int id_user = getUserID();
        ArrayList<ArrayList<Answer>> answer_list = new ArrayList<>();
        ArrayList<Question> question_list = Question.selectQuestionByIdTest( id_test, id_user );

        for ( int i = 0; i < question_list.size(); i++ ) {
            int id_question = question_list.get( i ).getId_question();
            answer_list.add( Answer.getAnswersByQuestionId( id_question ) );
        }

        JsonNode json = Json.toJson( question_list );
        JsonNode json1 = Json.toJson( answer_list );
        ArrayNode result = Json.newArray();
        result.add( json );
        result.add( json1 );
        return ok( result );
    }

    public Result deleteTest() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_user = getUserID();
        int id_test = Integer.parseInt( form.get( "id_test" ) );
        Test.delete( id_user, id_test );
        ObjectNode result = Json.newObject();
        result.put( "res", "ok" );
        return ok( result );
    }

    public Result detailModule() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_module = Integer.parseInt( form.get( "id_module" ) );
        ArrayList<Chapter> chapter_array = Chapter.getChaptersByModuleId( id_module );
        JsonNode json = Json.toJson( chapter_array );
        return ok( json );
    }

    public Result addChapter() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_module = Integer.parseInt( form.get( "id_module" ) );
        String chapter_name = form.get( "chapter_name" );
        Chapter chap = new Chapter();
        chap.setChapter_name( chapter_name );
        chap.setId_module( id_module );
        int id_chapter = chap.insert();
        ObjectNode result = Json.newObject();
        result.put( "id_chapter", id_chapter );
        return ok( result );
    }

    public Result deleteChapter() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_chapter = Integer.parseInt( form.get( "id_chapter" ) );
        int id_module = Integer.parseInt( form.get( "id_module" ) );
        Chapter.delete( id_chapter, id_module );
        ObjectNode result = Json.newObject();
        result.put( "res", "ok" );
        return ok( result );
    }
    

    public Result selectChapter() throws SQLException{
    	DynamicForm form = Form.form().bindFromRequest();
    	int id_module = Integer.parseInt(form.get("id_module"));
        ArrayList<Chapter> list = Chapter.getChaptersByModuleId(id_module);
        JsonNode json = Json.toJson( list );
        return ok( json );
    }
    
    public Result importQuestion() throws SQLException{
    	int id_user = getUserID();
    	DynamicForm form = Form.form().bindFromRequest();
    	int id_module = Integer.parseInt(form.get("import-module"));
    	String id_chapter = form.get("id_chapter");

	 	play.mvc.Http.MultipartFormData<File> body = request().body().asMultipartFormData();
	    play.mvc.Http.MultipartFormData.FilePart<File> file = body.getFile("excel-file");
	    
	    if (file != null) {
	        String fileName = file.getFilename();
	        String contentType = file.getContentType();
	        java.io.File fileio = file.getFile();
	        ExcelFile excel_file = new ExcelFile();
	        excel_file.setFile(fileio);
	        excel_file.setId_user(id_user);
	        excel_file.previewCsv(form);
	        JsonNode result = excel_file.readCsvFile(true,form.get("import-chapter"));
	        return ok(result);
	    }
	    ObjectNode result = Json.newObject();
	    result.put( "column_number", "-1" );
        return ok( result );
    }


	public Result GetCsvColumnTitle() throws SQLException{
		play.mvc.Http.MultipartFormData<File> body = request().body().asMultipartFormData();
	    play.mvc.Http.MultipartFormData.FilePart<File> file = body.getFile("excel-file");
	    if (file != null) {
	        String fileName = file.getFilename();
	        String contentType = file.getContentType();
	        java.io.File fileio = file.getFile();
	        ExcelFile excel_file = new ExcelFile();
	        excel_file.setFile(fileio);
	        JsonNode result = excel_file.getCsvColumnTitle();
	        return ok( result );
	    }
	    ObjectNode result = Json.newObject();
	    result.put( "column_number", "-1" );
        return ok( result );
	}
	
	public Result PreviewCsv() throws SQLException{
		int id_user = getUserID();
		DynamicForm form = Form.form().bindFromRequest();
		play.mvc.Http.MultipartFormData<File> body = request().body().asMultipartFormData();
	    play.mvc.Http.MultipartFormData.FilePart<File> file = body.getFile("excel-file");
	    ObjectNode result = Json.newObject();
	    ArrayList<String> column_array = new ArrayList<String>();
	    
	    if (file != null) {
	        String fileName = file.getFilename();
	        String contentType = file.getContentType();
	        java.io.File fileio = file.getFile();
	        ExcelFile excel_file = new ExcelFile();
	        excel_file.setFile(fileio);
	        excel_file.setId_user(id_user);
	        ArrayNode res = excel_file.previewCsv(form);
	        return ok(res);
	    }
	    result.put( "column_number", "-1" );
        return ok( result );
	}
	public Result filterQuestion() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        String token = session().get( "token" );
        int id = User.getIdByToken( token );

        ArrayList<Question> list = Question.filterQuestion(id);
        JsonNode json = Json.toJson( list );
        return ok( json );
        }
	
    public Result addExam() throws SQLException {
    	DynamicForm form = Form.form().bindFromRequest();
    	List<String> chapter_id_array = new ArrayList<String>();
    	if(form.get("hidden_nbr_id_chapter")!=null){
    		int nb_chapter = Integer.parseInt(form.get("hidden_nbr_id_chapter"));
    		for(int i=0;i<nb_chapter;i++){
    			if(form.get("chapter_for_exam"+i)!=null){
    				chapter_id_array.add(form.get("chapter_for_exam"+i));
    			}
    			
    		}
    	}
    	
        String token = session().get( "token" );
        int createby = User.getIdByToken( token );  
        int nbanswermax = Integer.parseInt(form.get("maxR"));
        int hours = Integer.parseInt(form.get( "hour" ));
        int minutes = Integer.parseInt(form.get( "minute" ));
        int time = (hours*60) + minutes;
        int number_of_questions = Integer.parseInt(form.get( "nbrQ" ));
        int good_answer = Integer.parseInt(form.get( "positiveP" ));
        int bad_answer = Integer.parseInt(form.get( "negativeP" ));
        int no_answer = Integer.parseInt(form.get( "nullP" ));
        String title = form.get( "title_exam" );
        boolean exam = true;
        
 
        Qcm examen = new Qcm();
        
        examen.setCreateby(createby);
        examen.setNbanswermax(nbanswermax);
        examen.setTime(time);
        examen.setNumber_of_questions(number_of_questions);
        examen.setGood_answer(good_answer);
        examen.setBad_answer(bad_answer);
        examen.setNo_answer(no_answer);
        examen.setTitle(title);
        examen.setExam(exam);
        int id_qcm = examen.createExam();
        insertExamWithChapter(chapter_id_array, id_qcm );
        
        return redirect( "/prof" );
    
    }
    
    public Result selectExam() throws SQLException {
        String token = session().get( "token" );
        int id = User.getIdByToken( token );
        ArrayList<Qcm> list = Qcm.selectExam( id );
        JsonNode json = Json.toJson( list );
        return ok( json );
    }
    public Result deleteExam() throws SQLException {
        DynamicForm form = Form.form().bindFromRequest();
        int id_qcm = Integer.parseInt( form.get( "id" ) );
        String token = session().get( "token" );
        int createby = User.getIdByToken( token );
        Qcm.deleteExam( createby, id_qcm );
        ObjectNode result = Json.newObject();
        result.put( "id_qcm", id_qcm );
        return ok( result );
    }
    
    public void insertExamWithChapter( List<String> id_chapter_array, int id_qcm ) throws SQLException {
        
        for ( int i = 0; i < id_chapter_array.size(); i++ ) {
            int id_chapter = Integer.parseInt(id_chapter_array.get(i));
            JoinQcmChapter join = new JoinQcmChapter();
            join.setId_chapter(id_chapter);
            join.setId_qcm(id_qcm);
            join.insert();
        }
           
    }
    
   

}

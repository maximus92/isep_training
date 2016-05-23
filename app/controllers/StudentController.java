package controllers;


import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.List;

import models.Chapter;
import models.Module;
import models.Qcm;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
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
        return ok(home_student.render(""));
    }

	
	public Result studentQcmSettings(){
		ArrayList<Module> modules = new ArrayList<Module>();
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		
		modules = Module.getAllModules();
		chapters = Chapter.getAllChapters();
		
		
		return ok(student_qcm_settings.render("", modules, chapters));
	}
	
	public Result studentDisplayTrainingQcm(){
		DynamicForm form = Form.form().bindFromRequest();
		String id_chapter = form.get("id_chapter");
		String question_num = form.get("question_num");
		String question_level = form.get("questino_level");
		ArrayList<Integer> questionsArray = new ArrayList<Integer>();
		
		questionsArray = Qcm.getQuestionsIdArrayByParam(
				Integer.parseInt(id_chapter), 
				Integer.parseInt(question_num), 
				Integer.parseInt(question_level)
		);
		
		Logger.debug(Integer.toString(questionsArray.get(0)));
		return ok(student_training_qcm.render("", questionsArray));
	}
    

}
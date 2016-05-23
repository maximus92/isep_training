package controllers;


import models.Answer;
import models.Module;
import models.Question;
import models.User;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.security.ProfessorSecurity;
import controllers.security.Secured;
import controllers.security.StudentSecurity;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
//@With(ProfessorSecurity.class)
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
	
	public Result addQ(){
		DynamicForm form = Form.form().bindFromRequest();
		String question = form.get("question");
		String correction = form.get("correction");
		String level = form.get("level");
		String id_chapter = form.get("id_chapter");
		String forexam = form.get("forexam");
		String file = form.get("file");
		String token = session().get("token");
		int reponse_counter = Integer.parseInt(form.get("reponse_counter"));
		int createby = User.getIdByToken(token);
		
		
		Logger.debug(question+" "+correction+" "+level+" "+id_chapter+" "+forexam+" "+file+" "+createby+" "+token);

		Question q = new Question(question, correction, level, id_chapter, forexam, file, createby);
		
		int id_question = q.insertQuestion();
		

		for(int i = 1; i <= reponse_counter; i++){
			String answer = form.get("reponse"+i+"");
			String istrue = isQuestionTrue(form.get("goodA"+i+""));
			Answer a = new Answer(answer, id_question, istrue);
			a.insertAnswer();
		}
		
		//Logger.debug(Integer.toString(id_question)+" "+istrue);

		return ok(home_prof.render(""));
		
	}
	
	public  String  isQuestionTrue(String s){
		if(s == "1"){
			return "1";
		}
		else{
			return "0";
		}
	}
	
	public Result addModule(){
		DynamicForm form = Form.form().bindFromRequest();
		String name = form.get("module_name");
		String token = session().get("token");
		int id_user = User.getIdByToken(token);
		Module mod = new Module(name,id_user);
		int id_module = mod.insert();
		ObjectNode result = Json.newObject();
	    result.put("name", name);
	    result.put("id", id_module);
	    return ok(result);
	}
	
	public Result selectModule(){
		String token = session().get("token");
		int id = User.getIdByToken(token);
		ArrayList<Module> list = Module.select(id);
		JsonNode json = Json.toJson(list);
	    return ok(json);
	}
	
	

}

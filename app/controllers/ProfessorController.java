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

		Question q = new Question(question, correction, level, id_chapter, forexam, file, createby);
		int id_question = q.insertQuestion();

		for(int i = 0; i <= reponse_counter; i++){
			String answer = form.get("reponse"+i+"");
			
			if(answer != null && answer != ""){
				String istrue = null;
				if(form.get("goodA"+i+"") == null){
					istrue = "0";
				}else{
					istrue = "1";
				}
				Answer a = new Answer(answer, id_question, istrue);
				a.insertAnswer();
			}
		}
		return redirect("/prof");
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
	
	public Result deleteModule(){
		DynamicForm form = Form.form().bindFromRequest();
		int id_module = Integer.parseInt(form.get("id"));
		String token = session().get("token");
		int id_user = User.getIdByToken(token);
		Module.delete(id_user, id_module);
		ObjectNode result = Json.newObject();
	    result.put("id_module", id_module);
		return ok(result);
	}
	
	public Result selectQuestion(){
		String token = session().get("token");
		int id = User.getIdByToken(token);
		ArrayList<Question> list = Question.select(id);
		JsonNode json = Json.toJson(list);
	    return ok(json);
	}
	

	public Result deleteQuestion(){
		DynamicForm form = Form.form().bindFromRequest();
		int id_question = Integer.parseInt(form.get("id"));
		String token = session().get("token");
		int createby = User.getIdByToken(token);
		Question.delete(createby, id_question);
		ObjectNode result = Json.newObject();
	    result.put("id_question", id_question);
		return ok(result);
	}
	
	public Result addTest(){
		DynamicForm form = Form.form().bindFromRequest();
		String token = session().get("token");
		int createby = User.getIdByToken(token);
		insertQandAFromTest(form,createby);
		return redirect("/prof");
	}
	
	private void insertQandAFromTest(DynamicForm form,int createby){
		
		int answer_test_counter = Integer.parseInt(form.get("answer_test_counter"));
		int question_test_counter = Integer.parseInt(form.get("question_test_counter"));
		String id_chapter = form.get("id_chapter");
		
		for(int i = 0; i <= question_test_counter; i++){
			for(int j=0; j<= answer_test_counter;j++){
				
				String question = form.get("question"+i);
				
				if(question != null && question != ""){
					Question q = new Question(question, "", "", id_chapter, "0", "", createby);
					int id_question = q.insertQuestion();
					String answer = form.get("question"+i+"_answer"+j);
					
					if(answer != null && answer != ""){
						String istrue = null;
						
						if(form.get("question"+i+"_goodA"+j) == null){
							istrue = "0";
						}else{
							istrue = "1";
						}
						Answer a = new Answer(answer, id_question, istrue);
						a.insertAnswer();
					}
				}
			}
		}
	}
	
	
}

package models;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;

public class ExcelFile {
	
	private int question_column = -1;
	private int answer_colum = -1;
	private int correction_column = -1;
	private int forexam_column = -1;
	private int istrue_column = -1;
	private int level_column = -1;
	private File file;
	private int id_user;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public int getQuestion_column() {
		return question_column;
	}
	public void setQuestion_column(int question_column) {
		this.question_column = question_column;
	}
	public int getAnswer_colum() {
		return answer_colum;
	}
	public void setAnswer_colum(int answer_colum) {
		this.answer_colum = answer_colum;
	}
	public int getCorrection_column() {
		return correction_column;
	}
	public void setCorrection_column(int correction_column) {
		this.correction_column = correction_column;
	}
	public int getForexam_column() {
		return forexam_column;
	}
	public void setForexam_column(int forexam_column) {
		this.forexam_column = forexam_column;
	}
	public int getIstrue_column() {
		return istrue_column;
	}
	public void setIstrue_column(int istrue_column) {
		this.istrue_column = istrue_column;
	}
	public int getLevel_column() {
		return level_column;
	}
	public void setLevel_column(int level_column) {
		this.level_column = level_column;
	}
	
	public ArrayNode readCsvFile(boolean insert_is_true,String id_chapter) throws SQLException{
    	// Initialize variables       
    	String question = null;
        String answer = null;
        String correction = null;
        int forexam = 0;
        boolean istrue = false;
        int level = 0;
        ArrayList<Answer> answer_array = new ArrayList<>();
        ArrayList<ArrayList<Answer>> array_of_answers = new ArrayList<>();
        ArrayList<Question> array_of_question = new ArrayList<>();
        // Initialize index of rows and columns
        int index_row = 1;
        int index_cel = 0;
        
        try {
        	// Create a workbook in order to read the excel file
            Workbook workbook = WorkbookFactory.create(this.file);
            // Read the first sheet of the excel file
            Sheet sheet = workbook.getSheet("Feuille1");
            // Initialize the first row
            Row row = sheet.getRow(index_row);
            int nb_column = sheet.getRow(1).getLastCellNum();
            // Read every row
            while (row != null) {
            	// Read row
                row = sheet.getRow(index_row++);
                // If this row is not null
                if(row != null){
                	// Initialize cel
	                Cell cel = row.getCell(index_cel);
	                // for each cel of this row
	                while(cel != null){
	                	// Read cels
	                	cel = row.getCell(index_cel++);
	                	// Make an association between column number and cels'index
	                	// Get the value of each cel and put it in variables
	                	if(cel != null  && getCellTypeToString(cel) != null && getCellTypeToString(cel) != ""){
	                		if(index_cel== 1 && question != null){
	                			Question question_object = createQuestion(question, correction, forexam, level,id_chapter);
	                			array_of_answers.add(answer_array);
	                			array_of_question.add(question_object);
	                			if(insert_is_true){
	                				addQuestionFromCsv(question_object,answer_array);
	                			}
	                		}	
	                		if(index_cel == this.question_column){
		                		question = cel.getStringCellValue();
		                		answer_array = new ArrayList<>();
		                	}else if(index_cel == this.answer_colum){
		                		answer = getCellTypeToString(cel);
		                	}else if(index_cel == this.correction_column){
		                		correction = getCellTypeToString(cel);
		                	}else if(index_cel == this.forexam_column){
		                		forexam = Integer.parseInt(getCellTypeToString(cel));
		                	}else if(index_cel == this.istrue_column){
		                		istrue = (Integer.parseInt(getCellTypeToString(cel)) != 0);
		                	}else if(index_cel == this.level_column){
		                		level = Integer.parseInt(getCellTypeToString(cel));
		                	}
	                		
	                	}
	                	if(answer != "" && index_cel==nb_column){
                			Answer answer_object = new Answer();
                			answer_object.setAnswer(answer);
                			answer_object.setIstrue(istrue);
                			answer_array.add(answer_object);
                		}
	                }
                }
                // Go back to the first column and change go to the following row
                index_cel = 0;
            }
            if(question != null){
            	Question question_object = createQuestion(question, correction, forexam, level,id_chapter);
            	array_of_answers.add(answer_array);
    			array_of_question.add(question_object);
            	if(insert_is_true){
            		addQuestionFromCsv(question_object,answer_array);
            	}	
            }
            
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        JsonNode json = Json.toJson( array_of_question );
        JsonNode json1 = Json.toJson( array_of_answers );
        ArrayNode result = Json.newArray();
        result.add( json );
        result.add( json1 );
        return result;
    }
	
	public static void addQuestionFromCsv(Question question, ArrayList<Answer> answer_array) throws SQLException{
    	int id_question = question.insertQuestion();
    	for(int i= 0; i<answer_array.size();i++){
    		answer_array.get(i).setId_question(id_question);
    		answer_array.get(i).insertAnswer();
    	}
    }
	
	public Question createQuestion (String question, String correction, int forexam, int level,String id_chapter) throws SQLException{
		Question question_object = new Question();
		question_object.setCreateby(this.id_user);
		question_object.setQuestion(question);
		question_object.setCorrection(correction);
		question_object.setForexam(Integer.toString(forexam));
		question_object.setLevel(Integer.toString(level));
		question_object.setId_chapter(id_chapter);
		return question_object;
	}
	
	public JsonNode getCsvColumnTitle(){
	    ArrayList<String> column_array = new ArrayList<String>();
	    JsonNode json = null;
	    String column = "";
		try{
        	Workbook workbook = WorkbookFactory.create(this.file);     
            Sheet sheet = workbook.getSheet("Feuille1");
            int nb_column = sheet.getRow(1).getLastCellNum();
            column_array.add(Integer.toString(nb_column));
            for(int i=0; i<nb_column; i++){
            	Cell cell = sheet.getRow(0).getCell(i);
            	
        		if (cell!=null) {
        		    column = getCellTypeToString(cell);
        		}
        		column_array.add(column);
        		
            }
            json = Json.toJson( column_array );
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
		return json;
    }
	
	public ArrayNode previewCsv(DynamicForm form) throws SQLException{
		 ArrayNode res = null;
		try{
        	Workbook workbook = WorkbookFactory.create(this.file);     
            Sheet sheet = workbook.getSheet("Feuille1");
            int nb_column = sheet.getRow(1).getLastCellNum();
            for(int i=0; i<nb_column;i++){
            	int index = i+1;
            	setCsvColumnValue(form.get("import-parameter"+i),index);
            }
            res = readCsvFile(false,null);
            
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }  
		return res;
	}
	
	public void setCsvColumnValue(String param, int val){
		switch (param) {
		case "question":
			this.question_column = val;
			break;
		case "answer":
			this.answer_colum = val;
			break;
		case "correction":
			this.correction_column = val;
			break;
		case "forexam":
			this.forexam_column = val;
			break;
		case "istrue":
			this.istrue_column = val;
			break;
		case "level":
			this.level_column = val;
			break;
		}
	}
	
	public String getCellTypeToString(Cell cell){
		String val="";
		switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
        	int myInt = (cell.getBooleanCellValue()) ? 1 : 0;
        	val = Integer.toString(myInt);
            break;
        case Cell.CELL_TYPE_NUMERIC:
        	val = Integer.toString((int)cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_STRING:
        	val = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_BLANK:
            break;
        
		}
		return val;
	}
    
}

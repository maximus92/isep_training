		var l = 1;
		var i =1;

	
	function addAnswer(answer){
		l++;
		var remove = 0;
		id = remove + l;
		var newdiv = document.createElement('div');
		newdiv.innerHTML = '<div id="'+id+'"><label for="reponse">Réponse</label><div class="row"><div class="col-sm-10"><input type="text" class="form-control" id="reponse1" size=100% name="reponse'+id+'"></div><div class="col-sm-1"><label><input type="checkbox" value="1" name="goodA'+id+'"></label></div><div class="col-sm-1"><i class="fa fa-times" id="delete_answer" onclick="removeDiv('+ id +');"></i></div></div></div>'
		document.getElementById(answer).appendChild(newdiv);
	}
	
	function addQuestion(question) { 
		var remove = 0; 
		id = remove + i; 
		var newdiv =document.createElement('div'); 
		if(i/2 == Math.round(i/2)){ 
			newdiv.innerHTML = '<div class="Qpaire">'+ 
			'<label for="question">Question</label> <input type="text"class="form-control" id="question" name="question" size=100%>'+ 
			'<div class="row">'+ 
			'<div class="col-xs-9"></div>'+ '<div class="col-xs-3">'+ 
			'<label>Cocher les bonnes </br>réponses</label>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '<div id="removeT0">'+ '<label for="reponse">Réponse</label>'+ 
		  '<div class="row">'+ '<div class="col-xs-10">'+ 
		  '<input type="text" class="form-control" id="reponse1" name="reponse1" size=100%>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ '<label> <input type="checkbox" value="1" name="goodA1">'+ 
		  '</label>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ 
		  '<i class="fa fa-times" id="delete_answer"></i>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '<div id="removeT1">'+ '<label for="reponse">Réponse</label>'+ 
		  '<div class="row">'+ '<div class="col-xs-10">'+ 
		  '<input type="text" class="form-control" id="reponse1" name="reponse1" size=100%>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ 
		  '<label> <input type="checkbox" value="1" name="goodA1">'+ 
		  '</label>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ 
		  '<i class="fa fa-times" id="delete_answer"></i>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '</div></br></br></br>'}
		else{
			newdiv.innerHTML = '<div class="Qimpaire">'+ 
			'<label for="question">Question</label> <input type="text"class="form-control" id="question" name="question" size=100%>'+ 
			'<div class="row">'+ 
			'<div class="col-xs-9"></div>'+ '<div class="col-xs-3">'+ 
			'<label>Cocher les bonnes </br>réponses</label>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '<div id="removeT0">'+ '<label for="reponse">Réponse</label>'+ 
		  '<div class="row">'+ '<div class="col-xs-10">'+ 
		  '<input type="text" class="form-control" id="reponse1" name="reponse1" size=100%>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ '<label> <input type="checkbox" value="1" name="goodA1">'+ 
		  '</label>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ 
		  '<i class="fa fa-times" id="delete_answer"></i>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '<div id="removeT1">'+ '<label for="reponse">Réponse</label>'+ 
		  '<div class="row">'+ '<div class="col-xs-10">'+ 
		  '<input type="text" class="form-control" id="reponse1" name="reponse1" size=100%>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ 
		  '<label> <input type="checkbox" value="1" name="goodA1">'+ 
		  '</label>'+ 
		  '</div>'+ 
		  '<div class="col-xs-1">'+ 
		  '<i class="fa fa-times" id="delete_answer"></i>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '</div>'+ 
		  '</div></br></br></br>'
		}
		 	  document.getElementById(question).appendChild(newdiv); i++; }

	function removeDiv(divId) {
		$("#" + divId).remove();
	}
	
	function getfile() {
		document.getElementById('imgInp').click();
		document.getElementById('selectedfile').value = document.getElementById('imgInp').value;
	}
	
	addOption("#nbrQ",200);
	addOption("#minR",10);
	addOption("#maxR",10);
	addOption("#hour",10);
	addOption("#minute",60);
	
	function addOption(id_select,num){
		for (var i = 1; i <= num; i++) {
			$(id_select).append($('<option>', { 
		        value: i,
		        text : i 
		    }));
			
		}
	}
	

$(document).ready(function(){ 
	countAnswer();  
	 function countAnswer(){
		  var v = $("#reponse_counter").val(); 
		  $("#addAnswer").click(function(){
			  v++; 
			  $("#reponse_counter").val(v); 
		  });
		  
		  $("#delete_answer").click(function(){ 
			  v = v-1;
			  $("#reponse_counter").val(v); 
		  });  
	  } 
	  $("#module-form").submit(function(event){ 
		  event.preventDefault(); 
	  });
	  
	  $("#btn-ajout-module").click(function(){ 
		  var dataString = $("#module-form").serialize();
		  $.ajax({ 
			  type: "POST", 
			  url: "/add-module",
			  data: dataString, 
			  dataType: "json",
	  
			  success: function(data) { 
				  $(".module-reponse").append('<div class="padding-10 col-sm-6 module-display'+data.id+'">'+ data.name+ 
						  					'</div>'+ 
						  					'<div class="padding-10 center col-sm-4 module-display'+data.id+'">'+ 
						  						'<button type="button" id="btn-delete-module'+data.id+'" class="btn btn-danger module-delete">Supprimer</button>'+ 
				  							'</div>'); 
			  }
		  }); 
	  });
	  $(".module-reponse").on('click', ".module-delete", function() {
		  var id = $(this).attr('id').substring(17);
		  $.ajax({ 
			  type: "POST", 
			  url: "/delete-module",
			  data: {id : id}, 
			  dataType: "json",
			  
			  success: function(data) {
				  var mod = ".module-display"+data.id_module;
				  $(mod).css({display: "none"});
			  }
		  }); 
		  
		});
	  
	  
	  $("#module-li").one("click",function(){ 
		  var dataString = $("#module-form").serialize();
		  $.ajax({ 
			  type: "POST", 
			  url: "/select-module",
			  data: dataString, 
			  dataType: "json",
	  
			  success: function(data) {
				 for(var i=0; i<data.length;i++){ 
				  $(".module-reponse").append('<div class="padding-10 col-sm-6 module-display'+data[i].id_module+'">'+ data[i].module_name+ 
						  					'</div>'+ 
						  					'<div class="padding-10 center col-sm-4 module-display'+data[i].id_module+'">'+ 
						  						'<button type="button" id="btn-delete-module'+data[i].id_module+'" class="btn btn-danger module-delete">Supprimer</button>'+ 
				  							'</div>'); 
				  
				 }
			  }	 
		  }); 
	  });
	  
	  $("#bdd-li").one("click",function(){
		  dataString = "";
		  
		  $.ajax({ 
			  type: "POST", 
			  url: "/select-question",
			  data: dataString, 
			  dataType: "json",
			  
			  success: function(data) {
					 for(var i=0; i<data.length;i++){ 
					  $(".question-select").append('<div class="padding-10 col-sm-8 question-display'+data[i].id_question+'">'+ data[i].question+ 
							  					'</div>'+
							  					'<div class="col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-warning">Modifier</button></div> '+
							  					'<div class="col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-danger question-delete" id="deleteQ'+data[i].id_question+'">Supprimer</button></div>');
					  
					 }
				  }
			  
	  
			
		  }); 
	  });
	  
	  $(".question-select").on('click', ".question-delete", function() {
		  var id = $(this).attr('id').substring(7);
		  $.ajax({ 
			  type: "POST", 
			  url: "/delete-question",
			  data: {id : id}, 
			  dataType: "json",
			  
			  success: function(data) {
				  var ques = ".question-display"+data.id_question;
				  $(ques).css({display: "none"});
			  }
		  }); 
		  
		});
});
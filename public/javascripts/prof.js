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
		 var l = 0;
		 var v = $("#reponse_counter").val(); 
		  
		  $("#addAnswer").click(function(){
			  	l++;
				 var newdiv = '<div id="remove'+l+'" >'+
				 				'<label for="reponse">Réponse</label>'+
				 				'<div class="row">'+
				 					'<div class="col-xs-10">'+
				 						'<input type="text" class="form-control" id="reponse'+l+'" name="reponse'+l+'">'+
				 					'</div>'+
				 					'<div class="col-xs-1">'+
				 						'<label>'+
				 							'<input type="checkbox" value="1" name="goodA'+l+'">'+
				 						'</label>'+
				 					'</div>'+
			 						'<div class="col-xs-1">'+
			 							'<i class="fa fa-times delete_answer" id="delete_answer'+l+'"></i>'+
			 						'</div>'+
			 					'</div>'+
			 				'</div>';
				$("#addQuestion").append(newdiv);
			  v++; 
			  $("#reponse_counter").val(v); 
		  });
		  
		  $("#addQuestion").on('click', ".delete_answer", function() {
			  var id = $(this).attr("id").substring(13);
			  $("#remove"+id).remove();
		  });  
	 
	 
	 
	  $(".modal-content").on('click', "#addAnswer", function() {
		  alert("ok");
	  });  



	  /**** MODULE ***/
	  
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
						  					'<div class="padding-10 center col-sm-3 pull-right module-display'+data.id+'">'+ 
						  						'<button type="button" id="btn-module-add-chapter'+data.id+'" class="btn btn-primary module-add-chapter">Ajouter un chapitre</button>'+ 
				  							'</div>'+
				  							'<div class="padding-10 col-sm-3 module-display'+data.id+'">'+ 
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
						  					'<div class="padding-10 center col-sm-3 module-display'+data[i].id_module+'">'+ 
						  					'<button type="button" id="btn-module-add-chapter'+data[i].id_module+'" class="btn btn-primary module-add-chapter">Ajouter un chapitre</button>'+ 
				  							'</div>'+
				  							'<div class="padding-10 col-sm-3 module-display'+data[i].id_module+'">'+ 
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
							  					'<div class="col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modifyQ">Modifier</button></div> '+
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
	  
	  /** TEST DE COURS **/
	  $("#add-test").hide();
	  	$("#create-test").click(function(){
	  		
	  		$(".test-info").hide();
	  		$("#add-test").fadeIn();
	  		
	  		var dataString = "";
			  $.ajax({ 
				  type: "POST", 
				  url: "/select-module",
				  data: dataString, 
				  dataType: "json",
				  success: function(data) {
					  $('#test_module option').remove();
					  $.each(data, function() {
						    $("#test_module").append($("<option />").val(this.id_module).text(this.module_name));
						});
				  }	 
			  });
	  	});
	  	
	  	$("#add-test-cancel").click(function(){
	  		$("#add-test").hide();
	  		$(".test-info").fadeIn();
	  		$(".Qpaire").remove();
	  		$(".Qimpaire").not(":first").remove();
	  		$("#test").find("input").val("");
	  		$('#question1_goodA1').attr('checked', false);
	  		return false;
	  	});
	  	
	  	var answer_test_counter = $("#answer_test_counter").val();
	  	var question_test_counter = $("#question_test_counter").val(); 
	  	var i = 2;
	  	var m = 1;
		$("#ajouter-test-q").click(function(){
			m++;
			var paire_ou_impaire;
			if(i/2 == Math.round(i/2)){
				paire_ou_impaire = "Qpaire";
			}else{
				paire_ou_impaire = "Qimpaire";
			}
			var newdiv = '<div class="'+paire_ou_impaire+'" id="Test_Q'+i+'">'+
							'<div id="question'+i+'">'+
								'<div class="row">'+
									'<div class="col-xs-12 padding-10">'+
										'<label for="question">Question</label>'+
									'</div>'+
									'<div class="col-xs-8">'+
										'<input type="text"class="form-control width-100" name="question'+i+'">'+
									'</div>'+
								'</div>'+	
								'<div id="question'+i+'_answer'+m+'">'+
									'<div class="row">'+
										'<div class="col-xs-8 padding-10">'+
											'<label for="reponse">Réponse</label>'+
										'</div>'+
										'<div class="col-xs-4 padding-left-100">'+
											'<label>Cocher les bonnes </br>réponses</label>'+
										'</div>'+	
									'</div>'+	
									'<div class="row">'+ 
										'<div class="col-xs-8">'+ 
											'<input type="text" class="form-control width-100" name="question'+i+'_answer'+m+'">'+ 
										'</div>'+ 
										'<div class="col-xs-2 align-right">'+ 
												'<input type="checkbox" value="1" name="question'+i+'_goodA'+m+'">'+ 
										'</div>'+ 
									'</div>'+ 
								'</div>'+ 
							'</div>'+
							'<br/>'+
							'<div class="row padding-10">'+
								'<div class="col-sm-9">'+
									'<button type="button" class="btn btn-danger delete-test-q" id="delete-test-q'+i+'">Supprimer cette question</button>'+
								'</div>'+
								'<div class="col-sm-3">'+	
									'<button type="button" class="btn btn-primary ajouter-test-r" id="ajouter-test-r'+i+'">Ajouter une réponse</button>'+
								'</div>'+	
							'</div>'+
						'</div>';
			$("#container_testQ").append(newdiv)
			 	  i++;
				question_test_counter++;
				$("#question_test_counter").val(question_test_counter);
				answer_test_counter++;
				$("#answer_test_counter").val(answer_test_counter);
		});
		
		$("#container_testQ").on('click', ".ajouter-test-r", function() {
			var id = $(this).attr("id").substring(14);
			m++;
			 var newdiv = '<div id="question'+id+'_answer'+m+'">'+ 
							 '<div class="row">'+
								'<div class="col-xs-8 padding-10">'+
									'<label for="reponse">Réponse</label>'+
								'</div>'+
							'</div>'+	
							'<div class="row">'+ 
								'<div class="col-xs-8">'+ 
									'<input type="text" class="form-control width-100" name="question'+id+'_answer'+m+'" >'+ 
								'</div>'+ 
								'<div class="col-xs-2 align-right">'+ 
										'<input type="checkbox" value="1" name="question'+id+'_goodA'+m+'">'+  
								'</div>'+ 
								'<div class="col-xs-2 center">'+ 
									'<i class="fa fa-times test_cours_delete_answer" id="question'+id+'_delete_answer'+m+'"></i>'+ 
								'</div>'+ 
							'</div>'+ 
						'</div>';
			 
			$("#question"+id).append(newdiv);
				answer_test_counter++;
				$("#answer_test_counter").val(answer_test_counter);
		});
		
		$("#container_testQ").on('click', ".test_cours_delete_answer", function() {
			  var id = $(this).attr("id").replace(/_delete/g, '');
			  $("#"+id).remove();
		});
		
		$("#container_testQ").on('click', ".delete-test-q", function() {
			  var id = $(this).attr("id").substring(13);
			  $("#Test_Q"+id).remove();
		});
		
		$("#test-li").click(function(){ 
			  var dataString = "";
			  $.ajax({ 
				  type: "POST", 
				  url: "/select-test-prof",
				  data: dataString, 
				  dataType: "json",
				  success: function(data) {
					  if(data.length > 1){
						  $('.nothing-in-test').remove();
						  $.each(data, function() {
							    $("#test-info").append('<div class="col-sm-8 nothing-in-test">'+this.title+'</div>');
							});
					  }  
				  }	 
			  }); 
		  });
		
});
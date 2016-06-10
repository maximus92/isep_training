	function removeDiv(divId) {
		$("#" + divId).remove();
	}
	
	
	
	function getfile(){
	    document.getElementById('hiddenfile').click();
	    document.getElementById('selectedfile').value=document.getElementById('hiddenfile').value;
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
				v++; 
				var newdiv = addAnswerDiv(l);
			 	$("#addQuestion").append(newdiv);
			 	$("#reponse_counter").val(v); 
		 });
		 
		 $("#addQuestion").on('click', ".delete_answer", function() {
			 var id = $(this).attr("id").substring(13);
			 $("#remove"+id).remove();
		 });
		 
	
	  /**** MODULE ***/
	  
		$("#module-form").submit(function(event){ 
		  event.preventDefault(); 
	  });
	  
	  $("#btn-ajout-module").click(function(){ 
		  var dataString = $("#module-form").serialize();
			ajaxBody("/add-module",dataString,function(data) { 
					$(".module-reponse").append(addModuleDiv(data.id,data.name)); 
			});
	  });
		
	  $(".module-reponse").on('click', ".module-delete", function() {
		  var id = $(this).attr('id').substring(17);
			var dataString = {id : id};
			ajaxBody("/delete-module",dataString,function(data) {
					var mod = ".module-display"+data.id_module;
					$(mod).css({display: "none"});
			});
		});
	
		$("#module-li").one("click",function(){
			$("#module-detail").hide();
		  var dataString = $("#module-form").serialize();
			ajaxBody("/select-module",dataString,function(data) {
			 		for(var i=0; i<data.length;i++){ 
							$(".module-reponse").append(displayModuleDiv(data,i)); 
			 		}
			});
	  });
		
		$(".module-reponse").on('click',".btn-module-detail",function(){
			$(".div-chapter").remove();
			$(".module-info").hide();
			$("#module-detail").fadeIn();
			var id_module = $(this).attr('id').substring(17);
			$("#hidden-id_module").val(id_module);
			var module_title = $("#module-info-name"+id_module).text();
			$("#module-detail-title").text(module_title);
			var dataString = {id_module: id_module};
			ajaxBody("/detail-module",dataString,function(data) {
				if(data.length){
					$(".chapter-reponse").html(displayChapterDiv(data)); 
				}
					
			});
				
		});
		
		$("#module-detail").on('click',".btn-module-add-chapter",function(){
			var id_module = $("#hidden-id_module").val();
			var chapter_name = $("#input-chapter_name").val();
			var dataString = {id_module: id_module, chapter_name: chapter_name};
			ajaxBody("/add-chapter",dataString,function(data) {
					$(".chapter-reponse").append(addChapterDiv(chapter_name,data.id_chapter));
			});
				
		});
		
		$("#module-detail").on('click',"#module-detail-cancel",function(){
			$(".module-info").fadeIn();
			$("#module-detail").hide();
		});
		
		$("#module-detail").on('click',".btn-delete-chapter",function(){
			var id_chapter = $(this).attr('id').substring(18);
			var id_module = $("#hidden-id_module").val();
			var dataString = {id_chapter: id_chapter, id_module: id_module};
			ajaxBody("/delete-chapter",dataString,function(data) {
				$("#div-chapter"+id_chapter).remove();
				
			});
		});
	  
	/*** FIN MODULE ***/
	/** BDD **/
	  $("#bdd-li").one("click",function(){
		  dataString = "";
		  var level;
		  var forexam;
		  $.ajax({ 
			  type: "POST", 
			  url: "/select-question",
			  data: dataString, 
			  dataType: "json",
			  
			  success: function(data) {
					 for(var i=0; i< data.length;i++){ 
						 if(data[i].level == "0"){
							 level = "facile";
						 }
						 if(data[i].level == 1){
							 level = "moyen";
						 }
						 if(data[i].level == 2){
							 level = "difficile";
						 }
						 if(data[i].forexam == 0){
							 forexam = "Entrainement";
						 }
						 else{
							 forexam = "Examen";
						 }
						 
					  $(".question-select").append('<div class="padding 10 col-sm-4 question-display'+data[i].id_question+'">'+ data[i].question+ 
							  					'</div>'+
							  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'">'+forexam+'</div>'+
							  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'">'+level+
							  					'</div>'+
							  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-warning modifyQA" data-toggle="modal" data-target="#modifyQ" id="modifyQA'+data[i].id_question+'">Modifier</button></div> '+
							  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-danger question-delete" id="deleteQ'+data[i].id_question+'">Supprimer</button></div></br></br>');
					  
					 }
				  }
		  }); 
	  });
	  
	  
	  
	  $(".question-select").on('click', ".question-delete", function() {
		  var id = $(this).attr('id').substring(7);
			var dataString = {id : id};
			ajaxBody("/delete-question",dataString,function(data) {
					var ques = ".question-display"+data.id_question;
					$(ques).css({display: "none"});
			});
		});
	  
	  
	  $(".question-select").on('click', ".modifyQA", function() {
		  var id = $(this).attr('id').substring(8);
			var dataString = {id : id};
			ajaxBody("/select-answer",dataString,function(data) {
				$(".form_update_question").remove();
				$(".answer-select").append(questionUpdateDiv(data));
				$("#reponse_counter_modify").val(data[0].length);
			});
	  });
	  
	  $(".answer-select").on('click', "#validateModification", function() {
		  dataString = "";

		  $.ajax({ 
			  type: "POST", 
			  url: "/update-question",
			  data: dataString, 
			  dataType: "json",
			  
			  success: function(data) {
				  alert("La question a été modifiée!");
			  }
		  }); 
		  
		});
	  
	  $(".modal-content").on('click', "#addAnswer", function() {
			var counter_answer_modify = $("#reponse_counter_modify").val();
			 var newdiv = addAnswerDiv(counter_answer_modify);
			 counter_answer_modify++;
			 $("#reponse_counter_modify").val(counter_answer_modify);
			 $("#addQuestionA").append(newdiv);

			
		 });  
	  
	  $("#filter_question").click(function(){
		  $("#form_filtre_question").submit(function(e){
			  e.preventDefault(); });
			$(".question-select").hide();
			var forexam;
			var level;
		  dataString = "";
		  $.ajax({ 
			  type: "POST", 
			  url: "/filter-question",
			  data: dataString, 
			  dataType: "json",
			  
			  success: function(data) {
				  for(var i=0; i< data.length;i++){
					  if(data[i].level == "0"){
							 level = "facile";
						 }
						 if(data[i].level == 1){
							 level = "moyen";
						 }
						 if(data[i].level == 2){
							 level = "difficile";
						 }
						 if(data[i].forexam == 0){
							 forexam = "Entrainement";
						 }
						 else{
							 forexam = "Examen";
						 }
					  $(".question_filter").append('<div class="padding 10 col-sm-4 question-display'+data[i].id_question+'">'+ data[i].question+ 
	  					'</div>'+
	  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'">'+forexam+'</div>'+
	  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'">'+level+
	  					'</div>'+
	  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-warning modifyQA" data-toggle="modal" data-target="#modifyQ" id="modifyQA'+data[i].id_question+'">Modifier</button></div> '+
	  					'<div class="padding 10 col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-danger question-delete" id="deleteQ'+data[i].id_question+'">Supprimer</button></div></br></br>');

			  }
			  }
				  
		  }); 
	  });
	  
	  
	  

  
	  /** TEST DE COURS **/
		$("#add-test").hide();
	  $(".test-detail").hide();
	  
	  $("#create-test").click(function(){
	  		$(".test-info").hide();
	  		$("#add-test").fadeIn();
	  		displayModuleInSelect("#test_module", 0);
	  	});
	  	
		$("#add-test-cancel").click(function(){
	  		$("#add-test").hide();
	  		$(".test-info").fadeIn();
	  		$(".Qpaire").remove();
	  		$(".Qimpaire").not(":first").remove();
	  		$("#test").find("input").not("#answer_test_counter, #question_test_counter").val("");
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
				var newdiv =  addTestQuestionDiv(paire_ou_impaire,i,m);
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
			 	var newdiv = addTestAnswerDiv(id,m);
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
			  ajaxBody("/select-test-prof","",
				function(data) {
					if(data.length >= 1){
						$('.nothing-in-test').hide();
						$.each(data, function() {
								$("#test-info").append(displayTestDiv(this.isenable,this.id_test,this.title));
						});
					}  
				});	  
		  });
		
			buttonEnable(".enable-test","1");
			buttonEnable(".disable-test","0");
			
			function buttonEnable(btn,isenable){
				$("#test-info").on('click', btn,function(){
					var id = $(this).attr("id").substring(11);
					var dataString = {id: id, isenable: isenable};
					ajaxBody("/enable-test-prof",dataString,
					function(data) {
						if(data.res != "1"){
							return false;
						}
						if(btn == ".enable-test"){
							$("#enable-test"+id).removeClass("enable-test");
							$("#enable-test"+id).addClass("disable-test");
							
							$("#dispo"+id).removeClass("red");
							$("#dispo"+id).addClass("green");
							$("#dispo"+id).text("Disponible");
							
							$("#enable-test"+id).removeClass("btn-success");
							$("#enable-test"+id).addClass("btn-danger");
							$("#enable-test"+id).text("Rendre indisponible");
						}else{
							$("#enable-test"+id).removeClass("disable-test");
							$("#enable-test"+id).addClass("enable-test");
							
							$("#dispo"+id).removeClass("green");
							$("#dispo"+id).addClass("red");
							$("#dispo"+id).text("Indisponible");
							
							$("#enable-test"+id).removeClass("btn-danger");
							$("#enable-test"+id).addClass("btn-success");
							$("#enable-test"+id).text("Rendre disponible");
						}	
					});
				});
			}
			
			$("#test-info").on('click', ".btn-test-detail",function(){
				$(".test-info").hide();
		  		$(".test-detail").fadeIn();
		  		var id_test = $(this).attr("id").substring(15);
						dataString = {id_test: id_test};
						ajaxBody("/select-test-prof",dataString,function(data){
									$("#test-detail-title").text(data[0].title);
									if(data[0].isenable == "1"){
										$("#test-detail-status").html("Statut : <span class='green'>Disponible</span>");
									}else{
										$("#test-detail-status").html("Statut : <span class='red'>Indisponible</span>");
									}
									$("#test-detail-delete").html("<button id='test-detail-delete"+id_test+"' class='btn btn-danger test-detail-delete'>Supprimer</button>");
									displayModuleInSelect("#test-detail-module",data[0].id_module);		
						});
						
						ajaxBody("/select-test-answer",dataString,function(data){
							$(".test-detail-ul").remove();
							$("#test-detail-question").append(displayTestDetailQuestionDiv(data));
						});		
			});
			
			$(".test-detail-back").click(function(){
				$(".test-info").fadeIn();
		  		$(".test-detail").hide();
			});
			
			$("#test-detail").on('click', '.test-detail-delete', function(){
				var id_test = $(this).attr("id").substring(18);
				var dataString = {id_test: id_test};
				ajaxBody("/delete-test",dataString,function(data) {
					$("#test-detail-list"+id_test).remove();
					$(".test-info").fadeIn();
			  		$(".test-detail").remove();
			  		$('.nothing-in-test').fadeIn();
				});
			});
			
		function ajaxBody(url,dataString,successFunction){
			$.ajax({ 
					type: "POST", 
					url: url,
					data: dataString, 
					dataType: "json",
					success: function(data) {
						successFunction(data);
					}	 
				}); 
		}
		
		function displayModuleInSelect(select_id,option_selected){
			ajaxBody("/select-module","",
					function(data){
							$(select_id+' option').remove();
							$(select_id).append($("<option />").val("0").text("Aucun"));
							$.each(data, function() {
								if(this.id_module == option_selected){
									var selected_value = "<option selected />";
								}else{
									var selected_value = "<option />";
								}
								$(select_id).append($(selected_value).val(this.id_module).text(this.module_name));
							});
					});
		}
		
});
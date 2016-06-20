
    function addAnswerDiv(l){
      var div = '<div id="remove'+l+'" >'+
             '<label for="reponse">Réponse</label>'+
             '<div class="row">'+
               '<div class="col-xs-10">'+
               '<input type="hidden" id="id_answer'+l+'" name="id_answer'+l+'" value="0">'+
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
      return div;
    }
    
    function addModuleDiv(id,name){
      var div = '<div class="padding-10 col-sm-6 module-display'+id+'" id="module-info-name'+id+'">'+ name+ 
                    '</div>'+ 
                    '<div class="padding-10 center col-sm-3 center module-display'+id+'">'+ 
                      '<button type="button" id="btn-module-detail'+id+'" class="btn btn-primary btn-module-detail">Détail</button>'+ 
                    '</div>'+
                    '<div class="padding-10 col-sm-3 module-display'+id+'">'+ 
                      '<button type="button" id="btn-delete-module'+id+'" class="btn btn-danger module-delete">Supprimer</button>'+ 
                    '</div>';
      return div;              
    }
    
    function displayModuleDiv(data,i){
      var div = '<div class="padding-10 col-sm-6 module-display'+data[i].id_module+'" id="module-info-name'+data[i].id_module+'">'+ data[i].module_name+ 
                    '</div>'+ 
                    '<div class="padding-10 center col-sm-3 module-display'+data[i].id_module+'">'+ 
                    '<button type="button" id="btn-module-detail'+data[i].id_module+'" class="btn btn-primary btn-module-detail">Détail</button>'+ 
                    '</div>'+
                    '<div class="padding-10 col-sm-3 module-display'+data[i].id_module+'">'+ 
                    '<button type="button" id="btn-delete-module'+data[i].id_module+'" class="btn btn-danger module-delete">Supprimer</button>'+ 
                  '</div>';
      return div;            
    }
    
    function addTestQuestionDiv(paire_ou_impaire,i,m){
      var div = '<div class="'+paire_ou_impaire+'" id="Test_Q'+i+'">'+
							'<div id="question'+i+'">'+
								'<div class="row">'+
									'<div class="col-xs-12 padding-10">'+
										'<label for="question">Question</label>'+
									'</div>'+
									'<div class="col-xs-8" id="question_input_div'+i+'">'+
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
            return div;
    }
    
    function addTestAnswerDiv(id,m){
      var div = '<div class="remove_answer_div'+id+'" id="question'+id+'_answer'+m+'">'+ 
              '<div class="row">'+
               '<div class="col-xs-8 padding-10">'+
                 '<label for="reponse">Réponse</label>'+
               '</div>'+
               '<div class="col-xs-4 padding-left-100">'+
					'<label id="cocher_reponse'+m+'"></label>'+
				'</div>'+
             '</div>'+	
             '<div class="row">'+ 
               '<div class="col-xs-8">'+ 
                 '<input type="text" class="form-control width-100" id="input_test_answer'+m+'" name="question'+id+'_answer'+m+'" >'+ 
               '</div>'+ 
               '<div class="col-xs-2 align-right">'+ 
                   '<input type="checkbox" value="1" name="question'+id+'_goodA'+m+'">'+  
               '</div>'+ 
               '<div class="col-xs-2 center">'+ 
               	'<span class="glyphicon glyphicon-remove test_cours_delete_answer" aria-hidden="true" id="question'+id+'_delete_answer'+m+'"></span>'+
               '</div>'+ 
             '</div>'+ 
           '</div>';
           return div;
    }
    
    function displayTestDiv(isenable,id_test,title){
        if(isenable == "0"){
          var dispo = '<div class="col-sm-2 red" id="dispo'+id_test+'">Indisponible</div>';
         var btn_dispo = '<button class="btn btn-success enable-test" id="enable-test'+id_test+'">Rendre disponible</button>';
        }else{
          var dispo = '<div class="col-sm-2 green" id="dispo'+id_test+'">Disponible</div>';
          var btn_dispo = '<button class="btn btn-danger disable-test" id="enable-test'+id_test+'">Rendre indisponible</button>';
        }
        
        var div = '<div class="row padding-10 nothing-in-test" id="test-detail-list'+id_test+'">'+
                      '<div class="col-sm-4">'+title+'</div>'+
                        dispo+
                      '<div class="col-sm-3 align-right">'+
                        '<button class="btn btn-primary btn-test-detail" id="btn-test-detail'+id_test+'">Détail</button>'+
                      '</div>'+
                      '<div class="col-sm-3">'+
                        btn_dispo+
                      '</div></div>';
        return div;              
      }
    
    function questionUpdateDiv(data){
      var option1 = "";
      var option2 = "";
      var option3 = "";
      var option4 = "";
      var option5 = "";
      var picture = "";
      
      switch (data[1][0].level){
        case "0":
          option1 = "selected";
          break;
        case "1":
          option2 = "selected";
          break;
        case "2":
          option3 = "selected";
          break;
      }
      
       var difficulty = '<div class="col-xs-3">'+
                          '<select name="level" id="level">'+
                               '<option value="0" '+option1+'>Facile</option>'+
                               '<option value="1" '+option2+'>Moyen</option>'+
                               '<option value="2" '+option3+'>Difficile</option>'+
                          '</select>'+
                        '</div>';	 
      
      
      if(data[1][0].forexam == "0"){
         option4 = "selected";
      }else{
         option5 = "selected";
      }
      
      var forexam = '<div class="col-xs-2">'+
                        '<SELECT name="forexam" id="forexam">'+
                            '<OPTION value="0" '+option4+'>Entrainement des élèves</OPTION>'+
                            '<OPTION value="1" '+option5+'>Examen</OPTION>'+
                        '</SELECT>'+
                    '</div>';
      
      if(data[1][0].file == null ){
    	  picture;
       }else{
           picture = '<img src="'+data[1][0].file+'" width="50" height="50">';
       }
      
                    	 
     var formulaire1 = '<form method="post" class="form_update_question" action="/update-question">'+
                            '<div class="row">'+
                              '<div class="col-xs-2">'+
                                '<label>Difficulté : </label>'+
                              '</div>'+difficulty+
                              '<div class="col-xs-3">'+
                                  '<label>Question pour : </label>'+
                              '</div>'+forexam+
                            '</div>'+
                            '</br>'+
                            '<div class="row">'+
                              '<div class="col-xs-2 ">'+
                                  '<label for="module">Module: </label>'+
                              '</div>'+
                              '<div class="col-xs-3">'+
                                '<select id="test_module" name="test_module" size="1"></select>'+
                              '</div>'+
                              '<div class="col-xs-2">'+
                                '<label for="chapter">Chapitre: </label>'+
                              '</div>'+
                              '<div class="col-xs-3">'+
                                '<select id="chapter_modal_modifyQ" name="test_chapter" size="1">'+
                                  '<option value="">Aucun</option>'+
                                '</select>'+
                              '</div>'+
                           '</div>'+
                           '</br>'+
                           '<div class="form-group">'+
                           '<input type="hidden" value="'+data[1][0].id_question+'" name="id_question" id="id_question" />'+
                             '<label for="question">Question</label> <input type="text"'+
                               'class="form-control" id="question" name="question" value="'+data[1][0].question+'">'+
                             '<div class="pull-right">'+picture+
                             '<input type="file" id="imgInp" style="display: none" name="file" />'+
     						'<button type="button" class="btn btn-primary" onclick="getfile();">Ajouter support</button>'+
                               '<button type="button" class="btn btn-success btn-sm">&</button>'+
                             '</div>'+
                           '</div>'+
                           '</br> </br>'+
                           '<div class="row">'+
                             '<div class="col-xs-3 pull-right">'+
                               '<label> Cocher les bonnes </br>réponses'+
                               '</label>'+
                             '</div>'+
                           '</div>'+
                           '<input type="hidden" value="1" name="reponse_counter_modify"'+
                           'id="reponse_counter_modify" />'+
                           '</br><div id="addQuestionA">';

         for(var i=0; i< data[0].length; i++){
              if(data[0][i].istrue == "0"){
                var checkA = "";
              }else{
                var checkA = "checked";
              }
             var isTrue =  '<div class="col-xs-1">'+
                             '<label> <input type="checkbox" value="'+data[0][i].istrue+'" name="goodA'+data[0][i].id_answer+ i+'" '+checkA+'>'+
                             '</label>'+
                           '</div>';
           
                formulaire1 += '<div id="remove0">'+
                               '<label for="reponse">Réponse</label>'+
                               '<div class="row">'+
                                 '<div class="col-xs-10">'+
                                 '<input type="hidden" id="id_answer'+i+'" name="id_answer'+i+'" value="'+data[0][i].id_answer+'">'+
                                   '<input type="text" class="form-control answer-label" name="reponse'+i+'" id="reponse'+ i +'" '+
                                     'value="'+data[0][i].answer+'">'+
                                 '</div>'+isTrue+
                                 '<div class="col-xs-1">'+
                                   '<i class="fa fa-times delete_answer" id="delete_answer0"></i>'+
                                 '</div>'+
                               '</div>'+
                             '</div>';
        }
         
         formulaire1 += '</div></br>'+
                           '<button type="button" class="btn btn-primary pull-left"'+
                           'id="addAnswer">Ajouter une réponse</button>'+
                         '</br> </br> </br> <label for="correction">Correction détaillée</label>'+
                         '<textarea rows="4" cols="50" class="form-control" name="correction" id="correction">'+data[1][0].correction+'</textarea>'+
                         '<button type="submit" class="btn btn-danger btn-lg pull-right" id="validateModification'+data[1][0].id_question+'">Valider</button>'+
                         '</br></br>'+
                       '</form>';
     return formulaire1;  
    }
    
    function displayTestDetailQuestionDiv(data){
    	var div = "";
    	for(var i=0; i< data[0].length;i++){
    			var num = i + 1;
    			div += '<ul class="list-group test-detail-ul">'+
				'<li class="list-group-item active">Question '+num+' : '+data[0][i].question+'</li>';
    			
    			for(var j=0; j< data[1][i].length;j++){
    				var success ="";
    				if(data[1][i][j].istrue == "1"){
    					success = "list-group-item-success";
    				}
    				div += '<li class="list-group-item '+success+'">'+data[1][i][j].answer+'</li>';
    			}
    			div += '</ul>';
    			  	
    	}
    	return div;
    }
    
    function displayChapterDiv(data){
    	var div = "";
    	for(var i=0;i<data.length;i++){
    		div += '<div class="padding-10 div-chapter" id="div-chapter'+data[i].id_chapter+'">'+
    				'<div class="col-sm-7 padding-10 div-chapter" id="div-chapter'+data[i].id_chapter+'">'+data[i].chapter_name+'</div>'+
    				'<div class="col-sm-5 div-chapter center padding-10" id="div-chapter'+data[i].id_chapter+'">'+
    					'<button class="btn btn-danger btn-delete-chapter" id="btn-delete-chapter'+data[i].id_chapter+'">Supprimer</button>'+
    				'</div></div>';	
    					
    	}
    	return div;        
    }
    
    function addChapterDiv(chapter_name,id_chapter){
    	var div = '<div  class="padding-10 div-chapter" id="div-chapter'+id_chapter+'">'+
    				'<div class="col-sm-7 padding-10 div-chapter" id="div-chapter'+id_chapter+'">'+chapter_name+'</div>'+
    				'<div class="col-sm-5 center padding-10 div-chapter" id="div-chapter'+id_chapter+'">'+
    					'<button class="btn btn-danger btn-delete-chapter" id="btn-delete-chapter'+id_chapter+'">Supprimer</button>'+
    				'</div></div>';	
    					
    	
    	return div;        
    }
  
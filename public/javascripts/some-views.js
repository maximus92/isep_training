
    function addAnswerDiv(l){
      var div = '<div id="remove'+l+'" >'+
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
      return div;
    }
    
    function addModuleDiv(id,name){
      var div = '<div class="padding-10 col-sm-6 module-display'+id+'">'+ name+ 
                    '</div>'+ 
                    '<div class="padding-10 center col-sm-3 center module-display'+id+'">'+ 
                      '<button type="button" id="btn-module-add-chapter'+id+'" class="btn btn-primary module-add-chapter">Ajouter un chapitre</button>'+ 
                    '</div>'+
                    '<div class="padding-10 col-sm-3 module-display'+id+'">'+ 
                      '<button type="button" id="btn-delete-module'+id+'" class="btn btn-danger module-delete">Supprimer</button>'+ 
                    '</div>';
      return div;              
    }
    
    function displayModuleDiv(data,i){
      var div = '<div class="padding-10 col-sm-6 module-display'+data[i].id_module+'">'+ data[i].module_name+ 
                    '</div>'+ 
                    '<div class="padding-10 center col-sm-3 module-display'+data[i].id_module+'">'+ 
                    '<button type="button" id="btn-module-add-chapter'+data[i].id_module+'" class="btn btn-primary module-add-chapter">Ajouter un chapitre</button>'+ 
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
            return div;
    }
    
    function addTestAnswerDiv(id,m){
      var div = '<div id="question'+id+'_answer'+m+'">'+ 
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
      
      var div = '<div class="row padding-10 nothing-in-test">'+
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
                          '<select name="level">'+
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
                        '<SELECT name="forexam">'+
                            '<OPTION value="0" '+option4+'>Entrainement des élèves</OPTION>'+
                            '<OPTION value="1" '+option5+'>Examen</OPTION>'+
                        '</SELECT>'+
                    '</div>';
                    	 
     var formulaire1 = '<form method="post" class="form_update_question" action="@routes.ProfessorController.updateA()">'+
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
                                '<select name="test_chapter" size="1">'+
                                  '<option value="">Aucun</option>'+
                                '</select>'+
                              '</div>'+
                           '</div>'+
                           '</br>'+
                           '<div class="form-group">'+
                             '<label for="question">Question</label> <input type="text"'+
                               'class="form-control" id="question" name="question" value="'+data[1][0].question+'">'+
                             '<div class="pull-right">'+
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
                           '</br>';

         for(var i=0; i< data[0].length; i++){
              if(data[0][i].istrue == "0"){
                var checkA = "";
              }else{
                var checkA = "checked";
              }
             var isTrue =  '<div class="col-xs-1">'+
                             '<label> <input type="checkbox" value="'+data[0][i].istrue+'" name="goodA'+data[0][i].id_answer+'" '+checkA+'>'+
                             '</label>'+
                           '</div>';
           
                formulaire1 += '<div id="addQuestionA">'+
                             '<input type="hidden" value="1" name="reponse_counter"'+
                               'id="reponse_counter" />'+
                             '<div id="remove0">'+
                               '<label for="reponse">Réponse</label>'+
                               '<div class="row">'+
                                 '<div class="col-xs-10">'+
                                   '<input type="text" class="form-control answer-label" id="reponse'+data[0][i].id_answer+'"'+
                                     'value="'+data[0][i].answer+'">'+
                                 '</div>'+isTrue+
                                 '<div class="col-xs-1">'+
                                   '<i class="fa fa-times delete_answer" id="delete_answer0"></i>'+
                                 '</div>'+
                               '</div>'+
                             '</div>'+
                           '</div>';
        }
         
         formulaire1 += '</br>'+
                           '<button type="button" class="btn btn-primary pull-left"'+
                           'id="addAnswer">Ajouter une réponse</button>'+
                         '</br> </br> </br> <label for="correction">Correction détaillée</label>'+
                         '<textarea rows="4" cols="50" class="form-control" name="correction">'+data[1][0].correction+'</textarea>'+
                         '</br>'+
                       '</form>';
     return formulaire1;  
    }

  
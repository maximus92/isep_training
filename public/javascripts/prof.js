//$(document).ready(function(){
	
	var i = 1;
	function add(addQuestion) {
		i++;
		var remove = 0;
		id = remove + i;
		var newdiv = document.createElement('div');
		newdiv.innerHTML = '<div id="'+id+'"><label for="reponse">Réponse</label><div class="row"><div class="col-sm-10"><input type="text" class="form-control" id="reponse1" size=50% name="reponse'+id+'"></div><div class="col-sm-1"><label><input type="checkbox" value="1" name="goodA'+id+'"></label></div><div class="col-sm-1"><i class="fa fa-times" id="delete_answer" onclick="removeDiv('+ id + ');"></i></div></div></div>'
		document.getElementById(addQuestion).appendChild(newdiv);
	}

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
			$("#delete_answer").live("click",function(){
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
	            	$(".module-reponse").append('<div class="padding-10 col-sm-6">'+
	            					data.name+
	            					'<input type="hidden" class="form-control" name="id_user" value="'+data.id+'">'+
	            				'</div>'+	
	            				'<div class="padding-10 center col-sm-4">'+
	                				'<button type="submit" id="btn-delete-module" class="btn btn-danger">Supprimer</button>'+
	              				'</div>');
	                          }
	        });       
		});
	});
	
//});
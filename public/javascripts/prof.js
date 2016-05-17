	
	var i = 1;
	function add(addQuestion) {
		i++;
		var remove;
		id = remove + i;
		var newdiv = document.createElement('div');
		newdiv.innerHTML = '<div id="'+id+'"><label for="reponse">Réponse</label><div class="row"><div class="col-sm-10"><input type="text" class="form-control" id="reponse1" size=50%></div><div class="col-sm-1"><label><input type="checkbox" value=""></label></div><div class="col-sm-1"><i class="fa fa-times" onclick="removeDiv('+ id + ');"></i></div></div></div>'
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
	


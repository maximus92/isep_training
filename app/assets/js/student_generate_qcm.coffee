$ ->
	$.get "/student/getAllModules", (getAllModules) ->
		$.each getAllModules, (index, module) ->
			$('#module').append $("<li>").text module	

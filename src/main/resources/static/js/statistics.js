var curSource = new Array();
//first source uses querystring to determine what events to pull back
//curSource[0] = '/hackyjson/cal?e1=' +  $('#e1').is(':checked') + '&e2='+ $('#e2').is(':checked');
//second source just returns all events
curSource[1] = '/api/calendar';
var newSource = new Array(); //we'll use this later

$(document).ready(function() {     
	$('#calendar').fullCalendar({
        eventSources: [curSource[1]],
		header : {
			left : 'prev,next today',
			center : 'title',
			right : 'month,agendaWeek,agendaDay'
		},
		firstDay: 1, // Monday because Estonia :D
		timeFormat: 'H(:mm)', // uppercase H for 24-hour clock
        
        eventRender: function (event, element) {
            element.attr('href', 'javascript:void(0);');
        }
    });

    $("#consultants").change(function() {
    	if($(this).find(':selected').val() == 0) {
    		var url = '/api/calendar';
    	} 
    	else {
    		var url = '/api/calendar/' + $(this).find(':selected').val();
    	}
    	
    	//var url = '/api/calendar/' + $(this).find(':selected').val();
    	console.log(url);
        //get current status of our filters into newSource
        //newSource[0] = '/hackyjson/cal?e1=' +  $('#e1').is(':checked') + '&e2='+ $('#e2').is(':checked');
        //newSource[1] = $('#e3').is(':checked') ? '/hackyjson/anothercal/' : '';
    	newSource[0] = '';
    	newSource[1] = url;

        //remove the old eventSources
        //$('#eventFilterCalendar').fullCalendar('removeEventSource', curSource[0]);
        $('#calendar').fullCalendar('removeEventSource', curSource[1]);
        $('#calendar').fullCalendar('refetchEvents');

        //attach the new eventSources
        //$('#eventFilterCalendar').fullCalendar('addEventSource', newSource[0]);
        $('#calendar').fullCalendar('addEventSource', newSource[1]);
        $('#calendar').fullCalendar('refetchEvents');

        //curSource[0] = newSource[0];
        curSource[1] = newSource[1];
    });
});

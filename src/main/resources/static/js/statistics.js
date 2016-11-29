var eventSource = '/api/calendar';

var dropDownListener = function() {
	$("#consultants").change(function() {
		if ($(this).find(':selected').val() == 0) {
			var url = '/api/calendar';
		} else {
			var url = '/api/calendar/' + $(this).find(':selected').val();
		}

		// remove the old eventSources
		$('#calendar').fullCalendar('removeEventSource', eventSource);
		$('#calendar').fullCalendar('refetchEvents');

		// attach the new eventSources
		$('#calendar').fullCalendar('addEventSource', url);
		$('#calendar').fullCalendar('refetchEvents');

		eventSource = url;
	});
}

$(document).ready(function() {
	$('#calendar').fullCalendar({
		eventSources : [ eventSource ],
		header : {
			left : 'prev,next today',
			center : 'title',
			right : 'month,agendaWeek,agendaDay'
		},
		firstDay : 1, // Monday because Estonia :D
		timeFormat : 'H(:mm)', // uppercase H for 24-hour clock

		eventRender : function(event, element) {
			element.attr('href', 'javascript:void(0);');
		}
	});

	dropDownListener();

});

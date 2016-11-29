
function retrieveStatistics(period) {
	console.log(period);
	$("#resultsBlock").load("/statistics/"+period);
}
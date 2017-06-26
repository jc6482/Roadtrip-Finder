/*
$(document).ready($("#userCriteria").mouseenter(
//on mouseenter
	function() {
		$(this).animate({
			left: '10%'
			}, 'slow' //sets animation speed to slow
		);
	}
));
$(document).ready($("#userCriteria").mouseleave(
//on mouseout
	function() {
		$(this).stop().animate({
			left: '-55%'
			}, 'slow'
		
		);		
	}
));
*/


var open = false;
$("#editUserCriteriaMenuItem").on("click", function(){
	if(!open){
	console.log("clicked");
		$("#userCriteria").animate({
			left: '0'
			}, 'slow' //sets animation speed to slow
		);
		open = true;
	}
	else{
		$("#userCriteria").animate({
			left: '-50%'
			}, 'slow' //sets animation speed to slow
		);
		open = false;
	}
});



var open2 = false;
$("#directionsPanelMenuItem").on("click", function(){
	if(!open2){
	console.log("clicked");
		$("#directionsPanel").animate({
			left: '0'
			}, 'slow' //sets animation speed to slow
		);
		open2 = true;
	}
	else{
		$("#directionsPanel").animate({
			left: '-50%'
			}, 'slow' //sets animation speed to slow
		);
		open2 = false;
	}
});
// $Id: tabs.js,v 1.2 2003/06/13 18:15:22 andreas Exp $

function Tab(i){
	// Variables for customisation:
	var numberOfTabs = 5;
	var colourOfInactiveTab = "#999966";
	var colourOfActiveTab = "#cccc99";
	var colourOfInactiveLink = "#333333";
	var colourOfActiveLink = "#ffffcc";
	
	var classOfInactiveTab = "lenya-tablink";
	var classOfActiveTab = "lenya-tablink-active";
	
	// end variables
	if (document.getElementById){
		for (f=1;f<numberOfTabs+1;f++){
			document.getElementById('contentblock'+f).style.display='none';
			document.getElementById('link'+f).className=classOfInactiveTab;
		}
		document.getElementById('contentblock'+i).style.display='block';
		document.getElementById('link'+i).className=classOfActiveTab;
	}
}

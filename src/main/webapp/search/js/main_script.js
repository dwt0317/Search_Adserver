$(document).ready(function(){
	getUserID();
	
	var config = {
		siteURL		: 'book.douban.com',	// Change this to your site
		searchSite	: true,
		type		: 'web',
		append		: false,
		perPage		: 8,			// A maximum of 8 is allowed by Google
		page		: 0				// The start page
	}
	
	var settings = {
		size		: 10,			// The number of result perpage
		start		: 0,				// The start 	
		append		:false,
		siteURL		: 'book.douban.com',	// Change this to your site
		se			: 'BingServlet',
	}
	
	
	// Adding the site domain as a label for the first radio button:
	$('#siteNameLabel').append(' '+config.siteURL);
	
	// Marking the Search tutorialzine.com radio as active:
	$('#BingSearch').click();	
	
	// Focusing the input text box:
	$('#q').focus();

	$('#searchForm').submit(function(){
		goToSearch(settings);
		return false;
	});
	
	$('#submitButton').click(function(){
		goToSearch(settings);
		return false;
	});
	
	$('#ESSearch,#BingSearch').change(function(){
		// Listening for a click on one of the radio buttons.
		// config.searchSite is either true or false.
		
		settings.se = this.value ;
	});
	
	
	function goToSearch(settings){
		var actionURL = '/DSearchEngine/'+settings.searchEngine;
		var resultsDiv = $('#resultsDiv');
		if($('#q').val()==""||$('#q').val()==null){
			return;
		}
			
		settings.q =  $('#q').val();
		settings.append = false;
		window.location.href="/DSearchEngine/search/search.html?"+"q="+settings.q+"&sid=-1"+"&start="+settings.start+"&se="+settings.se;
	}
});


function getUserID(){
    //  判断是否为新用户
	var actionURL = '/DSearchEngine/IDServlet';
    if ($.cookie('userid') == null){
    	$.getJSON(actionURL,{},function(r){
    		alert(r.id);
    		$.cookie('userid',r.id, {expires:30}); 
    	});
    }
    return $.cookie('userid');
}


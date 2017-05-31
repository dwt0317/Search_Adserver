$(document).ready(function () {
	getUserID();
	var settings = {
			start		: 0,				// The start page	
			sid	: -1,            //searchId for es, -1 means new search
			pageSize: 10
	}
	
	settings.q = getParameterByName("q");
	settings.start = getParameterByName("start");
	settings.sid = getParameterByName("sid");
	settings.se = getParameterByName("se");
	//alert(settings.q+"　"+settings.start+" "+settings.sid+" "+window.location.href);
	if(settings.q==""||settings.q==null){
		return;
	}
	
	var seid = "";	
	$("#q").val(settings.q);
	if(settings.se=="ESServlet"){
		esSearch(settings);
	}	
	//暂时屏蔽搜索结果
	else{
		seid = bingSearch(settings);
	}
	
	if(settings.start==0){   //load ad on first page only
		adSearch(settings.q, seid);
	}
		
	//包含一个settings的赋值，不能直接提交表单
//	$('searchForm').submit(function(){
//		goToSearch(settings);
//		return false;
//	});
	
	$('#submitButton').click(function(){
		goToSearch(settings);
		return false;
	}); 
	
	$("#q").keydown(function(event){  
	    if(event.which == "13")     {
	    	goToSearch(settings);
	    	return false;
	    } 	    	 
	});  
	
	//需要先绑定到已有元素，然后用on转接给动态生成的元素
    $('#text_ad').on('click', '.advertising', adClick);
    $('#content_right').on('click', '.advertising', adClick);
});


function goToSearch(settings){
	settings.q = $("#q").val();
	if(settings.q==""||settings.q==null){
		return false;
	}
	window.location.href="/DSearchEngine/search/search.html?" +"q="+settings.q+"&sid=-1"+"&start="+settings.start+"&se="+settings.se;
	return false;
}

//parse url
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


function bingSearch(settings){
	var actionURL = '/DSearchEngine/'+settings.se;
	var resultsDiv = $('#resultsDiv');
	var seid = "";
	settings.append = false;
		
	$.getJSON(actionURL,{q:settings.q,offset:settings.start,count:settings.pageSize},function(r){
		seid = r.seid;
		var results = r.webPages.value;
		var totalEstimatedMatches = r.webPages.totalEstimatedMatches;
		if(results!=null&&results.length){
			
			// If results were returned, add them to a pageContainer div,
			// after which append them to the #resultsDiv:
			
			var pageContainer = $('<div>',{className:'pageContainer'});
//			alert(results.length);
			for(var i=0;i<results.length;i++){
				// Creating a new result object and firing its toString method:
				pageContainer.append(new bingResult(results[i]) + '');
			}				
			resultsDiv.empty();
			
			pageContainer.append('<div class="clear"></div>')
						 .hide().appendTo(resultsDiv)
						 .fadeIn('slow');
			
			// Checking if there are more pages with results, 
			// and deciding whether to show the More button:
			var pageSize = settings.pageSize;
			var pageNum = settings.start/pageSize+1;
			
			var begin = pageNum<7 ? 1 : pageNum-pageSize/2;
			var end = Math.min((totalEstimatedMatches-1)/pageSize+1,begin+10);
			var ul = document.getElementById("paginationContainer");
			var url = "/DSearchEngine/search/search.html?" +"q="+settings.q+"&se="+settings.se+"&start=";
			for(var i=begin;i<=end;i++){
		　　　　  //添加 li
				var content="";
		　　　　  var li = document.createElement("li");
		  		content = "<a ";
		  		if(i==pageNum){
		  			content +="class=\"current\" ";
		  		}		  			
		　　　　  content +="href="+url+(i-1)*pageSize+">"+i+"</a>";
		  		li.innerHTML = content;
		　　　　  ul.appendChild(li);
			}						
		}
		else {
			resultsDiv.empty();
			$('<p>',{class:'notFound',html:'No Results Were Found!'}).hide().appendTo(resultsDiv).fadeIn();
			$('#pageNumDiv').hide();
		}		
	});
	return seid;
}

// search local repository with Elasticsearch
function esSearch(settings){
		var actionURL = '/DSearchEngine/'+settings.se;
		var resultsDiv = $('#resultsDiv');
			
		$.getJSON(actionURL,{q:settings.q,start:settings.start,sid:settings.sid},function(r){
			var results = r.responseData.results;
			$('#more').remove();
			
			if(results!=null&&results.length){
				
				// If results were returned, add them to a pageContainer div,
				// after which append them to the #resultsDiv:
				
				var pageContainer = $('<div>',{className:'pageContainer'});
//				alert(results.length);
				for(var i=0;i<results.length;i++){
					// Creating a new result object and firing its toString method:
					pageContainer.append(new cusResult(results[i]) + '');
				}				
				resultsDiv.empty();
				
				pageContainer.append('<div class="clear"></div>')
							 .hide().appendTo(resultsDiv)
							 .fadeIn('slow');
				
				var estimatedResultSize = r.responseData.estimatedResultSize;
				var sid = r.responseData.sid;
				settings.sid = sid;
				// Checking if there are more pages with results, 
				// and deciding whether to show the More button:
				var pageSize = settings.pageSize;
				var pageNum = settings.start/pageSize+1;
				
				var begin = pageNum<7 ? 1 : pageNum-pageSize/2;
				var end = (estimatedResultSize-1)/pageSize+1;
				var ul = document.getElementById("paginationContainer");
				var url = "/DSearchEngine/search/search.html?" +"q="+settings.q+"&sid="+sid+"&se="+settings.se+"&start=";
				for(var i=begin;i<=end;i++){
			　　　　  //添加 li
					var content="";
			　　　　  var li = document.createElement("li");
			  		content = "<a ";
			  		if(i==pageNum){
			  			content +="class=\"current\" ";
			  		}		  			
			　　　　  content +="href="+url+(i-1)*pageSize+">"+i+"</a>";
			  		li.innerHTML = content;
			　　　　  ul.appendChild(li);
				}				
			}
			else {
				resultsDiv.empty();
				$('<p>',{class:'notFound',html:'No Results Were Found!'}).hide().appendTo(resultsDiv).fadeIn();
				$('#pageNumDiv').hide();
			}
		});
	}


function adSearch(q, seid){
	var actionURL = '/DSearchEngine/AdServlet';
	var adCall = 'userid:' + $.cookie('userid')
	$.getJSON(actionURL,{q:q, seid:seid, adCall:adCall},function(r){
		var textAds = r.textAds;
		var imgAds = r.imgAds;
		var textAdContainer = $('#text_ad');
		var imgAdContainer = $('#content_right');
		for(var i=0; i < textAds.length; i++){
			textAdContainer.append(textAds[i].code+'');
		}
		for(var i=0;i < imgAds.length; i++){
			imgAdContainer.append(imgAds[i].code+'');
		}
		if(textAds.length == 0 && imgAds.length == 0){
			textAdContainer.append("未找到相关广告，重写后的关键词为: "+r.rewriteQ);
		}
	});
}

//now it can't capture open in new tab
function adClick(e){
    var click = $(this).data('click');
//    alert(click)
    var url = $(this).children("ins").children("a").attr('href');
    var land_url = url.substring(url.indexOf("dest=", 80) + 5);
    var land_url = "unknown"
    $.ajax({
        url: '/DSearchEngine/ClickServlet',
        type: 'GET',
        data: {click:click, url:land_url} ,
        contentType: 'application/json; charset=utf-8',
//        success: function (response) {
//            alert(response.status);
//        }
//        error: function () {
//            alert("error");
//        }
    }); 
}

//for es search
function cusResult(r){
	var arr = [];
	var info = r.info;
	r.info = info.replace("{firstSourceText=","").replace("(展开全部)","").replace("}","");
	arr = [
			'<div class="webResult">',
			'<h2><a href="',r.url,'" target="_blank">',r.title,'</a></h2>',
			'<p>',r.info,'</p>',
			'</div>'
		];
	// The toString method.
	this.toString = function(){
		return arr.join('');
	}
}

//generate search session id
function getSearchID(){
	var actionURL = '/DSearchEngine/IDServlet';
	var seid = "";
	$.getJSON(actionURL,{},function(r){
		seid = r.id;
	});
	return seid;
}

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


function bingResult(r){
	var arr = [];
//	alert(r.info);
	arr = [
			'<div class="webResult">',
			'<h2><a href="',r.url,'" target="_blank">',r.name,'</a></h2>',
			'<p>',r.snippet,'</p>',
			'</div>'
		];
	// The toString method.
	this.toString = function(){
		return arr.join('');
	}
}


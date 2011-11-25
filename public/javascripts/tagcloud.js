var word_list = [
	#{list items:tags, as:'tag'}
	    %{
	    	String url = play.mvc.Router.reverse("Application.sessionsAndMembersByInterest").add("interest",tag.get('interest')).url;
        	url = java.net.URLDecoder.decode(url, "UTF-8");
    	}%
	    {text: "${tag.get('interest')}", url: "${url}", weight: ${tag.get('pound')} }      
    	#{if !tag_isLast} , #{/if} 
    #{/list}
    ];
$(document).ready(function() {
	// Call jQCloud on a jQuery object passing the word list as the first argument. Chainability of methods is maintained.
	$("#wordcloud").jQCloud(word_list);
});

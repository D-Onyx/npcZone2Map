import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class testHTMLU {
	
	public static String convertData(String dataToConvert){
		return dataToConvert = dataToConvert.replace(", ", "-");
	}
	
	public static void main(String[] args) {
		final WebClient webClient = new WebClient();
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    HtmlPage page = null;
		try {
			page = webClient.getPage("http://www.wowdb.com/npcs/79020");
		} catch (FailingHttpStatusCodeException | IOException e) {
		}
	    final DomNodeList<DomElement> form = page.getElementsByTagName("span");
	    for (DomElement element : form) {
	    	if(element.getAttribute("class").equals("pin mapper-yellow")){
	    		System.out.println(convertData(element.getAttribute("title")));	
	    	}
	    }
	}

}

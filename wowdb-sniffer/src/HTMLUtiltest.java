import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class HTMLUtiltest {

	public static void main() throws Exception {
	    final WebClient webClient = new WebClient();

	    final HtmlPage page = webClient.getPage("http://www.wowdb.com/npcs/84967");
	    final DomNodeList<DomElement> form = page.getElementsByTagName("span");
	    for (DomElement element : form) {
	        System.out.println(element.getAttribute("class"));
	    }
	}
}

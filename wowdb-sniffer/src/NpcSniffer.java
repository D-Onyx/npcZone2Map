import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class NpcSniffer {

	private static int FIRST_INDEX = 1;
	private static String[] MAP_WOD =  {"7004", "7078", "6719", "6720", "6721", "6980", "6662", "6722", "6755", "6941", "6723"};
	private static int NPC_ID;
	private static int timeout = 0;
	private static int ukhost = 0;
	protected static WebClient client;


	private static String getIdNPC(String entireString){
		String[] split1 = entireString.split("http://www.wowdb.com/npcs/"); // Ex link : http://www.wowdb.com/npcs/84967-appraiser-sazsel-stickyfingers

		String temp =  split1[1];
		split1 = temp.split("-",2); // We get 84967
		
		return split1[0];
	}
	
	private static int getMaxPage(Elements listPage){
	       int count = 0;
	       for (Element location : listPage) {
	    	   if(location.attr("class").equals("b-pagination-item")){
	    		   try{
	        		   if(count < Integer.parseInt(location.text())){
	        			   count = Integer.parseInt(location.text());
	        		   }
	    		   }catch(NumberFormatException e){
	    			   
	    		   }
	    	   }
	        }
	       System.out.println("Final count = "+count);
	       return count;
	}
	
	public static String convertData(String dataToConvert){
		dataToConvert = dataToConvert.replace(", ", "-");
		 return dataToConvert = dataToConvert.replace(",", ".");
	}
	
	private static String getZoneXYbyId(String idNPC){

		try {
			client = new WebClient();
			String result = "";			
			client.getOptions().setThrowExceptionOnScriptError(false);
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			
		    HtmlPage page = null;
		    
			page = client.getPage("http://www.wowdb.com/npcs/"+idNPC);
		    final DomNodeList<DomElement> form = page.getElementsByTagName("span");
		    for (DomElement element : form) {
		    	if(element.getAttribute("class").equals("pin mapper-yellow")){
		    		result = convertData(element.getAttribute("title"));
		    		System.out.println("NPC ZoneX & ZoneY : "+result);
		    		client.close();
		    		return result;
		    	}
		    }
		    client.close();
		} catch (FailingHttpStatusCodeException | IOException e) {
			ukhost++;
		}
		return null;
	}
	
    public static void main(String[] args) throws Exception {
        /* First page */
        String url = "http://www.wowdb.com/zones/"+MAP_WOD[0]+"-frostwall?npcs-page="+FIRST_INDEX;
        Document document = Jsoup.connect(url).userAgent("Chrome").get();
        Elements list = document.select("li");
        int maxPage = getMaxPage(list);
        File allIdNPC = new File("idNPC2.txt");
        if(!allIdNPC.exists()){
        	allIdNPC.createNewFile();
        }else{
        	allIdNPC.delete();
        	allIdNPC.createNewFile();
        }
        
         //GET list of npc
     int y = 0;
     while( y < MAP_WOD.length){
    	 // GEt number of onglet npc
    	 try{
             url 		= "http://www.wowdb.com/zones/"+MAP_WOD[y]+"-frostwall?npcs-page="+FIRST_INDEX;
             document 	= Jsoup.connect(url).userAgent("Chrome").get();
             list 		= document.select("li");
             maxPage 	= getMaxPage(list);
             
             int i = 1;
             while(i <= maxPage){
             	try{
     	            url = "http://www.wowdb.com/zones/"+MAP_WOD[y]+"-frostwall?npcs-page="+i;
     	            System.out.println(url);
     	            
     	            document = Jsoup.connect(url).userAgent("Chrome").get();
     	            Elements list2 = document.select("td");
     	            list2 = list2.select("a");
     	            for(Element a : list2){
     	            	if(a.attr("class").equals("wow-npc t")){
     	            		System.out.println(MAP_WOD[y]+"-"+getIdNPC(a.attr("href")));
     	            		String id = getIdNPC(a.attr("href"));
     	            		//String coor = getZoneXYbyId(id);
     	        			//FileUtils.writeStringToFile(allIdNPC, MAP_WOD[y]+"-"+id+"-"+getZoneXYbyId(id)+"\n", true);
     	        			FileUtils.writeStringToFile(allIdNPC, MAP_WOD[y]+"-"+id+"\n", true);
     	
     	            	}
     	            }
     	            i++;
             	}catch(SocketTimeoutException e){
             		System.out.println("Connect time out sur "+ MAP_WOD[y] +"\nRetrying...");
             		timeout++;
             	}catch(UnknownHostException ee){
             		System.out.println("Unknown host "+ MAP_WOD[y] +"\nRetrying...");
             		ukhost++;        		
             	}catch(IOException eee){
             		eee.printStackTrace();
             		return;
             	}
             }
        y++;
    	}catch(SocketTimeoutException e){
     		System.out.println("Connect time out sur "+ MAP_WOD[y] +"\nRetrying...");
     		timeout++;
     	}catch(UnknownHostException ee){
     		System.out.println("Unknown host "+ MAP_WOD[y] +"\nRetrying...");
     		ukhost++;        		
     	}catch(IOException eee){
     		eee.printStackTrace();
     		return;
     	}
    	 
    }
    	System.out.println("Timeout number : "+timeout);
    	System.out.println("UkHost number : "+ukhost);
    	System.out.println("Number of id : "+NPC_ID);
    }

}
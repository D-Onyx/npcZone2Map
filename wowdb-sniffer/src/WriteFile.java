import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


public class WriteFile {

	public static void main(String[] args) {
		
		File file = new File("idNPC.txt");
		try {
			FileUtils.writeStringToFile(file, "First\n", true);
			FileUtils.writeStringToFile(file, "Second\n", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

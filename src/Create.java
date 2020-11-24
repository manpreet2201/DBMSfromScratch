import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import jdk.nashorn.internal.parser.JSONParser;

import java.io.File;


public class Create {

	public void createTable(String sql) throws JSONException {
		sql = sql.trim();
		sql = sql.replaceAll("[^a-zA-Z0-9]", " ");
		String[] splited = sql.split("\\s+");
		ArrayList<String> tablename = new ArrayList<String>();
		tablename.add(splited[2]);
		String table=splited[2];
		Map<String, String> columns = new HashMap<String, String>();
		for (int i = 3; i < splited.length-2; i = i + 2) {
			columns.put(splited[i], splited[i + 1]);
		}
		JSONObject obj = new JSONObject();
		obj.put("tablename", table);
		JSONArray arrayElementOneArray = new JSONArray();
		JSONObject arrayElementOneArrayElementOne = new JSONObject();
		for (Map.Entry<String,String> entry : columns.entrySet())  
		{
			arrayElementOneArrayElementOne.put(entry.getKey(), entry.getValue());
  
         }
		arrayElementOneArray.put(arrayElementOneArrayElementOne);
		obj.put("columnlist",arrayElementOneArray );
		try {
			File file = new File("/Users/manpreetsingh/Documents/DataWrshngProje ct/DataProject/src/files/" + table + ".json");
			file.createNewFile();
	         FileWriter writer= new FileWriter(file);
	         writer.write(obj.toString());
	         writer.close();
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	}
}

//import jdk.nashorn.internal.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Create {

	public void createTable(String sql, String databasename) throws JSONException {
sql=sql.toUpperCase();
		sql = sql.trim();
		sql = sql.replaceAll("[^a-zA-Z0-9]", " ");
		String[] splited = sql.split("\\s+");
		ArrayList<String> tablename = new ArrayList<String>();
		tablename.add(splited[2]);
		String table = splited[2];
		Map<String, String> columns = new HashMap<String, String>();
		for (int i = 3; i < splited.length - 1; i = i + 2) {
			columns.put(splited[i], splited[i + 1]);
		}
		JSONObject obj = new JSONObject();
		JSONArray datalistarray = new JSONArray();
		obj.put("tablename", table);
		obj.put("datalist", datalistarray);
		JSONArray arrayElementOneArray = new JSONArray();
		JSONObject arrayElementOneArrayElementOne = new JSONObject();
		for (Map.Entry<String, String> entry : columns.entrySet()) {
			arrayElementOneArrayElementOne.put(entry.getKey(), entry.getValue());

		}
		arrayElementOneArray.put(arrayElementOneArrayElementOne);
		obj.put("columnlist", arrayElementOneArray);
		try {
			File file = new File("src/files/" + databasename + "/" + table + ".json");

			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(obj.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

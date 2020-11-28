import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Database {
	boolean exit = true;

	public void createDatabase(String sql) {
		sql = sql.trim();
		sql = sql.replaceAll("[^a-zA-Z0-9]", " ");
		String[] splited = sql.split("\\s+");
		String databaseName = splited[2];

		JSONParser jsonParser = new JSONParser();
		try {
			FileReader reader = new FileReader(
					"/Users/manpreetsingh/Documents/dataproject5408/src/files/databases.json");

			Object obj = jsonParser.parse(reader);
			JSONArray databaseList1 = (JSONArray) obj;

			databaseList1.forEach(db -> {
				if (((JSONObject) db).get("dbname").equals(databaseName)) {
					System.out.println("Database already exists");
					exit = false;

				}
			});
			if (exit) {
				JSONArray databaseList;
				JSONObject json1 = new JSONObject();
				FileReader reader1 = new FileReader(
						"/Users/manpreetsingh/Documents/dataproject5408/src/files/databases.json");

				json1.put("dbname", databaseName);
				// if databases.json is empty
				if (reader1 == null) {
					databaseList = new JSONArray();
					databaseList.add(json1);
					FileWriter file = new FileWriter(
							"/Users/manpreetsingh/Documents/dataproject5408/src/files/databases.json");
					file.write(databaseList.toString());
					file.flush();
				}
				// reading from databases file and its not empty
				else {
					Object obj1 = jsonParser.parse(reader1);
					JSONArray databaseListFinal = (JSONArray) obj;
					databaseListFinal.add(json1);
					FileWriter file = new FileWriter(
							"/Users/manpreetsingh/Documents/dataproject5408/src/files/databases.json");
					file.write(databaseListFinal.toString());
					file.flush();

				}

				String path = "/Users/manpreetsingh/Documents/dataproject5408/src/files/" + databaseName;
				File file1 = new File(path);
				System.out.println(path);
				boolean bool = file1.mkdir();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

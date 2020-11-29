import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Insert {

	public void insert(String sql, String databasename) {
		sql = sql.trim();
		sql = sql.replaceAll("[^a-zA-Z0-9]", " ");
		String[] splited = sql.split("\\s+");
		String tablename = splited[2].toUpperCase();

		ArrayList<String> columnName = new ArrayList<String>();
		ArrayList<String> columnValues = new ArrayList<String>();
		int i = 3;
		while (!splited[i].equalsIgnoreCase("VALUES")) {
			columnName.add(splited[i].toUpperCase());
			i++;
		}
		i++;

		while (i < splited.length) {
			columnValues.add(splited[i]);
			i++;
		}
		System.out.println(columnName.size());

		System.out.println(columnValues.size());
		JSONParser jsonParser = new JSONParser();
		try {
			FileReader reader = new FileReader("src/files/" + databasename + "/" + tablename + ".json");

			Object obj = jsonParser.parse(reader);
			JSONArray datalist = (JSONArray) ((JSONObject) obj).get("datalist");

			for (int i2 = 0; i2 < columnValues.size();) {
				JSONObject row = new JSONObject();
				System.out.println("fddd");
				for (int i1 = 0; i1 < columnName.size(); i1++) {
					row.put(columnName.get(i1), columnValues.get(i2));
					i2++;
				}
				datalist.add(row);
			}

			((JSONObject) obj).put("datalist", datalist);
			FileWriter file = new FileWriter("src/files/" + databasename + "/" + tablename + ".json");
			file.write(obj.toString());
			file.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}

//INSERT INTO Customers(CustomerName, ContactName, Address, City, PostalCode, Country)
//VALUES ('Cardinal','Tom B. Erichsen','Skagen 21','Stavanger','4006','Norway');
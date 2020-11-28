//import jdk.nashorn.internal.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Update {

	public Boolean tableExists(String sql, String dbName) {
		String[] sqlArr = sql.split(" ");
		String tablename = sqlArr[1];
		File tableFile = new File("src/files/" + dbName + "/" + tablename + ".json");
		if (tableFile.exists() && !tableFile.isDirectory()) {
			return true;
		}
		return false;
	}

	public void updateTable(String sql, String dbName) {
		Map<Integer, Map<String, String>> dataID = new HashMap<Integer, Map<String, String>>();
		String og_sql = sql;
		sql = sql.trim();
		sql = sql.replaceAll("[^a-zA-Z0-9]", " ");
		String[] splited = sql.split("\\s+");

		File tableFile = new File("src/files/" + splited[1] + ".json");
		try {

			if (splited[2].equals("SET") == false && !sql.contains("WHERE")) {
				throw new Exception("Incorrect Syntax - SET and WHERE Clauses not found");
			}

			InputStream tableStream = new FileInputStream(tableFile);
			JSONTokener tokener = new JSONTokener(tableStream);
			JSONObject object = new JSONObject(tokener);
			JSONArray dataJSON = object.getJSONArray("datalist");

			for (int i = 0; i < dataJSON.length(); i++) {
				Map<String, String> dataValues = new HashMap<String, String>();
				JSONObject jsonObject = dataJSON.getJSONObject(i);
				Iterator<String> keys = jsonObject.keys();

				while (keys.hasNext()) {
					String key = keys.next();
					dataValues.put(key, jsonObject.get(key).toString());
				}

				dataID.put(i, dataValues);
			}

			System.out.println(dataID);
			String setClause = og_sql.substring(og_sql.indexOf("SET"), og_sql.indexOf("WHERE"));
			setClause = setClause.replace("SET ", "");
			String[] updateParams = setClause.split(",");
			for (int i = 0; i < updateParams.length; i++) {
				System.out.println(updateParams[i].trim());
			}

		} catch (FileNotFoundException e) {
			System.out.println("Unable to read file");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Incorrect Syntax - Please try again");
			e.printStackTrace();
		}
	}
}

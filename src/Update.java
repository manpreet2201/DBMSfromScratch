//import jdk.nashorn.internal.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

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

	public Boolean keyExists(String key, HashMap<Integer, HashMap<String, String>> database) {
		for (int i = 0; i < database.size(); i++) {
			if (database.get(i).containsKey(key)) {
				return true;
			}
		}
		return false;
	}

	public Integer valueExists(String value, HashMap<Integer, HashMap<String, String>> database) {
		for (int i = 0; i < database.size(); i++) {
			if (database.get(i).containsValue(value)) {
				return i;
			}
		}
		return -1;
	}

	public void updateTable(String sql, String dbName) {
		HashMap<Integer, HashMap<String, String>> dataID = new HashMap<Integer, HashMap<String, String>>();
		String og_sql = sql.toUpperCase();
		sql = sql.trim();
		sql = sql.replaceAll("[^a-zA-Z0-9]", " ");
		String[] sqlArr = sql.split("\\s+");
		String tablename = sqlArr[1];

		File tableFile = new File("src/files/" + dbName + "/" + tablename + ".json");
		try {

			if (sqlArr[2].equals("SET") == false && !sql.contains("WHERE")) {
				throw new Exception("Incorrect Syntax - SET and WHERE Clauses not found");
			}

			InputStream tableStream = new FileInputStream(tableFile);
			JSONTokener tokener = new JSONTokener(tableStream);
			JSONObject object = new JSONObject(tokener);
			JSONArray dataJSON = object.getJSONArray("datalist");

			for (int i = 0; i < dataJSON.length(); i++) {
				HashMap<String, String> dataValues = new HashMap<String, String>();
				JSONObject jsonObject = dataJSON.getJSONObject(i);
				Iterator<String> keys = jsonObject.keys();

				while (keys.hasNext()) {
					String key = keys.next();
					dataValues.put(key, jsonObject.get(key).toString());
				}

				dataID.put(i, dataValues);
			}

			System.out.println(dataID);

			// SET Clause
			String setClause = og_sql.substring(og_sql.indexOf("SET"), og_sql.indexOf("WHERE"));
			setClause = setClause.replace("SET ", "");
			String[] setParams = setClause.split(",");

			// WHERE Clause
			String whereClause = og_sql.substring(og_sql.indexOf("WHERE"), og_sql.length());
			whereClause = whereClause.replace("WHERE", "").trim();
			whereClause = whereClause.replaceAll("[^a-zA-Z0-9=]", "");
			String whereKey = whereClause.split("=")[0];
			String whereValue = whereClause.split("=")[1];
			Integer whereID = valueExists(whereValue, dataID);
			if (!keyExists(whereKey, dataID) && (whereID != -1)) {
				throw new Exception("Invalid WHERE Condition - Please try again");
			}

			for (int j = 0; j < dataJSON.length(); j++) {
				JSONObject jsonObject = dataJSON.getJSONObject(j);
				Iterator<String> keys = jsonObject.keys();

				while (keys.hasNext()) {
					String key = keys.next();
					if (key.equals(whereKey) && jsonObject.getString(key).equals(whereValue)) {
						for (int i = 0; i < setParams.length; i++) {
							setParams[i] = setParams[i].trim();
							String setkey = setParams[i].split("=")[0];
							String newValue = setParams[i].split("=")[1];
							newValue = newValue.replaceAll("[\'\"]", "");
							if (!keyExists(setkey, dataID)) {
								throw new Exception("Key Does Not Exist - Please check your syntax");
							}
							jsonObject.put(setkey, newValue);
						}
					}

				}

			}
			FileWriter updateFile = new FileWriter(tableFile);
			updateFile.write(object.toString());
			updateFile.close();
			System.out.println("Records Updated!");
//			Updating the DATA Strucuture
//			for (int i = 0; i < setParams.length; i++) {
//				setParams[i] = setParams[i].trim();
//				System.out.println(setParams[i]);
//				String key = setParams[i].split("=")[0];
//				String newValue = setParams[i].split("=")[1];
//				if (!keyExists(key, dataID)) {
//					throw new Exception("Key Does Not Exist - Please check your syntax");
//				}
//				dataID.get(whereID).replace(key, newValue);
//			}
//			System.out.println("------------");
//			System.out.println(dataID.toString());

		} catch (FileNotFoundException e) {
			System.out.println("Unable to read file");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Incorrect Syntax - Please try again");
			e.printStackTrace();
		}
	}
}

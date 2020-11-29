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

public class Delete {

	public Boolean tableExists(String sql, String dbName) {
		String[] sqlArr = sql.split(" ");
		String tablename = sqlArr[2];
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

	public void deleteQuery(String sql, String dbName) {
		HashMap<Integer, HashMap<String, String>> dataID = new HashMap<Integer, HashMap<String, String>>();
		String[] sqlArr = sql.split(" ");
		String tablename = sqlArr[2];
		File tableFile = new File("src/files/" + dbName + "/" + tablename + ".json");

		try {

			InputStream tableStream = new FileInputStream(tableFile);
			JSONTokener tokener = new JSONTokener(tableStream);
			JSONObject object = new JSONObject(tokener);
			JSONArray dataJSON = object.getJSONArray("datalist");

			// Delete All Rows
			if (sqlArr.length == 3 && !sql.contains("WHERE")) {
				System.out.println("Deleting all records from: " + tablename + " " + dataJSON.length());
				int length = dataJSON.length();
				for (int i = 0; i < length; i++) {
					dataJSON.remove(0);
				}
				System.out.println("After Deletion: " + dataJSON + " " + object);
				FileWriter updateFile = new FileWriter(tableFile);
				updateFile.write(object.toString());
				updateFile.close();
			}

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

			// FROM Clause
			String fromClause = sql.substring(sql.indexOf("FROM"), sql.indexOf("WHERE"));
			fromClause = fromClause.replace("FROM ", "");
			fromClause = fromClause.replaceAll("[^a-zA-Z0-9=,]", "");
			String[] fromParams = fromClause.split(",");
			if (fromParams.length > 1) {
				throw new Exception("Invalid FROM Clause");
			}

			// WHERE Clause
			String whereClause = sql.substring(sql.indexOf("WHERE"), sql.length());
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
						dataJSON.remove(j);
						j = 0; // Resetting the loop parameter so that the last JSON object is not skipped.
					}

				}

			}
			FileWriter updateFile = new FileWriter(tableFile);
			updateFile.write(object.toString());
			updateFile.close();
			System.out.println("Records Deleted Successfully");
			System.out.println("Remaining Records: " + dataJSON);

		} catch (FileNotFoundException e) {
			System.out.println("Unable to read file");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Incorrect Syntax - Please try again");
			e.printStackTrace();
		}

	}
}
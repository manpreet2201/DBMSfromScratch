import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class Transactions {

	public void beginTransactions(String username, String dbName) {
		System.out.println("Starting Transaction in Schema " + dbName + " by User " + username);
		HashMap<Integer, String> sequenceMap = new HashMap<Integer, String>();
		QueryProcessing process = new QueryProcessing();
		int seqCounter = 1;
		int rollbackKey = -1;
		Scanner scanner = new Scanner(System.in);
		Boolean bool = true;
		while (bool) {
			System.out.println("Enter Sequence " + seqCounter + ":");
			String queryString = scanner.nextLine();
			if (queryString.trim().equalsIgnoreCase("COMMIT")) {
				bool = false;
				break;
			}
			sequenceMap.put(seqCounter, queryString);
			if (queryString.trim().equalsIgnoreCase("ROLLBACK")) {
				// In case of multiple Rollback statements - get the last Rollback
				rollbackKey = seqCounter;
			}
			seqCounter++;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String transactionName = "T_" + username + "_" + dbName + "_" + timestamp.getTime();
		JSONObject jsonObject = new JSONObject();
		try {
			Set<Integer> keySet = sequenceMap.keySet();
			// If Rollback exists - do not perform any queries before it
			if (rollbackKey != -1) {
				for (int key : keySet) {
					if (key > rollbackKey) {
						String query = sequenceMap.get(key);
						process.QProcess(query, dbName, username, true);
					}
				}
			} else {
				for (int key : keySet) {
					String query = sequenceMap.get(key);
					process.QProcess(query, dbName, username, true);
				}
			}

			// Save Transaction Details
			jsonObject.put("username", username);
			jsonObject.put("database", dbName);
			jsonObject.put("sequence", sequenceMap);
			File file = new File("src/transactions/" + transactionName + ".json");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(jsonObject.toString());
			writer.close();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Your Transaction Sequence: \n" + jsonObject.toString());
	}

}

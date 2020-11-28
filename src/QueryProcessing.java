import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class QueryProcessing {
	String databasename = null;
	boolean exit = true;

	public void QProcess(String sql, String databasename) {

		if (sql.contains("CREATE") && sql.contains("DATABASE")) {
			Database d;
			d = new Database();
			d.createDatabase(sql);

		}

		else if (sql.contains("CREATE") && sql.contains("TABLE") && exit == true) {

			Create c;
			try {
				c = new Create();
				c.createTable(sql, databasename);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (sql.contains("INSERT")) {

		}

	}
}

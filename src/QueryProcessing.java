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

		} else if (sql.contains("UPDATE") && sql.contains("SET") && sql.contains("WHERE")) {
			Update update;
			try {
				update = new Update();
				if (!update.tableExists(sql, databasename)) {
					throw new Exception("Table Does Not Exist");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Incorrect Syntax - Please try again");
		}

	}
}

public class QueryProcessing {
	String databasename = null;
	boolean exit = true;

	public void QProcess(String sql, String databasename) {
		String[] keywords = sql.trim().split(" ");

		if (sql.toUpperCase().contains("CREATE") && sql.toUpperCase().contains("DATABASE")) {
			Database d;
			d = new Database();
			d.createDatabase(sql);

		}

		else if (sql.toUpperCase().contains("CREATE") && sql.toUpperCase().contains("TABLE") && exit == true) {

			Create c;
			try {
				c = new Create();
				c.createTable(sql, databasename);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (sql.toUpperCase().contains("INSERT")) {

			Insert c;
			try {
				c = new Insert();
				c.insert(sql, databasename);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (sql.toUpperCase().contains("SELECT")) {
			Select s;
			try {
				s = new Select();
				s.select(sql, databasename);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (sql.toUpperCase().contains("UPDATE") && sql.toUpperCase().contains("SET")
				&& sql.toUpperCase().contains("WHERE")) {
			Update update;
			try {
				update = new Update();
				if (!update.tableExists(sql, databasename)) {
					throw new Exception("Table Does Not Exist");
				}
				update.updateTable(sql, databasename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (sql.contains("DELETE") && sql.contains("FROM")) {
			Delete delete;
			try {
				delete = new Delete();
				if (!delete.tableExists(sql, databasename)) {
					throw new Exception("Table Does Not Exist");
				}
				delete.deleteQuery(sql, databasename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (sql.toUpperCase().contains("SHOW") && sql.toUpperCase().contains("TABLES") && keywords.length == 2) {
			Database database = new Database();
			database.showTables(databasename);
		} else if (sql.toUpperCase().contains("DROP") && sql.toUpperCase().contains("DATABASE")
				&& keywords.length == 3) {
			Database database = new Database();
			String dbName = keywords[2].replace(";", "");
			System.out.println(database.dropSchema(dbName));
		} else if (sql.toUpperCase().contains("DROP") && sql.toUpperCase().contains("TABLE") && keywords.length == 3) {
			Delete delete;
			try {
				delete = new Delete();
				if (!delete.tableExists(sql, databasename)) {
					throw new Exception("Table Does Not Exist");
				}
				delete.dropTable(sql, databasename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Incorrect Syntax - Please try again");
		}

	}
}

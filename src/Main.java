import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter your query: ");
		String sql = scanner.nextLine();
		sql=sql.toUpperCase();

		if (sql.contains("CREATE") && sql.contains("TABLE")) {
			
			Create c;
			try {
				c = new Create();
				c.createTable(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		} else if (sql.contains("INSERT")) {

		}
		
	}
}
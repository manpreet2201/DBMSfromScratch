import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Authentication {
	String credentialsFile = "credentials.csv";
	String credential = "";

	public void register(String username, String password) throws IOException {
		credential = "";
		boolean isRegistered = false;
		File file = new File("src/" + credentialsFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedReader bufferedReaderObject = new BufferedReader(new FileReader("src/" + credentialsFile));
		while ((credential = bufferedReaderObject.readLine()) != null) {
			String[] credentials = credential.split(",");
			if (credentials[0].toLowerCase().equals(username.toLowerCase())) {
				System.out.println("User Already Registered");
				isRegistered = true;
				break;
			}
		}
		if (!isRegistered) {
			BufferedWriter bufferedWriterObject = new BufferedWriter(new FileWriter(credentialsFile, true));
			bufferedWriterObject.write(username + "," + password + "\n");
			bufferedWriterObject.close();
			System.out.println("User Registered successfully");
		}
	}

	public void authenticate(String username, String password) throws IOException {
		BufferedReader bufferedReaderObject = new BufferedReader(new FileReader("src/" + credentialsFile));
		while ((credential = bufferedReaderObject.readLine()) != null) {
			String[] credentials = credential.split(",");
			if (credentials != null) {
				if (credentials[0] != "" || credentials[1] != "") {
					if (credentials[0].toLowerCase().equals(username.toLowerCase())
							&& credentials[1].equals(password)) {
						System.out.println("Authenticated");
						QueryInit qInit = new QueryInit();
						qInit.init();
					} else {
						System.out.println("Not Authenticated");
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Authentication testObject = new Authentication();
		DumpCreation dumpCreationObject = new DumpCreation();
		Scanner scannerObject = new Scanner(System.in);
		System.out.println(
				"Operations Available: 1. registration 2. authentication 3.DB Dump Creation 4. ERD Generation");
		System.out.println("Enter your choice ");
		int choice = scannerObject.nextInt();
		System.out.println("Enter the username");
		String username = scannerObject.next();
		System.out.println("Enter the password");
		String password = scannerObject.next();
		switch (choice) {
		case 1:
			testObject.register(username, password);
			break;
		case 2:
			testObject.authenticate(username, password);
			break;
		case 3:
			dumpCreationObject.CreateDump("src/files/JAY");
			break;
		case 4:
			Process p = Runtime.getRuntime().exec("python3 ERDExample.py");
			System.out.println("Database file generated");
			break;
		default:
			System.out.println("invalid choice");
		}

//https://stackabuse.com/reading-and-writing-csvs-in-java/

	}
}

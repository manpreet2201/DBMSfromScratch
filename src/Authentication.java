import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import logging.EventLogger;
import logging.GeneralLogger;
import org.json.JSONException;

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
		EventLogger log2=new EventLogger();
		BufferedReader bufferedReaderObject = new BufferedReader(new FileReader("src/" + credentialsFile));
		while ((credential = bufferedReaderObject.readLine()) != null) {
			String[] credentials = credential.split(",");
			if (credentials != null) {
				if (credentials[0] != "" || credentials[1] != "") {
					if (credentials[0].toLowerCase().equals(username.toLowerCase())
							&& credentials[1].equals(password)) {
						System.out.println("Authenticated");
						log2.authlog(username);
						QueryInit qInit = new QueryInit();
						qInit.init();
					} else {
						System.out.println("Not Authenticated");
						log2.errorauthlog(username);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, JSONException {
		Authentication testObject = new Authentication();
		DumpCreation dumpCreationObject = new DumpCreation();
		Scanner scannerObject = new Scanner(System.in);
		System.out.println(
				"Operations Available: 1. Registration 2. Authentication 3. DB Dump Creation 4. ERD Generation");
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
			System.out.println("Enter the database Name");
			String DatabaseName = scannerObject.next();
			dumpCreationObject.CreateDump(DatabaseName);
			break;
		case 4:
			System.out.println("Enter the database Name");
			String DatabaseForERD = scannerObject.next();
			Process p = Runtime.getRuntime().exec("python3 ERDGeneration.py "+DatabaseForERD);
			System.out.println("Database ERD generated");
			break;
		default:
			System.out.println("invalid choice");
		}

//https://stackabuse.com/reading-and-writing-csvs-in-java/

	}
}

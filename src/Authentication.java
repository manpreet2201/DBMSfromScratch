import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Authentication {
	String credentialsFile = "/Users/manpreetsingh/Documents/dataproject5408/src/credentials.csv";
	String credential = "";

	public void register(String username, String password) throws IOException {
		credential = "";
		File file = new File(credentialsFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedReader bufferedReaderObject = new BufferedReader(new FileReader(credentialsFile));
		while ((credential = bufferedReaderObject.readLine()) != null) {
			String[] credentials = credential.split(",");
			if (credentials[0] == username && credentials[1] == password) {
				System.out.println("User Already Registered");
			} else {
				BufferedWriter bufferedWriterObject = new BufferedWriter(new FileWriter(credentialsFile, true));
				bufferedWriterObject.write(username + "," + password + "\n");
				bufferedWriterObject.close();
				System.out.println("User Registered successfully");
			}
		}
	}

	public void authenticate(String username, String password) throws IOException {

		BufferedReader bufferedReaderObject = new BufferedReader(new FileReader(credentialsFile));
		while ((credential = bufferedReaderObject.readLine()) != null) {
			String[] credentials = credential.split(",");
			if (credentials[0].equals(username) && credentials[1].equals(password)) {
				System.out.println("Authenticated");
				QueryInit q1=new QueryInit();
				q1.init();
			} else {
				System.out.println("Not Authenticated");
			}
		}

	}

	public static void main(String[] args) throws IOException {
		Authentication testObject = new Authentication();
		Scanner scannerObject = new Scanner(System.in);
		DumpCreation dumpCreationObject=new DumpCreation();
		System.out.println("operations available 1. registration 2. authentication 3.DB Dump Creation");
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
			dumpCreationObject.CreateDump("firstDb");
			break;
		default:
			System.out.println("invalid choice");
		}

//https://stackabuse.com/reading-and-writing-csvs-in-java/

	}
}

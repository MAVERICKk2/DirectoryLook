/*
Java Class created to create objects of Watch class in order to be able of monitor multiple Paths and write on a specific log file

		Created			Version			Date
		Uriel Lopez		1.0				April-23-2014
*/

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Watcher extends Watch{

	public static void main(String[] args){

		DateFormat dateFormatEmail = new SimpleDateFormat("yyyy/MM/dd");

		// INBOX
		// After create a new Watch Object the constructor is set with the Path to watch
		final Watch watchFile = new Watch("Directory");
		// Define Regex Pattern to use in order to match the filename
		watchFile.setRegexPattern("^[r|R][0-9]*[.]");
		watchFile.setSubject("Subject");
		watchFile.setBody("Body");
		// The method run has two parameters the name of the log to be written and the file where the emails are going to be read
		Thread t = new Thread(new Runnable() {
			public void run()
			{
				watchFile.newFileRegex("Log","ListofEmails");
			}
		});
		t.start();

		// OUTBOX
		// After create a new Watch Object the constructor is set with the Path to watch
		final Watch watchFile2 = new Watch("Directory");
		// Define Regex Pattern to use in order to match the filename
		watchFile2.setRegexPattern("^[a-z]");
		watchFile2.setSubject("Subject");
		watchFile2.setBody("Body");
		// The method run has two parameters the name of the log to be written and the file where the emails are going to be read
		Thread t2 = new Thread(new Runnable() {
			public void run()
			{
				watchFile2.newFileRegex("Log","ListOfEmails");
			}
		});
		t2.start();

		// OUTBOX
		// After create a new Watch Object the constructor is set with the Path to watch
		final Watch watchFile3 = new Watch("Directory");
		// Define Regex Pattern to use in order to match the filename
		watchFile3.setRegexPattern("^[0-9]");
		watchFile3.setSubject("Subject");
		watchFile3.setBody("Body");
		// The method run has two parameters the name of the log to be written and the file where the emails are going to be read
		Thread t3 = new Thread(new Runnable() {
			public void run()
			{
				watchFile3.newFileRegex("Log","ListOfEmails");
			}
		});
		t3.start();
	}
}
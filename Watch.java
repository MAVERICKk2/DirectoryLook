/*
Java Class created to monitor a specific path and each time a file is created and have a specific name
an email notification will be triggered.

		Created			Version			Date
		Uriel Lopez		1.0				April-22-2014
*/

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.nio.file.*;
import java.io.IOException;
import static java.nio.file.StandardWatchEventKinds.*;

class Watch{
	private Path dir;
	private Path filename;
	private String regexPattern;
	private String subject;
	private String body;
	private String filenameCompare = "";

	Watch(String d){
		dir = Paths.get(d);
	}

	Watch(){
		dir = Paths.get("C:\\");
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return this.body;
	}

	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getSubject(){
		return this.subject;
	}

	public void setRegexPattern(String regexPattern){
		this.regexPattern = regexPattern;
	}

	public String getRegexPattern(){
		return this.regexPattern;
	}

	public void setDir(Path dir){
		this.dir = dir;
	}

	public Path getDir() {
		return this.dir;
	}

	public void newFileRegex(String logName, String emails){

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {

			// Code to create a log file to start saving problems on it
			File logFile = new File(logName);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileOutputStream oFile = new FileOutputStream(logFile, true);
			OutputStreamWriter osw = new OutputStreamWriter(oFile);
			BufferedWriter w = new BufferedWriter(osw);
			w.newLine();
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("-----------------------------------------Process Started to check Falabela Files -----------------------------------------");
			w.newLine();
			w.newLine();
			w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Directory to Watch: " + dir.toString());
			w.newLine();
			w.flush();

			WatchService watcher = FileSystems.getDefault().newWatchService();

			//WatchKey key = dir.register(watcher,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);
			WatchKey key = dir.register(watcher,ENTRY_MODIFY);

			for (;;){

				// wait for key to be signalled
				//WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + x.toString());
					w.newLine();
					w.flush();
					return;
				}

				for (WatchEvent<?> event: key.pollEvents()) {
					w.newLine();
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			An event was detected");
					w.newLine();
					w.flush();
					WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events, but an OVERFLOW event can
					// occur regardless if events are lost or discarded.
					if (kind == OVERFLOW) {
						continue;
					}

					// The filename is the context of the event
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					filename = ev.context();

					RegexPatternMatcher matchFile = new RegexPatternMatcher(filename.toString(),regexPattern);

					if (matchFile.find() && !filename.toString().equals(filenameCompare)){
						this.filenameCompare =	filename.toString();

						w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			New File detected");
						w.newLine();
						w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Path " + dir.toString());
						w.newLine();
						w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			File Name " + filename);
						w.newLine();
						w.flush();

						sentEmail(w,emails);
					}

				}

				// Reset the key -- this step is critical if you wan to receive further watch events.
				// If the key is no longer valid, the directory is inaccessible so exit the loop
				boolean valid = key.reset();
				if (!valid) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			There was an error with the Directory is inaccessible");
					w.newLine();
					w.close();
					break;
				}
			}

		} catch (IOException x) {
			System.err.println(x);
		}
	}

	public void newFile(String logName, String emails){

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {

			// Code to create a log file to start saving information on it
			File logFile = new File(logName);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileOutputStream oFile = new FileOutputStream(logFile, true);
			OutputStreamWriter osw = new OutputStreamWriter(oFile);
			BufferedWriter w = new BufferedWriter(osw);
			w.newLine();
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("-----------------------------------------Process Started to check Falabela Files -----------------------------------------");
			w.newLine();
			w.newLine();
			w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Directory to Watch: " + dir.toString());
			w.newLine();
			w.flush();

			WatchService watcher = FileSystems.getDefault().newWatchService();

			//WatchKey key = dir.register(watcher,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);
			WatchKey key = dir.register(watcher,ENTRY_CREATE);

			for (;;){

				// wait for key to be signalled
				//WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + x.toString());
					w.newLine();
					w.flush();
					return;
				}

				for (WatchEvent<?> event: key.pollEvents()) {
					w.newLine();
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			An event was detected");
					w.newLine();
					WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events, but an OVERFLOW event can
					// occur regardless if events are lost or discarded.
					if (kind == OVERFLOW) {
						continue;
					}

					// The filename is the context of the event
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					filename = ev.context();

					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			New File detected");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Path " + dir.toString());
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			File Name " + filename);
					w.newLine();
					w.flush();

					sentEmail(w,emails);

				}

				// Reset the key -- this step is critical if you wan to receive further watch events.
				// If the key is no longer valid, the directory is inaccessible so exit the loop
				boolean valid = key.reset();
				if (!valid) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			There was an error with the Directory is inaccessible");
					w.newLine();
					w.close();
					break;
				}
			}

		} catch (IOException x) {
			System.err.println(x);
		}
	}

	public void deleteFile(String logName, String emails){

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {

			// Code to create a log file to start saving information on it
			File logFile = new File(logName);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileOutputStream oFile = new FileOutputStream(logFile, true);
			OutputStreamWriter osw = new OutputStreamWriter(oFile);
			BufferedWriter w = new BufferedWriter(osw);
			w.newLine();
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("-----------------------------------------Process Started to check Falabela Files -----------------------------------------");
			w.newLine();
			w.newLine();
			w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Directory to Watch: " + dir.toString());
			w.newLine();
			w.flush();

			WatchService watcher = FileSystems.getDefault().newWatchService();

			//WatchKey key = dir.register(watcher,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);
			WatchKey key = dir.register(watcher,ENTRY_DELETE);

			for (;;){

				// wait for key to be signalled
				//WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + x.toString());
					w.newLine();
					w.flush();
					return;
				}

				for (WatchEvent<?> event: key.pollEvents()) {
					w.newLine();
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			An event was detected");
					w.newLine();
					WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events, but an OVERFLOW event can
					// occur regardless if events are lost or discarded.
					if (kind == OVERFLOW) {
						continue;
					}

					// The filename is the context of the event
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					filename = ev.context();

					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			New File detected");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Path " + dir.toString());
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			File Name " + filename);
					w.newLine();
					w.flush();

					sentEmail(w,emails);

				}

				// Reset the key -- this step is critical if you wan to receive further watch events.
				// If the key is no longer valid, the directory is inaccessible so exit the loop
				boolean valid = key.reset();
				if (!valid) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			There was an error with the Directory is inaccessible");
					w.newLine();
					w.close();
					break;
				}
			}

		} catch (IOException x) {
			System.err.println(x);
		}
	}

	public void modifyFile(String logName, String emails){

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {

			// Code to create a log file to start saving information on it
			File logFile = new File(logName);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileOutputStream oFile = new FileOutputStream(logFile, true);
			OutputStreamWriter osw = new OutputStreamWriter(oFile);
			BufferedWriter w = new BufferedWriter(osw);
			w.newLine();
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("-----------------------------------------Process Started to check Falabela Files -----------------------------------------");
			w.newLine();
			w.newLine();
			w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Directory to Watch: " + dir.toString());
			w.newLine();
			w.flush();

			WatchService watcher = FileSystems.getDefault().newWatchService();

			//WatchKey key = dir.register(watcher,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);
			WatchKey key = dir.register(watcher,ENTRY_MODIFY);

			for (;;){

				// wait for key to be signalled
				//WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + x.toString());
					w.newLine();
					w.flush();
					return;
				}

				for (WatchEvent<?> event: key.pollEvents()) {
					w.newLine();
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			An event was detected");
					w.newLine();
					WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events, but an OVERFLOW event can
					// occur regardless if events are lost or discarded.
					if (kind == OVERFLOW) {
						continue;
					}

					// The filename is the context of the event
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					filename = ev.context();

					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			New File detected");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Path " + dir.toString());
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			File Name " + filename);
					w.newLine();
					w.flush();

					sentEmail(w,emails);

				}

				// Reset the key -- this step is critical if you wan to receive further watch events.
				// If the key is no longer valid, the directory is inaccessible so exit the loop
				boolean valid = key.reset();
				if (!valid) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			There was an error with the Directory is inaccessible");
					w.newLine();
					w.close();
					break;
				}
			}

		} catch (IOException x) {
			System.err.println(x);
		}
	}

	public void allFile(String logName, String emails){

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {

			// Code to create a log file to start saving information on it
			File logFile = new File(logName);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileOutputStream oFile = new FileOutputStream(logFile, true);
			OutputStreamWriter osw = new OutputStreamWriter(oFile);
			BufferedWriter w = new BufferedWriter(osw);
			w.newLine();
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("--------------------------------------------------------------------------------------------------------------------------");
			w.newLine();
			w.write("-----------------------------------------Process Started to check Falabela Files -----------------------------------------");
			w.newLine();
			w.newLine();
			w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Directory to Watch: " + dir.toString());
			w.newLine();
			w.flush();

			WatchService watcher = FileSystems.getDefault().newWatchService();

			WatchKey key = dir.register(watcher,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);

			for (;;){

				// wait for key to be signalled
				//WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + x.toString());
					w.newLine();
					w.flush();
					return;
				}

				for (WatchEvent<?> event: key.pollEvents()) {
					w.newLine();
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			An event was detected");
					w.newLine();
					WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events, but an OVERFLOW event can
					// occur regardless if events are lost or discarded.
					if (kind == OVERFLOW) {
						continue;
					}

					// The filename is the context of the event
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					filename = ev.context();

					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			New File detected");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Path " + dir.toString());
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			File Name " + filename);
					w.newLine();
					w.flush();

					sentEmail(w,emails);

				}

				// Reset the key -- this step is critical if you wan to receive further watch events.
				// If the key is no longer valid, the directory is inaccessible so exit the loop
				boolean valid = key.reset();
				if (!valid) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			There was an error with the Directory is inaccessible");
					w.newLine();
					w.close();
					break;
				}
			}

		} catch (IOException x) {
			System.err.println(x);
		}
	}

	private void sentEmail(BufferedWriter w, String emails){

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat dateFormatEmail = new SimpleDateFormat("yyyy/MM/dd");
		BufferedReader br = null;
		String line;

		try {

			// Read emails file
			try {
				File emailFile = new File(emails);
				br = new BufferedReader(new FileReader(emailFile));


			} catch (FileNotFoundException f) {
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			FILE NOT FOUND Exception");
					w.newLine();
					w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + f.toString());
					w.newLine();
					w.flush();
					System.err.println(f);
			}

			w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Emailing file");
			w.newLine();

			// Email the file to the specified email alias
			System.out.format("Emailing file %s%n", filename);
			// Variable with the email host used to sent emails
			String host = "host";
			// Get System properties
			Properties properties = System.getProperties();
				// Setup mail server
			properties.setProperty("properties", host);
			// Get default Session object
			Session session = Session.getDefaultInstance(properties);

			String msgBody = this.body;
			try {

				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("From"));

				while ((line = br.readLine()) != null){
					System.out.println(line);
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(line));
				}

				this.subject = this.subject.replaceAll("((19|20)\\d\\d)/(0?[1-9]|1[012])/([0-9]*)",dateFormatEmail.format(Calendar.getInstance().getTime()));

				msg.setSubject(this.subject);
				msg.setText(msgBody);

				Transport.send(msg);

				w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Email Sent");
				w.newLine();
				w.flush();
				System.out.println("Email Sent");

			} catch (AddressException e) {
				w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
				w.newLine();
				w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + e.toString());
				w.newLine();
				w.flush();
				System.err.println(e);
			} catch (MessagingException e) {
				w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			Error------");
				w.newLine();
				w.write("Date " + dateFormat.format(Calendar.getInstance().getTime()) + "			" + e.toString());
				w.newLine();
				w.flush();
				System.err.println(e);
			}
		} catch (IOException x) {
			System.err.println(x);
		}
	}
}
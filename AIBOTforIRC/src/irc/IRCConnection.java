package irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import ChatBot.AI;
import ChatBot.Data;

public class IRCConnection {

	public BufferedWriter writer;
	
	public static void main(String[] args) {
		//new IRCConnection();
		Data data = new Data();
		data.loadAllData();
		AI ai = new AI(null);
		try {
			System.out.println(ai.replyTo("print \"cat\" to the console", "n/a"));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	boolean connected = false;
	AI ai;
	public IRCConnection() {
		// TODO Auto-generated constructor stub
		try {
			Socket sock = new Socket("irc.esper.net", 6667);
			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			writeLine("NICK NewBot");
			writeLine("USER NewBot 0 * :NewBot");
			writeLine("PING");

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(" > "+line);
				if (line.startsWith("PING")) {
					writeLine(line.replace("PING", "PONG"));
				}
				
				if (line.endsWith(":End of /MOTD command.")) {
					connected = true;
					ai = new AI(this);
					writeLine("JOIN #aibots");
				}
				
				if (connected) {
					if (line.contains("PRIVMSG #aibots :")) {
						String message = line.substring(line.indexOf(":", 1)+1);
						String name = line.substring(1, line.indexOf('!'));
						
						System.out.println(" [:] "+name+": "+message);
						//System.out.println(message);
						if (message.startsWith("NewBot: ")) {
							message = message.replace("NewBot: ","");
							try {
								String response = ai.replyTo(message, name);
								queueMessage(response, "aibots");
							} catch (Throwable e) {
								queueMessage("Caught Error: "+e.toString(), "aibots");
								e.printStackTrace();
							}
						}
						
					}
				}
				
			}
			sock.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeLine(String line) {
		try {
			writer.write(line+"\r\n");
			System.out.println(" < "+line);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Queue<String> messages = new LinkedList<String>();
	Thread queue;
	public void queueMessage(String msg, String chan) {	
		if (queue == null || !queue.isAlive()) {
			queue = new Thread(new Runnable() {
				
				@Override
				public void run() {
					String message;
					while ((message = messages.poll()) != null) {
						try {
							writeLine(message);
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			queue.start();
		}
		
		messages.add("PRIVMSG #"+chan+" :"+msg);
	}
	
	/**
	 * @author SirMangler (catty610)
	 * 30 Mar 2018
	 */
}

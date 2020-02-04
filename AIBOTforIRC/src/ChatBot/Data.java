package ChatBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {

	String[] nouns;
	String[] knownWords; 

	String[] hellos = { "hello", "hi", "yo", "greetings", "sup", "howdy", "hey", "heya" };
	String[] unknownReplies = { "Let's talk about something else.", "You mentioned being gay? Let's talk about that.", "uhhhh", "Let me consult my horoscope about that." };
	
	HashMap<String, String> dictionary = new HashMap<String, String>();
	HashMap<String, List<String>> commands = new HashMap<String, List<String>>();
	
	public void loadAllData() {
		loadNouns();
		loadKnownWords();
		loadCommands();
		System.out.print(commands.toString());
	}
	
	public void loadCommands() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Data.class.getResourceAsStream("/commands")));
			String line;
			
			String command = null;
			List<String> tokens = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(":")) {
					if (command != null) {
						commands.put(command, tokens);
					}
					
					command = "print";
					tokens = new ArrayList<String>();
					continue;
				}
				
				tokens.add(line);
			}
			
			commands.put(command, tokens);
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadDictionary() {
		if (!dictionary.isEmpty()) dictionary.clear();
		
		dictionary.put("How're you?", "I'm fine, thankyou.");
		dictionary.put("Who are you?", "I'm NewBot, a randomly made bot, for testing.");
		dictionary.put("What's up?", "Nothing much.");
		dictionary.put("What do you do?", "Basic call-responses.");
		dictionary.put("What are you?", "A basic key-value dictionary.");
		dictionary.put("What's the time?", "My system time is {time}.");
		dictionary.put("lol", "kappa");
		dictionary.put("reload dictionary", "{reload}");
		dictionary.put("reload", "{reload}");
		dictionary.put("Ping", "Pong");
		dictionary.put("Pong", "Ping");
		dictionary.put("Who made you?", "some twat.");
		dictionary.put("throw an error", "{error}");
		dictionary.put("Say goodbye.", "{shutdown}");
	}
	
	public void loadNouns() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Data.class.getResourceAsStream("/nouns")));
			String line = reader.readLine();
			nouns = line.split(";");
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadKnownWords() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Data.class.getResourceAsStream("/knownWords")));
			String line = reader.readLine();
			knownWords = line.split(";");
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author SirMangler (catty610)
	 * 31 Mar 2018
	 */
}

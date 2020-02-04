package ChatBot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import irc.IRCConnection;

public class AI extends Data {
	
	/**
	 * @author SirMangler (catty610)
	 * 30 Mar 2018
	 */
	
	IRCConnection irc;
	
	public AI(IRCConnection irc) {
		loadAllData();
		this.irc=irc;
	}
	
	public String replyTo(String line, String username) throws Throwable {
		for (String hello : hellos) {
			if (line.equalsIgnoreCase(hello)) {
				return "Greetings, "+username+".";
			}
		}
		
		System.out.println(commands.toString());
		for (Entry<String, List<String>> entry : commands.entrySet()) {
			String command = entry.getKey();
			List<String> structures = entry.getValue();
			for (String structure : structures) {
				String[] tokens = structure.split(":");
				int tokepos = 0;
				for (int i = 0; i < line.length(); i++) {
					String token = tokens[tokepos];
					
					
					
					if (i > token.length()) {
						break;
					}
					
					if (token.charAt(i) == '%') {
						String data = line.substring(line.indexOf(token), line.indexOf(tokens[tokepos+1]));
						
					}
					
					if (line.charAt(i) != token.charAt(i)) {
						break;
					}
				}
			}
		}
		
		System.out.println(dictionary.toString());
		int distance = 100;
		String resp = "";
		for (String call : dictionary.keySet()) {
			String response = dictionary.get(call);
			int tempDistance = minDistance(line, call);
			if (tempDistance < distance) {
				distance = tempDistance;
				resp = response;
			}
		}
		
		if (distance < 4) {
			resp = resp.replace("{time}", new Date().toString());
			if (resp.contains("{reload}")) {
				loadDictionary();
				loadAllData();
				
				resp = resp.replace("{reload}", "Reloaded.");
			}
			if (resp.contains("{error}")) {
				throw new java.lang.BootstrapMethodError("Why are you doing this to me?");
			}
			if (resp.contains("{shutdown}")) {
				resp = resp.replace("{shutdown}", "Goodbye.");
				new Thread(() -> {
						try {
							Thread.sleep(3000);
							irc.writeLine("QUIT Goodbye.");
							Thread.sleep(1000);
							System.exit(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();				
						}
				}).start();
			}
			return resp;
		} 
		
		Random r = new Random();
		return unknownReplies[r.nextInt(unknownReplies.length)];
	}
	
	
	public static int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];
	}
}

package jk6e11;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectData {
	
	private List<String> dateList;
	private List<String> tweetList;
	private List<Integer> scoreList;
	
	
	public CollectData() {
		FileInputStream fstream;
		
		try {
			fstream = new FileInputStream("bidu500.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			
			dateList = new ArrayList<String>();
			tweetList = new ArrayList<String>();
			scoreList = new ArrayList<Integer>();
			
			String strLine;
			String delims = "[,]";
			String[] tokens;

			String dateDelims = "[ ]";
			String date;
			String[] dateTokens;
			
			int a = 0;
			int b = 0;
			
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				// System.out.println (strLine);
				tokens = strLine.split(delims);

				for (int i = 0; i < tokens.length; i++) {
					//show the text line until entities
					if (tokens[i].contains("\"text\"")) {
						a = i;
					}else if (tokens[i].contains("\"entities\"")) {	
						b = i;
						StringBuffer description = new StringBuffer();
						for(int j=a; j<b; j++){
							description.append(tokens[j].replace("\"text\":", "").replace("\"", "")+ " ");
						}
						String str = description.toString();
						tweetList.add(str);
						//send the words of a line to analyser to count scores
						List<String> myList = new ArrayList<String>(Arrays.asList(str.split(" ")));
						SentiAnalyser analyser = new SentiAnalyser(myList);
						scoreList.add((Integer) analyser.getOutput().get("sentiment"));
						
//						System.out.println(str);
//						System.out.println("Score: " + analyser.getOutput().get("sentiment"));
//						System.out.println("Positive words: " +analyser.getOutput().get("positive_words"));
//						System.out.println("Negative words" + analyser.getOutput().get("negative_words"));
						
					//date of the tweet	
					}else if (tokens[i].contains("\"created_at\"")) {
						date = tokens[i];
						dateTokens = date.split(dateDelims);
						if(dateTokens[1].equals("Jan")){
							dateTokens[1] = "01";
						}else if(dateTokens[1].equals("Feb")){
							dateTokens[1] = "02";
						}else if(dateTokens[1].equals("Mar")){
							dateTokens[1] = "03";
						}else if(dateTokens[1].equals("Apr")){
							dateTokens[1] = "04";
						}else if(dateTokens[1].equals("May")){
							dateTokens[1] = "05";
						}else if(dateTokens[1].equals("Jun")){
							dateTokens[1] = "06";
						}else if(dateTokens[1].equals("Jul")){
							dateTokens[1] = "07";
						}else if(dateTokens[1].equals("Aug")){
							dateTokens[1] = "08";
						}else if(dateTokens[1].equals("Sep")){
							dateTokens[1] = "09";
						}else if(dateTokens[1].equals("Oct")){
							dateTokens[1] = "10";
						}else if(dateTokens[1].equals("Nov")){
							dateTokens[1] = "11";
						}else if(dateTokens[1].equals("Dec")){
							dateTokens[1] = "12";
						}
						String dateResult = dateTokens[5].replace("\"","") + dateTokens[1] + dateTokens[2];
						dateList.add(dateResult);
//						System.out.println(dateResult);
					}
				}
			}

			// Close the input stream
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public List<String> getTweetList(){
		return tweetList;
	}
	
	public List<String> getDateList(){
		return dateList;
	}
	
	public List<Integer> getScoreList(){
		return scoreList;
	}
}

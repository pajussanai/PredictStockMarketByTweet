package jk6e11;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SentiAnalyser {
	private String nwq = "nwq.txt";
	private String pwq = "pwq.txt";
	HashSet<String> pwqSet;
	HashSet<String> nwqSet;
	HashMap<String, Object> output;
	HashSet<String> positiveWords; 
	HashSet<String> negativeWords;
	
	public SentiAnalyser(List<String> myList) {
		pwqSet = readSentiSet(pwq);
		nwqSet = readSentiSet(nwq);
		output = new HashMap<String, Object>();
		positiveWords = new HashSet<String>();
		negativeWords = new HashSet<String>();
		counter(myList);
	}
	
	public void counter (List<String> line){
		int countP = 0;
		int countN = 0;

		for (String string : line) {

			if (pwqSet.contains(string)) {
				countP++;
				positiveWords.add(string);
			} else if (nwqSet.contains(string)) {
				countN++;
				negativeWords.add(string);
			}
		}
		
		if((countP - countN) >= 5){
			output.put("sentiment", 2);
		}else if((countP - countN) < 5 && (countP - countN) > 0){
			output.put("sentiment", 1);
		}else if((countP - countN) == 0){
			output.put("sentiment", 0);
		}else if((countP - countN) < 0 && (countP - countN) > -5){
			output.put("sentiment", -1);
		}else if((countP - countN) <= -5){
			output.put("sentiment", -2);
		}
		output.put("sentiment_positive", countP);
		output.put("sentiment_negative", countN);
		output.put("positive_words", positiveWords);
		output.put("negative_words", negativeWords);

	}
	
	public HashMap<String, Object> getOutput(){
		return output;
	}

	public HashSet<String> readSentiSet(String filepath) {
		HashSet<String> sentiSet = new HashSet<String>();
		FileInputStream fstream;
		
		try {

			fstream = new FileInputStream(filepath);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			String line = br.readLine();

			while (line != null) {
//				System.out.println(line);
				sentiSet.add(line.trim());
				line = br.readLine();
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
		return sentiSet;
	}
}

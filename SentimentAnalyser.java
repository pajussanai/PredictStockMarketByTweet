package jk6e11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openimaj.text.nlp.sentiment.SentimentExtractor;

public class SentimentAnalyser extends SentimentExtractor{
	private String nwq = "/jk6e11/nwq.txt";
	private String pwq = "/jk6e11/pwq.txt";

	@Override
	public Map<String, Object> extract(List<String> strings) {

		HashSet<String> pwqSet = readSentiSet(pwq);
		HashSet<String> nwqSet = readSentiSet(nwq);
		HashMap<String, Object> output = new HashMap<String, Object>();
		HashSet<String> positiveWords = new HashSet<String>();
		HashSet<String> negativeWords = new HashSet<String>();

		int countP = 0;
		int countN = 0;

		for (String string : strings) {

			if (pwqSet.contains(string)) {
				countP++;
				positiveWords.add(string);
			} else if (nwqSet.contains(string)) {
				countN++;
				negativeWords.add(string);
			}
		}

		output.put("sentiment", countP - countN);
		output.put("sentiment_positive", countP);
		output.put("sentiment_negative", countN);
		output.put("positive_words", positiveWords);
		output.put("negative_words", negativeWords);

		return output;
	}

	/**
	 *
	 * @param filepath
	 * @return
	 */
	public HashSet<String> readSentiSet(String filepath) {
		HashSet<String> sentiSet = new HashSet<String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					SentimentExtractor.class.getResourceAsStream(filepath)));
		} catch (Exception ex) {
			Logger.getLogger(SentimentExtractor.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		try {
			String line = br.readLine();

			while (line != null) {
				// System.out.println(line);
				sentiSet.add(line.trim());
				line = br.readLine();
			}
		} catch (IOException ex) {
			Logger.getLogger(SentimentExtractor.class.getName()).log(
					Level.SEVERE, null, ex);
		} finally {
			try {
				br.close();
			} catch (IOException ex) {
				Logger.getLogger(SentimentExtractor.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}

		return sentiSet;
	}
}

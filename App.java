package jk6e11;
//
//import org.openimaj.image.DisplayUtilities;
//import org.openimaj.image.MBFImage;
//import org.openimaj.image.colour.ColourSpace;
//import org.openimaj.image.colour.RGBColour;
//import org.openimaj.image.processing.convolution.FGaussianConvolve;
//import org.openimaj.image.typography.hershey.HersheyFont;
//import org.openimaj.stream.provider.twitter.TwitterStreamDataset;
//import org.openimaj.util.api.auth.DefaultTokenFactory;
//import org.openimaj.util.api.auth.common.TwitterAPIToken;
//import org.openimaj.util.function.Operation;
//
//import twitter4j.Status;
//
///**
// * OpenIMAJ Hello world!
// *
// */
//public class App {
//	public static void main(String[] args) {
//		// //Create an image
//		// MBFImage image = new MBFImage(320,70, ColourSpace.RGB);
//		//
//		// //Fill the image with white
//		// image.fill(RGBColour.WHITE);
//		//
//		// //Render some test into the image
//		// image.drawText("Hello World", 10, 60, HersheyFont.CURSIVE, 50,
//		// RGBColour.BLACK);
//		//
//		// //Apply a Gaussian blur
//		// image.processInplace(new FGaussianConvolve(2f));
//		//
//		// //Display the image
//		// DisplayUtilities.display(image);
//		TwitterAPIToken token = DefaultTokenFactory.get(TwitterAPIToken.class);
//		TwitterStreamDataset stream = new TwitterStreamDataset(token);
//		stream.forEach(new Operation<Status>() {
//			public void perform(Status status) {
//				if(status.getText().matches(".*\\b" + "APPL" + "\\b.*")){
//
//					System.out.println(status.getText());
//				}
//			}
//		});
//	}
//}

import twitter4j.*;

import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class App {
    /**
     * Usage: java twitter4j.examples.search.SearchTweets [query]
     *
     * @param args search query
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("java twitter4j.examples.search.SearchTweets [query]");
            System.exit(-1);
        }
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query = new Query(args[0]);
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                }
            } while ((query = result.nextQuery()) != null);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
}
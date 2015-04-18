package jk6e11;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
 
public class SimpleMongo {
       
        MongoClient mongoClient = null;
        DB db=null;
        private List<Integer> tscores;
        private List<Integer> tdates;
        private List<Integer> nOfTweet;
        private List<String> tweets;
        
        public DBCollection tweetTable;
        public String delims = "[ ]";
		public String[] tokens;
        
        public void mongoTest(String ip,int port,String dbname) throws Exception{
               //connect with mongoDB server
               mongoClient = new MongoClient(new ServerAddress(ip,port));
               db = mongoClient.getDB(dbname);
              
              tweetTable = db.getCollection("tweetTable");
//               BasicDBObject doc = new BasicDBObject("name", "MongoDB");
////                append("type", "database").
////                append("count", 1).
////                append("info", new BasicDBObject("date", 203).append("tweet", 102));
//               doc.put("type", "database");	   
//               doc.put("count", 1);	   
               
               //run collecting data and add filtered data in the list
               CollectData cd = new CollectData();
               List<DBObject> list = new ArrayList<DBObject>();
               List<String> tList = cd.getTweetList();
               
               //write tweet
               writeTweetFile(tList);
               
               
               List<String> dList = cd.getDateList();
               List<Integer> sList = cd.getScoreList();
               Iterator<String> titr = tList.iterator();
               Iterator<String> ditr = dList.iterator();
               Iterator<Integer> sitr = sList.iterator();
               
               //make a table in database (eg. date - tweet - score)
               while(titr.hasNext()){
            	   BasicDBObject data = new BasicDBObject();
               	   data.append("date", ditr.next());
               	   data.append("tweet", titr.next());
               	   data.append("score", sitr.next());
               	   list.add(data);
               }
               
               tweetTable.insert(list);
               
//               generateNumOfTweet();
               generateDailyScore();
//               writeDateFile();
               writeScoreFile();
//               writeNumOfTweetFile();
        }
        
        public void generateNumOfTweet(){
        	  DBObject group = new BasicDBObject(
          		    "$group", new BasicDBObject("_id", "$date").append(
          		        "scoreOfday", new BasicDBObject( "$sum", 1 )
          		    )
             );
             
             //sort by date
             DBObject sortFields = new BasicDBObject("_id", 1);
             DBObject sort = new BasicDBObject("$sort", sortFields );
             
             // run aggregation
             List<DBObject> pipeline = Arrays.asList(group, sort);
             AggregationOutput output = tweetTable.aggregate(pipeline);
             
             tdates = new ArrayList<Integer>();
             nOfTweet = new ArrayList<Integer>();
            
             for (DBObject result : output.results()) {
          	   tokens = result.toString().split(delims);
          	   tdates.add(Integer.valueOf(tokens[3].replace("\"", "")));
        	   nOfTweet.add(Integer.valueOf(tokens[7].replace("}", "")));
             }
        }
        
        public void generateDailyScore(){
        	
        	 //group date and sum scores in same day
            DBObject group = new BasicDBObject(
         		    "$group", new BasicDBObject("_id", "$date").append(
         		        "scoreOfday", new BasicDBObject( "$sum", "$score" )
         		    )
            );
            
            //sort by date
            DBObject sortFields = new BasicDBObject("_id", 1);
            DBObject sort = new BasicDBObject("$sort", sortFields );
            
            // run aggregation
            List<DBObject> pipeline = Arrays.asList(group, sort);
            AggregationOutput output = tweetTable.aggregate(pipeline);
            
            tdates = new ArrayList<Integer>();
            tscores = new ArrayList<Integer>();
            
            for (DBObject result : output.results()) {
           	   tokens = result.toString().split(delims);
           	   tdates.add(Integer.valueOf(tokens[3].replace("\"", "")));
        	   tscores.add(Integer.valueOf(tokens[7].replace("}", "")));
//        	   System.out.println(result);
            }
        }
        
        public void writeDateFile() throws IOException{
        	
        	File fout = new File("dateFile(aapl).txt");
        	FileOutputStream fos = new FileOutputStream(fout);
         
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
         
        	List<Integer> daate = getTdate();
			Iterator<Integer> dItr = daate.iterator();
			
			while(dItr.hasNext()){
				bw.write(dItr.next().toString());
				bw.newLine();
			}
			bw.close();
			System.out.println("Writing date of tweet is Done");
        }
        
        public void writeTweetFile(List<String> t){
        	try {
        		 
    			File file = new File("tweetFile(aapl).txt");
     
    			// if file doesnt exists, then create it
    			if (!file.exists()) {
    				file.createNewFile();
    			}
     
    			FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			List<String> tweet = t;
    			Iterator<String> tItr = tweet.iterator();
    			
    			while(tItr.hasNext()){
    				bw.write(tItr.next().toString());
    				bw.newLine();
    			}
    			bw.close();
     
    			System.out.println("Writing tweet is Done");
     
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
        
        public void writeScoreFile(){
        	try {
        		 
    			File file = new File("scoreFile(aapl).txt");
     
    			// if file doesnt exists, then create it
    			if (!file.exists()) {
    				file.createNewFile();
    			}
     
    			FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			List<Integer> ssore = getTscores();
    			Iterator<Integer> sItr = ssore.iterator();
    			
    			while(sItr.hasNext()){
    				bw.write(sItr.next().toString());
    				bw.newLine();
    			}
    			bw.close();
     
    			System.out.println("Writing score of each date is Done");
     
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
        
        public void writeNumOfTweetFile(){
        	try {
        		 
    			File file = new File("NumOfTweet(aapl).txt");
     
    			// if file doesnt exists, then create it
    			if (!file.exists()) {
    				file.createNewFile();
    			}
     
    			FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			List<Integer> not = getTnum();
    			Iterator<Integer> itr = not.iterator();
    			
    			while(itr.hasNext()){
    				bw.write(itr.next().toString());
    				bw.newLine();
    			}
    			bw.close();
     
    			System.out.println("Writing number of tweet is done");
     
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
        
        public List<Integer> getTdate(){
        	return tdates;
        }
        
        public List<Integer> getTscores(){
        	return tscores;
        }  
        

        public List<Integer> getTnum(){
        	return nOfTweet;
        }  
        
        public List<String> getTweet(){
        	return tweets;
        }  
        
        public static void main(String args[]) throws Exception{
               SimpleMongo testRunner = new SimpleMongo();
               testRunner.mongoTest("localhost", 27017, "tweet_aapl2_db");
        }
}
 
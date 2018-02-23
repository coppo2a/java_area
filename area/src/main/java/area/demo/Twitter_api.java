package area.demo;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Twitter_api {

    private String token_twitter;
    private String token_secret_twitter;
    private String token_facebook;

    public Twitter_api(String token_twitter, String token_secret_twitter, String token_facebook) {
        this.token_twitter = token_twitter;
        this.token_secret_twitter = token_secret_twitter;
        this.token_facebook = token_facebook;
        System.out.println("je passe dans twitter_api");
        apiCore();
    }

        public static String consumerKey = "YdEeLOs1ffkFEVBwDVlUjmXun";
        public static String consumerSecret = "Ict4KpsZFMLhS2mAstte58TPLoxe9N9uB6cLaENsa7xldX5CtG";
        public static String imageUrl = "";


        /**
         * Main method.
         *
         * @throws TwitterException
         */

    public void apiCore(){

        Thread t = new Thread() {
            public void run() {
                try {
                    mainbis ();
                } catch (TwitterException e) {
                    e.printStackTrace ();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        System.out.println("Mon traitement");

    }

    public void mainbis() throws TwitterException, InterruptedException {

        // The TwitterFactory object provides an instance of a Twitter object
        // via the getInstance() method. The Twitter object is the API consumer.
        // It has the methods for interacting with the Twitter API.
        while (true) {
            System.out.println("ACCESS TOKEN ==== " + this.token_twitter);
            System.out.println("ACCESS SECRET TOKEN ==== " + this.token_secret_twitter);

            ConfigurationBuilder cf = new ConfigurationBuilder();
            cf.setDebugEnabled(true)
                    .setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(this.token_twitter)
                    .setOAuthAccessTokenSecret(this.token_secret_twitter);
            TwitterFactory tf = new TwitterFactory(cf.build());
            Twitter twitter = tf.getInstance();
            Integer i = 0;

            ResponseList<Status> statuses;
            String users;
            //if (args.length == 1) {
            users = "";
            statuses = twitter.getUserTimeline("ATT");
            //} else {
            users = twitter.verifyCredentials().getScreenName();
            //System.out.println("LAAAA" + twitter.getFriendsIDs(Long.parseLong(String.valueOf(twitter.getId()))));
            statuses = twitter.getUserTimeline();
            //}
            //System.out.println("Showing @" + users + "'s user timeline.");
            for (Status status : statuses) {
                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                try {
                    String tweetID = String.valueOf(status.getId());
                    System.out.println("ICI " + twitter.getScreenName().toString());
                    System.out.println("IDTweet : " +  String.valueOf(twitter.getId()));
                    twitter4j.Status sts = twitter.showStatus(Long.parseLong(tweetID));
                    MediaEntity[] medias = sts.getMediaEntities(); //get the media entities from the status
                    for (MediaEntity m : medias ) { //search trough your entities
                        if (i == 0 && imageUrl.equals (m.getMediaURL ().toString ()) == false) {
                            System.out.println ("this is a pic" + "  " + m.getMediaURL ());
                            System.out.println ("I == " + i + "URL == " + imageUrl);

                            imageUrl = m.getMediaURL ();
                            //if (imageUrl == null || imageUrl != m.getMediaURL ()) {
                            System.out.println ("L'image va etre publiée");
                            imageUrl = m.getMediaURL ();


                            String destinationFile = "./image" + ".jpg";

                            try {
                                saveImage (imageUrl, destinationFile);
                            } catch (IOException e) {
                                e.printStackTrace ();
                            }
                            try {
                                postImageFacebook ();

                                try {
                                    Files.delete (Paths.get ("./image.jpg"));
                                } catch (IOException e) {
                                    e.printStackTrace ();
                                }

                            } catch (FileNotFoundException e) {
                                e.printStackTrace ();
                            }
                        } else System.out.println ("L'image ne va pas etre publiée");
                        i = i + 1;
                    }

                    //}
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
            Thread.sleep(30000);
        }
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        System.out.println(destinationFile);
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    public void postImageFacebook() throws FileNotFoundException {
        FacebookClient fbclient = new DefaultFacebookClient(this.token_facebook);


        FileInputStream fis = new FileInputStream(new File("./image.jpg"));

        FacebookType response = fbclient.publish("me/photos", FacebookType.class, BinaryAttachment.with("image.jpg", fis), com.restfb.Parameter.with("Test from restFb", "Radixcode Logo Image"));

        System.out.println("fb.com/"+response.getId());

    }
}

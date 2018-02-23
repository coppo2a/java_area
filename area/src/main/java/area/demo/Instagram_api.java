package area.demo;


import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.USER_AGENT;

public class Instagram_api {

    public static String url = "";
    public static String username = "";
    public static String token_facebook = "";

    public void launch(String username, String token_facebook) throws IOException, InterruptedException {
        Instagram_api.username = username;
        Instagram_api.token_facebook = token_facebook;
        apiCore();
    }

    public Instagram_api() throws IOException, InterruptedException {
        //this.username = username;
        //this.token_facebook = token_facebook;
        //apiCore();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public void         DownloadPic(String imageUrl, String pathToStore) throws IOException
    {
        System.out.println(pathToStore);
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(pathToStore);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    public void apiCore() throws IOException, JSONException, InterruptedException {
        Thread t = new Thread() {
            public void run() {
                try {
                    mainbis ();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        System.out.println("Mon traitement");

    }

    public void stop_thread(){
        Thread.interrupted();
    }

    public String getUrl() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://www.instagram.com/" + this.username + "/?__a=1");
        System.out.println(json.toString());
        String jjj = json.getJSONObject("user").getJSONObject("media").getJSONArray("nodes").getJSONObject(0).getString("thumbnail_src");

        return jjj;
    }

    public void mainbis() throws IOException, JSONException, InterruptedException {

        while (true) {
            System.out.println("Username : " + this.username);
            Instagram_api test = new Instagram_api();

            JSONObject json = readJsonFromUrl("https://www.instagram.com/" + this.username + "/?__a=1");
            System.out.println(json.toString());
            String jjj = json.getJSONObject("user").getJSONObject("media").getJSONArray("nodes").getJSONObject(0).getString("thumbnail_src");

            if (jjj.equals(url) == false) {
                url = jjj;
                test.DownloadPic(jjj, "./image.jpg");
                final FacebookClient fb = new DefaultFacebookClient(this.token_facebook);
                //final Page page = fb.fetchObject("me", Page.class);
                FileInputStream fi = new FileInputStream("./image.jpg");
                fb.publish("me/photos", FacebookType.class,
                        BinaryAttachment.with("image.jpg", fi), Parameter.with("Test from restFb", "Radixcode Logo Image"));
                Files.delete(Paths.get("./image.jpg"));
                System.out.println("l'image va etre publiée");
            }
            else
                System.out.println("L'image ne va pas être publiée");
            Thread.sleep(30000);
        }
    }

}

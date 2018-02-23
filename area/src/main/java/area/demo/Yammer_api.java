package area.demo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Yammer_api {

    String token;
    String to_send = "";

    Yammer_api(String token_yammer, String to_send_) throws IOException {
        token = token_yammer;
        to_send = to_send_;
        apiCore();
    }

    private void apiCore() throws IOException {
        getUser(this.token, to_send);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static void send_message_id(String id, String token, String msg) throws IOException {
        String urlParameters  = "";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = "https://www.yammer.com/api/v1/messages.json";
        URL url            = new URL( request );

        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setRequestMethod( "POST" );
        conn.setUseCaches( false );
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );


        System.out.print("bitebite " + token + " tt " + id);
        conn.setRequestProperty( "Accept", "*/*");
        conn.setRequestProperty( "Authorization", "Bearer " + token);
        conn.setRequestProperty( "accept-encoding", "");
        conn.setRequestProperty( "Content-Type", "application/json, charset=utf-8");


        String query = "{\n" +
                "    \"body\": \"" + msg + "\",\n" +
                "    \"direct_to_user_ids\": [\n" +
                "        " + id + "\n" +
                "    ]\n" +
                "}";
        conn.setRequestProperty( "Content-Length", Integer.toString(query.length()));
        conn.getOutputStream().write(query.getBytes("UTF8"));


        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    public static void getUser(String token, String msg) throws IOException, JSONException {
        String urlParameters  = "";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = "https://www.yammer.com/api/v1/users/current.json";
        URL    url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setRequestMethod( "GET" );
        conn.setRequestProperty( "Authorization", "Bearer " + token);
        StringBuffer response = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        JSONObject json = new JSONObject(response.toString());
        send_message_id(json.get("id").toString(), token, msg);
    }
}

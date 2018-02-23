package area.demo;

import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.USER_AGENT;

public class intra_w_yammer_api {

    public static Integer actualCredits = 0;
    public static String userEmail = "";
    public static Integer lineCredit = 0;
    public static Integer nbCreditsWon = 0;
    public static Integer lineMail = 0;
    public static String token_yammer = "";

    private String autolog = "";

    intra_w_yammer_api(String autolog_intra, String token_yammer_) {
        autolog = autolog_intra;
        token_yammer = token_yammer_;
        try {
            apiCore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void apiCore() throws IOException {
        System.out.println("Autlolog apiCore = " + autolog);
        Thread t = new Thread() {
            public void run() {
                try {
                    try {
                        try {
                            mainbis();
                        } catch (javax.mail.internet.AddressException e) {
                            e.printStackTrace();
                        }
                    } catch (sun.jvm.hotspot.debugger.AddressException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        System.out.println("Mon traitement");
    }

    public void mainbis() throws IOException, javax.mail.internet.AddressException {
        Integer turn = 0;

        while (true) {
            String httpsURL = this.autolog + "/user/?format=json";


            URL obj = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + httpsURL);
            System.out.println("Response Code : " + responseCode);


            StringBuffer response = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            //BufferedReader in = new BufferedReader ( new InputStreamReader ( con.getInputStream () ) );

            //add request header


            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }


            //print result
            System.out.println("DEBUT REPONSE");
            System.out.println(response.toString());
            System.out.println("FIN REPONSE");
            String[] strs = response.toString().replaceAll("\\s", "").split(",");
            for (int m = 0; m != strs.length; m++) {
                if (strs[m].indexOf("credit") != -1)
                    lineCredit = m;
                if (strs[m].indexOf("login") != -1)
                    lineMail = m;
            }
            String[] mailLine = strs[lineMail].replaceAll("\"", "").replaceAll("\\s", "").split(":");
            for (int x = 0; x != mailLine.length; x++) {
                System.out.println("X = " + x + "-----" + mailLine[x]);
            }
            userEmail = mailLine[1];
            System.out.println("USER EMAIL === " + userEmail);
            String[] creditsLine = strs[lineCredit].replaceAll("\\s", "").split(":");
            for (int i = 0; i != creditsLine.length; i++) {
                System.out.println("i == " + i + "----" + creditsLine[i].replaceAll("\\s", ""));

                if (i == 1) {
                    //if (actualCredits){
//                        actualCredits = Integer.parseInt ( creditsLine[i]);
                    //System.out.println ( "actualCrédits a pris la valeur");
                    //}
                    if (turn == 0) {
                        actualCredits = Integer.parseInt(creditsLine[i]);
                        turn = 1;
                    } else {
                        if (actualCredits != Integer.parseInt(creditsLine[i])) {
                            nbCreditsWon = Integer.parseInt(creditsLine[i]) - actualCredits;
                            System.out.println("Vous avez obtenu un crédit !! Créd === " + actualCredits);
                            actualCredits = Integer.parseInt(creditsLine[i]);
                            System.out.println("TOKEN Yammer : " + token_yammer);
                            String to_send = "Notifications Intranet: \n\n" + "You won " + nbCreditsWon + " credit(s).";
                            new Yammer_api(token_yammer, to_send);
                            //sendEmail ();
                        } else
                            System.out.println("Vos crédits n'ont pas bougé Créd === " + actualCredits + "crédits on line == " + creditsLine[i]);
                    }
                }
            }
            //if ((actualCredits = Integer.parseInt ( strs[i])) == 55) {
            //System.out.println ( "I ==== + " + i + "-----" + strs[i] + "-----" );
            //sendEmail ();

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
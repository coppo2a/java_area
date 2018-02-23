package area.demo;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import sun.jvm.hotspot.debugger.AddressException;
import sun.plugin2.message.Message;
import sun.plugin2.message.transport.Transport;

import javax.mail.*;
//import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

import static org.springframework.http.HttpHeaders.USER_AGENT;


class intra_api {



    public static Integer actualCredits = 0;
    public static String userEmail = "";
    public static Integer lineCredit = 0;
    public static Integer nbCreditsWon = 0;
    public static Integer lineMail = 0;

    private String autolog = "";

    intra_api(String autolog_intra) {
        autolog = autolog_intra;
        try {
            apiCore();
        } catch (IOException e) {
            e.printStackTrace ();
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
                            e.printStackTrace ();
                        }
                    } catch (sun.jvm.hotspot.debugger.AddressException e) {
                        e.printStackTrace ();
                    }
                } catch (IOException e) {
                    e.printStackTrace ();
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


        URL obj = new URL ( httpsURL );
        HttpsURLConnection con = ( HttpsURLConnection ) obj.openConnection ();

        // optional default is GET
        con.setRequestMethod ( "GET" );
        con.setRequestProperty ( "User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode ();
        System.out.println ( "\nSending 'GET' request to URL : " + httpsURL );
        System.out.println ( "Response Code : " + responseCode );


        StringBuffer response = new StringBuffer ();
        BufferedReader in = new BufferedReader ( new InputStreamReader ( con.getInputStream () ) );

        //BufferedReader in = new BufferedReader ( new InputStreamReader ( con.getInputStream () ) );

        //add request header



            String inputLine;

            while ((inputLine = in.readLine ()) != null) {
                response.append ( inputLine );
            }


            //print result
            System.out.println("DEBUT REPONSE");
            System.out.println(response.toString());
            System.out.println("FIN REPONSE");
            String[] strs = response.toString ().replaceAll ( "\\s", "" ).split ( "," );
            for (int m = 0; m != strs.length ; m++){
                if (strs[m].indexOf("credit") != -1)
                    lineCredit = m;
                if (strs[m].indexOf("login") != -1)
                    lineMail = m;
            }
            String[] mailLine = strs[lineMail].replaceAll ( "\"", "" ).replaceAll ( "\\s", "" ).split ( ":" );
            for (int x = 0; x != mailLine.length; x++){
                System.out.println ( "X = " + x + "-----" +  mailLine[x] );
            }
            userEmail = mailLine[1];
            System.out.println ( "USER EMAIL === " + userEmail );
            String[] creditsLine = strs[lineCredit].replaceAll ( "\\s", "" ).split ( ":" );
            for (int i = 0; i != creditsLine.length; i++) {
                System.out.println ( "i == " + i + "----" + creditsLine[i].replaceAll ( "\\s", "" ) );

                if (i == 1) {
                    //if (actualCredits){
//                        actualCredits = Integer.parseInt ( creditsLine[i]);
                    //System.out.println ( "actualCrédits a pris la valeur");
                    //}
                    if (turn == 0){
                        actualCredits = Integer.parseInt ( creditsLine[i] );
                        turn = 1;
                    }
                    else {
                        if (actualCredits != Integer.parseInt ( creditsLine[i] )) {
                            nbCreditsWon = Integer.parseInt ( creditsLine[i] ) - actualCredits;
                            System.out.println ( "Vous avez obtenu un crédit !! Créd === " + actualCredits );
                            actualCredits = Integer.parseInt ( creditsLine[i] );
                            sendEmail ();
                        } else System.out.println ( "Vos crédits n'ont pas bougé Créd === " + actualCredits + "crédits on line == " + creditsLine[i]);
                    }
                }
            }
            //if ((actualCredits = Integer.parseInt ( strs[i])) == 55) {
            //System.out.println ( "I ==== + " + i + "-----" + strs[i] + "-----" );
            //sendEmail ();

            try {
                TimeUnit.SECONDS.sleep ( 5 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
    }



    public static void sendEmail() throws javax.mail.internet.AddressException {
        String smtpServer = "smtp.gmail.com";
        int port = 587;
        final String userid = "habibruben@gmail.com";//change accordingly
        final String password = "Losangeles1998";//change accordingly
        String contentType = "text/html";
        String subject = "Signal Alert Credits";
        String from = "habibruben@gmail.com";
        String to = userEmail;//some invalid address
        String bounceAddr = userEmail;//change accordingly
        String body = "You won " + nbCreditsWon + " credit(s).";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", "587");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.from", bounceAddr);

        Session mailSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userid, password);
                    }
                });

        MimeMessage message = new MimeMessage(mailSession);
        try {
            message.addFrom(InternetAddress.parse(from));
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace ();
        }
        try {
            message.setRecipients( javax.mail.Message.RecipientType.TO, to);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace ();
        }
        try {
            message.setSubject(subject);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace ();
        }
        try {
            message.setContent(body, contentType);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace ();
        }

        javax.mail.Transport transport = null;
        try {
            transport = mailSession.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace ();
        }
        try {
            System.out.println("Sending ....");
            transport.connect(smtpServer, port, userid, password);
            transport.sendMessage(message,
                    message.getRecipients( javax.mail.Message.RecipientType.TO));
            System.out.println("Sending done ...");
        } catch (Exception e) {
            System.err.println("Error Sending: ");
            e.printStackTrace();

        }
        try {
            transport.close ();
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace ();
        }

    }
}

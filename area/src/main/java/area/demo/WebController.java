package area.demo;

import com.dropbox.core.DbxException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@Controller
@RequestMapping(value = "/home")
public class WebController {

    @Autowired
    private UserRepository repository;

    private String image_uploaded_ext = "";
    private String image_uploaded = "";
    private String token_facebook = "";
    private String token_twitter = "";
    private String token_secret_twitter = "";
    private String token_dropbox = "";
    private String user_instagram = "";
    private String token_deezer = "";
    private String token_youtube = "";
    private String user = "";
    private String token_yammer = "";
    private String autolog_intra = "";

    @RequestMapping(value = {""})
    public String MyHome(HttpServletRequest request) {
        System.out.println("je passe dans /home");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().compareTo("name") == 0 && cookies[i].getValue().length() != 0) {
                    return "redirect:/connected/index.html?" + cookies[i].getValue();
                }
            }
        }
        return "home/index.html";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String CreateAccount(@RequestParam("inputLogin") String login, @RequestParam("inputPass") String pass) {
        System.out.println("login is : " + login + " " + pass);
        Database db = new Database(repository);
        if (!db.IsUserValide(login, pass)) {
            repository.save(new User(login, pass));
            return "redirect:/home?yes";
        } else {
            return "redirect:/home?no";
        }

    }

    @RequestMapping(value = "connect", method = RequestMethod.POST)
    public String Connect(@RequestParam("inputLogin") String login, @RequestParam("inputPass") String pass,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        Database db = new Database(repository);
        if (db.IsUserValide(login, pass) && !Objects.equals(login, "") && !Objects.equals(pass, "")) {
            System.out.println("Connected");
            System.out.println("login : " + login);
            Cookie myCookie = new Cookie("name", login);
            response.addCookie(myCookie);
            return "redirect:/connected/index.html?" + login;
        }
        return "redirect:/home?wrong_identifiant";
    }

    @RequestMapping(value = "disconnect", method = RequestMethod.POST)
    public String Disconnect(HttpServletResponse response) {
        System.out.println("je passe dans disconnect");
        Cookie cookie = new Cookie("name", "");
        response.addCookie(cookie);
        return "redirect:/home";
    }

    @RequestMapping(value = "connected/api_request")
    public String api_request(@RequestParam("token") String token, @RequestParam("user") String user,
                                   @RequestParam("api_name") String api_name, @RequestParam("token_secret") String token_secret) throws IOException, DbxException {
        System.out.println("api_name : " + api_name + " Token : " + token + " User : " + user);
        this.user = user;
        if (Objects.equals(api_name, "facebook")) {
            token_facebook = token;
            new Facebook_api(token);
        }
        if (Objects.equals(api_name, "twitter")) {
            token_twitter = token;
            token_secret_twitter = token_secret;
        }
        if (Objects.equals(api_name, "yammer"))
            token_yammer = token;
        if (Objects.equals(api_name, "youtube")) {
            token_youtube = token;
            new Youtube_api(token);
        }
        if (Objects.equals(api_name, "dropbox")){
            token_dropbox = token;
        }
        return "redirect:/connected/index.html?" + user + "?" + api_name;
    }

    @RequestMapping(value = "connected/uploadFile", method = RequestMethod.POST)
    public String uploadFileHandler(@RequestParam("file") MultipartFile file) throws DbxException {
        if (Objects.equals(token_dropbox, ""))
            return "redirect:/connected/index.html?" + user + "?wrong";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                System.out.println("toto =" + File.separator + ":" + file.getOriginalFilename());

                if (!image_uploaded_ext.isEmpty())
                {
                    System.out.println("empty image uploaded = " + image_uploaded_ext);
                    File ToDel = new File(image_uploaded_ext);
                    ToDel.delete();
                }

                String ext = "." + FilenameUtils.getExtension(file.getOriginalFilename()); // returns "txt"

                // Create the file on server
                DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy-HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                File serverFile = new File("src/main/resources/images_upload"
                        + File.separator + "image" + dateFormat.format(date) + ext);

                System.out.println(serverFile.getAbsolutePath());
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                image_uploaded_ext = "src/main/resources/images_upload"
                        + File.separator + "image" + dateFormat.format(date) + ext;
                image_uploaded = "image" + dateFormat.format(date) + ext;

                System.out.println("Server File Location="
                        + serverFile.getAbsolutePath());
                new Dropbox_api(token_dropbox, image_uploaded_ext, image_uploaded);
            } catch (Exception e) {
                return "You failed to upload " + "image" + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + "image"
                    + " because the file was empty.";
        }
        return "redirect:/connected/index.html?" + user;
    }

    @RequestMapping(value = "connected/twitter_fb", method = RequestMethod.GET)
    public String twitter_fb(@RequestParam("user") String user) {
        System.out.println("je passe dans twitter_fb");
        if (!Objects.equals(token_secret_twitter, "") && !Objects.equals(token_twitter, "")) {
            new Twitter_api(token_twitter, token_secret_twitter, token_facebook);
            return "redirect:/connected/index.html?" + user;
        }
        else
            return "redirect:/connected/index.html?" + user + "?wrong";
    }

    @RequestMapping(value = "connected/intra", method = RequestMethod.GET)
    public String get_autologin(@RequestParam("user") String user, @RequestParam("autologin") String autolog) {
        System.out.println("User : " + user + " autlogin Epitech : " + autolog);
        if (!autolog.contains("https://intra.epitech.eu/auth"))
            return "redirect:/connected/index.html?" + user + "?wrong_autolog";
        this.user = user;
        autolog_intra = autolog;
        return "redirect:/connected/index.html?" + user + "?epitech";
    }

    @RequestMapping(value = "connected/instagram", method = RequestMethod.GET)
    public String get_user_insta(@RequestParam("user1") String user, @RequestParam("user_instagram") String user_instagram) {
        System.out.println("User : " + user + " user instagram : " + user_instagram);
        if (Objects.equals(user_instagram, ""))
            return "redirect:/connected/index.html?" + user + "?wrong_user_instagram";
        this.user = user;
        this.user_instagram = user_instagram;
        return "redirect:/connected/index.html?" + user + "?instagram";
    }

    @RequestMapping(value = "connected/intra_mail", method = RequestMethod.GET)
    public String intra_mail(@RequestParam("user") String user) {
        if (Objects.equals(autolog_intra, "")){
            return "redirect:/connected/index.html?" + user + "?wrong";
        }
        new intra_api(this.autolog_intra);
        return "redirect:/connected/index.html?" + this.user;
    }

    @RequestMapping(value = "connected/insta_fb", method = RequestMethod.GET)
    public String insta_fb(@RequestParam("user") String user) throws IOException, InterruptedException {
        if (Objects.equals(user_instagram, "") && Objects.equals(token_facebook, "")){
            return "redirect:/connected/index.html?" + user + "?wrong";
        }
        //new Instagram_api(this.user_instagram, this.token_facebook);
        Instagram_api launch = new Instagram_api();
        launch.launch(this.user_instagram, this.token_facebook);
        return "redirect:/connected/index.html?" + user;
    }

    @RequestMapping(value = "connected/intra_yammer", method = RequestMethod.GET)
    public String get_user_insta(@RequestParam("user") String user) throws IOException {
        System.out.println("User : " + user + " user yammer : " + token_yammer);
        if (Objects.equals(token_yammer, ""))
            return "redirect:/connected/index.html?" + user + "?wrong";
        this.user = user;
        new intra_w_yammer_api(this.autolog_intra, this.token_yammer);
        return "redirect:/connected/index.html?" + this.user;
    }

}

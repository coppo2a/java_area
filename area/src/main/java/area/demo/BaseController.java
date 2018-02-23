package area.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class BaseController {
    @RequestMapping(value = "")

    public String redirHome() {
        System.out.println("redirHome");
        return "redirect:/home";
    }
}

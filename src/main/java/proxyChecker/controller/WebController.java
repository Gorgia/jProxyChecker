package proxyChecker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by andrea on 27/01/15.
 */

@Controller
public class WebController {

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/staticPage", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:/pages/final.htm";
    }

}


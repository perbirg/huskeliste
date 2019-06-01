package no.pbe.huskeliste;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HuskelisteWebController {

    @RequestMapping("/visHuskelister")
    public String visHuskelister() {
        return "visHuskelister";
    }

}

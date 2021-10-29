package de.neuefische.backend.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactRoutingForward {

    @GetMapping(value = "/**/{[path:[^\\.]*}")
    public String forwardToRoutUrl() {
        return "forward:/";
    }
}

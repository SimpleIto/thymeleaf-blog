package org.ito.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {

    @RequestMapping
    public String baseRedirect(){
        return "redirect:/blog";
    }

}

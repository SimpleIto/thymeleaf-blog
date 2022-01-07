package org.ito.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/blog/login")
public class LoginAdminController {
    public static final String USERNAME = "x";
    public static final String PASSWORD = "x";

    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "username") String username,@RequestParam("password") String password) throws IOException {
        if(username.equals(USERNAME)&&password.equals(PASSWORD)){
            request.getSession().setAttribute("admin",USERNAME);
            response.sendRedirect("/blog/admin");
            return null;
        }
        else {
            return "admin/login";
        }
    }

}

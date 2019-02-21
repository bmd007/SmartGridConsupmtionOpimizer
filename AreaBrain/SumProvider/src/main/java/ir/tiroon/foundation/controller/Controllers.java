package ir.tiroon.foundation.controller;

import ir.tiroon.foundation.service.UserInfoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Lenovo on 01/07/2016.
 */

@Controller
public class Controllers {

    @Autowired
    UserInfoServices userServices;

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public String test(Model model) {
        model.addAttribute("o", "OO");
        model.addAttribute("userInfos", userServices.getEntityList());
        return "/test";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/index")
    public String index(Model model) {
        return "/index";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/login")
    public String login(Model model) {
        return SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken ? "/login" :  "redirect:jsfViews/admin/index2.jsf";
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }


    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "/accessDenied";
    }

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }


}

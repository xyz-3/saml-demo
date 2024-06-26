package org.data.mujinaidpplatform.controller;


import org.data.mujinaidpplatform.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.data.mujinaidpplatform.Entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserDao userDao;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("username") String username, @RequestParam("password") String password,
                            RedirectAttributes redirectAttributes){
        User user = userDao.getUser(username, password);
        if(user != null){
            redirectAttributes.addFlashAttribute(user);
            if (user.getAuthorities().contains("ROLE_ADMIN")) {
                return "redirect:/platform.html";
            }else{
                return "redirect:/Profile.html";
            }
        }
        return "index";
    }

    @GetMapping({"platform", "/platform.html"})
    public String platform(ModelMap modelMap){
        List<User> users = userDao.getAllUsers();
        modelMap.addAttribute("users", users);
        return "platform";
    }

    @GetMapping({"profile", "/Profile.html"})
    public String profile(ModelMap modelMap){
        return "profile";
    }
}

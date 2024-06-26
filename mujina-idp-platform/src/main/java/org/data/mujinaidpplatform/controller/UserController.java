package org.data.mujinaidpplatform.controller;


import org.data.mujinaidpplatform.dao.UserDao;
import org.data.mujinaidpplatform.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

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
        UserDto user = userDao.getUser(username, password);
        if(user != null){
            redirectAttributes.addFlashAttribute("user", user);
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
        List<UserDto> users = userDao.getAllUsers();
        modelMap.addAttribute("users", users);
        return "platform";
    }

    @GetMapping({"profile", "/Profile.html"})
    public String profile(ModelMap modelMap){
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }

    @PostMapping("/modifyAuthorities")
    public String modifyUserAuthorities(@RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
        List<UserDto> users = userDao.getAllUsers();
        UserDto admin = users.stream().filter(user -> user.getAuthorities().contains("ROLE_ADMIN")).findFirst().orElse(null);
        for (UserDto user : users) {
            if(user.equals(admin)){
                continue;
            }
            String addAuthority = allParams.get("addAuthority_" + user.getName());
            String removeAuthority = allParams.get("delAuthority_" + user.getName());

            if (addAuthority != null && !addAuthority.isEmpty()) {
                user.getAuthorities().add(addAuthority);
                userDao.addAuthority(user.getId(), addAuthority);
            }

            if (removeAuthority != null && !removeAuthority.isEmpty()) {
                user.getAuthorities().remove(removeAuthority);
                userDao.removeAuthority(user.getId(), removeAuthority);
            }
        }
        redirectAttributes.addFlashAttribute("user", admin);
        redirectAttributes.addFlashAttribute("users", users);
        return "redirect:/platform";
    }
}

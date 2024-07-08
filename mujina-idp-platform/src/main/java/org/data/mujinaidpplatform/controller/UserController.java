package org.data.mujinaidpplatform.controller;


import org.data.mujinaidpplatform.Entity.Application;
import org.data.mujinaidpplatform.dao.ApplicationDao;
import org.data.mujinaidpplatform.dao.UserApplicationDao;
import org.data.mujinaidpplatform.dao.UserDao;
import org.data.mujinaidpplatform.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApplicationDao applicationDao;
    @Autowired
    private UserApplicationDao userApplicationDao;

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
        // filter admin out of users
        UserDto admin = users.stream().filter(user -> user.getAuthorities().contains("ROLE_ADMIN")).findFirst().orElse(null);
        modelMap.addAttribute("user", admin);
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

    @GetMapping("/AllUsers.html")
    public String allUsers(ModelMap modelMap){
        List<UserDto> users = userDao.getAllUsers();
        // filter admin out of users
        UserDto admin = users.stream().filter(user -> user.getAuthorities().contains("ROLE_ADMIN")).findFirst().orElse(null);
        users.remove(admin);
        modelMap.addAttribute("users", users);
        return "AllUsers";
    }

    @GetMapping("/Applications.html")
    public String applications(ModelMap modelMap){
        List<Application> apps = applicationDao.getAllApplications();
        modelMap.addAttribute("apps", apps);
        return "Applications";
    }

    @PostMapping("/modifyAuthorities")
    public String modifyUserAuthorities(@RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
        List<UserDto> users = userDao.getAllUsers();
        UserDto admin = users.stream().filter(user -> user.getAuthorities().contains("ROLE_ADMIN")).findFirst().orElse(null);
        // filter admin out of users
        users.remove(admin);
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
        return "redirect:/AllUsers.html";
    }

    @PostMapping("/registerApp")
    public ResponseEntity<byte[]> registerApplication(@RequestParam Map<String, String> allParams) throws IOException {
        String appName = allParams.get("appName");
        String entityId = allParams.get("entityId");
        String baseUrl = allParams.get("baseUrl");
        String acsPath = allParams.get("acsPath");
        String logoutPath = allParams.get("sloPath");

        // Register the application
        String metadata = applicationDao.registerApplication(appName, entityId, baseUrl, acsPath, logoutPath);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=metadata.xml");
        return new ResponseEntity<>(metadata.getBytes(), headers, HttpStatus.OK);
    }

    @PostMapping("/addUserToApp")
    public ResponseEntity<Map<String, String>> addUserToApp(@RequestParam Map<String, String> allParams){
        String userName = allParams.get("userName");
        String entityId = allParams.get("entity_id");
        boolean ret = userApplicationDao.addUserApplication(userName, entityId);
        Map<String, String> response = new HashMap<>();
        if(ret){
            response.put("message", "Success!");
            return ResponseEntity.ok(response);
        }else{
            response.put("message", "Failed!");
            return ResponseEntity.badRequest().body(response);
        }
    }
}

package mujina.sp;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    /*
    如果用户没有登录or登录仍然未失效，Authentication可以获取上下文 -> 非空 -> 重定向到登录后的用户界面；
    否则，就回到登录界面
     */
    @GetMapping("/")
    public String index(Authentication authentication) {
        return authentication == null ? "index" : "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin.html";
        } else {
            return "redirect:/user.html";
        }
    }

    @GetMapping({"user", "/user.html"})
    public String user(Authentication authentication, ModelMap modelMap) {
        modelMap.addAttribute("user", authentication.getPrincipal());
        return "user";
    }

    @GetMapping({"admin", "/admin.html"})
    public String admin(Authentication authentication, ModelMap modelMap) {
        if(authentication == null) {
            return "redirect:/";
        }
        if(authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/home";
        }
        modelMap.addAttribute("user", authentication.getPrincipal());
        return "admin";
    }

}

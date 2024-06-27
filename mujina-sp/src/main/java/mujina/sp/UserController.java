package mujina.sp;

import mujina.Response.RedirectResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    /*
    如果用户没有登录or登录仍然未失效，Authentication可以获取上下文 -> 非空 -> 重定向到登录后的用户界面；
    否则，就回到登录界面
     */
    @GetMapping("/")
    public RedirectResponse index(Authentication authentication) {
        if (authentication != null) {
            // check if user is admin
            List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            if (roles.contains("ROLE_ADMIN")) {
                return new RedirectResponse("/admin", "success", roles);
            } else {
                return new RedirectResponse("/user", "success", roles);
            }
        }else{
            return new RedirectResponse("/", "success", null);
        }
    }

    @GetMapping("/home")
    public RedirectResponse home(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (roles.contains("ROLE_ADMIN")) {
            return new RedirectResponse("/admin", "success", roles);
        } else {
            return new RedirectResponse("/user", "success", roles);
        }
    }

    @GetMapping({"user", "/user.html"})
    public String user(Authentication authentication, ModelMap modelMap) {
        modelMap.addAttribute("user", authentication.getPrincipal());
        modelMap.addAttribute("authorities", authentication.getAuthorities());
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

    @GetMapping({"page1", "/page1.html"})
    public String page1(Authentication authentication, ModelMap modelMap) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SVIP"))) {
            modelMap.addAttribute("user", authentication.getPrincipal());
            return "page1";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping({"page2", "/page2.html"})
    public String page2(Authentication authentication, ModelMap modelMap) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VIP"))) {
            modelMap.addAttribute("user", authentication.getPrincipal());
            return "page2";
        } else {
            return "redirect:/home";
        }
    }

}

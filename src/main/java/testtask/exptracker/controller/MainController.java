package testtask.exptracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import testtask.exptracker.domain.User;

@Controller
public class MainController {
    @GetMapping("/greeting")
    public String greeting(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("roles", user.getRoles());
        return "greeting";
    }
}

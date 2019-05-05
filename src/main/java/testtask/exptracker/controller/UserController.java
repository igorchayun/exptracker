package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.Role;
import testtask.exptracker.domain.User;
import testtask.exptracker.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(@RequestParam(required = false, defaultValue = "") String filterUsr, Model model) {
        model.addAttribute("users", userService.findUsers(filterUsr));
        return "users";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("allRoles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(@Valid User user, @RequestParam String newPassword, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "userEdit";
        }

        userService.saveUser(user, newPassword);

        return "redirect:/users";
    }

    @GetMapping("/new")
    public String addUserForm() {
        return "";
    }

    @PostMapping("/new")
    public String addUser() {
        return "";
    }
}

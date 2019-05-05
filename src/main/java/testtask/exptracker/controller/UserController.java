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
    public String userEditSave(
            @Valid User user,
            BindingResult bindingResult,
            @RequestParam String newPassword,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", Role.values());
            return "userEdit";
        }

        boolean isUserSaved = userService.saveUser(user, newPassword);

        if (!isUserSaved) {
            model.addAttribute("usernameError", "User exists!");
            model.addAttribute("allRoles", Role.values());
            return "userEdit";
        }

        return "redirect:/users";
    }

    @GetMapping("/new")
    public String addNewUserForm(User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("allRoles", Role.values());
        return "userAdd";
    }

    @PostMapping("/new")
    public String addNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("allRoles", Role.values());
            return "userAdd";
        }
        boolean isNewUserAdded = userService.addNewUser(user);

        if (!isNewUserAdded) {
            model.addAttribute("usernameError", "User exists!");
            model.addAttribute("allRoles", Role.values());
            return "userAdd";
        }
        return "redirect:/users";
    }
}

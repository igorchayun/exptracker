package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.User;
import testtask.exptracker.service.UserService;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model
    ) {
        model.addAttribute("users", userService.findUsers(currentUser, filter));
        model.addAttribute("filterUsr", filter);
        model.addAttribute("curUserIsAdmin", currentUser.isAdmin());
        return "users";
    }

    @GetMapping("{user}")
    public String userEditForm(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
        if (currentUser.isManager() && user.isAdmin()) {
            return "redirect:/users";
        }
        model.addAttribute("allRoles", userService.getAllowedRoles(currentUser));
        model.addAttribute("user", user);
        return "userEdit";
    }

    @PostMapping
    public String userEditSave(
            @AuthenticationPrincipal User currentUser,
            @Valid User user,
            BindingResult bindingResult,
            @RequestParam String newPassword,
            Model model
    ) {
        model.addAttribute("allRoles", userService.getAllowedRoles(currentUser));
        if (bindingResult.hasErrors()) {
            return "userEdit";
        }
        if (userService.saveUser(currentUser, user, newPassword) == null) {
            model.addAttribute("usernameError", "User exists!");
            return "userEdit";
        }
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String addNewUserForm(@AuthenticationPrincipal User currentUser, User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllowedRoles(currentUser));
        return "userAdd";
    }

    @PostMapping("/new")
    public String addNewUser(
            @AuthenticationPrincipal User currentUser,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("allRoles", userService.getAllowedRoles(currentUser));
        if(bindingResult.hasErrors()) {
            return "userAdd";
        }
        if (userService.addNewUser(currentUser, user) == null) {
            model.addAttribute("usernameError", "User exists!");
            return "userAdd";
        }
        return "redirect:/users";
    }
}

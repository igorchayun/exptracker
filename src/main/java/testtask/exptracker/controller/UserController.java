package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.Role;
import testtask.exptracker.domain.User;
import testtask.exptracker.service.UserService;
import javax.validation.Valid;
import java.util.EnumSet;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(
            @AuthenticationPrincipal User authUser,
            @RequestParam(required = false, defaultValue = "") String filterUsr,
            Model model
    ) {
        if (authUser.isAdmin()) {
            model.addAttribute("users", userService.findAllUsers(filterUsr));
        } else {
            model.addAttribute("users", userService.findUsersNonAdmins(filterUsr));
        }
        model.addAttribute("filterUsr", filterUsr);

        return "users";
    }

    @GetMapping("{user}")
    public String userEditForm(@AuthenticationPrincipal User authUser, @PathVariable User user, Model model) {
        model.addAttribute("user", user);
        if (authUser.isAdmin()) {
            model.addAttribute("allRoles", Role.values());
        } else {
            EnumSet<Role> managerRoles = EnumSet.of(Role.USER, Role.MANAGER );
            model.addAttribute("allRoles", managerRoles);
        }
        return "userEdit";
    }

    @PostMapping
    public String userEditSave(
            @AuthenticationPrincipal User authUser,
            @Valid User user,
            BindingResult bindingResult,
            @RequestParam String newPassword,
            Model model
    ) {
        if (authUser.isAdmin()) {
            model.addAttribute("allRoles", Role.values());
        } else {
            model.addAttribute("allRoles", EnumSet.of(Role.USER, Role.MANAGER ));
        }

        if (bindingResult.hasErrors()) {
            return "userEdit";
        }

        boolean isUserSaved = userService.saveUser(user, newPassword);

        if (!isUserSaved) {
            model.addAttribute("usernameError", "User exists!");
            return "userEdit";
        }
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String addNewUserForm(@AuthenticationPrincipal User authUser, User user, Model model) {
        model.addAttribute("user", user);
        if (authUser.isAdmin()) {
            model.addAttribute("allRoles", Role.values());
        } else {
            model.addAttribute("allRoles", EnumSet.of(Role.USER, Role.MANAGER ));
        }
        return "userAdd";
    }

    @PostMapping("/new")
    public String addNewUser(
            @AuthenticationPrincipal User authUser,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        if (authUser.isAdmin()) {
            model.addAttribute("allRoles", Role.values());
        } else {
            model.addAttribute("allRoles", EnumSet.of(Role.USER, Role.MANAGER ));
        }

        if(bindingResult.hasErrors()) {
            return "userAdd";
        }

        boolean isNewUserAdded = userService.addNewUser(user);

        if (!isNewUserAdded) {
            model.addAttribute("usernameError", "User exists!");
            return "userAdd";
        }
        return "redirect:/users";
    }
}

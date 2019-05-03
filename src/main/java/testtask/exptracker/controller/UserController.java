package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.Role;
import testtask.exptracker.domain.User;
import testtask.exptracker.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("allRoles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "expenseEdit";
        }

        user.setPassword(userRepository.getOne(user.getId()).getPassword());

        userRepository.save(user);

        return "redirect:/users";
    }
}

package testtask.exptracker.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.User;
import testtask.exptracker.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> list(@AuthenticationPrincipal User currentUser,
                           @RequestParam(required = false, defaultValue = "") String filter) {
        return userService.findUsers(currentUser, filter);
    }

    @GetMapping("{id}")
    public User getOne(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        return userService.getAllowedUser(currentUser, id);
    }

    @PostMapping
    public User create(@AuthenticationPrincipal User currentUser, @RequestBody User user) {
        return userService.addNewUser(currentUser, user);
    }

    @PutMapping("{id}")
    public User update(@AuthenticationPrincipal User currentUser,
                       @PathVariable("id") User userFromDb,
                       @RequestBody User user
    ) {
        BeanUtils.copyProperties(user, userFromDb, "id");
        return userService.saveUser(currentUser, userFromDb, null);
    }

    @DeleteMapping("{id}")
    public void delete(@AuthenticationPrincipal User currentUser, @PathVariable("id") User user) {
        userService.deleteUser(currentUser, user);
    }
}

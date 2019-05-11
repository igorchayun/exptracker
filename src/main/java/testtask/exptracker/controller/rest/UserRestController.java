package testtask.exptracker.controller.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "/api/v1/users", description = "User management operations")
public class UserRestController {

    private final UserService userService;
    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Displays a list of users")
    @GetMapping
    public List<User> list(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "") String filter
    ) {
        return userService.findUsers(currentUser, filter);
    }

    @ApiOperation(value = "Display user by id")
    @GetMapping("{id}")
    public User getOne(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        return userService.getAllowedUser(currentUser, id);
    }

    @ApiOperation(value = "Create new user")
    @PostMapping
    public User create(@AuthenticationPrincipal User currentUser, @RequestBody User user) {
        return userService.addNewUser(currentUser, user);
    }

    @ApiOperation(value = "Edit user by id")
    @PutMapping("{id}")
    public User update(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("id") User userFromDb,
            @RequestBody User user
    ) {
        BeanUtils.copyProperties(user, userFromDb, "id");
        return userService.saveUser(currentUser, userFromDb, null);
    }

    @ApiOperation(value = "Delete user by id")
    @DeleteMapping("{id}")
    public void delete(@AuthenticationPrincipal User currentUser, @PathVariable("id") User user) {
        userService.deleteUser(currentUser, user);
    }
}

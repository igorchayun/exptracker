package testtask.exptracker.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.User;
import testtask.exptracker.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> list() {
        return userService.findAllUsers(null);
    }

    @GetMapping("{id}")
    public User getOne(@PathVariable("id") User user) {
        return user;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    @PutMapping("{id}")
    public User update(@PathVariable("id") User userFromDb, @RequestBody User user) {
        BeanUtils.copyProperties(user, userFromDb, "id");
        return userService.saveUser(userFromDb, null);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") User user) {
        userService.deleteUser(user);
    }
}

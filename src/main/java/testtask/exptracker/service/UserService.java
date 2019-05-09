package testtask.exptracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import testtask.exptracker.domain.Role;
import testtask.exptracker.domain.User;
import testtask.exptracker.repository.UserRepository;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    private boolean isUsernameExist(String username) {
        User userFromDb = userRepository.findByUsername(username);

        return userFromDb != null;
    }

    public User addUser(User user) {

        if (isUsernameExist(user.getUsername())) {
            return null;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<User> findAllUsers(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return userRepository.findByUsernameContaining(filter);
        }
        return userRepository.findAll();
    }

    public List<User> findUsersNonAdmins(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return userRepository.findByUsernameContainingAndRolesNotContaining(filter, Role.ADMIN);
        }
        return userRepository.findByRolesNotContaining(Role.ADMIN);
    }

    public User saveUser(User user, String newPassword) {
        User otherUserFromDb = userRepository.findByUsernameAndIdNot(user.getUsername(),user.getId());
        if (otherUserFromDb != null) {
            return null;
        }
        if (!StringUtils.isEmpty(newPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(user);
    }

    public User addNewUser(User user) {
        if (isUsernameExist(user.getUsername())) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        user.setActive(false);
        userRepository.save(user);
    }
}

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
import testtask.exptracker.exceptions.ForbiddenException;
import testtask.exptracker.exceptions.NotFoundException;
import testtask.exptracker.repository.UserRepository;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public User registerUser(User user) {
        if (isUsernameExist(user.getUsername())) {
            return null;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findUsers(User currentUser, String filter){
        if (currentUser.isAdmin()) {
            if (filter != null && !filter.isEmpty()) {
                return userRepository.findByUsernameContaining(filter);
            }
            return userRepository.findAll();
        } else {
            if (filter != null && !filter.isEmpty()) {
                return userRepository.findByUsernameContainingAndRolesNotContaining(filter, Role.ADMIN);
            }
            return userRepository.findByRolesNotContaining(Role.ADMIN);
        }
    }

    public User getAllowedUser(User currentUser, Long id) {
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            throw new NotFoundException();
        }
        User user = oUser.get();
        if (currentUser.isManager() && user.isAdmin()) {
            throw new ForbiddenException();
        }
        return user;
    }

    public EnumSet<Role> getAllowedRoles(User currentUser) {
        if (currentUser.isAdmin()) {
            return EnumSet.of(Role.USER, Role.MANAGER, Role.ADMIN );
        } else {
            return EnumSet.of(Role.USER, Role.MANAGER );
        }
    }

    public User saveUser(User currentUser, User user, String newPassword) {
        if (currentUser.isManager() && user.isAdmin()) {
            throw new ForbiddenException();
        }
        User otherUserFromDb = userRepository.findByUsernameAndIdNot(user.getUsername(),user.getId());
        if (otherUserFromDb != null) {
            return null;
        }
        if (!StringUtils.isEmpty(newPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.save(user);
    }

    public User addNewUser(User currentUser, User user) {
        if (isUsernameExist(user.getUsername())) {
            return null;
        }
        if (currentUser.isManager() && user.isAdmin()) {
            throw new ForbiddenException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void deleteUser(User currentUser, User user) {
        if (currentUser.isManager() && user.isAdmin()) {
            throw new ForbiddenException();
        }
        user.setActive(false);
        userRepository.save(user);
    }
}

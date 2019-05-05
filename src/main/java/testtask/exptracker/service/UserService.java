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

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    public List<User> findUsers(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return userRepository.findByUsernameContaining(filter);
        }
        return userRepository.findAll();
    }

    public void saveUser(User user, String newPassword) {
        if (!StringUtils.isEmpty(newPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        user.setActive(userRepository.getOne(user.getId()).isActive());
        user.getPassword();
        //user.setPassword(userRepository.getOne(user.getId()).getPassword());

        userRepository.save(user);
    }
}

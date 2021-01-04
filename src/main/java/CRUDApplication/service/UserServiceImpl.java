package CRUDApplication.service;

import CRUDApplication.dao.RoleDAO;
import CRUDApplication.dao.UserDAO;
import CRUDApplication.models.Role;
import CRUDApplication.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @Override
    @Transactional
    public boolean saveUser(User user) {
        if (userDAO.getUserByName(user.getUsername()) != null) {
            return false;
        }
        if (user.getRoles() == null) {
            user.setRoles(Collections.singleton(roleDAO.showById(1L)));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.saveUser(user);
        return true;

    }

    @Override
    @Transactional
    public void editUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.editUser(user);

    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userDAO.getUserById(id);
        userDAO.delete(user);

    }

    @Override
    public void setRole(User user, String userRole) {
        Set<Role> roles = new HashSet<>();
        if (userRole.toUpperCase().contains("ADMIN")) {
            roles.add(roleDAO.showById(2L));
        }
        if (userRole.toUpperCase().contains("USER")) {
            roles.add(roleDAO.showById(1L));
        }
        user.setRoles(roles);
    }

    @Override
    @Transactional
    public User getUserByName(String name) {
        return userDAO.getUserByName(name);
    }

}

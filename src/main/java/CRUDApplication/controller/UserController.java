package CRUDApplication.controller;

import CRUDApplication.models.User;
import CRUDApplication.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/user")
    public String getUserPage(ModelMap model, Authentication authentication){
        model.addAttribute("user", userService.getUserByName(authentication.getName()));
        return "user";
    }

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/admin/addUser")
    public String addUser(User user) {
        return "addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(@Valid User user, @RequestParam("role") String role) {
        userService.setRole(user, role);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        User user = new User();
        user.setId(id);
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @Valid User user,
                             @RequestParam("role") String role,
                             BindingResult result, Model model) {
        user.setId(id);
        userService.setRole(user, role);
        userService.editUser(user);
        return "redirect:/admin";
    }


    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userService.delete(id);

        return "redirect:/admin";
    }
}

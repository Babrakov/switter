package com.example.switter.controller;

import com.example.switter.domain.Role;
import com.example.switter.domain.User;
import com.example.switter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users",userRepository.findAll());
        return "user-list";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
//        User user = userRepository.findById(id).orElse(new User());
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "user-edit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String,String> form,
            @RequestParam("userId") User user
    ){
        user.setUsername(username);
        user.getRoles().clear();
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        for (String key: form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
        return "redirect:/user";
    }

}

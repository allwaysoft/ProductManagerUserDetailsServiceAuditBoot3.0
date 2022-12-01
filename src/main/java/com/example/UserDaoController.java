package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class UserDaoController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/userdao")
    public String listUsers(Model model) {
        List<User> listUsers = userDao.findUsersByUsername("admin");
        model.addAttribute("listUsers", listUsers);

        return "user/list_user";
    }

}

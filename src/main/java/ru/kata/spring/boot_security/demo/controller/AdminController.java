package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.filter.HiddenHttpMethodFilter;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.List;

@Controller
public class AdminController {

    private final UserServiceImp userService;
    private final RoleService roleService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Autowired
    public AdminController(UserServiceImp userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")  //Отображает список пользователей в административной панели
    public String showListUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        logger.info("Отображен список пользователей");
        return "admin";
    }

    @GetMapping("/admin/new")  //Отображает форму добавления нового пользователя
    public String showAddUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.listRoles());
        logger.info("Отображена форма добавления пользователя");
        return "addUser";
    }

    @PostMapping("/admin/add")  //Обрабатывает запрос на добавление нового пользователя
    public String addUser(@ModelAttribute("user") User user) {
        userService.add(user);
        logger.info("Добавлен новый пользователь: {}", user.getUsername());
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")  //Отображает форму редактирования пользователя по его ID
    public String showEditUserForm(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.listRoles());
        logger.info("Отображена форма редактирования пользователя с ID {}", id);
        return "editUser";
    }

    @PutMapping("/admin/update")  //Обрабатывает запрос на обновление информации о пользователе
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        logger.info("Обновлён пользователь с ID {}", user.getId());
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/delete")  //Обрабатывает запрос на удаление пользователя по его ID.
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        logger.info("Удалён пользователь с ID {}", id);
        return "redirect:/admin";
    }
}

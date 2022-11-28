package com.example;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

@Controller

public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/role/new")
    public String showNewRoleForm(Model model) {
        model.addAttribute("role", new Role());

        return "role/new_role";
    }

    @GetMapping("/role")
    public String listRoles(Model model) {
        List<Role> listRoles = roleRepository.findAll();
        model.addAttribute("listRoles", listRoles);

        return "role/list_role";
    }

    @GetMapping("/role/edit/{id}")
    public String editRole(@PathVariable("id") Long id, Model model) {
        Role role = roleRepository.findById(id).orElse(null);
        List<Permission> listPermissions = permissionRepository.findAll();
        model.addAttribute("role", role);
        model.addAttribute("listPermissions", listPermissions);
        return "role/edit_role";
    }

    @PostMapping("/role/save")
    public String saveRole(Role role) {
        roleRepository.save(role);

        return "redirect:/role";
    }

    @RequestMapping("/role/delete/{id}")
    public String deleteRole(@PathVariable(name = "id") Long id) {
        roleRepository.deleteById(id);

        return "redirect:/role";
    }

}

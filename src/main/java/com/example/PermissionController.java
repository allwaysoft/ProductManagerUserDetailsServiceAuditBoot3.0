package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    @RequestMapping("/permission")
    public String indexPermissions(Model model) {
        List<Permission> listPermissions = permissionRepository.findAll();
        model.addAttribute("listPermissions", listPermissions);

        return "permission/list_permission";
    }

    @RequestMapping("/permission/new")
    public String showNewPermissionForm(Model model) {
        Permission permission = new Permission();
        model.addAttribute("permission", permission);

        return "permission/new_permission";
    }

    @RequestMapping(value = "/permission/save", method = RequestMethod.POST)
    public String savePermission(@ModelAttribute("product") Permission permission) {
        permissionRepository.save(permission);

        return "redirect:/permission";
    }

    @RequestMapping("/permission/edit/{id}")
    public ModelAndView showEditPermissionForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("permission/edit_permission");

        Permission permission = permissionRepository.findById(id).orElse(null);
        mav.addObject("permission", permission);

        return mav;
    }

    @RequestMapping("/permission/delete/{id}")
    public String deletePermission(@PathVariable(name = "id") Long id) {
        permissionRepository.deleteById(id);

        return "redirect:/permission";
    }
}

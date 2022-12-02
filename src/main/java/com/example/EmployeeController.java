package com.example;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @GetMapping("/employees")
    public String getEmployees(@Param("keyword") String keyword, @PageableDefault(size = 10, sort = "id") Pageable pageable,
            Model model) {

        if (keyword == null || keyword.trim().isEmpty()) {
            Page<Employee> page = repository.findAll(pageable);
            List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
            if (sortOrders.size() > 0) {
                Sort.Order order = sortOrders.get(0);
                model.addAttribute("sortProperty", order.getProperty());
                model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
            }
            model.addAttribute("keyword", "");
            model.addAttribute("page", page);
        } else {
            Page<Employee> page = repository.search(keyword, pageable);
            List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
            if (sortOrders.size() > 0) {
                Sort.Order order = sortOrders.get(0);
                model.addAttribute("sortProperty", order.getProperty());
                model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
            }
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
        }

        return "employee-page";

    }

}

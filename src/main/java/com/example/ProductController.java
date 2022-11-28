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
public class ProductController {

    @Autowired
    private ProductService service;

    @RequestMapping("/product")
    public String viewHomePage(Model model) {
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);

        return "product/list_product";
    }

    @RequestMapping("/product/new")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);

        return "product/new_product";
    }

    @RequestMapping(value = "/product/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        service.save(product);

        return "redirect:/product";
    }

    @RequestMapping("/product/edit/{id}")
    public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("product/edit_product");

        Product product = service.get(id);
        mav.addObject("product", product);

        return mav;
    }

    @RequestMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        service.delete(id);

        return "redirect:/product";
    }
}

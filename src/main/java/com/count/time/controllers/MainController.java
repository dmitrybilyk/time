package com.count.time.controllers;

import com.count.time.model.Customer;
import com.count.time.model.EndUser;
import com.count.time.services.CustomerService;
import com.count.time.services.EndUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private CustomerService customerService;
    private EndUserService endUserService;

    public MainController(CustomerService customerService, EndUserService endUserService) {
        this.customerService = customerService;
        this.endUserService = endUserService;
    }

    @RequestMapping(value = {"/"})
    public String index() {
        //to add some endUsers and Customers if db is empty;
        Customer anticafeCustomer = new Customer("My Anticafe", "0504759475");
        customerService.save(anticafeCustomer);
        endUserService.save(new EndUser("Some End User", 3000, "any description", anticafeCustomer));
        endUserService.save(new EndUser("Some End User2", 5000, "any description2", anticafeCustomer));
        return "index";
    }
}

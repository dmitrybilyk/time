package com.count.time.controllers;

import com.count.time.model.Role;
import com.count.time.model.User;
import com.count.time.repositories.RoleRepository;
import com.count.time.repositories.UserRepository;
import com.count.time.services.CustomerService;
import com.count.time.services.EndUserService;
import com.count.time.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MainController {

    private CustomerService customerService;
    private EndUserService endUserService;
    private ServiceService serviceService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public MainController(CustomerService customerService, EndUserService endUserService, ServiceService serviceService,
                          UserRepository userRepository, RoleRepository roleRepository) {
        this.customerService = customerService;
        this.endUserService = endUserService;
        this.serviceService = serviceService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @RequestMapping(value = {"/", "/index"})
    public ModelAndView index() {
        Role userRole = roleRepository.findByName("USER");
        List<User> endUsers = userRepository.findAllByRolesContaining(userRole);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("endUsers", endUsers);
//        modelAndView.setViewName("index");
        modelAndView.setViewName("scheduler");
//        //to add some endUsers and Customers if db is empty;
//        Customer anticafeCustomer1 = new Customer("My Anticafe", "0504759475");
//        Customer anticafeCustomer2 = new Customer("My Anticafe", "0504759475");
//        customerService.save(anticafeCustomer1);
//        customerService.save(anticafeCustomer2);
//        EndUser newEndUser1 = new EndUser("Some End User", 3000, "any description", anticafeCustomer1);
//        endUserService.save(newEndUser1);
//        EndUser newEndUser2 = new EndUser("Some End User2", 5000, "any description2", anticafeCustomer1);
//        endUserService.save(newEndUser2);
//
//        Service service1 = new Service("Service 1", Arrays.asList(newEndUser1, newEndUser2));
//        Service service2 = new Service("Service 2", Collections.singletonList(newEndUser1));
//        serviceService.save(service1);
//        serviceService.save(service2);

        return modelAndView;
    }
}

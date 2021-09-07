package com.count.time.controllers;

import com.count.time.model.Customer;
import com.count.time.model.Role;
import com.count.time.model.User;
import com.count.time.repositories.CustomerRepository;
import com.count.time.repositories.RoleRepository;
import com.count.time.repositories.UserRepository;
import com.count.time.services.CustomerService;
import com.count.time.services.EndUserService;
import com.count.time.services.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Slf4j
//@Controller
public class MainController {

    private CustomerService customerService;
    private EndUserService endUserService;
    private ServiceService serviceService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;

    public MainController(CustomerService customerService, EndUserService endUserService, ServiceService serviceService,
                          UserRepository userRepository, RoleRepository roleRepository,
                          CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.endUserService = endUserService;
        this.serviceService = serviceService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
    }
//
//    @RequestMapping(value = {"/", "/index"})
//    public ModelAndView index() {
//        Role userRole = roleRepository.findByName("USER");
//        ModelAndView modelAndView = new ModelAndView();
//        List<User> endUsers = Collections.emptyList();
//        if (userRole != null) {
//            endUsers = userRepository.findAllByRolesContaining(userRole);
//        }
//        modelAndView.addObject("endUsers", endUsers);
////        modelAndView.setViewName("index");
//        modelAndView.setViewName("scheduler");
////        //to add some endUsers and Customers if db is empty;
////        Customer anticafeCustomer1 = new Customer("My Anticafe", "0504759475");
////        Customer anticafeCustomer2 = new Customer("My Anticafe", "0504759475");
////        customerService.save(anticafeCustomer1);
////        customerService.save(anticafeCustomer2);
////        EndUser newEndUser1 = new EndUser("Some End User", 3000, "any description", anticafeCustomer1);
////        endUserService.save(newEndUser1);
////        EndUser newEndUser2 = new EndUser("Some End User2", 5000, "any description2", anticafeCustomer1);
////        endUserService.save(newEndUser2);
////
////        Service service1 = new Service("Service 1", Arrays.asList(newEndUser1, newEndUser2));
////        Service service2 = new Service("Service 2", Collections.singletonList(newEndUser1));
////        serviceService.save(service1);
////        serviceService.save(service2);
//
//        return modelAndView;
//    }

    @GetMapping(path = "/")
    public String index() {
        return "external";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }

    @GetMapping(path = "/customers")
    public String customers(Principal principal, Model model) {
        addCustomers();
        log.info(((RefreshableKeycloakSecurityContext)((KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getCredentials()).getRefreshToken()
        );
        Iterable<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
//        model.addAttribute("username", principal.getName());
        model.addAttribute("username", "current user");
        return "customers";
    }

    // add customers for demonstration
    public void addCustomers() {

        Customer customer1 = new Customer();
        customer1.setAddress("1111 foo blvd");
        customer1.setName("Foo Industries");
        customer1.setServiceRendered("Important services");
        customerRepository.save(customer1);
    }
}

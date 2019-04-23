package com.chixing.controller;

import com.chixing.entity.Customer;
import com.chixing.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/get/{id}")
    public Customer get(@PathVariable("id") int customerId){
        return customerService.getById(customerId);
    }
}

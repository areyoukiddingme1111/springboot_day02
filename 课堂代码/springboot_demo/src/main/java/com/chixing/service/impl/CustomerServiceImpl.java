package com.chixing.service.impl;

import com.chixing.dao.CustomerDao;
import com.chixing.entity.Customer;
import com.chixing.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private  CustomerDao customerDao;


    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }



    @Override
    public Customer getById(int custId) {
        return customerDao.selectByPrimaryKey(custId);
    }
}

package com.chixing.dao;

import com.chixing.dao.example.CustomerExample;
import com.chixing.entity.Customer;
import org.springframework.stereotype.Repository;

/**
 * CustomerDao继承基类
 */
@Repository
 
public interface CustomerDao extends MyBatisBaseDao<Customer, Integer, CustomerExample> {
}
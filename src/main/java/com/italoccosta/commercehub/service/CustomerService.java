package com.italoccosta.commercehub.service;

import com.italoccosta.commercehub.dto.CustomerRequest;
import com.italoccosta.commercehub.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer registerCustomer(CustomerRequest customerRequest);
    Customer findCustomerById(UUID customerId);
    Customer findCustomerByEmail(String email);
    List<Customer> findAllCustomers();
    void updateCustomerName(UUID customerId, String newName);
    void updateCustomerPassword(UUID customerId, String newPassword);
    void deleteCustomer(UUID customerId);

}

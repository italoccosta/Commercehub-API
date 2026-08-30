package com.italoccosta.commercehub.service.impl;

import com.italoccosta.commercehub.dto.CustomerRequest;
import com.italoccosta.commercehub.entity.Customer;
import com.italoccosta.commercehub.exceptions.CustomerNotFoundException;
import com.italoccosta.commercehub.repository.CustomerRepository;
import com.italoccosta.commercehub.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer registerCustomer(CustomerRequest customerRequest) {

        Customer newCustomer = toCustomerEntity(customerRequest);
        repository.save(newCustomer);

        return newCustomer;

    }

    @Override
    @Transactional(readOnly = true)
    public Customer findCustomerById(UUID customerId) {

        return repository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found."));

    }

    @Override
    @Transactional(readOnly = true)
    public Customer findCustomerByEmail(String email) {

        return repository.findByEmail(email)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found."));

    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAllCustomers() {
        return repository.findAll();
    }

    @Override
    public void updateCustomerName(UUID customerId, String newName) {
        Customer customer = findCustomerById(customerId);
        customer.updateName(newName);
    }

    @Override
    public void updateCustomerPassword(UUID customerId, String newPassword) {
        Customer customer = findCustomerById(customerId);
        customer.updatePassword(newPassword);
    }

    @Override
    public void deleteCustomer(UUID customerId) {
        Customer customer = findCustomerById(customerId);
        repository.delete(customer);
    }

    private Customer toCustomerEntity(CustomerRequest customerRequest) {
        return new Customer(
                customerRequest.name(),
                customerRequest.email(),
                customerRequest.password()
        );
    }

}

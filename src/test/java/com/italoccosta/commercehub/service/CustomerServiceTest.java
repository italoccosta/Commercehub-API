package com.italoccosta.commercehub.service;

import com.italoccosta.commercehub.dto.CustomerRequest;
import com.italoccosta.commercehub.entity.Customer;
import com.italoccosta.commercehub.exceptions.CustomerNotFoundException;
import com.italoccosta.commercehub.repository.CustomerRepository;
import com.italoccosta.commercehub.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "Luiz Costa",
                "Luiz@email.com",
                "teste123"
        );
    }

    @Test
    void shouldRegisterNewCustomerSuccessfully() {

        CustomerRequest customerRequest = new CustomerRequest(
                "Luiz Costa",
                "Luiz@email.com",
                "teste123"
        );

        when(repository.save(any(Customer.class)))
                .thenReturn(customer);

        Customer result = service.registerCustomer(customerRequest);

        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getEmail(), result.getEmail());
        assertEquals(customer.getPassword(), result.getPassword());

        verify(repository).save(result);

    }

    @Test
    void shouldReturnCustomerByIdSuccessfully() {

        UUID customerId = UUID.randomUUID();

        when(repository.findById(customerId))
                .thenReturn(Optional.of(customer));

        Customer result = service.findCustomerById(customerId);

        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getEmail(), result.getEmail());
        assertEquals(customer.getPassword(), result.getPassword());

        verify(repository).findById(customerId);

    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenFindingById() {

        UUID customerId = UUID.randomUUID();
        when(repository.findById(customerId))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> service.findCustomerById(customerId));

        verify(repository).findById(customerId);

    }

    @Test
    void shouldReturnCustomerByEmailSuccessfully() {

        String customerEmail = "luiz@email.com";
        when(repository.findByEmail(customerEmail))
                .thenReturn(Optional.of(customer));

        Customer result = service.findCustomerByEmail(customerEmail);

        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getEmail(), result.getEmail());
        assertEquals(customer.getPassword(), result.getPassword());

        verify(repository).findByEmail(customerEmail);

    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenFindingByEmail() {

        String customerEmail = "luiz@email.com";
        when(repository.findByEmail(customerEmail))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> service.findCustomerByEmail(customerEmail));

        verify(repository).findByEmail(customerEmail);

    }

    @Test
    void shouldReturnAllCustomersSuccessfully() {

        Customer customer1 = new Customer(
                "Ana Silva",
                "ana@email.com",
                "teste123"
        );
        Customer customer2 = new Customer(
                "Maria Santos",
                "maria@email.com",
                "teste456"
        );

        when(repository.findAll())
                .thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> result = service.findAllCustomers();

        assertEquals(2, result.size());
        assertTrue(result.contains(customer1));
        assertTrue(result.contains(customer2));

        verify(repository).findAll();

    }

    @Test
    void shouldReturnEmptyListWhenFindingAllCustomers() {

        when(repository.findAll())
                .thenReturn(Collections.emptyList());

        List<Customer> result = service.findAllCustomers();

        assertTrue(result.isEmpty());
        verify(repository).findAll();

    }

    @Test
    void shouldUpdateCustomerNameSuccessfully() {

        UUID customerId = UUID.randomUUID();
        when(repository.findById(customerId))
                .thenReturn(Optional.of(customer));

        String newName = "Luiz Silva";

        service.updateCustomerName(customerId, newName);

        assertEquals(newName, customer.getName());
        verify(repository).findById(customerId);

    }

    @Test
    void shouldUpdateCustomerPasswordSuccessfully() {

        UUID customerId = UUID.randomUUID();
        when(repository.findById(customerId))
                .thenReturn(Optional.of(customer));

        String newPassword = "teste456";

        service.updateCustomerPassword(customerId, newPassword);

        assertEquals(newPassword, customer.getPassword());
        verify(repository).findById(customerId);

    }

    @Test
    void shouldDeleteCustomerSuccessfully() {

        UUID customerId = UUID.randomUUID();
        when(repository.findById(customerId))
                .thenReturn(Optional.of(customer));

        service.deleteCustomer(customerId);

        verify(repository).findById(customerId);
        verify(repository).delete(customer);

    }

}

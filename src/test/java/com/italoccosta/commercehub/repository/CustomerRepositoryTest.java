package com.italoccosta.commercehub.repository;

import com.italoccosta.commercehub.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;


    @Test
    void shouldReturnCustomerByEmail() {

        Customer customer = new Customer(
                "Carla Silva",
                "carla@email.com",
                "teste123"
        );

        repository.save(customer);

        Optional<Customer> result = repository.findByEmail(customer.getEmail());

        assertTrue(result.isPresent());
        assertEquals(customer.getId(), result.get().getId());

    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {

        String email = "invalid@email.com";
        Optional<Customer> result = repository.findByEmail(email);

        assertTrue(result.isEmpty());
    }
}

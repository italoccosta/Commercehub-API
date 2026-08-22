package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidCustomerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void shouldCreateCustomerSuccessfully(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        assertNotNull(customer);
        assertEquals("Carla Santos", customer.getName());
        assertEquals("carla@email.com", customer.getEmail());
        assertEquals("teste123", customer.getPassword());
    }

    @Test
    void shouldRejectNullName(){

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            null,
                            "carla@email.com",
                            "teste123"
                    );
                });

    }

    @Test
    void shouldRejectBlankName(){

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            "",
                            "carla@email.com",
                            "teste123"
                    );
                });

    }

    @Test
    void shouldRejectNameLongerThan100(){

        String invalidName = "Este é um nome de cliente propositalmente " +
                "muito longo para validar o limite máximo permitido pela entidade.";

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            invalidName,
                            "carla@email.com",
                            "teste123"
                    );
                });

    }

    @Test
    void shouldRejectNullEmail(){

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            "Carla Santos",
                            null,
                            "teste123"
                    );
                });

    }

    @Test
    void shouldRejectBlankEmail(){

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            "Carla Santos",
                            "",
                            "teste123"
                    );
                });

    }

    @Test
    void shouldRejectNullPassword(){

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            "Carla Santos",
                            "carla@email.com",
                            null
                    );
                });

    }

    @Test
    void shouldRejectBlankPassword(){

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            "Carla Santos",
                            "carla@email.com",
                            ""
                    );
                });
    }

    @Test
    void shouldRejectPasswordShorterThan8(){

        assertThrows(InvalidCustomerException.class,
                () -> {
                    new Customer(
                            "Carla Santos",
                            "carla@email.com",
                            "teste12"
                    );
                });

    }

    @Test
    void shouldUpdateNameSuccessfully(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        customer.updateName("Andre Silva");

        assertEquals("Andre Silva", customer.getName());

    }

    @Test
    void shouldRejectNullNameToUpdate(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );


        assertThrows(InvalidCustomerException.class,
                () -> customer.updateName(null));

        assertEquals("Carla Santos", customer.getName());
    }

    @Test
    void shouldRejectBlankNameToUpdate(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        assertThrows(InvalidCustomerException.class,
                () -> customer.updateName(""));

        assertEquals("Carla Santos", customer.getName());

    }

    @Test
    void shouldRejectNameLongerThan100ToUpdate(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        String invalidName = "Este é um nome de cliente propositalmente " +
                "muito longo para validar o limite máximo permitido pela entidade.";

        assertThrows(InvalidCustomerException.class,
                () -> customer.updateName(invalidName));

        assertEquals("Carla Santos", customer.getName());
    }

    @Test
    void shouldUpdatePasswordSuccessfully(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        customer.updatePassword("senha123");

        assertEquals("senha123", customer.getPassword());
    }

    @Test
    void shouldRejectNullPasswordToUpdate(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        assertThrows(InvalidCustomerException.class,
                () -> customer.updatePassword(null));

        assertEquals("teste123", customer.getPassword());

    }

    @Test
    void shouldRejectBlankPasswordToUpdate(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        assertThrows(InvalidCustomerException.class,
                () -> customer.updatePassword(""));

        assertEquals("teste123", customer.getPassword());
    }

    @Test
    void shouldRejectPasswordShorterThan8ToUpdate(){

        Customer customer = new Customer(
                "Carla Santos",
                "carla@email.com",
                "teste123"
        );

        assertThrows(InvalidCustomerException.class,
                () -> customer.updatePassword("senha12"));

        assertEquals("teste123", customer.getPassword());
    }

}

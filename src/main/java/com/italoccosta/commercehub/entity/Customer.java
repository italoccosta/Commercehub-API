package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidCustomerException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String email;
    private String password;

    public Customer(String name, String email, String password) {
        validateCustomer(name, email, password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateName(String name) {
        if(name == null || name.isBlank() || name.length() > 100) {
            throw new InvalidCustomerException("Invalid name");
        }
        this.name = name;
    }

    public void updatePassword(String password) {
        if(password == null || password.isBlank() || password.length() < 8) {
            throw new InvalidCustomerException("Invalid password");
        }
        this.password = password;
    }

    private void validateCustomer(String name, String email, String password) {

        if(name == null){
            throw new InvalidCustomerException("The name can't be null");
        }
        if(email == null){
            throw new InvalidCustomerException("The email can't be null");
        }
        if(password == null){
            throw new InvalidCustomerException("The password can't be null");
        }
        if(name.isBlank()){
            throw new InvalidCustomerException("The name can't be blank");
        }
        if(email.isBlank()){
            throw new InvalidCustomerException("The email can't be blank");
        }
        if(password.isBlank()){
            throw new InvalidCustomerException("The password can't be blank");
        }
        if(name.length() > 100){
            throw new InvalidCustomerException("The name can't be longer than 100 characters");
        }
        if(password.length() < 8){
            throw new InvalidCustomerException("The password must be at least 8 characters");
        }
    }

}

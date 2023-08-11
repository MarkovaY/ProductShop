package com.example.productshop.models.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

//    Users have an id, first name (optional) and last name (at least 3 characters) and age (optional).
//    Users should have many products sold and many products bought.
//    Users should have many friends (i.e. users).

    private String firstName;
    private String lastName;
    private Integer age;
    private Set<User> friends;
    private Set<Product> productsForSale;

    public User() {
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @ManyToMany
    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    @OneToMany(mappedBy = "seller")
    public Set<Product> getProductsForSale() {
        return productsForSale;
    }

    public void setProductsForSale(Set<Product> productsForSale) {
        this.productsForSale = productsForSale;
    }
}

package com.example.productshop.repositories;

import com.example.productshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User AS u WHERE (SELECT COUNT(p) FROM Product AS p WHERE p.seller.id = u.id) > 0 ORDER BY u.lastName, u.firstName")
    List<User> findAllUsersWithAtLeastOneProductSoldOrderByLastNameThenByFirstName();
}

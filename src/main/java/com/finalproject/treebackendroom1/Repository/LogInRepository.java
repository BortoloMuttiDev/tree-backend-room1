package com.finalproject.treebackendroom1.Repository;

import com.finalproject.treebackendroom1.entity.LogIn;
import com.finalproject.treebackendroom1.entity.Utente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface LogInRepository extends CrudRepository<LogIn, Long> {

    Optional<LogIn> findByUsername(String username);
    Optional<LogIn> findByCookie(UUID cookie);
}

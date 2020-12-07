package com.finalproject.treebackendroom1.Repository;


import com.finalproject.treebackendroom1.entity.Evento;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventoRepository extends CrudRepository<Evento, UUID> {
    Optional<Evento> findByName(String name);
}

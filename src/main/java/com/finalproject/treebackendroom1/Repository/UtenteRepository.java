package com.finalproject.treebackendroom1.Repository;

import com.finalproject.treebackendroom1.entity.Evento;
import com.finalproject.treebackendroom1.entity.Utente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UtenteRepository extends CrudRepository<Utente, String> {


}

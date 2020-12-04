package com.finalproject.treebackendroom1.Controller;

import com.finalproject.treebackendroom1.Repository.LogInRepository;
import com.finalproject.treebackendroom1.Repository.UtenteRepository;
import com.finalproject.treebackendroom1.Service.PasswordSecurity;
import com.finalproject.treebackendroom1.entity.LogIn;
import com.finalproject.treebackendroom1.entity.Utente;
import com.finalproject.treebackendroom1.view.UtenteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UtenteController {
    @Autowired
    LogInRepository logInRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordSecurity passwordSecurity;


    @PostMapping("/user")
    public UtenteView signUpUser(@RequestBody UtenteView utenteView, HttpServletRequest request, HttpServletResponse response){
        //Registra un utente alla piattaforma
        System.out.println(utenteView.getUsername());
        Optional<Utente> utenteEntity = utenteRepository.findById(utenteView.getUsername());
        if(utenteEntity.isEmpty()){
            String password = passwordSecurity.passwordSecurity(utenteView.getPassword());
            Utente utenteToAdd = new Utente(utenteView.getUsername(), utenteView.getName(),
                    utenteView.getSurname(), utenteView.getBirthDate(), utenteView.getGender(), password);
            System.out.println(utenteView.getUsername() + " " + utenteView.getName());
            //utenteView.setPassword(password);
            utenteRepository.save(utenteToAdd);
            response.setStatus(201);
            UUID idCookie = UUID.randomUUID();
            LogIn logInUtente = new LogIn(utenteToAdd.getUsername(), idCookie);
            Cookie unaCookie = new Cookie("idCookie", idCookie.toString());
            response.addCookie(unaCookie);
            logInRepository.save(logInUtente);
            return utenteView;
        }


        response.setStatus(400);
        return utenteView;
    }

    @GetMapping("/login?username={username}&password={password}")
    public UtenteView logInUtente(@PathVariable String username, @PathVariable String password, HttpServletResponse response
            /*, @CookieValue(value = "idCookie") String request*/){
        Optional<Utente> utenteRepo = utenteRepository.findById(username);
        if(utenteRepo.isPresent()){
            String passwordPostSecurity = passwordSecurity.passwordSecurity(password);
            if(passwordPostSecurity.equals(utenteRepo.get().getPassword())){
                Optional<LogIn> logInUtente = logInRepository.findByUsername(username);
                if(logInUtente.isPresent()){
                   


                }

            }


        }


        return null;
    }

}

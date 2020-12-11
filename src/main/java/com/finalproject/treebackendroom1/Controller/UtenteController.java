package com.finalproject.treebackendroom1.Controller;

import com.finalproject.treebackendroom1.Repository.LogInRepository;
import com.finalproject.treebackendroom1.Repository.UtenteRepository;
import com.finalproject.treebackendroom1.Service.PasswordSecurity;
import com.finalproject.treebackendroom1.Service.UtenteService;
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

    @Autowired
    UtenteService utenteService;


    @PostMapping("/user")
    public UtenteView signUpUser(@RequestBody UtenteView utenteView, HttpServletRequest request, HttpServletResponse response){
        UtenteView utenteViewToReturn = utenteService.signUpUser(utenteView, request, response);

        Optional<LogIn> logInUtente = logInRepository.findByUsername(utenteView.getUsername());

        if(logInUtente.isPresent()){
            response.setStatus(201);
            Cookie unaCookie = new Cookie("idCookie", logInUtente.get().getCookie().toString());
            response.addCookie(unaCookie);

        }
        return utenteViewToReturn;
    }

    @GetMapping("/login")
    public UtenteView logIn(@RequestParam String username, @RequestParam String password, HttpServletResponse response){
        return utenteService.logIn(username, password, response);
    }
}

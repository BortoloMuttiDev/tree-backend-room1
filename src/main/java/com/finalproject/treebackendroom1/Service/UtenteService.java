package com.finalproject.treebackendroom1.Service;

import com.finalproject.treebackendroom1.Repository.LogInRepository;
import com.finalproject.treebackendroom1.Repository.UtenteRepository;
import com.finalproject.treebackendroom1.entity.LogIn;
import com.finalproject.treebackendroom1.entity.Utente;
import com.finalproject.treebackendroom1.view.UtenteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    LogInRepository logInRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordSecurity passwordSecurity;


    //Metodi

    public UtenteView signUpUser(UtenteView utenteView, HttpServletRequest request, HttpServletResponse response){
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

    public UtenteView logIn(String username, String password, HttpServletResponse response){
        Optional<Utente> utenteRepo = utenteRepository.findById(username);
        if(utenteRepo.isPresent()){
            System.out.println("Utente presente");
            String passwordPostSecurity = passwordSecurity.passwordSecurity(password);
            if(passwordPostSecurity.equals(utenteRepo.get().getPassword())){
                System.out.println("Password uguale");
                Optional<LogIn> logInUtente = logInRepository.findByUsername(username);
                if(logInUtente.isPresent()){
                    System.out.println("loginpresente");
                    Cookie unaCookie  = new Cookie("idCookie", logInUtente.get().getCookie().toString());
                    response.addCookie(unaCookie);
                    response.setStatus(200);
                    UtenteView utenteView = new UtenteView(utenteRepo.get().getUsername(), utenteRepo.get().getName(),
                            utenteRepo.get().getSurname(), utenteRepo.get().getBirthDate(), utenteRepo.get().getGender(),
                            passwordPostSecurity);

                    return utenteView;



                }

            }


        }

        response.setStatus(404);
        return null;
    }


}

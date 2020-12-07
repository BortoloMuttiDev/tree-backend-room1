package com.finalproject.treebackendroom1.Controller;

import com.finalproject.treebackendroom1.Repository.EventoRepository;
import com.finalproject.treebackendroom1.Repository.LogInRepository;
import com.finalproject.treebackendroom1.Repository.UtenteRepository;
import com.finalproject.treebackendroom1.Service.PasswordSecurity;
import com.finalproject.treebackendroom1.entity.Evento;
import com.finalproject.treebackendroom1.entity.LogIn;
import com.finalproject.treebackendroom1.entity.Utente;
import com.finalproject.treebackendroom1.view.EventoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class EventoController {
    //Attributi

    @Autowired
    LogInRepository logInRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordSecurity passwordSecurity;

    @Autowired
    EventoRepository eventoRepository;


    //Metodi

    @GetMapping("/events")
    public List<EventoView> getActiveEvents(HttpServletRequest request, HttpServletResponse response,
                                            @CookieValue(value = "idCookie") String requestCookie){
        List<EventoView> listaEventi = new ArrayList<>();
        Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
        if(logInUtente.isPresent()){
            List<Evento> eventiRepo = (List<Evento>) eventoRepository.findAll();
            if(eventiRepo.isEmpty()){
                System.out.println("No hay eventos");
            } else{
                for(Evento evento : eventiRepo){
                    if(evento.getNumUtentiRegistrati() < evento.getCapacity()){
                        EventoView eventoView = new EventoView(/*evento.getEventid(),*/ evento.getName(), (Timestamp) evento.getDate(),
                                evento.getPlace(), evento.getCapacity());
                        eventoView.setEventid(evento.getEventid());
                        listaEventi.add(eventoView);
                    }
                }

                response.setStatus(200);
                Cookie unaCookie = new Cookie("idCookie", requestCookie);
                response.addCookie(unaCookie);
                return listaEventi;
            }

        }

        return null;
    }

    @PostMapping("/join/{eventid}")
    public EventoView joinEvent(@PathVariable("eventid") String eventid, HttpServletRequest request, HttpServletResponse response,
                                @CookieValue(value = "idCookie") String requestCookie){

        System.out.println("id evento: " + eventid);
        Optional<Evento> eventoToJoin = eventoRepository.findById(UUID.fromString(eventid));
        if(eventoToJoin.isPresent()){
            if(eventoToJoin.get().getNumUtentiRegistrati() < eventoToJoin.get().getCapacity()){
                Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
                    if(logInUtente.isPresent()){
                        Optional<Utente> utenteToJoin = utenteRepository.findById(logInUtente.get().getUsername());
                        if(utenteToJoin.isPresent()){

                            eventoToJoin.get().aumentaNumeroPartecipanti();
                            utenteToJoin.get().addEventoPartecipazione(eventoToJoin.get());
                            utenteRepository.save(utenteToJoin.get());
                            eventoRepository.save(eventoToJoin.get());
                            response.setStatus(201);
                            Cookie unaCookie = new Cookie("idCookie", requestCookie);
                            response.addCookie(unaCookie);
                            return new EventoView(/*eventoToJoin.get().getEventid(),*/eventoToJoin.get().getName(),
                                    (Timestamp) eventoToJoin.get().getDate(), eventoToJoin.get().getPlace(),
                                    eventoToJoin.get().getCapacity());

                        }

                    }

            }
        }


        return null;
    }

    @PostMapping("/unjoin/{eventid}")
    public EventoView unjoinEvent(@PathParam("eventid") String eventid, HttpServletRequest request, HttpServletResponse response,
                                @CookieValue(value = "idCookie") String requestCookie){
        Optional<Evento> eventoToUnJoin = eventoRepository.findById(UUID.fromString(eventid));
        if(eventoToUnJoin.isPresent()){

            Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
            if(logInUtente.isPresent()){
                Optional<Utente> utenteToUnJoin = utenteRepository.findById(logInUtente.get().getUsername());
                if(utenteToUnJoin.isPresent()){
                    if(utenteToUnJoin.get().getEventiPartecipazione().contains(eventoToUnJoin.get())){
                        utenteToUnJoin.get().removeEventoPartecipazione(eventoToUnJoin.get());
                        eventoToUnJoin.get().disminuisciNumeroParticipanti();

                        utenteRepository.save(utenteToUnJoin.get());
                        eventoRepository.save(eventoToUnJoin.get());

                        response.setStatus(201);
                        Cookie unaCookie = new Cookie("idCookie", requestCookie);
                        response.addCookie(unaCookie);
                        return new EventoView(/*eventoToUnJoin.get().getEventid(),*/ eventoToUnJoin.get().getName(),
                                (Timestamp) eventoToUnJoin.get().getDate(), eventoToUnJoin.get().getPlace(),
                                eventoToUnJoin.get().getCapacity());

                    }

                }

            }

        }

        return null;
    }

    @PostMapping("/event")
    public EventoView createEvent(@RequestBody EventoView eventoView, HttpServletRequest request, HttpServletResponse response,
                                  @CookieValue(value = "idCookie") String requestCookie){
        Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
        if(logInUtente.isPresent()){
            Optional<Utente> utenteCreatore = utenteRepository.findById(logInUtente.get().getUsername());
            if(utenteCreatore.isPresent()){
                if(eventoRepository.findByName(eventoView.getName()).isEmpty()){

                    Evento eventoToCreate = new Evento(eventoView.getName(),eventoView.getDate(),
                            eventoView.getPlace(),eventoView.getCapacity(), utenteCreatore.get());
                    eventoView.setEventid(eventoToCreate.getEventid());

                    utenteCreatore.get().addEventoCreato(eventoToCreate);
                    utenteCreatore.get().addEventoPartecipazione(eventoToCreate);
                    eventoToCreate.aumentaNumeroPartecipanti();

                    eventoRepository.save(eventoToCreate);
                    utenteRepository.save(utenteCreatore.get());


                    response.setStatus(201);
                    Cookie unaCookie = new Cookie("idCookie", requestCookie);
                    response.addCookie(unaCookie);
                    return eventoView;
                }

            }
        }

        return null;
    }



}
package com.finalproject.treebackendroom1.Service;

import com.finalproject.treebackendroom1.Repository.EventoRepository;
import com.finalproject.treebackendroom1.Repository.LogInRepository;
import com.finalproject.treebackendroom1.Repository.UtenteRepository;
import com.finalproject.treebackendroom1.entity.Evento;
import com.finalproject.treebackendroom1.entity.LogIn;
import com.finalproject.treebackendroom1.entity.Utente;
import com.finalproject.treebackendroom1.view.EventoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class EventoService {
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

    public List<EventoView> getActiveEvents(HttpServletRequest request, HttpServletResponse response,
                                           String requestCookie){
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
        response.setStatus(404);
        return null;
    }


    public EventoView joinEvent(String eventid, HttpServletRequest request, HttpServletResponse response,
                                String requestCookie){

        System.out.println("id evento: " + eventid);
        Optional<Evento> eventoToJoin = eventoRepository.findById(UUID.fromString(eventid));
        if(eventoToJoin.isPresent()){
            if(eventoToJoin.get().getNumUtentiRegistrati() < eventoToJoin.get().getCapacity()){
                Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
                if(logInUtente.isPresent()){
                    Optional<Utente> utenteToJoin = utenteRepository.findById(logInUtente.get().getUsername());
                    if(utenteToJoin.isPresent()){

                        if(!utenteToJoin.get().getEventiPartecipazione().contains(eventoToJoin.get())){
                            eventoToJoin.get().aumentaNumeroPartecipanti();
                            utenteToJoin.get().addEventoPartecipazione(eventoToJoin.get());
                            utenteRepository.save(utenteToJoin.get());
                            eventoRepository.save(eventoToJoin.get());
                            response.setStatus(201);
                            Cookie unaCookie = new Cookie("idCookie", requestCookie);
                            response.addCookie(unaCookie);

                            EventoView eventoView = new EventoView(/*eventoToJoin.get().getEventid(),*/eventoToJoin.get().getName(),
                                    (Timestamp) eventoToJoin.get().getDate(), eventoToJoin.get().getPlace(),
                                    eventoToJoin.get().getCapacity());
                            eventoView.setEventid(eventoToJoin.get().getEventid());
                            eventoView.setJoined(true);
                            return eventoView;

                        }
                    }

                }

            }
        }

        response.setStatus(404);
        return null;
    }


    public EventoView unjoinEvent(String eventid, HttpServletRequest request, HttpServletResponse response,
                                  String requestCookie){
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

                        EventoView eventoView = new EventoView(/*eventoToUnJoin.get().getEventid(),*/ eventoToUnJoin.get().getName(),
                                (Timestamp) eventoToUnJoin.get().getDate(), eventoToUnJoin.get().getPlace(),
                                eventoToUnJoin.get().getCapacity());
                        eventoView.setEventid(eventoToUnJoin.get().getEventid());
                        eventoView.setJoined(true);

                        return eventoView;

                    }

                }

            }

        }
        response.setStatus(404);
        return null;
    }


    public EventoView createEvent(EventoView eventoView, HttpServletRequest request, HttpServletResponse response,
                                   String requestCookie){
        Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
        if(logInUtente.isPresent()){
            Optional<Utente> utenteCreatore = utenteRepository.findById(logInUtente.get().getUsername());
            if(utenteCreatore.isPresent()){

                UUID eventoid = UUID.randomUUID();
                Evento eventoToCreate = new Evento(eventoid, eventoView.getName(),eventoView.getDate(),
                        eventoView.getPlace(),eventoView.getCapacity(), utenteCreatore.get());
                eventoView.setEventid(eventoid);
                eventoView.setJoined(true);
                eventoView.setOwned(true);

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
        response.setStatus(404);
        return null;
    }


    public EventoView getEventDetails (String eventid,HttpServletRequest request, HttpServletResponse response,
                                       String requestCookie){

        Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
        if(logInUtente.isPresent()){

            Optional<Evento> eventoToFind = eventoRepository.findById(UUID.fromString(eventid));
            if (eventoToFind.isPresent()){

                response.setStatus(200);
                EventoView eventoView = new EventoView(eventoToFind.get().getName(),(Timestamp) eventoToFind.get().getDate(), eventoToFind.get().getPlace(),
                        (eventoToFind.get().getCapacity()-eventoToFind.get().getNumUtentiRegistrati()));
                eventoView.setEventid(eventoToFind.get().getEventid());

                if(utenteRepository.findById(logInUtente.get().getUsername()).isPresent()){
                    if(utenteRepository.findById(logInUtente.get().getUsername()).get().getEventiCreati().
                            contains(eventoToFind.get())){
                        eventoView.setOwned(true);
                    }
                    if(utenteRepository.findById(logInUtente.get().getUsername()).get().getEventiPartecipazione().
                            contains(eventoToFind.get())){
                        eventoView.setJoined(true);
                    }
                }
                return eventoView;
            }

        }
        response.setStatus(404);

        return null;
    }


    public List<EventoView> getUserEvents(HttpServletRequest request, HttpServletResponse response,
                                           String requestCookie){
        LocalDate localDate = LocalDate.now();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date dataDiOggi  = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());


        List<EventoView> listaEventi = new ArrayList<>();
        Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));
        if(logInUtente.isPresent()){
            Optional<Utente> utente = utenteRepository.findById(logInUtente.get().getUsername());
            if(utente.isPresent()){
                for(Evento evento : utente.get().getEventiPartecipazione()){

                    if (evento.getDate().after(dataDiOggi)){

                        EventoView eventoView = new EventoView(evento.getName(), (Timestamp) evento.getDate(),
                                evento.getPlace(), evento.getCapacity());
                        eventoView.setEventid(evento.getEventid());
                        eventoView.setJoined(true);
                        if(utente.get().getEventiCreati().contains(evento)){
                            eventoView.setOwned(true);
                        }
                        listaEventi.add(eventoView);
                    }

                }

                response.setStatus(200);
                Cookie unaCookie = new Cookie("idCookie", requestCookie);
                response.addCookie(unaCookie);
                return listaEventi;
            }


        }
        response.setStatus(404);

        return null;
    }

    public EventoView cancelEvent (String eventid, HttpServletRequest request, HttpServletResponse response,
                                   String requestCookie){
        Optional<Evento> eventoToDelete = eventoRepository.findById(UUID.fromString(eventid));
        if(eventoToDelete.isPresent()){
            Optional<LogIn> logInUtente = logInRepository.findByCookie(UUID.fromString(requestCookie));

            if(logInUtente.isPresent()){
                Optional<Utente> utente = utenteRepository.findById(logInUtente.get().getUsername());
                if(utente.isPresent()){
                    if(utente.get().getEventiCreati().contains(eventoToDelete.get())){

                        utente.get().getEventiCreati().remove(eventoToDelete.get());
                        EventoView eventoViewToReturn = new EventoView(eventoToDelete.get().getName(), (Timestamp) eventoToDelete.get().getDate(),
                                eventoToDelete.get().getPlace(), eventoToDelete.get().getCapacity());
                        eventoViewToReturn.setEventid(eventoToDelete.get().getEventid());

                        for(Utente utenteRepo : utenteRepository.findAll()){
                            utenteRepo.getEventiPartecipazione().remove(eventoToDelete.get());
                        }

                        eventoRepository.delete(eventoToDelete.get());
                        utenteRepository.save(utente.get());

                        response.setStatus(200);
                        Cookie unaCookie = new Cookie("idCookie", requestCookie);
                        response.addCookie(unaCookie);
                        return eventoViewToReturn;

                    }
                }
            }



        }

        return null;
    }

}

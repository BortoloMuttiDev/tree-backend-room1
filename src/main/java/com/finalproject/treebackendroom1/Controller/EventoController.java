package com.finalproject.treebackendroom1.Controller;

import com.finalproject.treebackendroom1.Repository.EventoRepository;
import com.finalproject.treebackendroom1.Repository.LogInRepository;
import com.finalproject.treebackendroom1.Repository.UtenteRepository;
import com.finalproject.treebackendroom1.Service.EventoService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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

    @Autowired
    EventoService eventoService;

    //Metodi

    @GetMapping("/events")
    public List<EventoView> getActiveEvents(HttpServletRequest request, HttpServletResponse response,
                                            @CookieValue(value = "idCookie") String requestCookie){

        return eventoService.getActiveEvents(request, response, requestCookie);
    }

    @PostMapping("/join/{eventid}")
    public EventoView joinEvent(@PathVariable("eventid") String eventid, HttpServletRequest request, HttpServletResponse response,
                                @CookieValue(value = "idCookie") String requestCookie){

        return eventoService.joinEvent(eventid, request, response, requestCookie);
    }

    @PostMapping("/unjoin/{eventid}")
    public EventoView unjoinEvent(@PathVariable String eventid, HttpServletRequest request, HttpServletResponse response,
                                @CookieValue(value = "idCookie") String requestCookie){

        return eventoService.unjoinEvent(eventid, request, response, requestCookie);
    }

    @PostMapping("/event")
    public EventoView createEvent(@RequestBody EventoView eventoView, HttpServletRequest request, HttpServletResponse response,
                                  @CookieValue(value = "idCookie") String requestCookie){

        return eventoService.createEvent(eventoView, request, response, requestCookie);
    }

    @GetMapping("/event/{eventid}")
    public EventoView getEventDetails (@PathVariable ("eventid") String eventid,HttpServletRequest request, HttpServletResponse response,
                                       @CookieValue(value = "idCookie") String requestCookie){

        return eventoService.getEventDetails(eventid, request, response, requestCookie);
    }

    @GetMapping("/user/events")
    public List<EventoView> getUserEvents(HttpServletRequest request, HttpServletResponse response,
                                          @CookieValue(value = "idCookie") String requestCookie){

        return eventoService.getUserEvents(request, response, requestCookie);
    }
    @DeleteMapping("/event/{eventid}")
    public EventoView cancelEvent (@PathVariable("eventid") String eventid, HttpServletRequest request, HttpServletResponse response,
                                   @CookieValue(value = "idCookie") String requestCookie){

        return eventoService.cancelEvent(eventid, request, response, requestCookie);
    }
}

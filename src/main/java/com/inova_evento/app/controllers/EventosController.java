package com.inova_evento.app.controllers;

import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.services.EventosService;
import com.inova_evento.app.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventosController {

    @Autowired
    private EventosService eventosService;

    @PostMapping("/create/{Id}")
    public ResponseEntity<EventosEntity> create(@RequestBody EventosEntity evento, @PathVariable Long userId) {
        try {
            EventosEntity savedEvento = eventosService.save(evento, userId);
            return new ResponseEntity<>(savedEvento, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventosEntity> update(@RequestBody EventosEntity evento, @PathVariable Long id) {
        try {
            EventosEntity updatedEvento = eventosService.update(evento, id);
            return new ResponseEntity<>(updatedEvento, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<EventosEntity> findById(@PathVariable Long id) {
        try {
            EventosEntity evento = eventosService.findById(id);
            return new ResponseEntity<>(evento, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/findall")
    public ResponseEntity<List<EventosEntity>> findAll() {
        try {
            List<EventosEntity> eventos = eventosService.findALl();
            return new ResponseEntity<>(eventos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

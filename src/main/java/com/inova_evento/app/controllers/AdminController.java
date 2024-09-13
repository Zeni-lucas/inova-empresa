package com.inova_evento.app.controllers;

import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.services.EventosService;
import com.inova_evento.app.services.UsuariosService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EventosService eventosService;

    @Autowired
    private UsuariosService usuariosService;

    @PostMapping("eventos/create/{Id}")
    public ResponseEntity<EventosEntity> create(@Valid @RequestBody EventosEntity evento, @PathVariable Long userId) {
            EventosEntity savedEvento = eventosService.save(evento, userId);
            return new ResponseEntity<>(savedEvento, HttpStatus.CREATED);
    }

    @PutMapping("eventos/update/{id}")
    public ResponseEntity<EventosEntity> update(@Valid @RequestBody EventosEntity evento,  @PathVariable Long id) {
            EventosEntity updatedEvento = eventosService.update(evento, id);
            return new ResponseEntity<>(updatedEvento, HttpStatus.OK);
    }

    @GetMapping("eventos/findbyid/{id}")
    public ResponseEntity<EventosEntity> findById(@PathVariable Long id) {
            EventosEntity evento = eventosService.findById(id);
            return new ResponseEntity<>(evento, HttpStatus.OK);
    }
    @GetMapping("eventos/findall")
    public ResponseEntity<List<EventosEntity>> findAll() {
            List<EventosEntity> eventos = eventosService.findALl();
            return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    @PutMapping("/user/roles/{id}")
    public ResponseEntity<UsuariosEntity> updateRole(@PathVariable Long id, @RequestBody UsuariosEntity user){
        var usuario = usuariosService.setRole(id, user);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}

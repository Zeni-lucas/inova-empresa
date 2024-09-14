package com.inova_evento.app.controllers;

import com.inova_evento.app.entities.JuradosEntity;
import com.inova_evento.app.services.JuradosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/jurados")
public class JuradosController {

    @Autowired
    private JuradosService juradosService;

    @PostMapping("/save/{id}")
    public ResponseEntity<JuradosEntity> save(@PathVariable Long id, @RequestBody JuradosEntity jurado){
        return new ResponseEntity<>(juradosService.save(id, jurado), HttpStatus.CREATED);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<JuradosEntity> findById(@PathVariable Long id){
        return new ResponseEntity<>(juradosService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<JuradosEntity>> findAll(){
        return new ResponseEntity<>(juradosService.findAll(), HttpStatus.OK);
    }
}

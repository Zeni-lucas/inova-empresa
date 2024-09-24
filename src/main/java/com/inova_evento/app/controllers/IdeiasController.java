package com.inova_evento.app.controllers;

import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.services.IdeiasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideias")
public class IdeiasController {

    @Autowired
    IdeiasService ideiasService;

    @PostMapping("/save")
    public ResponseEntity<IdeiasEntity> save(@Valid @RequestBody IdeiasEntity ideia){
        var obj = ideiasService.save(ideia);
        return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<IdeiasEntity> findById(@PathVariable Long id){
        return new ResponseEntity<>(ideiasService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findall")
    public ResponseEntity<List<IdeiasEntity>> findAll(){
        return new ResponseEntity<>(ideiasService.findAll(), HttpStatus.OK);
    }

}

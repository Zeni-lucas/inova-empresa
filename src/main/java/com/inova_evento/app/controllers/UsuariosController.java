package com.inova_evento.app.controllers;

import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.services.UsuariosService;
import com.inova_evento.app.services.VotosPopularesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    @Autowired
    UsuariosService usuariosService;

    @Autowired
    private VotosPopularesService votosPopularesService;

    @PostMapping("/save")
    public ResponseEntity<UsuariosEntity> save(@Valid @RequestBody UsuariosEntity user){
        usuariosService.save(user);
        return new ResponseEntity<>(user, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<UsuariosEntity> findById(@PathVariable Long id){
        var user = usuariosService.findById(id);
        return new ResponseEntity<>(user , HttpStatus.OK);
    }

    @GetMapping("/findall")
    public ResponseEntity<List<UsuariosEntity>> findAll(){
        var user = usuariosService.findALl();
        return new ResponseEntity<>(user , HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UsuariosEntity> update( @PathVariable Long id, @Valid @RequestBody UsuariosEntity usuario ){
        var user = usuariosService.update(usuario, id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        usuariosService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/votar")
    public ResponseEntity<String> votar(@RequestParam Long usuarioId, @RequestParam Long ideiaId) {

        String resultado = votosPopularesService.votar(usuarioId, ideiaId);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

}

package com.api.url.controllers;

import com.api.url.error.MiExceptions;
import com.api.url.models.LoginDto;
import com.api.url.models.UrlDto;
import com.api.url.services.UrlServicio;
import com.api.url.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UrlControlador {

    @Autowired
    private UrlServicio urlServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/crear")
    public ResponseEntity<?> shortenUrl(@RequestParam String larga, @RequestParam String nombre, @RequestParam Long id) {
        try{
            String url = urlServicio.crearUrl(larga, nombre, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("url", url));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Mensaje", "Error en el servidor","Error", ex.getMessage()));
        }
    }

    @PostMapping("/lista")
    public ResponseEntity<?> index(@RequestParam Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(urlServicio.listarUrl(id));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Mensaje", "No se encotro ninguna lista","Error",ex.getMessage()));
        }
    }

    @PostMapping("/eliminar")
    public ResponseEntity<?> eliminarUrl(@RequestParam Long id,@RequestParam String corta){
        try{
            urlServicio.eliminarUrl(id, corta);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("Mensaje", "Se a eliminado el recurso"));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", "No se pudo eliminar el recurso"));
        }
    }


}

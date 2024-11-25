package com.api.url.controllers;

import com.api.url.services.AdminServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private AdminServicio adminServicio;

    @PostMapping("/lista")
    public ResponseEntity<?> lista(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(adminServicio.listaUsuario());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }

    @PostMapping("/eliminar")
    public ResponseEntity<?> eliminar(@RequestParam Long id){
        try{
            adminServicio.eliminar(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("Mensaje", "El usuario fue elminado"));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }

}

package com.api.url.controllers;

import com.api.url.error.MiExceptions;
import com.api.url.models.LoginDto;
import com.api.url.models.RecuperarPasswordDto;
import com.api.url.models.Url;
import com.api.url.models.UsuarioDto;
import com.api.url.services.UrlServicio;
import com.api.url.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class IndexControlador {

    @Autowired
    private UrlServicio urlServicio;

    @Autowired
    public UsuarioServicio usuarioServicio;

    @PostMapping("/registrar")
    public ResponseEntity<?> registro(@RequestBody UsuarioDto usuarioDto){
        try{
            usuarioServicio.crearUsuario(usuarioDto.getNombre(), usuarioDto.getEmail(),usuarioDto.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje","registrado","permisos",false));
        }catch (MiExceptions ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje",ex.getMessage(),"permisos",false));
        }
    }

    @GetMapping("/{corta}")
    public RedirectView redirectToOriginalUrl(@PathVariable String corta) {
        Url url = urlServicio.ObtenerUrlOriginal(corta);
        return new RedirectView(url.getLarga());
    }

    @GetMapping("/verificacion/{code}")
    public RedirectView verify(@PathVariable String code) {
        try {
            usuarioServicio.verificarEmail(code);
            return new RedirectView("https://camaleones.netlify.app/util/verificado");
        } catch (Exception e) {
            return new RedirectView("https://camaleones.netlify.app/util/no-funciona");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(usuarioServicio.validarUsuario(loginDto));
        }catch (MiExceptions ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of( "error",ex.getMessage()));
        }
    }

    @PostMapping("/recuperacion")
    public ResponseEntity<?> recuperacion(@RequestParam String email){
        try{
            usuarioServicio.recuperarPassword(email);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("mensaje",true));
        }catch (MiExceptions ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/recuperar")
    private ResponseEntity<?> recuperar(@RequestBody RecuperarPasswordDto recuperarPasswordDto){
        try{
            usuarioServicio.cambiarPassword(recuperarPasswordDto.getCodigo(), recuperarPasswordDto.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("mensaje", true));
        }catch (MiExceptions ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", true, "mensaje", ex.getMessage()));
        }

    }
}

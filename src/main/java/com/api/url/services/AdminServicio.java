package com.api.url.services;

import com.api.url.models.Usuario;
import com.api.url.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public List<Usuario> listaUsuario(){
        return usuarioRepositorio.findAll();
    }

    public void eliminar(Long id){
        usuarioRepositorio.deleteById(id);
    }

}

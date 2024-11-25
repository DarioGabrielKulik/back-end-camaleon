package com.api.url.security;

import com.api.url.models.Usuario;
import com.api.url.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceIm implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> response = usuarioRepositorio.findByEmail(email);

        if (response == null){
            throw new UsernameNotFoundException("El usuario" + response + " no se encontro" );
        }

        Usuario u = response.get();
        List<GrantedAuthority> permisos = new ArrayList<>();

        if (response.get().getPermisos()) {
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + u.getRol().toString());
            permisos.add(p);
        }else {
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_NOT_VERIFIED");
            permisos.add(p);
        }

        return new User(u.getEmail(), u.getPassword(), permisos);
    }


}

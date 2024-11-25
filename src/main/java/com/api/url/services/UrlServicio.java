package com.api.url.services;

import com.api.url.models.Url;
import com.api.url.models.Usuario;
import com.api.url.repository.UrlRepositorio;
import com.api.url.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlServicio {

    @Autowired
    private UrlRepositorio urlRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;

    @Transactional
    public String crearUrl(String larga, String nombre, Long id){
        Optional<Usuario> response = usuarioRepositorio.findById(id);
        if(response.isPresent()){
            Usuario usuario = response.get();
            Url url = new Url();

            String shortUrl = generarUrlCorta();

            url.setLarga(larga);
            url.setCorta(shortUrl);
            url.setNombre(nombre);
            usuario.getUrl().add(url);
            usuarioRepositorio.save(usuario);
            return shortUrl;
        }
        return null;
    }

    public String generarUrlCorta(){
        Random random = new Random();
        StringBuilder shortUrl;
        do {
            shortUrl = new StringBuilder();
            for (int i = 0; i < SHORT_URL_LENGTH; i++) {
                shortUrl.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
        } while (urlRepositorio.existsById(shortUrl.toString()));

        return shortUrl.toString();
    }

    public Url ObtenerUrlOriginal(String urlCorta) {
        Url url = urlRepositorio.findById(urlCorta)
                .orElseThrow(() -> new RuntimeException("URL no encontrada"));
        url.setVisitas(url.getVisitas() + 1);
        return urlRepositorio.save(url);
    }

    public List<Url> listarUrl(Long id){
       Optional<Usuario> response = usuarioRepositorio.findById(id);

       if(response.isPresent()){

           Usuario usuario = response.get();
           List<Url> url = usuario.getUrl();
           return url;
       }

       return null;
    }

    @Transactional
    public void eliminarUrl(Long id, String corta){
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        Optional<Url> url = urlRepositorio.findById(corta);
        usuario.ifPresent( c -> {
            c.getUrl().remove(url.orElseThrow());
            usuarioRepositorio.save(c);
        });

    }
}

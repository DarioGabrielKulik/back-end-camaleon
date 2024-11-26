package com.api.url.services;

import com.api.url.error.MiExceptions;
import com.api.url.models.LoginDto;
import com.api.url.models.Usuario;
import com.api.url.models.UsuarioRespons;
import com.api.url.repository.UsuarioRepositorio;
import com.api.url.roles.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void crearUsuario(String nombre, String email, String password) throws MiExceptions {
        validacion(nombre,email,password);
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);

        String codigo= String.format("%06d", new Random().nextInt(999999));
        usuario.setCodigo(codigo);


        usuarioRepositorio.save(usuario);


        String mensaje = "Gracias por registrarte!\n\n" +
                "Para verificar tu cuenta, ingresa a:\n" +
                "https://camaleon.onrender.com/util/verificacion/" + codigo + "\n\n" +
                "Si no creaste esta cuenta, puedes ignorar este mensaje.";

        enviarMail(usuario.getEmail(), "Verifica tu cuenta", mensaje);
    }

    public UsuarioRespons validarUsuario(LoginDto loginDto) throws MiExceptions {
        validacionEmail(loginDto.getEmail(), loginDto.getPassword());
        Optional<Usuario> response = usuarioRepositorio.findByEmail(loginDto.getEmail());
        Usuario usuario;
        if(response.isPresent()){
           usuario  = response.get();
           UsuarioRespons usuarioRespons = new UsuarioRespons(usuario.getId(), usuario.getNombre(), usuario.getPermisos(),usuario.getRol().toString());
           return usuarioRespons;
        }
        return null;
    }

    public void enviarMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);
        mailSender.send(message);
    }

    @Transactional
    public void verificarEmail(String codigo){
        Optional<Usuario> response = usuarioRepositorio.findByCodigo(codigo);

        Usuario user = response.get();
        user.setCodigo(null);
        user.setPermisos(true);
        usuarioRepositorio.save(user);
    }

    public void validacion(String nombre, String email, String password) throws MiExceptions {
        Optional<Usuario> response = usuarioRepositorio.findByEmail(email);

        if(response.isPresent()){
            throw new MiExceptions("El usuario ya esta registrado");
        }

        if(nombre.isEmpty() || nombre == null){
            throw new MiExceptions("El nombre no puede estar vacio");
        }
        if(password.isEmpty() || password == null){
            throw new MiExceptions("El password no puede estar vacio");
        }
        if(email.isEmpty() || email == null){
            throw new MiExceptions("El email no puede estar vacio");
        }
    }

    public void validacionEmail(String email, String password) throws MiExceptions{
        Optional<Usuario> response = usuarioRepositorio.findByEmail(email);
        if(response.isEmpty()){
            throw new MiExceptions("El usuario no existe");
        }
        if(!response.get().getPermisos()){
            throw new MiExceptions("No estas verificado");
        }
        if(!passwordEncoder.matches(password, response.get().getPassword())){
            throw new MiExceptions("Contraseña incorrecta");
        }

    }

    @Transactional
    public void recuperarPassword(String email) throws MiExceptions {
        validacionRecuperacioContrasena(email);
        Optional<Usuario> response = usuarioRepositorio.findByEmail(email);
        Usuario usuario = response.get();
        String codigo= String.format("%06d", new Random().nextInt(999999));
        usuario.setCodigo(codigo);

        usuarioRepositorio.save(usuario);


        String mensaje = "Ya casi estas!\n\n" +
                "Tu código de verificación es: " + codigo + "\n\n" +
                "Para cambiar tu contraseña, ingresa a:\n" +
                "https://camaleones.netlify.app/util/cambio-password" + "\n\n" +
                "Si no creaste esta cuenta, puedes ignorar este mensaje.";

        enviarMail(usuario.getEmail(), "Cambio de contraseña", mensaje);
    }

    @Transactional
    public void cambiarPassword(String codigo, String password) throws MiExceptions {
        validarPasswordEmail(codigo, password);

        Usuario usuario = usuarioRepositorio.findByCodigo(codigo).get();

        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setCodigo(null);

        usuarioRepositorio.save(usuario);
    }

    public void validacionRecuperacioContrasena(String email) throws MiExceptions {
        Optional<Usuario> response = usuarioRepositorio.findByEmail(email);

        if(!response.isPresent()){
            throw new MiExceptions("No hay ningun usuario con este mail");
        }
    }

    public void validarPasswordEmail(String codigo, String password) throws MiExceptions {

        Optional<Usuario> response = usuarioRepositorio.findByCodigo(codigo);

        if (response.get().getCodigo() == codigo){
            throw new MiExceptions("El usuario con este codigo no existe");
        }
        if (password.isEmpty() || password == null){
            throw new MiExceptions("El password no puede estar vacio");
        }
    }
}

package com.api.url.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRespons {

    private Long id;
    private String nombre;
    private boolean permisos;
    private String roles;
}

package com.api.url.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecuperarPasswordDto {

    private String codigo;
    private String password;
    private String password2;

}

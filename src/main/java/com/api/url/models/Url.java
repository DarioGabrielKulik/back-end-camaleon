package com.api.url.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "urls")
public class Url {

    @Id
    private String Corta;
    private String larga;
    private String nombre;
    private int visitas;

}

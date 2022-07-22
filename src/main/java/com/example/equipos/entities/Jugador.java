package com.example.equipos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="jugadores")
@Data
@NoArgsConstructor @AllArgsConstructor @ToString
public class Jugador implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable=true, name = "nombre")
    private String nombre;

    @Column(nullable=false, name = "idnac")
    private Long idnac;

}

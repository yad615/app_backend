package com.tecsup.orientatec.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "preguntas_simu")
@Data
public class PreguntaSimu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpreguntas_simu;

    @Column(name = "pregunta_simu", nullable = false)
    private String preguntaSimu;

    @Column(name = "materia", nullable = false)
    private String materia;

    @OneToMany
    @JoinColumn(name = "id_preguntas_simu", referencedColumnName = "idpreguntas_simu")
    private List<OpcionSimu> opciones;
}

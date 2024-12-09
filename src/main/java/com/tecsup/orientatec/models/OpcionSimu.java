package com.tecsup.orientatec.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "opciones_simu")
@Data
public class OpcionSimu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idopciones_simu;

    private String opciones;

    @Column(name = "id_preguntas_simu")
    private Long idPreguntasSimu;
}

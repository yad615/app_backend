package com.tecsup.orientatec.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "respuestas_simu")
@Data
public class RespuestaSimu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idrespuestas_simu;

    @Column(name = "respuesta_simu")
    private String respuestaSimu;

    @Column(name = "preguntas_simu_id")
    private Long preguntasSimuId;
}

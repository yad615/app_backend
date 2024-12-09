package com.tecsup.orientatec.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulacros")
@Data
public class Simulacro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idsimulacros;

    @Column(name = "usuarios_id")
    private Long usuariosId;

    @Column(name = "puntaje_obtenido")
    private Double puntajeObtenido;

    private Integer duracion;

    @Column(name = "fecha_simulacro")
    private LocalDateTime fechaSimulacro;

    @Column(name = "preguntas_simu_id")
    private Long preguntasSimuId;

    @Column(name = "respuesta_simu_id")
    private Long respuestaSimuId;

    @Column(name = "opciones_simu_id")
    private Long opcionesSimuId;
}

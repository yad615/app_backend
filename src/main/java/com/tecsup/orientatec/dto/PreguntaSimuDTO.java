package com.tecsup.orientatec.dto;

import lombok.Data;
import java.util.List;

@Data
public class PreguntaSimuDTO {
    private Long idPregunta;
    private String pregunta;
    private List<String> opciones;
    private String respuestaCorrecta;
}

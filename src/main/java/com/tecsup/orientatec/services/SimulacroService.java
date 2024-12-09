package com.tecsup.orientatec.services;

import com.tecsup.orientatec.dto.PreguntaSimuDTO;
import com.tecsup.orientatec.dto.RespuestaUsuarioDTO;
import com.tecsup.orientatec.dto.RespuestaDetalleDTO;
import com.tecsup.orientatec.dto.ResultadoSimulacroDTO;
import com.tecsup.orientatec.models.OpcionSimu;
import com.tecsup.orientatec.models.PreguntaSimu;
import com.tecsup.orientatec.models.RespuestaSimu;
import com.tecsup.orientatec.repositories.OpcionSimuRepository;
import com.tecsup.orientatec.repositories.PreguntaSimuRepository;
import com.tecsup.orientatec.repositories.RespuestaSimuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulacroService {

    @Autowired
    private PreguntaSimuRepository preguntaSimuRepository;

    @Autowired
    private OpcionSimuRepository opcionSimuRepository;

    @Autowired
    private RespuestaSimuRepository respuestaSimuRepository;

    public List<PreguntaSimuDTO> obtenerSimulacro() {
        List<PreguntaSimuDTO> simulacro = new ArrayList<>();
        List<PreguntaSimu> preguntas = preguntaSimuRepository.findAll();

        for (PreguntaSimu pregunta : preguntas) {
            List<OpcionSimu> opciones = opcionSimuRepository.findByIdPreguntasSimu(pregunta.getIdpreguntas_simu());
            RespuestaSimu respuesta = respuestaSimuRepository.findByPreguntasSimuId(pregunta.getIdpreguntas_simu());

            PreguntaSimuDTO preguntaSimuDTO = new PreguntaSimuDTO();
            preguntaSimuDTO.setIdPregunta(pregunta.getIdpreguntas_simu());
            preguntaSimuDTO.setPregunta(pregunta.getPreguntaSimu());

            List<String> opcionesTexto = new ArrayList<>();
            for (OpcionSimu opcion : opciones) {
                opcionesTexto.add(opcion.getOpciones());
            }
            preguntaSimuDTO.setOpciones(opcionesTexto);
            preguntaSimuDTO.setRespuestaCorrecta(respuesta.getRespuestaSimu());

            simulacro.add(preguntaSimuDTO);
        }

        return simulacro;
    }

    public ResultadoSimulacroDTO contarRespuestas(List<RespuestaUsuarioDTO> respuestasUsuario) {
        int aciertos = 0;
        int errores = 0;
        List<RespuestaDetalleDTO> preguntasIncorrectas = new ArrayList<>();

        for (RespuestaUsuarioDTO respuestaUsuario : respuestasUsuario) {
            RespuestaSimu respuestaCorrecta = respuestaSimuRepository.findByPreguntasSimuId(respuestaUsuario.getIdPregunta());

            if (respuestaCorrecta != null) {
                if (respuestaCorrecta.getRespuestaSimu().equals(respuestaUsuario.getRespuestaSeleccionada())) {
                    aciertos++;
                } else {
                    errores++;
                    RespuestaDetalleDTO detalle = new RespuestaDetalleDTO();
                    detalle.setIdPregunta(respuestaUsuario.getIdPregunta());
                    detalle.setPregunta(preguntaSimuRepository.findById(respuestaUsuario.getIdPregunta()).get().getPreguntaSimu());
                    detalle.setRespuestaUsuario(respuestaUsuario.getRespuestaSeleccionada());
                    detalle.setRespuestaCorrecta(respuestaCorrecta.getRespuestaSimu());
                    preguntasIncorrectas.add(detalle);
                }
            }
        }

        ResultadoSimulacroDTO resultado = new ResultadoSimulacroDTO();
        resultado.setAciertos(aciertos);
        resultado.setErrores(errores);
        resultado.setPreguntasIncorrectas(preguntasIncorrectas);

        return resultado;
    }

}

package com.tecsup.orientatec.rest_controllers;

import com.tecsup.orientatec.dto.PreguntaSimuDTO;
import com.tecsup.orientatec.dto.RespuestaUsuarioDTO;
import com.tecsup.orientatec.dto.ResultadoSimulacroDTO;
import com.tecsup.orientatec.services.SimulacroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulacro")

public class SimulacroController {

    @Autowired
    private SimulacroService simulacroService;

    @GetMapping
    public List<PreguntaSimuDTO> obtenerSimulacro() {
        return simulacroService.obtenerSimulacro();
    }

    @PostMapping("/calificar")
    public ResponseEntity<ResultadoSimulacroDTO> calificarSimulacro(@RequestBody List<RespuestaUsuarioDTO> respuestasUsuario) {
        ResultadoSimulacroDTO resultado = simulacroService.contarRespuestas(respuestasUsuario);
        return ResponseEntity.ok(resultado);
    }
}

package com.tecsup.orientatec.dto;

import java.util.List;

public class ResultadoSimulacroDTO {
    private int aciertos;
    private int errores;
    private List<RespuestaDetalleDTO> preguntasIncorrectas;

    public int getAciertos() {
        return aciertos;
    }

    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }

    public int getErrores() {
        return errores;
    }

    public void setErrores(int errores) {
        this.errores = errores;
    }

    public List<RespuestaDetalleDTO> getPreguntasIncorrectas() {
        return preguntasIncorrectas;
    }

    public void setPreguntasIncorrectas(List<RespuestaDetalleDTO> preguntasIncorrectas) {
        this.preguntasIncorrectas = preguntasIncorrectas;
    }
}

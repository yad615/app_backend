package com.tecsup.orientatec.repositories;

import com.tecsup.orientatec.models.RespuestaSimu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaSimuRepository extends JpaRepository<RespuestaSimu, Long> {
    RespuestaSimu findByPreguntasSimuId(Long preguntasSimuId);
}

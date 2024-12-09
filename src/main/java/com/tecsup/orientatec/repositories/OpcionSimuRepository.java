package com.tecsup.orientatec.repositories;

import com.tecsup.orientatec.models.OpcionSimu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionSimuRepository extends JpaRepository<OpcionSimu, Long> {
    List<OpcionSimu> findByIdPreguntasSimu(Long idPreguntasSimu);
}

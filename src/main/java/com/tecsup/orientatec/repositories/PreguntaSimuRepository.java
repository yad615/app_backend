package com.tecsup.orientatec.repositories;

import com.tecsup.orientatec.models.PreguntaSimu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreguntaSimuRepository extends JpaRepository<PreguntaSimu, Long> {
}

package com.tecsup.orientatec.repositories;

import com.tecsup.orientatec.models.Simulacro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulacroRepository extends JpaRepository<Simulacro, Long> {
}

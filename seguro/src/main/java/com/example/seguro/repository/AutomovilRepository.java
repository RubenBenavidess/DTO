package com.example.seguro.repository;

import com.example.seguro.model.Automovil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomovilRepository extends JpaRepository<Automovil, Long> {
}

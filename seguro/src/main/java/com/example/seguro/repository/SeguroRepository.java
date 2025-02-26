package com.example.seguro.repository;

import com.example.seguro.model.Seguro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeguroRepository extends JpaRepository<Seguro, Long> {
}

package com.example.seguro.controller;

import com.example.seguro.dto.AutomovilDTO;
import com.example.seguro.service.AutomovilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automoviles")
public class AutomovilController {

    private final AutomovilService automovilService;

    public AutomovilController(AutomovilService automovilService) {
        this.automovilService = automovilService;
    }

    /**
     * Obtener todos los automóviles
     */
    @GetMapping
    public ResponseEntity<List<AutomovilDTO>> obtenerTodos() {
        List<AutomovilDTO> automoviles = automovilService.obtenerTodos();
        return ResponseEntity.ok(automoviles);
    }

    /**
     * Obtener automóvil por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutomovilDTO> obtenerPorId(@PathVariable Long id) {
        AutomovilDTO automovil = automovilService.obtenerPorId(id);
        return ResponseEntity.ok(automovil);
    }

    /**
     * Crear un nuevo automóvil
     */
    @PostMapping
    public ResponseEntity<AutomovilDTO> crear(@RequestBody AutomovilDTO automovilDTO) {
        AutomovilDTO nuevoAutomovil = automovilService.crear(automovilDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAutomovil);
    }

    /**
     * Modificar un automóvil existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutomovilDTO> modificar(@PathVariable Long id, @RequestBody AutomovilDTO automovilDTO) {
        AutomovilDTO automovilModificado = automovilService.modificar(id, automovilDTO);
        return ResponseEntity.ok(automovilModificado);
    }

    /**
     * Eliminar un automóvil por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        automovilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}


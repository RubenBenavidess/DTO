package com.example.seguro.controller;

import com.example.seguro.dto.SeguroDTO;
import com.example.seguro.service.SeguroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguros")
public class SeguroController {

    private final SeguroService seguroService;

    public SeguroController(SeguroService seguroService) {
        this.seguroService = seguroService;
    }

    /**
     * Generar un seguro para un automóvil específico
     */
    @PostMapping("/generar/{automovilId}")
    public ResponseEntity<SeguroDTO> generarSeguro(@PathVariable Long automovilId) {
        SeguroDTO nuevoSeguro = seguroService.generarSeguro(automovilId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoSeguro);
    }

    /**
     * Obtener todos los seguros
     */
    @GetMapping
    public ResponseEntity<List<SeguroDTO>> obtenerTodos() {
        List<SeguroDTO> seguros = seguroService.obtenerTodos();
        return ResponseEntity.ok(seguros);
    }

    /**
     * Obtener seguro por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SeguroDTO> obtenerPorId(@PathVariable Long id) {
        SeguroDTO seguro = seguroService.obtenerPorId(id);
        return ResponseEntity.ok(seguro);
    }

    /**
     * Crear un nuevo seguro
     */
    @PostMapping
    public ResponseEntity<SeguroDTO> crear(@RequestBody SeguroDTO seguroDTO) {
        SeguroDTO nuevoSeguro = seguroService.crear(seguroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoSeguro);
    }

    /**
     * Modificar un seguro existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeguroDTO> modificar(@PathVariable Long id, @RequestBody SeguroDTO seguroDTO) {
        SeguroDTO seguroModificado = seguroService.modificar(id, seguroDTO);
        return ResponseEntity.ok(seguroModificado);
    }

    /**
     * Eliminar un seguro por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        seguroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.seguro.controller;

import com.example.seguro.dto.PropietarioDTO;
import com.example.seguro.service.PropietarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propietarios")
public class PropietarioController {

    private final PropietarioService propietarioService;

    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }

    /**
     * Obtener todos los propietarios
     */
    @GetMapping
    public ResponseEntity<List<PropietarioDTO>> obtenerTodos() {
        List<PropietarioDTO> propietarios = propietarioService.obtenerTodos();
        return ResponseEntity.ok(propietarios);
    }

    /**
     * Obtener propietario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropietarioDTO> obtenerPorId(@PathVariable Long id) {
        PropietarioDTO propietario = propietarioService.obtenerPorId(id);
        return ResponseEntity.ok(propietario);
    }

    /**
     * Crear un nuevo propietario
     */
    @PostMapping
    public ResponseEntity<PropietarioDTO> crear(@RequestBody PropietarioDTO propietarioDTO) {
        PropietarioDTO nuevoPropietario = propietarioService.crear(propietarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPropietario);
    }

    /**
     * Modificar un propietario existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<PropietarioDTO> modificar(@PathVariable Long id, @RequestBody PropietarioDTO propietarioDTO) {
        PropietarioDTO propietarioModificado = propietarioService.modificar(id, propietarioDTO);
        return ResponseEntity.ok(propietarioModificado);
    }

    /**
     * Eliminar un propietario por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        propietarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}


package com.example.seguro.service;

import com.example.seguro.dto.AutomovilDTO;
import com.example.seguro.model.Automovil;
import com.example.seguro.model.Propietario;
import com.example.seguro.repository.AutomovilRepository;
import com.example.seguro.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AutomovilService {
    private final AutomovilRepository automovilRepository;
    private final PropietarioRepository propietarioRepository;

    // Constructor con ambos repositorios
    public AutomovilService(AutomovilRepository automovilRepository, PropietarioRepository propietarioRepository) {
        this.automovilRepository = automovilRepository;
        this.propietarioRepository = propietarioRepository;  // Inyectamos correctamente el repositorio de propietarios
    }

    /**
     * Obtener todos los automóviles
     */
    public List<AutomovilDTO> obtenerTodos() {
        List<Automovil> automoviles = (List<Automovil>) automovilRepository.findAll(); // Convertir Iterable a List
        return automoviles.stream()
                .map(automovil -> new AutomovilDTO(
                        automovil.getAccidentes(),
                        automovil.getId(),
                        automovil.getModelo(),
                        automovil.getValor(),


                        automovil.getPropietario().getId()
                ))
                .collect(Collectors.toList());
    }


    /**
     * Obtener automóvil por ID
     */
    public AutomovilDTO obtenerPorId(Long id) {
        Automovil automovil = automovilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Automóvil no encontrado"));

        return new AutomovilDTO(
                automovil.getAccidentes(),
                automovil.getId(),
                automovil.getModelo(),
                automovil.getValor(),
                automovil.getPropietario().getId()
        );
    }

    /**
     * Crear un nuevo automóvil
     */
    public AutomovilDTO crear(AutomovilDTO automovilDTO) {
        Automovil automovil = new Automovil();

        // Verificar que el propietarioId no sea nulo antes de hacer cualquier otra asignación
        if (automovilDTO.getPropietarioId() != null) {
            // Buscar el propietario por su ID
            Propietario propietario = propietarioRepository.findById(automovilDTO.getPropietarioId())
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
            automovil.setPropietario(propietario);  // Asignar el propietario al automóvil
        } else {
            throw new RuntimeException("El ID del propietario es obligatorio.");
        }

        // Asignar los demás valores después de asignar el propietario
        automovil.setModelo(automovilDTO.getModelo());
        automovil.setValor(automovilDTO.getValor());
        automovil.setAccidentes(automovilDTO.getAccidentes());

        // Guardar el automóvil en la base de datos
        automovilRepository.save(automovil);

        // Retornar el DTO del automóvil creado
        return obtenerPorId(automovil.getId());
    }

    /**
     * Modificar un automóvil existente
     */
    public AutomovilDTO modificar(Long id, AutomovilDTO automovilDTO) {
        Automovil automovil = automovilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Automóvil no encontrado"));

        automovil.setModelo(automovilDTO.getModelo());
        automovil.setValor(automovilDTO.getValor());
        automovil.setAccidentes(automovilDTO.getAccidentes());
        automovilRepository.save(automovil);

        return obtenerPorId(automovil.getId());
    }

    /**
     * Eliminar un automóvil por ID
     */
    public void eliminar(Long id) {
        automovilRepository.deleteById(id);
    }
}

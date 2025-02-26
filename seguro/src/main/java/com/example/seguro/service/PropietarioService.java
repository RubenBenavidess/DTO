package com.example.seguro.service;

import com.example.seguro.dto.PropietarioDTO;
import com.example.seguro.model.Automovil;
import com.example.seguro.model.Propietario;
import com.example.seguro.repository.AutomovilRepository;
import com.example.seguro.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropietarioService {
    private final PropietarioRepository propietarioRepository;
    private final AutomovilRepository automovilRepository;

    public PropietarioService(PropietarioRepository propietarioRepository, AutomovilRepository automovilRepository) {
        this.propietarioRepository = propietarioRepository;
        this.automovilRepository = automovilRepository;
    }

    /**
     * Obtener todos los propietarios
     */
    public List<PropietarioDTO> obtenerTodos() {
        return propietarioRepository.findAll().stream()
                .map(propietario -> new PropietarioDTO(
                        propietario.getId(),
                        propietario.getNombre(),
                        propietario.getApellido(),
                        propietario.getEdad(),
                        (propietario.getAutomoviles() != null) ?
                                propietario.getAutomoviles().stream()
                                        .map(Automovil::getId)
                                        .collect(Collectors.toList())
                                : List.of()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtener propietario por ID
     */
    public PropietarioDTO obtenerPorId(Long id) {
        Propietario propietario = propietarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        List<Long> automovilesIds = (propietario.getAutomoviles() != null) ?
                propietario.getAutomoviles().stream()
                        .map(Automovil::getId)
                        .collect(Collectors.toList())
                : List.of();

        return new PropietarioDTO(
                propietario.getId(),
                propietario.getNombre(),
                propietario.getApellido(),
                propietario.getEdad(),
                automovilesIds
        );
    }

    /**
     * Crear un nuevo propietario
     */
    public PropietarioDTO crear(PropietarioDTO propietarioDTO) {
        Propietario propietario = new Propietario();
        propietario.setNombre(propietarioDTO.getNombreCompleto().split(" ")[0]);
        propietario.setApellido(propietarioDTO.getNombreCompleto().split(" ")[1]);
        propietario.setEdad(propietarioDTO.getEdad());

        // Asociar automóviles si vienen en el DTO
        if (propietarioDTO.getAutomovilesIds() != null && !propietarioDTO.getAutomovilesIds().isEmpty()) {
            List<Automovil> automoviles = automovilRepository.findAllById(propietarioDTO.getAutomovilesIds());
            propietario.setAutomoviles(automoviles);
        }

        propietarioRepository.save(propietario);
        return obtenerPorId(propietario.getId());
    }

    /**
     * Modificar un propietario existente
     */
    public PropietarioDTO modificar(Long id, PropietarioDTO propietarioDTO) {
        Propietario propietario = propietarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        propietario.setNombre(propietarioDTO.getNombreCompleto().split(" ")[0]);
        propietario.setApellido(propietarioDTO.getNombreCompleto().split(" ")[1]);

        propietario.setEdad(propietarioDTO.getEdad());

        // Actualizar automóviles si vienen en el DTO
        if (propietarioDTO.getAutomovilesIds() != null) {
            List<Automovil> automoviles = automovilRepository.findAllById(propietarioDTO.getAutomovilesIds());
            propietario.setAutomoviles(automoviles);
        }

        propietarioRepository.save(propietario);
        return obtenerPorId(propietario.getId());
    }

    /**
     * Eliminar un propietario por ID
     */
    public void eliminar(Long id) {
        propietarioRepository.deleteById(id);
    }
}

package com.example.seguro.service;

import com.example.seguro.dto.SeguroDTO;
import com.example.seguro.model.Automovil;
import com.example.seguro.model.Seguro;
import com.example.seguro.repository.AutomovilRepository;
import com.example.seguro.repository.SeguroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeguroService {
    private final SeguroRepository seguroRepository;
    private final AutomovilRepository automovilRepository;

    public SeguroService(SeguroRepository seguroRepository, AutomovilRepository automovilRepository) {
        this.seguroRepository = seguroRepository;
        this.automovilRepository = automovilRepository;
    }

    /**
     * Método para generar un seguro para un automóvil específico
     */
    public SeguroDTO generarSeguro(Long automovilId) {
        Automovil automovil = automovilRepository.findById(automovilId)
                .orElseThrow(() -> new RuntimeException("Automóvil no encontrado"));

        double costoTotal = calcularSeguro(automovil);
        Seguro seguro = new Seguro(costoTotal, automovil);
        seguroRepository.save(seguro);

        return new SeguroDTO(seguro.getId(), seguro.getCostoTotal(), automovil.getId());
    }

    /**
     * Crear un nuevo seguro
     */
    public SeguroDTO crear(SeguroDTO seguroDTO) {
        Automovil automovil = automovilRepository.findById(seguroDTO.getAutomovilId())
                .orElseThrow(() -> new RuntimeException("Automóvil no encontrado"));

        Seguro seguro = new Seguro(seguroDTO.getCostoTotal(), automovil);
        seguroRepository.save(seguro);

        return new SeguroDTO(seguro.getId(), seguro.getCostoTotal(), automovil.getId());
    }

    /**
     * Modificar un seguro existente
     */
    public SeguroDTO modificar(Long id, SeguroDTO seguroDTO) {
        Seguro seguro = seguroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seguro no encontrado"));

        Automovil automovil = automovilRepository.findById(seguroDTO.getAutomovilId())
                .orElseThrow(() -> new RuntimeException("Automóvil no encontrado"));

        seguro.setCostoTotal(seguroDTO.getCostoTotal());
        seguro.setAutomovil(automovil);
        seguroRepository.save(seguro);

        return new SeguroDTO(seguro.getId(), seguro.getCostoTotal(), automovil.getId());
    }

    /**
     * Eliminar un seguro por ID
     */
    public void eliminar(Long id) {
        System.out.println("Eliminando seguro con ID: " + id);
        seguroRepository.deleteById(id);
    }

    /**
     * Obtener todos los seguros registrados en el sistema
     */
    public List<SeguroDTO> obtenerTodos() {
        return seguroRepository.findAll().stream()
                .map(seguro -> new SeguroDTO(
                        seguro.getId(),
                        seguro.getCostoTotal(),
                        seguro.getAutomovil().getId()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtener un seguro por su ID
     */
    public SeguroDTO obtenerPorId(Long id) {
        Seguro seguro = seguroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seguro no encontrado"));

        return new SeguroDTO(
                seguro.getId(),
                seguro.getCostoTotal(),
                seguro.getAutomovil().getId()
        );
    }

    /**
     * Método para calcular el costo total del seguro basado en:
     * - Valor del automóvil
     * - Modelo del automóvil
     * - Edad del propietario
     * - Cantidad de accidentes
     */
    public double calcularSeguro(Automovil automovil) {
        if (automovil == null) {
            throw new IllegalArgumentException("El automóvil no puede ser nulo.");
        }

        double valorAuto = automovil.getValor();
        int edad = automovil.getPropietario().getEdad();
        int accidentes = automovil.getAccidentes();
        String modelo = automovil.getModelo();

        // Costo base del seguro
        double costo = valorAuto * 0.035;

        // Ajustes según modelo, edad y accidentes
        costo += obtenerCargoModelo(modelo, valorAuto);
        costo += obtenerCargoEdad(edad);
        costo += obtenerCargoAccidentes(accidentes, costo);

        return costo;
    }

    /**
     * Método para calcular el costo adicional según el modelo del automóvil
     */
    private double obtenerCargoModelo(String modelo, double valorAuto) {
        switch (modelo.toUpperCase()) {
            case "A":
                return valorAuto * 0.011;
            case "B":
                return valorAuto * 0.012;
            case "C":
                return valorAuto * 0.015;
            default:
                return 0;
        }
    }

    /**
     * Método para calcular el costo adicional según la edad del propietario
     */
    private double obtenerCargoEdad(int edad) {
        if (edad >= 18 && edad < 25) return 360;
        if (edad >= 25 && edad < 44) return 240;
        if (edad >= 44) return 130;
        throw new IllegalArgumentException("Edad fuera de rango. No aseguramos.");
    }

    /**
     * Método para calcular el costo adicional según la cantidad de accidentes
     */
    private double obtenerCargoAccidentes(int accidentes, double costoBase) {
        if (accidentes < 3) {
            return accidentes * (costoBase * 0.17);
        } else {
            return (3 * (costoBase * 0.17)) + ((accidentes - 3) * (costoBase * 0.21));
        }
    }
}

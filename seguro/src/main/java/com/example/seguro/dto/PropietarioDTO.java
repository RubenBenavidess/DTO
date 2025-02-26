package com.example.seguro.dto;

import java.util.List;

public class PropietarioDTO {

    private Long id;
    private String nombreCompleto;
    private int edad;

    private List<Long> AutomovilesIds;

    public PropietarioDTO() {
    }

    public PropietarioDTO(Long id, String nombre, String apellido, int edad, List<Long> automovilesIds) {
        this.id = id;
        this.nombreCompleto = (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
        this.edad = edad;
        this.AutomovilesIds = automovilesIds;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public List<Long> getAutomovilesIds() {
        return AutomovilesIds;
    }

    public void setAutomovilesIds(List<Long> automovilesIds) {
        AutomovilesIds = automovilesIds;
    }
}

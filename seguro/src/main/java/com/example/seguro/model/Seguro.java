package com.example.seguro.model;

import jakarta.persistence.*;

@Entity
public class Seguro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double costoTotal;

    @OneToOne
    @JoinColumn(name = "automovil_id", nullable = false)
    private Automovil automovil;

    public Seguro() {
        // Constructor vacío requerido por JPA
    }

    //Constructor corregido para aceptar costoTotal y automovil
    public Seguro(double costoTotal, Automovil automovil) {
        this.costoTotal = costoTotal;
        this.automovil = automovil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Automovil getAutomovil() {
        return automovil;
    }

    public void setAutomovil(Automovil automovil) {
        this.automovil = automovil;
    }
}

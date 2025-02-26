package com.example.seguro.dto;
    
public class AutomovilDTO {
        private Long id;
        private String modelo;
        private double valor;
        private int accidentes;
        private Long propietarioId;  // Se añade el ID del propietario


        public AutomovilDTO(int accidentes, Long id, String modelo, double valor, Long propietarioId) {
            this.accidentes = accidentes;
            this.id = id;
            this.modelo = modelo;
            this.propietarioId = propietarioId;
            this.valor = valor;
        }

        //getters setters

        public int getAccidentes() {
            return accidentes;
        }

        public void setAccidentes(int accidentes) {
            this.accidentes = accidentes;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getModelo() {
            return modelo;
        }

        public void setModelo(String modelo) {
            this.modelo = modelo;
        }

        public Long getPropietarioId() {
            return propietarioId;
        }

        public void setPropietarioId(Long propietarioId) {
            this.propietarioId = propietarioId;
        }


        public double getValor() {
            return valor;
        }

        public void setValor(double valor) {
            this.valor = valor;
        }
    }


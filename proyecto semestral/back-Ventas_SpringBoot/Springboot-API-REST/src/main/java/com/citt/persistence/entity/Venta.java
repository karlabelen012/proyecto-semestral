package com.citt.persistence.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Entity
@Data
@Builder
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idVenta;
    @NotBlank(message = "La dirección es obligatoria")
    private String direccionCompra;
    private Integer valorCompra;
    @NotNull(message = "Fecha de compra es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  // Especifica el formato de fecha
    private LocalDate fechaCompra;
    @Default
    private Boolean despachoGenerado = false;

    public Venta() {
    }

    public Venta(Long idVenta, String direccionCompra, Integer valorCompra, LocalDate fechaCompra, Boolean despachoGenerado) {
        this.idVenta = idVenta;
        this.direccionCompra = direccionCompra;
        this.valorCompra = valorCompra;
        this.fechaCompra = fechaCompra;
        this.despachoGenerado = despachoGenerado;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public String getDireccionCompra() {
        return direccionCompra;
    }

    public void setDireccionCompra(String direccionCompra) {
        this.direccionCompra = direccionCompra;
    }

    public Integer getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(Integer valorCompra) {
        this.valorCompra = valorCompra;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Boolean getDespachoGenerado() {
        return despachoGenerado;
    }

    public void setDespachoGenerado(Boolean despachoGenerado) {
        this.despachoGenerado = despachoGenerado;
    }
}

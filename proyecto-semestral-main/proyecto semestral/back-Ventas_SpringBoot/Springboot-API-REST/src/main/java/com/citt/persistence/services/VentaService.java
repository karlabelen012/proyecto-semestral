package com.citt.persistence.services;

import java.util.List;

import org.springframework.lang.NonNull;

import com.citt.exceptions.VentaNotFoundException;
import com.citt.persistence.entity.Venta;

public interface VentaService {
    List<Venta> findAllVentas();

    Venta saveVenta(@NonNull Venta venta);

    Venta updateVenta(@NonNull Long idVenta, @NonNull Venta venta) throws VentaNotFoundException;

    void deleteVenta(@NonNull Long idVenta) throws VentaNotFoundException;

    Venta findById(@NonNull Long idVenta) throws VentaNotFoundException;
}

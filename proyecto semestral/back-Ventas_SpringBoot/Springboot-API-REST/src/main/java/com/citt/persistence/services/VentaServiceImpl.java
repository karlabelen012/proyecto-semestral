package com.citt.persistence.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.citt.exceptions.VentaNotFoundException;
import com.citt.persistence.entity.Venta;
import com.citt.persistence.repository.VentaRepository;

@Service
public class VentaServiceImpl implements VentaService{

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> findAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta saveVenta(@NonNull Venta venta) {
        Objects.requireNonNull(venta, "venta no puede ser null");
        return ventaRepository.save(venta);
    }

    @Override
    @SuppressWarnings("null")
    public Venta updateVenta(@NonNull Long idVenta, @NonNull Venta venta) throws VentaNotFoundException {
        Objects.requireNonNull(idVenta, "idVenta no puede ser null");
        Objects.requireNonNull(venta, "venta no puede ser null");

        Optional<Venta> optionalVenta = ventaRepository.findById(idVenta);

        if (optionalVenta.isPresent()) {
            Venta ventaDB = optionalVenta.get();

            if (Objects.nonNull(venta.getDireccionCompra()) && !venta.getDireccionCompra().trim().isEmpty()) {
                ventaDB.setDireccionCompra(venta.getDireccionCompra());
            }

            if (Objects.nonNull(venta.getValorCompra())) {
                ventaDB.setValorCompra(venta.getValorCompra());
            }

            if (Objects.nonNull(venta.getFechaCompra())) {
                ventaDB.setFechaCompra(venta.getFechaCompra());
            }

            if (Objects.nonNull(venta.getDespachoGenerado())) {
                ventaDB.setDespachoGenerado(venta.getDespachoGenerado());
            }

            // Guardar la venta actualizada en la BD
            return Objects.requireNonNull(ventaRepository.save(ventaDB));
        } else {
            throw new VentaNotFoundException("!No es posible actualizar! No existe venta con ID: " + idVenta);
        }
    }

    @Override
    public void deleteVenta(@NonNull Long idVenta) throws VentaNotFoundException {
        Objects.requireNonNull(idVenta, "idVenta no puede ser null");
        Optional<Venta> venta = ventaRepository.findById(idVenta);
        if(!venta.isPresent()) {
            throw new VentaNotFoundException("¡No es posible eliminar! No existe venta con el ID: " + idVenta);
        }else {
            ventaRepository.deleteById(idVenta);
        }
    }

    @Override
    public Venta findById(@NonNull Long idVenta) throws VentaNotFoundException {
        Objects.requireNonNull(idVenta, "idVenta no puede ser null");
        Optional<Venta> venta = ventaRepository.findById(idVenta);
        if(!venta.isPresent()) throw new VentaNotFoundException("Venta no encontrada con el ID: " + idVenta);
        return Objects.requireNonNull(venta.get(), "venta encontrada es null");
    }
}

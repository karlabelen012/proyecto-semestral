package com.citt.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.citt.exceptions.DespachoNotFoundException;
import com.citt.persistence.entity.Despacho;
import com.citt.persistence.services.DespachoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/despachos")
@Tag(name = "Despacho", description = "Controlador para gestionar despachos")
public class DespachoController {

    @Autowired
    private DespachoService despachoService;

    @Operation(summary = "Crear un nuevo despacho")
    @PostMapping
    public ResponseEntity<Despacho> crearDespacho(
            @RequestBody @NonNull Despacho despacho){
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idDespacho}")
                .buildAndExpand(despacho.getIdDespacho())
                .toUri();
        despachoService.saveDespacho(despacho);
        return ResponseEntity.created(location).body(despacho);
    }

    @Operation(summary = "Actualizar un despacho existente")
    @PutMapping("/{idDespacho}")
    public ResponseEntity<Despacho> actualizarDespacho(
            @PathVariable @NonNull Long idDespacho,
            @Valid @RequestBody @NonNull Despacho despacho) throws DespachoNotFoundException {
        Despacho despachoActualizado = despachoService.updateDespacho(idDespacho, despacho);
        return ResponseEntity.ok(despachoActualizado);
    }

    @Operation(summary = "Obtener todos los despachos")
    @GetMapping
    public ResponseEntity<List<Despacho>> getAllDespachos() {
        return ResponseEntity.ok(despachoService.findAllDespachos());
    }

    @Operation(summary = "Obtener un despacho por ID")
    @GetMapping("/{idDespacho}")
    public ResponseEntity<Despacho> obtenerDespacho(
            @PathVariable @NonNull Long idDespacho) throws DespachoNotFoundException {
        Despacho despacho = despachoService.findById(idDespacho);
        return ResponseEntity.ok(despacho);
    }

    @Operation(summary = "Eliminar un despacho por ID")
    @DeleteMapping("/{idDespacho}")
    public ResponseEntity<Void> eliminarDespacho(@PathVariable @NonNull Long idDespacho) throws DespachoNotFoundException {
        despachoService.deleteDespacho(idDespacho);
        return ResponseEntity.noContent().build();
    }
}

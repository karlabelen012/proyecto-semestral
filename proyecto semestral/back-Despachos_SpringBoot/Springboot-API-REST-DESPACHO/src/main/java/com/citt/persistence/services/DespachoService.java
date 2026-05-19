package com.citt.persistence.services;

import java.util.List;

import org.springframework.lang.NonNull;

import com.citt.exceptions.DespachoNotFoundException;
import com.citt.persistence.entity.Despacho;

public interface DespachoService {
    List<Despacho> findAllDespachos();
    Despacho saveDespacho(@NonNull Despacho despacho);
    Despacho updateDespacho(@NonNull Long idDespacho, @NonNull Despacho despacho) throws DespachoNotFoundException;
    void deleteDespacho(@NonNull Long idDespacho) throws DespachoNotFoundException, DespachoNotFoundException;
    Despacho findById(@NonNull Long idDespacho) throws DespachoNotFoundException;
}
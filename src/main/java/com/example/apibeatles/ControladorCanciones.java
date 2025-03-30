package com.example.apibeatles;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ControladorCanciones {

    private final ServicioCanciones servicioCanciones;
    private final ServicioDiscos servicioDiscos;

    @Autowired
    public ControladorCanciones(ServicioCanciones servicioCanciones, ServicioDiscos servicioDiscos) {
        this.servicioCanciones = servicioCanciones;
        this.servicioDiscos = servicioDiscos;
    }

    @PostMapping("/beatlesapi/cancion")
    @ResponseStatus(HttpStatus.CREATED)
    public Cancion createCancion(@Valid @RequestBody Cancion cancion) {
        return servicioCanciones.crearCancion(cancion);
    }

    @GetMapping
    public List<Cancion> getAllCanciones() {
        return servicioCanciones.obtenerTodas();
    }

    @GetMapping("/beatlesapi/cancion/{id}")
    public Cancion getCancion(@PathVariable Long id) {
        return servicioCanciones.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cancion no encontrada"));
    }

    @DeleteMapping("/beatlesapi/cancion/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCancion(@PathVariable Long id) {
        if (!servicioCanciones.eliminarPorId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cancion no encontrada");
        }
        servicioDiscos.eliminarCancionDeTodosLosDiscos(id);
    }

    @PutMapping("/beatlesapi/cancion/{id}")
    public void updateCancion(@PathVariable Long id, @Valid @RequestBody Cancion actualizada) {
        servicioCanciones.actualizarCancion(id, actualizada);
    }


}

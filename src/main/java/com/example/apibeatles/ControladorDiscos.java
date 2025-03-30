package com.example.apibeatles;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ControladorDiscos {
    //Método de guardado de discos outdated
    private final ArrayList<Disco> discos = new ArrayList<>();
    private final ServicioDiscos servicioDiscos;
    private final ServicioCanciones servicioCanciones;

    public ControladorDiscos(ServicioDiscos servicioDiscos, ServicioCanciones servicioCanciones) {
        this.servicioDiscos = servicioDiscos;
        this.servicioCanciones = servicioCanciones;
    }

    @PostMapping("/beatlesapi/disco")
    @ResponseStatus(HttpStatus.CREATED)
    public Disco createDisco(@Valid @RequestBody Disco disco) {
        List<Cancion> cancionesConId = disco.listadoCanciones().stream()
                .map(servicioCanciones::crearCancion)
                .toList();

        Disco nuevoDisco = new Disco(
                disco.id(),
                disco.titulo(),
                disco.año(),
                disco.portada(),
                cancionesConId,
                disco.descripcion()
        );
        return servicioDiscos.crearDisco(nuevoDisco);
    }


    @GetMapping("/beatlesapi/disco/titulo/{titulo}")
    public Disco getDisco(@PathVariable String titulo) {
        return servicioDiscos.buscarPorTitulo(titulo)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado"));
    }

    @GetMapping("/beatlesapi/disco/{Id}")
    public Disco getDisco(@PathVariable Long Id) {
        return servicioDiscos.buscarPorId(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado"));
    }

    @GetMapping("/beatlesapi/disco")
    public List<Disco> getAllDiscos() {
        return servicioDiscos.obtenerTodas();
    }

    @DeleteMapping("/beatlesapi/disco/{Id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDisco(@PathVariable Long Id) {
        if (!servicioDiscos.eliminarPorId(Id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado");
        }
        servicioDiscos.eliminarPorId(Id);
    }

    @DeleteMapping("/beatlesapi/disco/{Id}/cancion/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCancionDeDisco(@PathVariable Long Id, @PathVariable Long songId) {
        Disco disco = servicioDiscos.buscarPorId(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado"));
        Long id = disco.id();
        servicioDiscos.eliminarCancionPorId(id, songId);
    }


    @PutMapping("/beatlesapi/disco/{Id}")
    public void updateDisco(@PathVariable Long Id, @Valid @RequestBody Disco disco) {
        servicioDiscos.actualizarDisco(Id, disco);
    }

    @PutMapping("/beatlesapi/disco/{id}/agregarCancion")
    public Disco agregarCancionAUnDisco(@PathVariable Long id, @RequestBody Cancion cancion) {
        Disco disco = servicioDiscos.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado"));

        Cancion finalCancion;
        if (cancion.id() != null) {
            finalCancion = servicioCanciones.buscarPorId(cancion.id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canción no encontrada"));
        } else {
            finalCancion = servicioCanciones.crearCancion(cancion);
        }

        List<Cancion> nuevasCanciones = new ArrayList<>(disco.listadoCanciones());
        nuevasCanciones.add(finalCancion);

        Disco actualizado = new Disco(
                disco.id(),
                disco.titulo(),
                disco.año(),
                disco.portada(),
                nuevasCanciones,
                disco.descripcion()
        );

        servicioDiscos.actualizarDisco(id, actualizado);
        return actualizado;
    }


    //Métodos de actualización adicionales (outdated)
    @PutMapping("/beatlesapi/disco/{titulo}/portada/{portadaURL}")
    public void updatePortada(@PathVariable String titulo, String portadaURL) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).titulo().equalsIgnoreCase(titulo)) {
                Disco viejo = discos.get(i);
                Disco actualizado = new Disco(
                        viejo.id(),
                        viejo.titulo(),
                        viejo.año(),
                        portadaURL,
                        viejo.listadoCanciones(),
                        viejo.descripcion()
                );
                discos.set(i, actualizado);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado");
    }

    @PutMapping("/beatlesapi/disco/{titulo}/titulo/{nuevoTitulo}")
    public void updateTitulo(@PathVariable String titulo, @PathVariable String nuevoTitulo) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).titulo().equalsIgnoreCase(titulo)) {
                Disco viejo = discos.get(i);
                Disco actualizado = new Disco(
                        viejo.id(),
                        nuevoTitulo,
                        viejo.año(),
                        viejo.portada(),
                        viejo.listadoCanciones(),
                        viejo.descripcion()
                );
                discos.set(i, actualizado);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado");
    }

    @PutMapping("/beatlesapi/disco/{titulo}/año/{year}")
    public void updateYear(@PathVariable String titulo, @PathVariable Integer year) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).titulo().equalsIgnoreCase(titulo)) {
                Disco viejo = discos.get(i);
                Disco actualizado = new Disco(
                        viejo.id(),
                        viejo.titulo(),
                        year,
                        viejo.portada(),
                        viejo.listadoCanciones(),
                        viejo.descripcion()
                );
                discos.set(i, actualizado);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado");
    }

    @PutMapping("/beatlesapi/disco/{titulo}/listadoCanciones")
    public void updateListadoCanciones(@PathVariable String titulo, @Valid @RequestBody ArrayList<Cancion> listadoCanciones) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).titulo().equalsIgnoreCase(titulo)) {
                Disco viejo = discos.get(i);
                Disco actualizado = new Disco(
                        viejo.id(),
                        viejo.titulo(),
                        viejo.año(),
                        viejo.portada(),
                        listadoCanciones,
                        viejo.descripcion()
                );
                discos.set(i, actualizado);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado");
    }

    //Añadir canción a disco ya existente
    @PutMapping("/beatlesapi/disco/{titulo}/cancion")
    public void updateListadoCancionesAdd(@PathVariable String titulo, @Valid @RequestBody Cancion cancion) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).titulo().equalsIgnoreCase(titulo)) {
                Disco viejo = discos.get(i);
                List<Cancion> listadoCanciones = new ArrayList<>();
                listadoCanciones = viejo.listadoCanciones();
                listadoCanciones.add(cancion);
                Disco actualizado = new Disco(
                        viejo.id(),
                        viejo.titulo(),
                        viejo.año(),
                        viejo.portada(),
                        listadoCanciones,
                        viejo.descripcion()
                );
                discos.set(i, actualizado);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado");
    }


    //IMPORTANTE: Declarar como texto y no como JSON en el Postman
    @PutMapping("/beatlesapi/disco/{titulo}/descripcion")
    public void updateDescripcion(@PathVariable String titulo, @RequestBody String descripcion) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).titulo().equalsIgnoreCase(titulo)) {
                Disco viejo = discos.get(i);
                Disco actualizado = new Disco(
                        viejo.id(),
                        viejo.titulo(),
                        viejo.año(),
                        viejo.portada(),
                        viejo.listadoCanciones(),
                        descripcion
                );
                discos.set(i, actualizado);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disco no encontrado");
    }

}

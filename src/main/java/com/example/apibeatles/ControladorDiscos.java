package com.example.apibeatles;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ControladorDiscos {
    private final ArrayList<Disco> discos = new ArrayList<>();
    private final ServicioDiscos servicioDiscos;
    private final ServicioCanciones servicioCanciones;
    private long albumId = 1;

    public ControladorDiscos(ServicioDiscos servicioDiscos, ServicioCanciones servicioCanciones) {
        this.servicioDiscos = servicioDiscos;
        this.servicioCanciones = servicioCanciones;
    }

    @PostMapping("/beatlesapi/disco")
    @ResponseStatus(HttpStatus.CREATED)
    public Disco createDisco(@Valid @RequestBody Disco disco) {
        List<Cancion> cancionesConId = new ArrayList<>();
        for (Cancion c : disco.listadoCanciones()) {
            Cancion creada = servicioCanciones.crearCancion(c);
            cancionesConId.add(creada);
        }
        return servicioDiscos.crearDisco(disco);
    }

    @GetMapping("/beatlesapi/disco/{titulo}")
    public Disco getDisco(@PathVariable String titulo) {
        return servicioDiscos.buscarPorTitulo(titulo)
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


    @PutMapping("/beatlesapi/disco/{Id}")
    public void updateDisco(@PathVariable Long Id, @Valid @RequestBody Disco disco) {
        servicioDiscos.actualizarDisco(Id, disco);
    }

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

    //Eliminar canción de disco ya existente
    @DeleteMapping("/beatlesapi/disco/{titulo}/cancion/{id}")
    public void updateListadoCancionesDelete(@PathVariable String titulo, @PathVariable Long id) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).titulo().equalsIgnoreCase(titulo)) {
                Disco viejo = discos.get(i);

                List<Cancion> nuevasCanciones = viejo.listadoCanciones()
                        .stream()
                        .filter(c -> c.id() == null || !id.equals(c.id()))
                        .toList();
                Disco actualizado = new Disco(
                        viejo.id(),
                        viejo.titulo(),
                        viejo.año(),
                        viejo.portada(),
                        nuevasCanciones,
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

    public void eliminarCancionDeTodosLosDiscos(Long id) {
        for (int i = 0; i < discos.size(); i++) {
            Disco disco = discos.get(i);
            List<Cancion> cancionesActualizadas = disco.listadoCanciones().stream()
                    .filter(c -> !c.id().equals(id))
                    .toList();

            Disco actualizado = new Disco(
                    disco.id(),
                    disco.titulo(),
                    disco.año(),
                    disco.portada(),
                    cancionesActualizadas,
                    disco.descripcion()
            );

            discos.set(i, actualizado);
        }
    }

}

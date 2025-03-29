package com.example.apibeatles;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioDiscos {
    private final ArrayList<Disco> discos = new ArrayList<>();
    private long Id = 1;

    public Disco crearDisco(Disco disco){

        Disco nuevo = new Disco(Id++, disco.titulo(), disco.año(), disco.portada(), disco.listadoCanciones(), disco.descripcion());
        discos.add(nuevo);
        return nuevo;
    }

    public Optional<Disco> buscarPorTitulo(String titulo) {
        return discos.stream()
                .filter(c -> c.titulo().equalsIgnoreCase(titulo))
                .findFirst();
    }

    public void actualizarDisco(Long id, Disco actualizado) {
        for (int i = 0; i < discos.size(); i++) {
            if (discos.get(i).id().equals(id)) {
                Disco nuevo = new Disco(id, actualizado.titulo(), actualizado.año(), actualizado.portada(), actualizado.listadoCanciones(), actualizado.descripcion());
                discos.set(i, nuevo);
                return;
            }
        }
    }

    public boolean eliminarPorId(Long id) {
        return discos.removeIf(c -> c.id().equals(id));
    }

    public Optional<Disco> buscarPorId(Long id) {
        return discos.stream()
                .filter(c -> c.id().equals(id))
                .findFirst();
    }

    public List<Disco> obtenerTodas() {
        return discos;
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

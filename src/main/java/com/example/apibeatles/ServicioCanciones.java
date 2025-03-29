package com.example.apibeatles;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioCanciones {
    //Lista general para todas las canciones
    private final ArrayList<Cancion> canciones = new ArrayList<>();
    private long Id = 1;

    public Cancion crearCancion(Cancion cancion) {
        Cancion nueva = new Cancion(Id++, cancion.titulo(), cancion.duracion());
        canciones.add(nueva);
        return nueva;
    }

    public Cancion buscarPorTitulo(String titulo) {
        return canciones.stream()
                .filter(c -> c.titulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    public void actualizarCancion(Long id, Cancion actualizada) {
        for (int i = 0; i < canciones.size(); i++) {
            if (canciones.get(i).id().equals(id)) {
                Cancion nueva = new Cancion(id, actualizada.titulo(), actualizada.duracion());
                canciones.set(i, nueva);
                return;
            }
        }
    }

    public boolean eliminarPorId(Long id) {
        return canciones.removeIf(c -> c.id().equals(id));
    }

    public Optional<Cancion> buscarPorId(Long id) {
        return canciones.stream()
                .filter(c -> c.id().equals(id))
                .findFirst();
    }

    public List<Cancion> obtenerTodas() {
        return canciones;
    }
}

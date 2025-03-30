package com.example.apibeatles;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Cancion(
    Long id,

    @NotBlank(message="La canción necesita un título")
    String titulo,

    @NotNull(message="La canción necesita una duración")
    int duracion
) {}
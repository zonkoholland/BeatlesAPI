package com.example.apibeatles;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record Disco(
   Long id,

   @NotBlank(message="El disco necesita un título")
   String titulo,

   @NotNull(message="El disco necesita un año de estreno")
   Integer año,

   //URL a imagen de portada
   String portada,

   @ElementCollection @Valid
   List<Cancion> listadoCanciones,

   String descripcion
) {}
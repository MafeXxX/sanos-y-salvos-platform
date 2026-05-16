package cl.duoc.sanosysalvos.mascotas.builder;

import cl.duoc.sanosysalvos.mascotas.model.Mascota;

public class MascotaBuilder {

    private String nombre;
    private String especie;
    private String raza;
    private String color;
    private int edad;
    private Long usuarioId;

    public MascotaBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public MascotaBuilder especie(String especie) {
        this.especie = especie;
        return this;
    }

    public MascotaBuilder raza(String raza) {
        this.raza = raza;
        return this;
    }

    public MascotaBuilder color(String color) {
        this.color = color;
        return this;
    }

    public MascotaBuilder edad(int edad) {
        this.edad = edad;
        return this;
    }

    public MascotaBuilder usuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
        return this;
    }

    public Mascota build() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la mascota es obligatorio");
        }
        if (especie == null || especie.isBlank()) {
            throw new IllegalArgumentException("La especie de la mascota es obligatoria");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }
        return Mascota.builder()
                .nombre(nombre)
                .especie(especie)
                .raza(raza)
                .color(color)
                .edad(edad)
                .usuarioId(usuarioId)
                .tieneReporteActivo(false)
                .build();
    }
}

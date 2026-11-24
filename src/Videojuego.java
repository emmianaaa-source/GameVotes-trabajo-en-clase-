package com.gamevotes;

import java.util.Objects;

public class Videojuego {
    private int codigoID;
    private String nombre;
    private String estudio;
    private String categoria;
    private int anioLanzamiento;
    private int votos;

    public Videojuego(int codigoID, String nombre, String estudio, String categoria, int anioLanzamiento, int votos) {
        this.codigoID = codigoID;
        this.nombre = nombre.trim();
        this.estudio = estudio.trim();
        this.categoria = categoria.trim();
        this.anioLanzamiento = anioLanzamiento;
        this.votos = Math.max(0, votos);
    }

    public int getCodigoID() {
        return codigoID;
    }

    public void setCodigoID(int codigoID) {
        this.codigoID = codigoID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria.trim();
    }

    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(int anioLanzamiento) {
        this.anioLanzamiento = anioLanzamiento;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = Math.max(0, votos);
    }

    public void restarVotos(int cantidad) {
        if (cantidad > 0) {
            this.votos = Math.max(0, this.votos - cantidad);
        }
    }

    public String formatoRanking() {
        return String.format("[%d] %s — Votos: %d (%s | %d)",
                codigoID, nombre, votos, categoria, anioLanzamiento);
    }

    @Override
    public String toString() {
        return String.format("Código: %d | %s | Estudio: %s | %s (%d) | Votos: %d",
                codigoID, nombre, estudio, categoria, anioLanzamiento, votos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Videojuego)) return false;
        Videojuego that = (Videojuego) o;
        return codigoID == that.codigoID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoID);
    }
}
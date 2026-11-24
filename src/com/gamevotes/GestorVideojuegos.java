package com.gamevotes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestorVideojuegos {
    public static final int ANIO_MINIMO = 1980;
    public static final String[] CATEGORIAS = {
            "Acción", "Estrategia", "RPG", "Plataformas", "Simulación"
    };

    private final ArrayList<Videojuego> catalogo = new ArrayList<>();

    public GestorVideojuegos() {
        precargar();
    }

    public int getAnioActual() {
        return LocalDate.now().getYear();
    }

    public void precargar() {
        catalogo.clear();
        String[] categorias = CATEGORIAS;
        int anioActual = getAnioActual();

        for (int i = 0; i < 20; i++) {
            int codigo = 1290 - i * 10;
            String cat = categorias[i % categorias.length];
            int anio = ANIO_MINIMO + (i % (anioActual - ANIO_MINIMO + 1));
            int votos = 10 + (i * 2) % 30;
            catalogo.add(new Videojuego(
                    codigo,
                    "Juego Indie " + (i + 1),
                    "Estudio " + (i + 1),
                    cat,
                    anio,
                    votos
            ));
        }
        ordenarPorCodigoDesc();
    }

    // --- Validaciones ---

    public boolean campoVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public boolean codigoValido(int codigo) {
        return codigo > 0;
    }

    public boolean anioValido(int anio) {
        return anio >= ANIO_MINIMO && anio <= getAnioActual();
    }

    public String validarCampos(String codigoStr, String nombre, String estudio, String anioStr, boolean validarCodigo) {
        if (validarCodigo) {
            if (campoVacio(codigoStr)) {
                return "Ingrese un código.";
            }
            int codigo;
            try {
                codigo = Integer.parseInt(codigoStr.trim());
            } catch (NumberFormatException e) {
                return "El código debe ser un número entero.";
            }
            if (!codigoValido(codigo)) {
                return "El código debe ser positivo.";
            }
            if (existeCodigo(codigo)) {
                return "Ese código ya existe.";
            }
        }

        if (campoVacio(nombre) || campoVacio(estudio) || campoVacio(anioStr)) {
            return "Complete todos los campos obligatorios.";
        }

        int anio;
        try {
            anio = Integer.parseInt(anioStr.trim());
        } catch (NumberFormatException e) {
            return "El año debe ser un número entero.";
        }
        if (!anioValido(anio)) {
            return "El año debe estar entre " + ANIO_MINIMO + " y " + getAnioActual() + ".";
        }

        return null;
    }

    // --- Ordenamiento y búsqueda ---

    public void ordenarPorCodigoDesc() {
        catalogo.sort((a, b) -> Integer.compare(b.getCodigoID(), a.getCodigoID()));
    }

    public int busquedaBinaria(int codigo) {
        if (!codigoValido(codigo)) {
            return -1;
        }
        int izq = 0;
        int der = catalogo.size() - 1;

        while (izq <= der) {
            int mid = (izq + der) / 2;
            int actual = catalogo.get(mid).getCodigoID();
            if (actual == codigo) {
                return mid;
            }
            if (actual < codigo) {
                der = mid - 1;
            } else {
                izq = mid + 1;
            }
        }
        return -1;
    }

    public boolean existeCodigo(int codigo) {
        return busquedaBinaria(codigo) != -1;
    }

    // --- Operaciones ---

    public void agregar(int codigo, String nombre, String estudio, String categoria, int anio) {
        catalogo.add(new Videojuego(codigo, nombre, estudio, categoria, anio, 0));
        ordenarPorCodigoDesc();
    }

    public Videojuego getJuego(int indice) {
        if (indice < 0 || indice >= catalogo.size()) {
            return null;
        }
        return catalogo.get(indice);
    }

    public void editar(int indice, String nombre, String estudio, String categoria, int anio) {
        Videojuego j = getJuego(indice);
        if (j != null) {
            j.setNombre(nombre);
            j.setEstudio(estudio);
            j.setCategoria(categoria);
            j.setAnioLanzamiento(anio);
            ordenarPorCodigoDesc();
        }
    }

    public void eliminar(int indice) {
        if (indice >= 0 && indice < catalogo.size()) {
            catalogo.remove(indice);
        }
    }

    public void restarVotos(int indice, int cantidad) {
        Videojuego j = getJuego(indice);
        if (j != null && cantidad >= 1 && cantidad <= 3) {
            j.setVotos(Math.max(0, j.getVotos() - cantidad));
        }
    }

    public List<Videojuego> rankingPorVotos() {
        List<Videojuego> copia = new ArrayList<>(catalogo);
        copia.sort(Comparator.comparingInt(Videojuego::getVotos).reversed());
        return copia;
    }

    public ArrayList<Videojuego> getCatalogo() {
        return catalogo;
    }

    public int getCantidad() {
        return catalogo.size();
    }
}
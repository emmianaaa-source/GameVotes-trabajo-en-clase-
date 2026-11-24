
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GestorVideojuegos {
    public static final int ANIO_MINIMO = 1980;
    public static final String[] CATEGORIAS = {
            "Acción", "Estrategia", "RPG", "Plataformas", "Simulación"
    };

    private final ArrayList<Videojuego> catalogo;
    private boolean precargaRealizada;

    public GestorVideojuegos() {
        this.catalogo = new ArrayList<>();
        this.precargaRealizada = false;
        precargarVeinteVideojuegos();
    }

    public int getAnioMaximo() {
        return LocalDate.now().getYear();
    }

    public final void precargarVeinteVideojuegos() {
        if (precargaRealizada) {
            return;
        }
        precargaRealizada = true;
        catalogo.clear();

        Object[][] datosIniciales = {
                {1290, "Juego 1", "Estudio 1", "Plataformas", 2018, 45},
                {1280, "Juego 2", "Estudio 2", "Plataformas", 2017, 60},
                {1270, "Juego 3", "Estudio 3", "Acción", 2020, 52},
                {1260, "Juego 4", "Estudio 4", "Simulación", 2016, 48},
                {1250, "Juego 5", "Estudio 5", "RPG", 2015, 38},
                {1240, "Juego 6", "Estudio 6", "Estrategia", 2018, 25},
                {1230, "Juego 7", "Estudio 7", "Acción", 2018, 33},
                {1220, "Juego 8", "Estudio 8", "Simulación", 2020, 41},
                {1210, "Juego 9", "Estudio 9", "RPG", 2019, 55},
                {1200, "Juego 10", "Estudio 10", "Simulación", 2019, 44},
                {1190, "Juego 11", "Estudio 11", "Estrategia", 2019, 36},
                {1180, "Juego 12", "Estudio 12", "Plataformas", 2017, 31},
                {1170, "Juego 13", "Estudio 13", "Acción", 2019, 27},
                {1160, "Juego 14", "Estudio 14", "RPG", 2023, 29},
                {1150, "Juego 15", "Estudio 15", "Simulación", 2018, 34},
                {1140, "Juego 16", "Estudio 16", "Plataformas", 2014, 22},
                {1130, "Juego 17", "Estudio 17", "Acción", 2022, 40},
                {1120, "Juego 18", "Estudio 18", "Estrategia", 2023, 24},
                {1110, "Juego 19", "Estudio 19", "RPG", 2022, 28},
                {1100, "Juego 20", "Estudio 20", "Simulación", 2023, 35}
        };

        for (Object[] fila : datosIniciales) {
            int codigo = (int) fila[0];
            String nombre = (String) fila[1];
            String estudio = (String) fila[2];
            String categoria = (String) fila[3];
            int anio = (int) fila[4];
            int votos = (int) fila[5];

            if (esCodigoValido(codigo) && !existeCodigo(codigo)
                    && esTextoValido(nombre) && esTextoValido(estudio)
                    && esCategoriaValida(categoria) && esAnioValido(anio)) {
                catalogo.add(new Videojuego(codigo, nombre, estudio, categoria, anio, votos));
            }
        }

        ordenarPorCodigoDesc();
    }

    // --- Validaciones ---

    public boolean esTextoValido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    public boolean esCodigoValido(int codigo) {
        return codigo > 0;
    }

    public boolean esAnioValido(int anio) {
        return anio >= ANIO_MINIMO && anio <= getAnioMaximo();
    }

    public boolean esCategoriaValida(String categoria) {
        if (!esTextoValido(categoria)) {
            return false;
        }
        for (String c : CATEGORIAS) {
            if (c.equals(categoria.trim())) {
                return true;
            }
        }
        return false;
    }

    public String validarDatosRegistro(String codigoTexto, String nombre, String estudio,
                                       String categoria, String anioTexto) {
        if (!esTextoValido(codigoTexto)) {
            return "El código es obligatorio.";
        }
        int codigo;
        try {
            codigo = Integer.parseInt(codigoTexto.trim());
        } catch (NumberFormatException e) {
            return "El código debe ser un número entero válido.";
        }
        if (!esCodigoValido(codigo)) {
            return "El código debe ser un número entero positivo mayor que 0.";
        }
        if (existeCodigo(codigo)) {
            return "Ya existe un videojuego registrado con el código " + codigo + ".";
        }
        if (!esTextoValido(nombre)) {
            return "El nombre del videojuego es obligatorio.";
        }
        if (!esTextoValido(estudio)) {
            return "El estudio es obligatorio.";
        }
        if (!esCategoriaValida(categoria)) {
            return "Debe seleccionar una categoría válida.";
        }
        if (!esTextoValido(anioTexto)) {
            return "El año de lanzamiento es obligatorio.";
        }
        int anio;
        try {
            anio = Integer.parseInt(anioTexto.trim());
        } catch (NumberFormatException e) {
            return "El año de lanzamiento debe ser un número entero válido.";
        }
        if (!esAnioValido(anio)) {
            return "El año de lanzamiento debe estar entre " + ANIO_MINIMO + " y " + getAnioMaximo() + ".";
        }
        return null; // Sin errores
    }

    public String validarDatosEdicion(String nombre, String estudio, String categoria, String anioTexto) {
        if (!esTextoValido(nombre)) {
            return "El nombre del videojuego es obligatorio.";
        }
        if (!esTextoValido(estudio)) {
            return "El estudio es obligatorio.";
        }
        if (!esCategoriaValida(categoria)) {
            return "Debe seleccionar una categoría válida.";
        }
        if (!esTextoValido(anioTexto)) {
            return "El año de lanzamiento es obligatorio.";
        }
        int anio;
        try {
            anio = Integer.parseInt(anioTexto.trim());
        } catch (NumberFormatException e) {
            return "El año de lanzamiento debe ser un número entero válido.";
        }
        if (!esAnioValido(anio)) {
            return "El año de lanzamiento debe estar entre " + ANIO_MINIMO + " y " + getAnioMaximo() + ".";
        }
        return null; // Sin errores
    }

    // --- Ordenamiento y búsqueda ---

    public void ordenarPorCodigoDesc() {
        catalogo.sort((a, b) -> Integer.compare(b.getCodigoID(), a.getCodigoID()));
    }

    /**
     * Búsqueda binaria propia sobre catalogo ordenado de mayor a menor por codigoID.
     * Devuelve el índice en catalogo o -1 si no existe.
     */
    public int busquedaBinaria(int codigoBuscado) {
        if (!esCodigoValido(codigoBuscado)) {
            return -1;
        }
        ordenarPorCodigoDesc();

        int inicio = 0;
        int fin = catalogo.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int codigoMedio = catalogo.get(medio).getCodigoID();

            if (codigoMedio == codigoBuscado) {
                return medio;
            } else if (codigoMedio < codigoBuscado) {
                // Como está ordenado descendente, los códigos mayores están a la izquierda
                fin = medio - 1;
            } else {
                // Los códigos menores están a la derecha
                inicio = medio + 1;
            }
        }
        return -1;
    }

    public boolean existeCodigo(int codigo) {
        return busquedaBinaria(codigo) != -1;
    }

    // --- Operaciones CRUD y Ranking ---

    public boolean registrarVideojuego(int codigo, String nombre, String estudio,
                                       String categoria, int anio) {
        if (!esCodigoValido(codigo) || existeCodigo(codigo)
                || !esTextoValido(nombre) || !esTextoValido(estudio)
                || !esCategoriaValida(categoria) || !esAnioValido(anio)) {
            return false;
        }
        Videojuego nuevo = new Videojuego(codigo, nombre, estudio, categoria, anio, 0);
        catalogo.add(nuevo);
        ordenarPorCodigoDesc();
        return true;
    }

    public Videojuego obtenerPorIndice(int indice) {
        if (indice < 0 || indice >= catalogo.size()) {
            return null;
        }
        return catalogo.get(indice);
    }

    public boolean actualizarVideojuegoPorIndice(int indice, String nombre, String estudio,
                                                 String categoria, int anio) {
        if (indice < 0 || indice >= catalogo.size()) {
            return false;
        }
        if (!esTextoValido(nombre) || !esTextoValido(estudio)
                || !esCategoriaValida(categoria) || !esAnioValido(anio)) {
            return false;
        }
        Videojuego juego = catalogo.get(indice);
        juego.setNombre(nombre);
        juego.setEstudio(estudio);
        juego.setCategoria(categoria);
        juego.setAnioLanzamiento(anio);
        ordenarPorCodigoDesc();
        return true;
    }

    public boolean eliminarVideojuegoPorIndice(int indice) {
        if (indice < 0 || indice >= catalogo.size()) {
            return false;
        }
        catalogo.remove(indice);
        ordenarPorCodigoDesc();
        return true;
    }

    public boolean restarVotosPorIndice(int indice, int cantidad) {
        if (indice < 0 || indice >= catalogo.size() || cantidad < 1 || cantidad > 3) {
            return false;
        }
        Videojuego juego = catalogo.get(indice);
        juego.restarVotos(cantidad);
        return true;
    }

    public List<Videojuego> rankingPorVotos() {
        ArrayList<Videojuego> copiaRanking = new ArrayList<>(catalogo);
        copiaRanking.sort(Comparator.comparingInt(Videojuego::getVotos).reversed());
        return Collections.unmodifiableList(copiaRanking);
    }

    public List<Videojuego> getCatalogo() {
        ordenarPorCodigoDesc();
        return Collections.unmodifiableList(catalogo);
    }

    public int getCantidad() {
        return catalogo.size();
    }
}
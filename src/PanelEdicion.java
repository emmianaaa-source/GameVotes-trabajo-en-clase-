import javax.swing.*;
import java.awt.*;

/**
 * Tab 2 (derecha) — R4: búsqueda binaria por código y edición.
 * Autor: Martín Aniceto.
 * Germán agrega aquí el botón Eliminar y el spinner de Restar votos.
 */
public class PanelEdicion extends JPanel {

    private final GestorVideojuegos gestor;
    private final Runnable alCambiar;      // refresca las demás vistas después de editar
    private int indiceSeleccionado = -1;   // -1 = no hay videojuego seleccionado

    private final JTextField txtCodigoBuscar = new JTextField(8);
    private final JButton btnBuscar = new JButton("Buscar por código");
    private final JLabel lblCodigo = new JLabel("-");
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtEstudio = new JTextField(15);
    private final JComboBox<String> cmbCategoria = new JComboBox<>(GestorVideojuegos.CATEGORIAS);
    private final JTextField txtAnio = new JTextField(6);
    private final JLabel lblVotos = new JLabel("-");
    private final JButton btnGuardar = new JButton("Guardar cambios");

    public PanelEdicion(GestorVideojuegos gestor, Runnable alCambiar) {
        this.gestor = gestor;
        this.alCambiar = alCambiar;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Búsqueda binaria por código"));

        JPanel filaBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        filaBuscar.add(txtCodigoBuscar);
        filaBuscar.add(btnBuscar);

        agregarFila(0, "Código:", filaBuscar);
        agregarFila(1, "Seleccionado:", lblCodigo);
        agregarFila(2, "Nombre:", txtNombre);
        agregarFila(3, "Estudio:", txtEstudio);
        agregarFila(4, "Categoría:", cmbCategoria);
        agregarFila(5, "Año:", txtAnio);
        agregarFila(6, "Votos:", lblVotos);      // solo se muestra, no se edita
        agregarFila(7, "", btnGuardar);

        btnBuscar.addActionListener(e -> buscarPorCodigo());
        btnGuardar.addActionListener(e -> guardarCambios());

        limpiarSeleccion();
    }

    private void buscarPorCodigo() {
        int codigo;
        try {
            codigo = Integer.parseInt(txtCodigoBuscar.getText().trim());
        } catch (NumberFormatException e) {
            limpiarSeleccion();
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!gestor.esCodigoValido(codigo)) {
            limpiarSeleccion();
            JOptionPane.showMessageDialog(this, "El código debe ser positivo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int indice = gestor.busquedaBinaria(codigo);
        if (indice == -1) {
            limpiarSeleccion();   // evita editar por error el juego buscado antes
            JOptionPane.showMessageDialog(this, "No existe un videojuego con el código " + codigo + ".",
                    "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        cargarDatos(gestor.obtenerPorIndice(indice), indice);
    }

    private void cargarDatos(Videojuego v, int indice) {
        indiceSeleccionado = indice;
        lblCodigo.setText(String.valueOf(v.getCodigoID()));
        txtNombre.setText(v.getNombre());
        txtEstudio.setText(v.getEstudio());
        cmbCategoria.setSelectedItem(v.getCategoria());
        txtAnio.setText(String.valueOf(v.getAnioLanzamiento()));
        lblVotos.setText(String.valueOf(v.getVotos()));
        habilitarEdicion(true);
    }

    private void guardarCambios() {
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Primero busque un videojuego por código.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = txtNombre.getText();
        String estudio = txtEstudio.getText();
        String categoria = (String) cmbCategoria.getSelectedItem();
        String anioTexto = txtAnio.getText();

        // Se valida todo antes de tocar el objeto: si algo falla no hay cambios parciales.
        String error = gestor.validarDatosEdicion(nombre, estudio, categoria, anioTexto);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "No se guardaron cambios",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = gestor.actualizarVideojuegoPorIndice(indiceSeleccionado, nombre, estudio,
                categoria, Integer.parseInt(anioTexto.trim()));
        if (!ok) {
            limpiarSeleccion();
            JOptionPane.showMessageDialog(this, "El videojuego ya no existe.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Videojuego actualizado correctamente.");
        alCambiar.run();   // Tab 1, búsqueda de texto y ranking muestran los cambios
    }

    /** Llamado por VentanaPrincipal cuando otra parte cambia los datos. */
    public void refrescar() {
        if (indiceSeleccionado == -1) return;
        int codigo = Integer.parseInt(lblCodigo.getText());
        int indice = gestor.busquedaBinaria(codigo);   // el índice puede cambiar tras registrar/borrar
        if (indice == -1) {
            limpiarSeleccion();
        } else {
            indiceSeleccionado = indice;
            lblVotos.setText(String.valueOf(gestor.obtenerPorIndice(indice).getVotos()));
        }
    }

    public void limpiarSeleccion() {
        indiceSeleccionado = -1;
        lblCodigo.setText("-");
        txtNombre.setText("");
        txtEstudio.setText("");
        cmbCategoria.setSelectedIndex(0);
        txtAnio.setText("");
        lblVotos.setText("-");
        habilitarEdicion(false);
    }

    private void habilitarEdicion(boolean activo) {
        txtNombre.setEnabled(activo);
        txtEstudio.setEnabled(activo);
        cmbCategoria.setEnabled(activo);
        txtAnio.setEnabled(activo);
        btnGuardar.setEnabled(activo);
    }

    private void agregarFila(int fila, String etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.gridx = 0;
        c.gridy = fila;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(campo, c);
    }
}
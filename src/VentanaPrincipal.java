import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JTabbedPane tabbedPane;

    // Tab 1: Catálogo y Registro (R1 y R2)
    private JPanel panelTab1;
    private JLabel lblTotal;
    private JList<String> listaCatalogo;
    private JTextField txtRegCodigo;
    private JTextField txtRegNombre;
    private JTextField txtRegEstudio;
    private JComboBox<String> cmbRegCategoria;
    private JTextField txtRegAnio;
    private JButton btnRegistrar;

    // Tab 2 Izquierda: Búsqueda lineal de texto (R3)
    private JPanel panelTab2;
    private JPanel panelIzq;
    private JTextField txtBuscarTexto;
    private JButton btnBuscarTexto;
    private JTextArea areaResultadosTexto;

    // Tab 2 Derecha: Búsqueda por código, edición, borrar y votar (R4 y R5)
    private JPanel panelDer;
    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JTextField txtEditCodigo;
    private JTextField txtEditNombre;
    private JTextField txtEditEstudio;
    private JComboBox<String> cmbEditCategoria;
    private JTextField txtEditAnio;
    private JTextField txtEditVotos;
    private JButton btnGuardarCambios;
    private JButton btnEliminar;
    private JSpinner spnVotos;
    private JButton btnRestarVotos;

    // Tab 3: Ranking (R6)
    private JPanel panelTab3;
    private JList<String> listaRanking;

    private final GestorVideojuegos gestor;
    private final DefaultListModel<String> modeloCatalogo;
    private final DefaultListModel<String> modeloRanking;
    private int indiceSeleccionado = -1;
    private String ultimaBusquedaTexto = "";

    public VentanaPrincipal(GestorVideojuegos gestor) {
        this.gestor = gestor;
        setContentPane(panelPrincipal);
        setTitle("GameVotes");
        setSize(980, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloCatalogo = new DefaultListModel<>();
        listaCatalogo.setModel(modeloCatalogo);

        modeloRanking = new DefaultListModel<>();
        listaRanking.setModel(modeloRanking);

        spnVotos.setModel(new SpinnerNumberModel(1, 1, 3, 1));
        limpiarEdicion();

        configurarEventos();
        refrescarCatalogo();
        refrescarRanking();
    }

    private void configurarEventos() {
        // R2: Registrar videojuego
        btnRegistrar.addActionListener(e -> registrar());

        // R3: Búsqueda lineal case-sensitive
        btnBuscarTexto.addActionListener(e -> {
            String texto = txtBuscarTexto.getText();
            if (texto == null || texto.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un texto válido para buscar.");
                return;
            }
            ultimaBusquedaTexto = texto;
            recalcularBusquedaTexto();
        });

        // R4: Buscar por código usando búsqueda binaria
        btnBuscarCodigo.addActionListener(e -> buscarPorCodigo());

        // R4: Guardar edición sin cambios parciales
        btnGuardarCambios.addActionListener(e -> guardarEdicion());

        // R4: Eliminar videojuego
        btnEliminar.addActionListener(e -> eliminarJuego());

        // R5: Restar votos con spinner de 1 a 3
        btnRestarVotos.addActionListener(e -> restarVotos());
    }

    private void registrar() {
        String codStr = txtRegCodigo.getText();
        String nombre = txtRegNombre.getText();
        String estudio = txtRegEstudio.getText();
        String categoria = (String) cmbRegCategoria.getSelectedItem();
        String anioStr = txtRegAnio.getText();

        String error = gestor.validarDatosRegistro(codStr, nombre, estudio, categoria, anioStr);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error);
            return;
        }

        int codigo = Integer.parseInt(codStr.trim());
        int anio = Integer.parseInt(anioStr.trim());

        if (gestor.registrarVideojuego(codigo, nombre, estudio, categoria, anio)) {
            txtRegCodigo.setText("");
            txtRegNombre.setText("");
            txtRegEstudio.setText("");
            txtRegAnio.setText("");
            limpiarEdicion();
            refrescarTodo();
            JOptionPane.showMessageDialog(this, "Videojuego registrado correctamente.");
        }
    }

    private void recalcularBusquedaTexto() {
        if (ultimaBusquedaTexto == null || ultimaBusquedaTexto.trim().isEmpty()) {
            areaResultadosTexto.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        List<Videojuego> lista = gestor.getCatalogo();
        for (int i = 0; i < lista.size(); i++) {
            Videojuego v = lista.get(i);
            if (v.getNombre().contains(ultimaBusquedaTexto) || v.getEstudio().contains(ultimaBusquedaTexto)) {
                sb.append(v.toString()).append("\n");
            }
        }
        if (sb.length() == 0) {
            areaResultadosTexto.setText("Sin coincidencias exactas para: " + ultimaBusquedaTexto);
        } else {
            areaResultadosTexto.setText(sb.toString());
        }
    }

    private void buscarPorCodigo() {
        String codStr = txtBuscarCodigo.getText();
        if (codStr == null || codStr.trim().isEmpty()) {
            limpiarEdicion();
            JOptionPane.showMessageDialog(this, "Ingrese un código.");
            return;
        }

        int codigo;
        try {
            codigo = Integer.parseInt(codStr.trim());
        } catch (NumberFormatException ex) {
            limpiarEdicion();
            JOptionPane.showMessageDialog(this, "El código debe ser un número entero.");
            return;
        }

        if (!gestor.esCodigoValido(codigo)) {
            limpiarEdicion();
            JOptionPane.showMessageDialog(this, "El código debe ser positivo.");
            return;
        }

        int idx = gestor.busquedaBinaria(codigo);
        if (idx == -1) {
            limpiarEdicion();
            JOptionPane.showMessageDialog(this, "No existe un videojuego con ese código.");
            return;
        }

        indiceSeleccionado = idx;
        cargarDatosEdicion(gestor.obtenerPorIndice(idx));
    }

    private void cargarDatosEdicion(Videojuego v) {
        if (v == null) {
            limpiarEdicion();
            return;
        }
        txtEditCodigo.setText(String.valueOf(v.getCodigoID()));
        txtEditNombre.setText(v.getNombre());
        txtEditEstudio.setText(v.getEstudio());
        cmbEditCategoria.setSelectedItem(v.getCategoria());
        txtEditAnio.setText(String.valueOf(v.getAnioLanzamiento()));
        txtEditVotos.setText(String.valueOf(v.getVotos()));

        txtEditNombre.setEnabled(true);
        txtEditEstudio.setEnabled(true);
        cmbEditCategoria.setEnabled(true);
        txtEditAnio.setEnabled(true);
        btnGuardarCambios.setEnabled(true);
        btnEliminar.setEnabled(true);
        spnVotos.setEnabled(true);
        btnRestarVotos.setEnabled(true);
    }

    private void limpiarEdicion() {
        indiceSeleccionado = -1;
        txtEditCodigo.setText("");
        txtEditNombre.setText("");
        txtEditEstudio.setText("");
        txtEditAnio.setText("");
        txtEditVotos.setText("");

        txtEditNombre.setEnabled(false);
        txtEditEstudio.setEnabled(false);
        cmbEditCategoria.setEnabled(false);
        txtEditAnio.setEnabled(false);
        btnGuardarCambios.setEnabled(false);
        btnEliminar.setEnabled(false);
        spnVotos.setEnabled(false);
        btnRestarVotos.setEnabled(false);
    }

    private void guardarEdicion() {
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Primero busque un videojuego por código.");
            return;
        }

        String nombre = txtEditNombre.getText();
        String estudio = txtEditEstudio.getText();
        String categoria = (String) cmbEditCategoria.getSelectedItem();
        String anioStr = txtEditAnio.getText();

        String error = gestor.validarDatosEdicion(nombre, estudio, categoria, anioStr);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error);
            return;
        }

        int anio = Integer.parseInt(anioStr.trim());
        if (gestor.actualizarVideojuegoPorIndice(indiceSeleccionado, nombre, estudio, categoria, anio)) {
            refrescarTodo();
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        }
    }

    private void eliminarJuego() {
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un videojuego válido.");
            return;
        }
        if (gestor.eliminarVideojuegoPorIndice(indiceSeleccionado)) {
            limpiarEdicion();
            txtBuscarCodigo.setText("");
            refrescarTodo();
            JOptionPane.showMessageDialog(this, "Videojuego eliminado.");
        }
    }

    private void restarVotos() {
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un videojuego válido.");
            return;
        }
        int cantidad = (Integer) spnVotos.getValue();
        if (gestor.restarVotosPorIndice(indiceSeleccionado, cantidad)) {
            Videojuego actualizado = gestor.obtenerPorIndice(indiceSeleccionado);
            txtEditVotos.setText(String.valueOf(actualizado.getVotos()));
            refrescarTodo();
        }
    }

    public void refrescarCatalogo() {
        modeloCatalogo.clear();
        for (Videojuego v : gestor.getCatalogo()) {
            modeloCatalogo.addElement(v.toString());
        }
        lblTotal.setText("Total de videojuegos: " + gestor.getCantidad());
    }

    public void refrescarRanking() {
        modeloRanking.clear();
        for (Videojuego v : gestor.rankingPorVotos()) {
            modeloRanking.addElement(v.formatoRanking());
        }
    }

    public void refrescarTodo() {
        refrescarCatalogo();
        recalcularBusquedaTexto();
        refrescarRanking();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestorVideojuegos gestor = new GestorVideojuegos();
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }
}
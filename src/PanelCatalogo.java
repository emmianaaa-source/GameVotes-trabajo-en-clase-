import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class PanelCatalogo extends JPanel {
    private final GestorVideojuegos gestor;
    private final DefaultListModel<Videojuego> modeloLista;
    private final JList<Videojuego> listaJuegos;
    private final JLabel etiquetaCantidad;
    private final JTextField campoCodigo;
    private final JTextField campoNombre;
    private final JTextField campoEstudio;
    private final JComboBox<String> comboCategoria;
    private final JTextField campoAnio;

    public PanelCatalogo(GestorVideojuegos gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(8, 8));

        modeloLista = new DefaultListModel<>();
        listaJuegos = new JList<>(modeloLista);
        etiquetaCantidad = new JLabel();
        add(new JLabel("Catálogo de videojuegos"), BorderLayout.NORTH);
        add(new JScrollPane(listaJuegos), BorderLayout.CENTER);

        campoCodigo = new JTextField();
        campoNombre = new JTextField();
        campoEstudio = new JTextField();
        comboCategoria = new JComboBox<>(GestorVideojuegos.CATEGORIAS);
        campoAnio = new JTextField();
        JButton botonRegistrar = new JButton("Registrar");

        JPanel formulario = new JPanel(new GridLayout(0, 2, 6, 6));
        formulario.add(new JLabel("Código:"));
        formulario.add(campoCodigo);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Estudio:"));
        formulario.add(campoEstudio);
        formulario.add(new JLabel("Categoría:"));
        formulario.add(comboCategoria);
        formulario.add(new JLabel("Año:"));
        formulario.add(campoAnio);
        formulario.add(etiquetaCantidad);
        formulario.add(botonRegistrar);
        add(formulario, BorderLayout.SOUTH);

        botonRegistrar.addActionListener(evento -> registrarVideojuego());
        refrescar();
    }

    private void registrarVideojuego() {
        String codigoTexto = campoCodigo.getText();
        String nombre = campoNombre.getText();
        String estudio = campoEstudio.getText();
        String categoria = (String) comboCategoria.getSelectedItem();
        String anioTexto = campoAnio.getText();

        String error = gestor.validarDatosRegistro(
                codigoTexto, nombre, estudio, categoria, anioTexto);
        if (error != null) {
            JOptionPane.showMessageDialog(
                    this, error, "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int codigo = Integer.parseInt(codigoTexto.trim());
        int anio = Integer.parseInt(anioTexto.trim());
        boolean registrado = gestor.registrarVideojuego(
                codigo, nombre, estudio, categoria, anio);
        if (!registrado) {
            JOptionPane.showMessageDialog(
                    this, "No se pudo registrar el videojuego.",
                    "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        limpiarFormulario();
        refrescar();
    }

    private void limpiarFormulario() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoEstudio.setText("");
        campoAnio.setText("");
        comboCategoria.setSelectedIndex(0);
    }

    public void refrescar() {
        modeloLista.clear();
        for (Videojuego juego : gestor.getCatalogo()) {
            modeloLista.addElement(juego);
        }
        etiquetaCantidad.setText("Cantidad: " + gestor.getCantidad());
    }
}
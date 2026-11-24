import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.List;


public class PanelRanking extends JPanel {

    private final GestorVideojuegos gestor;
    private final DefaultListModel<String> modeloRanking;
    private final JList<String> listaRanking;
    private final JLabel lblTotal;

    public PanelRanking(GestorVideojuegos gestor) {
        this.gestor = gestor;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Ranking de popularidad (de más a menos votos)");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        modeloRanking = new DefaultListModel<>();
        listaRanking = new JList<>(modeloRanking);
        listaRanking.setFont(new Font("Monospaced", Font.PLAIN, 14));
        listaRanking.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(listaRanking), BorderLayout.CENTER);

        lblTotal = new JLabel();
        JButton btnActualizar = new JButton("Actualizar ranking");
        btnActualizar.addActionListener(e -> refrescar());

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblTotal, BorderLayout.WEST);
        panelInferior.add(btnActualizar, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                refrescar();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });

        refrescar();
    }


    public void refrescar() {
        modeloRanking.clear();
        List<Videojuego> ranking = gestor.rankingPorVotos();

        int posicion = 1;
        for (Videojuego v : ranking) {
            modeloRanking.addElement(String.format("%2d.  Código: %-6d  |  %-25s  |  Votos: %d",
                    posicion, v.getCodigoID(), v.getNombre(), v.getVotos()));
            posicion++;
        }
        lblTotal.setText("Total: " + ranking.size() + " videojuegos");
    }
}
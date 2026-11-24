package com.gamevotes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;

public class VentanaPrincipal extends JFrame {
    private final GestorVideojuegos gestor;
    private final JTabbedPane tabs;

    private final JPanel tab1;
    private final JPanel tab2Izquierda;
    private final JPanel tab2Derecha;
    private final JPanel tab3;

    public VentanaPrincipal(GestorVideojuegos gestor) {
        this.gestor = gestor;
        setTitle("GameVotes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();

        tab1 = new JPanel();
        tabs.addTab("Catálogo", tab1);

        JPanel tab2 = new JPanel(new GridLayout(1, 2));
        tab2Izquierda = new JPanel();
        tab2Derecha = new JPanel();
        tab2.add(tab2Izquierda);
        tab2.add(tab2Derecha);
        tabs.addTab("Buscar / Editar", tab2);

        tab3 = new JPanel();
        tabs.addTab("Ranking", tab3);

        add(tabs);
    }

    public GestorVideojuegos getGestor() {
        return gestor;
    }

    public JPanel getTab1() {
        return tab1;
    }

    public JPanel getTab2Izquierda() {
        return tab2Izquierda;
    }

    public JPanel getTab2Derecha() {
        return tab2Derecha;
    }

    public JPanel getTab3() {
        return tab3;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestorVideojuegos g = new GestorVideojuegos();
            VentanaPrincipal v = new VentanaPrincipal(g);
            v.setVisible(true);
        });
    }
}
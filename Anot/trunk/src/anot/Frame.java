/*
 * Frame.java
 *
 * Created on April 16, 2008, 12:21 PM
 */
package anot;

import javax.swing.*;

/**
 *
 * @author  tgwizard
 */
public class Frame extends javax.swing.JFrame {

    public static void main(String[] args) {

        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // nothing
        }
        /*ActivityStore as = ActivityStoreBuilder.loadActivityStoreFromJar("activities.xml");
        System.out.println(as);
        ActivityStoreBuilder.saveActivityStoreToFile(as, "data/written.xml");
        System.out.println("---");
        ActivityStore as2 = ActivityStoreBuilder.loadActivityStoreFromFile("data/written.xml");
        System.out.println(as);*/

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }

    /** Creates new form Frame */
    public Frame() {
        initComponents();
        
        //TODO: error handling, popup-window etc.
        ActivityStore as = ActivityStoreBuilder.loadActivityStoreFromFile("data/activities.xml");
        
        diagramPanel.setActivityStore(as);
        addTabPanel.setActivityStore(as);
        viewTabPanel.setActivityStore(as);
        
        tabbedPane.setEnabledAt(1, false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        diagramPanel = new anot.DiagramPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        addTabPanel = new anot.AddTabPanel();
        viewTabPanel = new anot.ViewTabPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(610, 480));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout diagramPanelLayout = new javax.swing.GroupLayout(diagramPanel);
        diagramPanel.setLayout(diagramPanelLayout);
        diagramPanelLayout.setHorizontalGroup(
            diagramPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 619, Short.MAX_VALUE)
        );
        diagramPanelLayout.setVerticalGroup(
            diagramPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
        );

        getContentPane().add(diagramPanel);

        tabbedPane.setMaximumSize(new java.awt.Dimension(32767, 210));
        tabbedPane.setMinimumSize(new java.awt.Dimension(50, 210));
        tabbedPane.addTab("Add", addTabPanel);
        tabbedPane.addTab("View", viewTabPanel);

        getContentPane().add(tabbedPane);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        quitMenuItem.setMnemonic('Q');
        quitMenuItem.setText("Quit");
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");

        helpMenuItem.setText("Help");
        helpMenu.add(helpMenuItem);
        helpMenu.add(jSeparator1);

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private anot.AddTabPanel addTabPanel;
    private anot.DiagramPanel diagramPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JTabbedPane tabbedPane;
    private anot.ViewTabPanel viewTabPanel;
    // End of variables declaration//GEN-END:variables
}

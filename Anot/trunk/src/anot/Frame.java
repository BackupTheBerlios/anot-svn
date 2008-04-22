/*
 * Frame.java
 *
 * Created on April 16, 2008, 12:21 PM
 */
package anot;

import java.awt.Component;
import java.awt.event.*;
import java.io.FileNotFoundException;
import javax.swing.*;

/**
 * @author William Lundin Forssén <shazmodan@gmail.com>
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class Frame extends javax.swing.JFrame {

    public static void main(String[] args) {

        try {
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
    protected ActivityStore activityStore;

    /** Creates new form Frame */
    public Frame() {

        initComponents();

        quitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        aboutMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            //TODO: Pop-Up Description about the program.
            }
        });

        helpMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            //TODO: Pop-Up help!! YEAH!
            }
        });


        //TODO: error handling, popup-window etc.
        try {
            activityStore = ActivityStoreBuilder.loadActivityStoreFromFile("activities.xml");
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There are errors in the xml-data file. Please correct them (or remove the file):\n"
                    + e.getMessage(), "Errors in xml-file", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } finally {
            if (activityStore == null) {
                activityStore = new ActivityStore("activities.xml");
            }
        }

        DiagramPanel diagramPanel = new DiagramPanel();
        diagramPanel.setActivityStore(activityStore);
        scrollPane.setViewportView(diagramPanel);

        addTabPanel.setActivityStore(activityStore);
        viewTabPanel.setActivityStore(activityStore);

        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.setToolTipTextAt(0, "Add Tab (Alt+1)");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.setToolTipTextAt(1, "View Tab (Alt+2)");

        tabbedPane.setEnabledAt(1, false);
        activityStore.addSelectionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Activity a = activityStore.getSelectedActivity();
                if (a == null) {
                    tabbedPane.setEnabledAt(1, false);
                    tabbedPane.setSelectedIndex(0);
                } else {
                    tabbedPane.setEnabledAt(1, true);
                    tabbedPane.setSelectedIndex(1);
                }

            }
        });
        
        ButtonGroup group = new ButtonGroup();

        sortByDateMenuItem = new JRadioButtonMenuItem("Sort by date");
        group.add(sortByDateMenuItem);
        sortByDateMenuItem.setSelected(ActivityStore.createDateComparator().getType().equals(
                activityStore.getSortComparator().getType()));
        sortByDateMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activityStore.sortActivites(ActivityStore.createDateComparator());
            }
        });
        sortMenu.add(sortByDateMenuItem);

        sortBySubjectMenuItem = new JRadioButtonMenuItem("Sort by subject");
        group.add(sortBySubjectMenuItem);
        sortBySubjectMenuItem.setSelected(ActivityStore.createSubjectComparator().getType().equals(
                activityStore.getSortComparator().getType()));
        sortBySubjectMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activityStore.sortActivites(ActivityStore.createSubjectComparator());
            }
        });
        sortMenu.add(sortBySubjectMenuItem);

        sortMenu.add(new JPopupMenu.Separator());

        reverseSortMenuItem = new JCheckBoxMenuItem("Reverse sort");
        reverseSortMenuItem.setState(activityStore.isReverseSort());
        reverseSortMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (activityStore.isReverseSort()) {
                    activityStore.setReverseSort(false);
                } else {
                    activityStore.setReverseSort(true);
                }
            }
        });
        sortMenu.add(reverseSortMenuItem);
        
        /*JPopupMenu popup = new JPopupMenu();
        
        Component[] cs = sortMenu.getComponents();
        
        for (Component c : cs)
            popup.add(c);
        popup.validate();*/
        diagramPanel.setPopupMenu(sortMenu.getPopupMenu());
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        tabbedPane = new javax.swing.JTabbedPane();
        addTabPanel = new anot.AddTabPanel();
        viewTabPanel = new anot.ViewTabPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        sortMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(610, 480));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        getContentPane().add(scrollPane);

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

        sortMenu.setMnemonic('S');
        sortMenu.setText("Sort");
        menuBar.add(sortMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");

        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setMnemonic('H');
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
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem quitMenuItem;
    protected javax.swing.JScrollPane scrollPane;
    private javax.swing.JMenu sortMenu;
    private javax.swing.JTabbedPane tabbedPane;
    private anot.ViewTabPanel viewTabPanel;
    // End of variables declaration//GEN-END:variables

    private JCheckBoxMenuItem reverseSortMenuItem;
    private JRadioButtonMenuItem sortByDateMenuItem;
    private JRadioButtonMenuItem sortBySubjectMenuItem;
}

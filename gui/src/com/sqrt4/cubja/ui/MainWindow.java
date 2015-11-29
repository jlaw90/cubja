/*
 * Created by JFormDesigner on Tue Jul 29 10:22:58 BST 2014
 */

package com.sqrt4.cubja.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import com.sqrt4.cubja.model.GameCubeImage;
import com.sqrt4.cubja.model.fst.DirNode;
import com.sqrt4.cubja.model.fst.FSNode;
import com.sqrt4.cubja.model.fst.FileNode;
import com.sqrt4.cubja.ui.component.HexEditorPanel;
import org.netbeans.swing.outline.*;

/**
 * @author James Lawrence
 */
public class MainWindow extends JFrame {
    private JFileChooser fileDialog;
    private FSTreeModel treeModel;
    private BusyDialog _busy;
    private GameCubeImage image;

    public MainWindow() {
        initComponents();
        fileTree.setRenderDataProvider(new RenderDataProvider() {
            public String getDisplayName(Object o) {
                if (o instanceof FSNode) {
                    if (((FSNode) o).name == null)
                        return "root";
                    return ((FSNode) o).name;
                }

                if (o instanceof String)
                    return (String) o;
                return String.valueOf(o);
            }

            public boolean isHtmlDisplayName(Object o) {
                return false;
            }

            public Color getBackground(Object o) {
                return fileTree.getBackground();
            }

            public Color getForeground(Object o) {
                return fileTree.getForeground();
            }

            public String getTooltipText(Object o) {
                return null;
            }

            public Icon getIcon(Object o) {
                if(o instanceof DirNode)
                    return null; // Use default

                try {
                    return FileSystemView.getFileSystemView().getSystemIcon(File.createTempFile("icon", ((FSNode) o).name));
                } catch (IOException e) {
                    return null;
                }
            }
        });
        fileTree.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting())
                    return;
                int row = fileTree.getSelectionModel().getLeadSelectionIndex();
                Object selObj = fileTree.getModel().getValueAt(fileTree.convertRowIndexToModel(row), 0);
                if (selObj == null || !(selObj instanceof FSNode))
                    return;
                FSNode selected = (FSNode) selObj;
                nodeSelected(selected);
            }
        });
    }

    private void nodeSelected(final FSNode node) {
        runAsyncWithPopup("Loading " + node.name + "...", new Runnable() {
            @Override
            public void run() {
                if(node instanceof FileNode) {
                    try {
                        HexEditorPanel hep = new HexEditorPanel((FileNode) node);
                        editorPanel.removeAll();
                        editorPanel.add(hep);

                        editorPanel.invalidate();
                        editorPanel.revalidate();
                        editorPanel.repaint();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, false);
    }

    public void runAsyncWithPopup(String message, final Runnable r, boolean canCancel) {
        if(_busy != null) {
            System.err.println("Async overlap!");
        }
        final Thread asyncThread = new Thread() {
            public void run() {
                while (_busy == null || !_busy.isVisible()) {
                    try {
                        Thread.sleep(10); // Wait until the busy dialog is shown...
                    } catch (InterruptedException ignore) {
                    }
                }
                try {
                    r.run(); // Do it...

                } catch (Throwable e) {
                    e.printStackTrace(); // Catch any errors
                } finally {
                    _busy.setVisible(false);
                    _busy = null;
                }
            }
        };
        asyncThread.setDaemon(true);
        asyncThread.start();
        try {
            if (_busy == null)
                _busy = new BusyDialog(this);
            _busy.setCancellable(canCancel);
            _busy.setCancelCallback(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    asyncThread.interrupt();
                }
            });
            _busy.setTitle("Please wait...");
            _busy.setMessage(message);
            _busy.pack();
            _busy.setVisible(true);
        } catch (Throwable t) {
            if (_busy != null) {
                _busy.setVisible(false);
                _busy = null;
            }
            t.printStackTrace();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem2 = new JMenuItem();
        menuItem1 = new JMenuItem();
        panel1 = new JPanel();
        label1 = new JLabel();
        toolBar1 = new JToolBar();
        button1 = new JButton();
        splitPane1 = new JSplitPane();
        panel2 = new JPanel();
        fileSearch = new JTextField();
        scrollPane1 = new JScrollPane();
        fileTree = new Outline();
        editorPanel = new JPanel();
        label2 = new JLabel();
        exitAction = new ExitAction();
        openAction = new OpenAction();

        //======== this ========
        setTitle("Cubja");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("File");
                menu1.setMnemonic('F');

                //---- menuItem2 ----
                menuItem2.setAction(openAction);
                menuItem2.setMnemonic('O');
                menu1.add(menuItem2);
                menu1.addSeparator();

                //---- menuItem1 ----
                menuItem1.setAction(exitAction);
                menuItem1.setMnemonic('X');
                menu1.add(menuItem1);
            }
            menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout(5, 0));

            //---- label1 ----
            label1.setText("Information bar");
            panel1.add(label1, BorderLayout.WEST);
        }
        contentPane.add(panel1, BorderLayout.SOUTH);

        //======== toolBar1 ========
        {

            //---- button1 ----
            button1.setText("text");
            button1.setAction(openAction);
            toolBar1.add(button1);
        }
        contentPane.add(toolBar1, BorderLayout.NORTH);

        //======== splitPane1 ========
        {
            splitPane1.setOneTouchExpandable(true);
            splitPane1.setResizeWeight(0.2);

            //======== panel2 ========
            {
                panel2.setLayout(new BorderLayout());
                panel2.add(fileSearch, BorderLayout.NORTH);

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(fileTree);
                }
                panel2.add(scrollPane1, BorderLayout.CENTER);
            }
            splitPane1.setLeftComponent(panel2);

            //======== editorPanel ========
            {
                editorPanel.setLayout(new BorderLayout());

                //---- label2 ----
                label2.setText("Open an ISO and select a file to begin");
                label2.setHorizontalAlignment(SwingConstants.CENTER);
                editorPanel.add(label2, BorderLayout.CENTER);
            }
            splitPane1.setRightComponent(editorPanel);
        }
        contentPane.add(splitPane1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void open(File f) {
        try {
            image = new GameCubeImage(Files.newByteChannel(f.toPath(), StandardOpenOption.READ));

            treeModel = new FSTreeModel();
            fileTree.setModel(DefaultOutlineModel.createOutlineModel(treeModel, treeModel, true, "File"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JFileChooser getFileDialog() {
        if (fileDialog == null) {
            fileDialog = new JFileChooser();
            fileDialog.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    String name = f.getName().toLowerCase();
                    return name.endsWith(".iso") || name.endsWith(".gcm");
                }

                public String getDescription() {
                    return "GameCube image";
                }
            });
        }
        return fileDialog;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem1;
    private JPanel panel1;
    private JLabel label1;
    private JToolBar toolBar1;
    private JButton button1;
    private JSplitPane splitPane1;
    private JPanel panel2;
    private JTextField fileSearch;
    private JScrollPane scrollPane1;
    private Outline fileTree;
    private JPanel editorPanel;
    private JLabel label2;
    private ExitAction exitAction;
    private OpenAction openAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class FSTreeModel implements TreeModel, RowModel {
        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueFor(Object o, int i) {
            switch (i) {
                case 0:
                    if (o instanceof DirNode)
                        return "<dir>";
                    return ((FSNode) o).name.substring(((FSNode) o).name.lastIndexOf('.') + 1).toUpperCase();
                case 1:
                    if (o instanceof DirNode)
                        return null;
                    return ((FileNode) o).length;
            }
            return "???";
        }

        @Override
        public Class getColumnClass(int i) {
            switch (i) {
                case 1:
                    return Long.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(Object o, int i) {
            return false;
        }

        @Override
        public void setValueFor(Object o, int i, Object o2) {

        }

        @Override
        public String getColumnName(int i) {
            switch (i) {
                case 0:
                    return "Type";
                case 1:
                    return "Size";
            }
            return "???";
        }

        @Override
        public Object getRoot() {
            return image.directoryTree;
        }

        @Override
        public Object getChild(Object parent, int index) {
            return ((DirNode) parent).children.get(index);
        }

        @Override
        public int getChildCount(Object parent) {
            if (parent instanceof DirNode)
                return ((DirNode) parent).children.size();
            return 0;
        }

        @Override
        public boolean isLeaf(Object node) {
            return !(node instanceof DirNode);
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {

        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            return ((DirNode) parent).children.indexOf(child);
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {

        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {

        }
    }

    private class ExitAction extends AbstractAction {
        private ExitAction() {
            // JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner non-commercial license
            putValue(NAME, "Exit");
            putValue(SHORT_DESCRIPTION, "Close the application");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
            // JFormDesigner - End of action initialization  //GEN-END:initComponents
        }

        public void actionPerformed(ActionEvent e) {
            MainWindow.this.hide();
        }
    }

    private class OpenAction extends AbstractAction {
        private OpenAction() {
            // JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner non-commercial license
            putValue(NAME, "Open");
            putValue(SHORT_DESCRIPTION, "Open a GCM/ISO");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
            // JFormDesigner - End of action initialization  //GEN-END:initComponents
        }

        public void actionPerformed(ActionEvent e) {
            if (getFileDialog().showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                // Todo: open file
                File f = fileDialog.getSelectedFile();
                open(f);
            }
        }
    }
}

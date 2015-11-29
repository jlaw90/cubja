/*
 * Created by JFormDesigner on Tue Jul 29 16:02:36 BST 2014
 */

package com.sqrt4.cubja.ui.component;

import java.awt.event.*;
import com.sqrt4.cubja.model.fst.FileNode;
import javafx.scene.control.SelectionModel;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * @author James Lawrence
 */
public class HexEditorPanel extends EditorPanel {
    public HexEditorPanel() {
        initComponents();
    }

    public HexEditorPanel(FileNode node) throws IOException {
        super(node);
        initComponents();

        asciiScroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        int amount = asciiScroller.getVerticalScrollBar().getValue();
                        hexScroller.getVerticalScrollBar().setValue(amount);
                    }
                });
            }
        });

        final AtomicBoolean scrollLock = new AtomicBoolean();
        hexTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if(scrollLock.get()) {
                            scrollLock.set(false);
                            return;
                        }
                        scrollLock.set(true);
                        ListSelectionModel s = hexTable.getSelectionModel();
                        ListSelectionModel d = asciiTable.getSelectionModel();
                        d.setSelectionInterval(s.getMinSelectionIndex(), s.getMaxSelectionIndex());
                        d.setAnchorSelectionIndex(s.getAnchorSelectionIndex());
                        d.setLeadSelectionIndex(s.getLeadSelectionIndex());
                    }
                });
            }
        });

        asciiTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if(scrollLock.get()) {
                            scrollLock.set(false);
                            return;
                        }
                        scrollLock.set(true);
                        ListSelectionModel s = asciiTable.getSelectionModel();
                        ListSelectionModel d = hexTable.getSelectionModel();
                        d.setSelectionInterval(s.getMinSelectionIndex(), s.getMaxSelectionIndex());
                        d.setAnchorSelectionIndex(s.getAnchorSelectionIndex());
                        d.setLeadSelectionIndex(s.getLeadSelectionIndex());
                    }
                });
            }
        });

        final TableCellRenderer delegate = hexTable.getTableHeader().getDefaultRenderer();
        final TableCellRenderer header = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        hexTable.getTableHeader().setDefaultRenderer(header);
        asciiTable.getTableHeader().setDefaultRenderer(header);

        hexTable.setDefaultRenderer(String.class, new HexTableCellRenderer());
        asciiTable.setDefaultRenderer(String.class, new HexTableCellRenderer());
        hexTable.setModel(new HexTableModel(true));
        asciiTable.setModel(new HexTableModel(false));

        hexTable.getColumnModel().getColumn(0).setWidth(hexTable.getFontMetrics(hexTable.getFont()).stringWidth("000000"));
    }

    private void createUIComponents() {
        hexTable = new JTable() {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Object value = getValueAt(row, column);

                boolean isSelected = false;
                boolean hasFocus = false;

                // Only indicate the selection and focused cell if not printing
                if (!isPaintingForPrint()) {
                    isSelected = isCellSelected(row, column);

                    boolean rowIsLead =
                            (selectionModel.getLeadSelectionIndex() == row);
                    boolean colIsLead =
                            (columnModel.getSelectionModel().getLeadSelectionIndex() == column);

                    hasFocus = (rowIsLead && colIsLead) && isFocusOwner();
                }

                if(column == 0) {
                    Component c = getTableHeader().getDefaultRenderer().getTableCellRendererComponent(this, value, isSelected, hasFocus, row, column);
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                    return c;
                }
                return renderer.getTableCellRendererComponent(this, value, isSelected, hasFocus, row, column);
            }
        };
    }

    private void hexScrollerMouseWheelMoved(MouseWheelEvent e) {
        asciiScroller.dispatchEvent(e);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        createUIComponents();

        splitPane1 = new JSplitPane();
        hexScroller = new JScrollPane();
        asciiScroller = new JScrollPane();
        asciiTable = new JTable();

        //======== this ========
        setLayout(new BorderLayout());

        //======== splitPane1 ========
        {
            splitPane1.setResizeWeight(0.5);
            splitPane1.setOneTouchExpandable(true);

            //======== hexScroller ========
            {
                hexScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                hexScroller.addMouseWheelListener(new MouseWheelListener() {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        hexScrollerMouseWheelMoved(e);
                    }
                });

                //---- hexTable ----
                hexTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                hexScroller.setViewportView(hexTable);
            }
            splitPane1.setLeftComponent(hexScroller);

            //======== asciiScroller ========
            {

                //---- asciiTable ----
                asciiTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                asciiScroller.setViewportView(asciiTable);
            }
            splitPane1.setRightComponent(asciiScroller);
        }
        add(splitPane1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSplitPane splitPane1;
    private JScrollPane hexScroller;
    private JTable hexTable;
    private JScrollPane asciiScroller;
    private JTable asciiTable;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class HexTableModel implements TableModel {
        private final boolean hex;

        public HexTableModel(boolean hex) {
            this.hex = hex;
        }

        public int getRowCount() {
            return (data.capacity() + 15) / 16;
        }

        public int getColumnCount() {
            return 16 + (hex? 1: 0);
        }

        public String getColumnName(int columnIndex) {
            if(hex && columnIndex == 0)
                return null;
            return Integer.toHexString(columnIndex - (hex? 1: 0)).toUpperCase();
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if(hex && columnIndex == 0)
                return Integer.toHexString(rowIndex).toUpperCase();

            if(hex)
                columnIndex -= 1;

            int off = (rowIndex << 4) + columnIndex;
            if(off >= data.capacity())
                return "";
            byte b = data.get(off);
            if(hex)
                return String.format("%02X", b);
            char c = (char) b;
            return Character.isISOControl(c)? ".": c;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        public void addTableModelListener(TableModelListener l) {

        }

        public void removeTableModelListener(TableModelListener l) {

        }
    }

    private class HexTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(c instanceof JLabel)
                ((JLabel) c).setHorizontalTextPosition(JLabel.CENTER);

            return c;
        }
    }
}
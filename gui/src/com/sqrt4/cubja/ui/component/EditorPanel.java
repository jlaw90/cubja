/*
 * Created by JFormDesigner on Tue Jul 29 15:59:57 BST 2014
 */

package com.sqrt4.cubja.ui.component;

import com.sqrt4.cubja.model.fst.FileNode;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.swing.*;

/**
 * @author James Lawrence
 */
public abstract class EditorPanel extends JPanel {
    public FileNode file;
    public ByteBuffer data;

    public EditorPanel() {
        initComponents();
    }

    public EditorPanel(FileNode node) throws IOException {
        super();
        file = node;
        data = file.readData();
    }

    public String getTitle() {
        return "Unregistered Editor";
    }

    public Icon getIcon() {
        return UIManager.getIcon("Tree.leafNode");
    }



    public boolean isModified() {
        return false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license

        //======== this ========
        setLayout(new BorderLayout());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

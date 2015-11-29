/*
 * Created by JFormDesigner on Tue Jul 29 16:51:01 BST 2014
 */

package com.sqrt4.cubja.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author James Lawrence
 */
public class BusyDialog extends JDialog {
    private Action cancelCallback;
    private boolean canCancel;

    public BusyDialog(Frame owner) {
        super(owner);
        initComponents();
    }

    public BusyDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    public void setMessage(String msg) {
        label1.setText(msg);
        pack();
    }

    public void setCancelCallback(Action callback) {
        this.cancelCallback = callback;
    }

    public void setCancellable(boolean b) {
        canCancel = b;
        cancelAction.setEnabled(canCancel);
        pack();
    }

    private void thisKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && cancelAction.isEnabled())
            cancelAction.actionPerformed(null);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        progressBar1 = new JProgressBar();
        label1 = new JLabel();
        panel1 = new JPanel();
        cancelButton = new JButton();
        cancelAction = new CancelAction();

        //======== this ========
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                thisKeyPressed(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- progressBar1 ----
        progressBar1.setIndeterminate(true);
        contentPane.add(progressBar1, BorderLayout.CENTER);

        //---- label1 ----
        label1.setText("text");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(label1, BorderLayout.NORTH);

        //======== panel1 ========
        {
            panel1.setLayout(new FlowLayout());

            //---- cancelButton ----
            cancelButton.setVisible(false);
            cancelButton.setAction(cancelAction);
            panel1.add(cancelButton);
        }
        contentPane.add(panel1, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JProgressBar progressBar1;
    private JLabel label1;
    private JPanel panel1;
    private JButton cancelButton;
    private CancelAction cancelAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class CancelAction extends AbstractAction {
        private CancelAction() {
            // JFormDesigner - Action initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner non-commercial license
            putValue(NAME, "Cancel");
            putValue(SHORT_DESCRIPTION, "Cancel this operation");
            // JFormDesigner - End of action initialization  //GEN-END:initComponents
        }

        public void actionPerformed(ActionEvent e) {
            if (cancelCallback != null)
                cancelCallback.actionPerformed(new ActionEvent(this, 0, "cancel"));
        }
    }
}

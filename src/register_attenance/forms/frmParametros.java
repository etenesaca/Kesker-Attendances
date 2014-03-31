/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance.forms;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import register_attenance.clsConnection_to_OERP;
import register_attenance.gl;

/**
 *
 * @author edgar
 */
public class frmParametros extends javax.swing.JDialog {

    /**
     * Creates new form frmParametros
     */
    public frmParametros(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    frmParametros() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        txtPort = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnTestConnection = new javax.swing.JButton();
        cmbDb = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configurar los parámetros de Conexión");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel2.setText("Host:");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel3.setText("Puerto:");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel4.setText("Base de Datos:");

        txtHost.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtHostCaretUpdate(evt);
            }
        });
        txtHost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHostActionPerformed(evt);
            }
        });
        txtHost.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                txtHostHierarchyChanged(evt);
            }
        });
        txtHost.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtHostInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                txtHostCaretPositionChanged(evt);
            }
        });
        txtHost.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtHostPropertyChange(evt);
            }
        });
        txtHost.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                txtHostVetoableChange(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/kanban-apply.png"))); // NOI18N
        btnGuardar.setMnemonic('G');
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/STOCK_CANCEL.png"))); // NOI18N
        btnCancelar.setMnemonic('C');
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnTestConnection.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        btnTestConnection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/gtk-refresh.png"))); // NOI18N
        btnTestConnection.setMnemonic('P');
        btnTestConnection.setText("Probar Conexión");
        btnTestConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestConnectionActionPerformed(evt);
            }
        });

        cmbDb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDb.setName(""); // NOI18N
        cmbDb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDbActionPerformed(evt);
            }
        });
        cmbDb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbDbFocusGained(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTestConnection, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPort)
                            .addComponent(txtHost)
                            .addComponent(cmbDb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbDb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)
                    .addComponent(btnTestConnection))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private boolean validar_cajas(){
        boolean resp;
        if ("".equals(this.txtHost.getText())){
            JOptionPane.showMessageDialog(null, "Primero Ingresa la Dirección del Host","Aviso", JOptionPane.WARNING_MESSAGE);
            this.txtHost.requestFocus();
            resp = false;
        }
        else{
            if ("".equals(this.txtPort.getText())){
                JOptionPane.showMessageDialog(null, "Ingresa el número del Puerto","Aviso", JOptionPane.WARNING_MESSAGE);
                this.txtPort.requestFocus();
                resp = false;
            }
            else{
               String selected_db = "";
               try {
                 selected_db = cmbDb.getSelectedItem().toString();
               } catch (Exception e) {}
               if ("".equals(selected_db)){
                    JOptionPane.showMessageDialog(null, "Ingresa el Nombre de la Base de Datos a la cual se va a conectar.","Aviso", JOptionPane.WARNING_MESSAGE);
                    this.cmbDb.requestFocus();
                    resp = false;
                }
                else{
                   resp = true; 
                }
            }
        }
        return resp;
    }
    void Test_Connection(){
        clsConnection_to_OERP con_oerp = new clsConnection_to_OERP();
        String selected_db = "";
        try {
          selected_db = cmbDb.getSelectedItem().toString();
        } catch (Exception e) {}
        String resp_login = con_oerp.login("test","6JtigKav8QQkqOpCcE3TMYYKtRtJzGG3hxcIAEe/ywQ=", txtHost.getText(), Integer.parseInt(txtPort.getText()), selected_db);
        if ("error_conexion".equals(resp_login)){
            JOptionPane.showMessageDialog(null, "Error de Conexión.","Error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            if ("error_login".equals(resp_login)){
                JOptionPane.showMessageDialog(null, "Error de Conexión.","Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Conexión Correcta." ,"Ok", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    void Aceptar(){
        if (this.validar_cajas()){
            Object [] opciones ={"Aceptar","Cancelar"};
            JOptionPane nombreDelDialogo= new JOptionPane();
            int eleccion = JOptionPane.showOptionDialog(null,
            "¿Estás seguro de cambiar los parametros de conexión del Sistema?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            "Aceptar");
            if (eleccion == JOptionPane.YES_OPTION)
            {
                String selected_db = "";
                try {
                  selected_db = cmbDb.getSelectedItem().toString();
                } catch (Exception e) {}
                try {
                    gl.setHost(txtHost.getText());
                    gl.setPort(txtPort.getText());
                    gl.setDb(selected_db);
                    this.dispose();
                } catch (IOException ex) {
                    Logger.getLogger(frmParametros.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void centrarVentana() {
        // Se obtienen las dimensiones en pixels de la pantalla.
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        // Se obtienen las dimensiones en pixels de la ventana.
        Dimension ventana = getSize();
        // Una cuenta para situar la ventana en el centro de la pantalla.
        setLocation((pantalla.width - ventana.width) / 2,(pantalla.height - ventana.height) / 2);
    }
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        centrarVentana();
        txtHost.setText(gl.getHost());
        txtPort.setText("" + gl.getPort());
        
        recargar_databases();
        cmbDb.setSelectedItem(gl.getDb());
    }//GEN-LAST:event_formWindowOpened

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Aceptar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnTestConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestConnectionActionPerformed
        if (this.validar_cajas()){
            Test_Connection();
        }
    }//GEN-LAST:event_btnTestConnectionActionPerformed

    private void cmbDbFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbDbFocusGained
        String current_db = "";
        try {
            current_db = cmbDb.getSelectedItem().toString();
        } catch (Exception e) {}
        recargar_databases();
        cmbDb.setSelectedItem(current_db);
    }//GEN-LAST:event_cmbDbFocusGained

    private void txtHostInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtHostInputMethodTextChanged
    }//GEN-LAST:event_txtHostInputMethodTextChanged

    private void txtHostCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtHostCaretPositionChanged
    }//GEN-LAST:event_txtHostCaretPositionChanged

    private void txtHostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHostActionPerformed
    }//GEN-LAST:event_txtHostActionPerformed

    private void txtHostHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_txtHostHierarchyChanged
    }//GEN-LAST:event_txtHostHierarchyChanged

    private void txtHostCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtHostCaretUpdate
    }//GEN-LAST:event_txtHostCaretUpdate

    private void txtHostVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_txtHostVetoableChange
    }//GEN-LAST:event_txtHostVetoableChange

    private void txtHostPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtHostPropertyChange
    }//GEN-LAST:event_txtHostPropertyChange

    private void cmbDbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDbActionPerformed
    
    private void recargar_databases(){
        clsConnection_to_OERP con_oerp = new clsConnection_to_OERP();
        Vector<String> DatabaseList = con_oerp.getDatabaseList(txtHost.getText(),Integer.parseInt(txtPort.getText()));
        cmbDb.removeAllItems();
        for (String db : DatabaseList){
            cmbDb.addItem(db);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmParametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmParametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmParametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmParametros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmParametros dialog = new frmParametros(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnTestConnection;
    private javax.swing.JComboBox cmbDb;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables
}
 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance.forms;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import register_attenance.OpenERP;
import register_attenance.gl;
import register_attenance.hupernikao;

/**
 *
 * @author edgar
 */
public class frmOK extends javax.swing.JDialog {

    /**
     * Creates new form frmOK
     *
     * @param parent
     * @param modal
     */
    public frmOK(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jFrame1 = new javax.swing.JFrame();
        lblHora_de_registro = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblPhoto = new javax.swing.JLabel();
        lblPuntos = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstActivities = new jclist.jcList();
        btnOK = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblHora_de_registro.setText("Hora de Registro : 15:30");

        lblNombre.setText("Colaborador");

        lblPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/person_dark.png"))); // NOI18N
        lblPhoto.setPreferredSize(new java.awt.Dimension(64, 64));

        lblPuntos.setText("Puntos");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Puntos:");

        jLabel5.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Hora de registro:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tareas Asignadas"));
        jPanel2.setToolTipText("");
        jPanel2.setName(""); // NOI18N

        lstActivities.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstActivities.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstActivities.setAlignmentX(2.6F);
        lstActivities.setColorNoSeleccionado(new java.awt.Color(13, 111, 16));
        lstActivities.setColorSeleccionado(new java.awt.Color(13, 111, 16));
        lstActivities.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        lstActivities.setIconoNoSeleccionado(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/check_16.png"))); // NOI18N
        lstActivities.setIconoSeleccionado(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/check_16.png"))); // NOI18N
        jScrollPane1.setViewportView(lstActivities);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/kanban-apply.png"))); // NOI18N
        btnOK.setMnemonic('A');
        btnOK.setText("Aceptar");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        btnOK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOKKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHora_de_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lblPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombre)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPuntos)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHora_de_registro)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOK)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void centrarVentana() {
        // Se obtienen las dimensiones en pixels de la pantalla.
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        // Se obtienen las dimensiones en pixels de la ventana.
        Dimension ventana = getSize();
        // Una cuenta para situar la ventana en el centro de la pantalla.
        setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
    }

    private static Image getImage(byte[] bytes, boolean isThumbnail) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator readers = ImageIO.getImageReadersByFormatName("png");
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis; // File or InputStream
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        if (isThumbnail) {
            param.setSourceSubsampling(4, 4, 0, 0);
        }
        Image img = reader.read(0, param);
        return img;
    }

    class RoundedBorder extends AbstractBorder {

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.red);
            int arc = 10000;
            g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc);
        }
    }

    private class getCollaboratorPhoto extends Thread {

        int collaborator_id;

        public getCollaboratorPhoto(int collaborator_id) {
            this.collaborator_id = collaborator_id;
        }

        public void getPhoto() {
            OpenERP oerp = hupernikao.BuildOpenERPConnection();
            String photo_field = "photo_medium";
            HashMap<String, Object> Collaborator = oerp.read("kemas.collaborator", this.collaborator_id, new String[]{photo_field});
            String photo_str = Collaborator.get(photo_field).toString();
            byte[] foto = hupernikao.DecodeB64ToBytes(photo_str);
            if (foto != null) {
                ImageIcon Photo = hupernikao.ReziseImage(foto, 64);
                lblPhoto.setIcon(Photo);
            }
        }

        @Override
        public void run() {
            getPhoto();
        }
    }
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        centrarVentana();
        String hora_str;
        Calendar Cal = Calendar.getInstance();
        //String fec= Cal.get(Cal.DATE)+"/"+(Cal.get(Cal.MONTH)+1)+"/"+Cal.get(Cal.YEAR)+" "+Cal.get(Cal.HOUR_OF_DAY)+":"+Cal.get(Cal.MINUTE)+":"+Cal.get(Cal.SECOND);
        //Capturar Hora
        String hora = "" + Cal.get(Calendar.HOUR_OF_DAY);
        if (hora.length() < 2) {
            hora = "0" + hora;
        }
        //Capturar Minutos
        String minutos = "" + Cal.get(Calendar.MINUTE);
        if (minutos.length() < 2) {
            minutos = "0" + minutos;
        }
        //Capturar Segundos
        String segundos = "" + Cal.get(Calendar.SECOND);
        if (segundos.length() < 2) {
            segundos = "0" + segundos;
        }

        hora_str = hora + ":" + minutos + ":" + segundos;
        //lblCodigo.setText(gl.Login_Collaborator.getCode());
        lblNombre.setText(gl.Login_Collaborator.getShortName());
        lblPuntos.setText("" + gl.Login_Collaborator.getPoint());
        lblHora_de_registro.setText(hora_str);

        //Mostrar la foto del colaborador
        ImageIcon person = new ImageIcon(getClass().getResource("/Imagenes/person_dark.png"));
        ImageIcon CollaboratorAvatar = hupernikao.ReziseImage(person.getImage(), 96);
        lblPhoto.setIcon(CollaboratorAvatar);
        new getCollaboratorPhoto(Integer.parseInt(gl.Login_Collaborator.getId())).start();

        //Listar las actividades asignadas
        OpenERP oerp = hupernikao.BuildOpenERPConnection();

        int event_id = gl.Current_event.getId();
        //Object res = oerp.read("kemas.event", Long.parseLong(gl.Current_event.getId() + ""), new String[]{"service_id"});
        ArrayList<Object> args = new ArrayList<Object>();
        args.add(new Object[]{"event_id", "=", event_id});
        args.add(new Object[]{"collaborator_id", "=", Integer.parseInt(gl.Login_Collaborator.getId())});

        String model = "kemas.event.collaborator.line";
        Long[] line_ids = oerp.search(model, args);
        List<HashMap<String, Object>> lines = oerp.read(model, line_ids, new String[]{"activity_ids"});

        DefaultListModel mdlactivities = new DefaultListModel();
        int num_activities = 0;
        for (HashMap<String, Object> line : lines) {
            Object[] activity_ids = (Object[]) line.get("activity_ids");
            List<HashMap<String, Object>> activities = oerp.read("kemas.activity", activity_ids, new String[]{"name"});
            for (HashMap<String, Object> activity : activities) {
                mdlactivities.addElement(activity.get("name"));
                num_activities++;
            }
        }
        if (num_activities == 0) {
            mdlactivities.addElement("");
            mdlactivities.addElement("");
            mdlactivities.addElement("<html><b><i>     --No hay actividades asignadas-- </i></b></html>");
            lstActivities.setIconoSeleccionado(null);
            lstActivities.setIconoNoSeleccionado(null);
            lstActivities.setColorSeleccionado(Color.red);
            lstActivities.setColorNoSeleccionado(Color.red);
        }
        lstActivities.setModel(mdlactivities);

        this.setTitle("Registro de asistencia agregado Correctamente.");
        btnOK.requestFocus();
    }//GEN-LAST:event_formWindowOpened

    private void btnOKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOKKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 13) {
            this.dispose();
        }
    }//GEN-LAST:event_btnOKKeyPressed

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
            java.util.logging.Logger.getLogger(frmOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmOK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frmOK dialog = new frmOK(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnOK;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHora_de_registro;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPhoto;
    private javax.swing.JLabel lblPuntos;
    private jclist.jcList lstActivities;
    // End of variables declaration//GEN-END:variables
}

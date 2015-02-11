/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import register_attenance.Collaborator;
import register_attenance.Event;
import register_attenance.OpenERP;
import register_attenance.gl;
import register_attenance.Threads;
import register_attenance.hupernikao;
import register_attenance.OpenERP.RespRegistrarAsisitencia;

/**
 *
 * @author edgar
 */
public class frmRegister_attendance extends javax.swing.JFrame {

    Timer timer_obj;

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/icon.png"));
        return retValue;
    }

    public class IconCellRenderer extends DefaultTableCellRenderer {

        /**
         * Acá redefinimos como se muestra, vemos q ahora lo forzamos a trabajar
         * con JLabel, pero si no lo es, por ejemplo un String igual lo muestro
         * llamando a Super
         *
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                JLabel label = (JLabel) value;
                label.setOpaque(true);
                fillColor(table, label, isSelected);
                return label;
            } else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }

        /**
         * Este método es para que pinte el fondo del JLabel cuando lo
         * seleccionamos para que no quede en blanco, desentonando con el resto
         * de las celdas que no son JLabel
         *
         * @param t
         * @param l
         * @param isSelected
         */
        public void fillColor(JTable t, JLabel l, boolean isSelected) {
            if (isSelected) {
                l.setBackground(t.getSelectionBackground());
                l.setForeground(t.getSelectionForeground());
            } else {
                l.setBackground(t.getBackground());
                l.setForeground(t.getForeground());
            }
        }
    }

    private class BuildBanner extends Thread {

        public void setBanner() {
            OpenERP oerp = hupernikao.BuildOpenERPConnection();
            ImageIcon Banner = new ImageIcon(oerp.getBanner());
            lblBanner.setIcon(Banner);
        }

        @Override
        public void run() {
            setBanner();
        }
    }

    public void Refresh() {
        //Cargar Evento del día de hoy
        int uid = Integer.parseInt("" + gl.user.get("id"));
        String password = "" + gl.user.get("password");
        String ip = gl.getHost();
        int port = gl.getPort();
        String db = "" + gl.getDb();

        int hour;
        Calendar Cal = Calendar.getInstance();
        hour = Cal.get(Calendar.HOUR);

        Event current_event = null;
        DefaultListModel mdleventos = new DefaultListModel();
        OpenERP oerp = hupernikao.BuildOpenERPConnection();
        List<Event> Eventos = oerp.getEventsToday();
        for (Event event : Eventos) {
            if (event.isCurrent_event()) {
                current_event = event;
            }
            mdleventos.addElement(event);
        }
        lstEventos.setModel(mdleventos);
        gl.Current_event = current_event;
        txtusername.setText("");
        txtpassword.setText("");

        if (current_event != null) {
            Dimension ventana = new Dimension();
            ventana.setSize(655, 670);
            this.setMinimumSize(ventana);
            this.setPreferredSize(ventana);
            this.setSize(ventana);
            centrarVentana();

            lstEventos.setSelectedValue(current_event, rootPaneCheckingEnabled);
            this.setTitle("Evento: " + current_event.getName());
            pgRestante.setVisible(true);
            pgRestante_puntual.setVisible(true);
            frmRegistrar_asistencia.setVisible(true);
            txtusername.requestFocus();
            pnlNext_event.setVisible(false);
            pnlcollaborators.setVisible(true);

            colaboradores_del_evento colaboradores_del_evento_obj = new colaboradores_del_evento(current_event.getId());
            colaboradores_del_evento_obj.start();
        } else {
            //---Cargar proximo evento---------------
            HashMap Evento = OpenERP.get_next_event();
            gl.setNext_event(Evento);
            gl.setSeconds_sum(0);
            //---------------------------------------
            Dimension ventana = new Dimension();
            ventana.setSize(550, 350);

            this.setMinimumSize(ventana);
            this.setPreferredSize(ventana);
            this.setSize(ventana);
            centrarVentana();

            lblRestante.setText("");
            lblRestante_puntual.setText("");
            this.setTitle("No hay ningún evento para registrar asistencia");
            pgRestante.setVisible(false);
            pgRestante_puntual.setVisible(false);
            frmRegistrar_asistencia.setVisible(false);
            pnlNext_event.setVisible(true);
            pnlcollaborators.setVisible(false);
        }

        //Obtener la imagen que se muestra en la parte superior derecha
        new BuildBanner().start();
    }

    private void Actualizar_contadores() {
        try {
            Calendar Cal = Calendar.getInstance();
            //Modificar indicadores de progreso
            if (gl.Current_event != null) {
                int now;
                now = (Cal.get(Calendar.HOUR_OF_DAY) * 60) + Cal.get(Calendar.MINUTE);
                int horas_restantes, horas_restantes_mod, minutos_restantes, segundos_restantes, segundos_restantes_mod;
                segundos_restantes = gl.Current_event.getTime_limit_int() * 60 - (now * 60 + Cal.get(Calendar.SECOND));
                horas_restantes = (segundos_restantes / 60) / 60;
                minutos_restantes = (segundos_restantes / 60) % 60;
                segundos_restantes_mod = segundos_restantes % 60;

                String horas_restantes_str, minutos_restantes_str, segundos_restantes_str;

                if (("" + horas_restantes).length() < 2) {
                    horas_restantes_str = "0" + horas_restantes;
                } else {
                    horas_restantes_str = "" + horas_restantes;
                }
                if (("" + minutos_restantes).length() < 2) {
                    minutos_restantes_str = "0" + minutos_restantes;
                } else {
                    minutos_restantes_str = "" + minutos_restantes;
                }
                if (("" + segundos_restantes_mod).length() < 2) {
                    segundos_restantes_str = "0" + segundos_restantes_mod;
                } else {
                    segundos_restantes_str = "" + segundos_restantes_mod;
                }

                //Marcar los colaboradores que ya hayan registrado su asistencia;
                if ((Cal.get(Calendar.SECOND) % 15 == 0) && gl.isLoad_registereds()) {
                    colaboradores_registrados colaboradores_registrados_obj = new colaboradores_registrados();
                    colaboradores_registrados_obj.start();
                    gl.setLoad_registereds(false);
                }
                if (Cal.get(Calendar.SECOND) % 15 != 0 && gl.isLoad_registereds() == false) {
                    gl.setLoad_registereds(true);
                }

                if (minutos_restantes < 1 && segundos_restantes < 1) {
                    System.out.println("Danger" + segundos_restantes_str);
                    ReloadInformation();
                }

                //Modificar barra de Progreso
                int progress;
                int seconds_limit;
                seconds_limit = (gl.Current_event.getTime_limit_int() * 60) - (gl.Current_event.getTime_entry_int() * 60);
                progress = (segundos_restantes * 100) / seconds_limit;
                pgRestante.setValue(progress);
                lblRestante.setText("Tiempo total restante para registrar asistencia " + horas_restantes_str + ":" + minutos_restantes_str + ":" + segundos_restantes_str);
                //====================================================================
                //Restante-------------------------------------------------
                //====================================================================
                segundos_restantes = gl.Current_event.getTime_register_int() * 60 - (now * 60 + Cal.get(Calendar.SECOND));
                if (segundos_restantes < 1) {
                    segundos_restantes = 0;
                    lblRestante.setForeground(Color.red);
                    lblRestante_puntual.setForeground(Color.gray);
                } else {
                    lblRestante.setForeground(Color.blue);
                    lblRestante_puntual.setForeground(Color.black);
                }
                horas_restantes = (segundos_restantes / 60) / 60;
                minutos_restantes = (segundos_restantes / 60) % 60;
                segundos_restantes_mod = segundos_restantes % 60;

                if (("" + horas_restantes).length() < 2) {
                    horas_restantes_str = "0" + horas_restantes;
                } else {
                    horas_restantes_str = "" + horas_restantes;
                }
                if (("" + minutos_restantes).length() < 2) {
                    minutos_restantes_str = "0" + minutos_restantes;
                } else {
                    minutos_restantes_str = "" + minutos_restantes;
                }
                if (("" + segundos_restantes_mod).length() < 2) {
                    segundos_restantes_str = "0" + segundos_restantes_mod;
                } else {
                    segundos_restantes_str = "" + segundos_restantes_mod;
                }

                //Modificar barra de Progreso
                seconds_limit = (gl.Current_event.getTime_register_int() * 60) - (gl.Current_event.getTime_entry_int() * 60);
                progress = (segundos_restantes * 100) / seconds_limit;
                pgRestante_puntual.setValue(progress);
                lblRestante_puntual.setText("Para asistencia puntual " + horas_restantes_str + ":" + minutos_restantes_str + ":" + segundos_restantes_str);
            } else {
                //Ver cuanto tiempo falta para el proximo evento------
                load_next_event();
                //Verficar si ya un evento en curso.
                if (Cal.get(Calendar.SECOND) > 0 && Cal.get(Calendar.SECOND) < 30 && gl.isLoad_datas()) {
                    cargar_eventos_actuales current_obj = new cargar_eventos_actuales();
                    current_obj.start();
                    gl.setLoad_datas(false);
                }
                if (Cal.get(Calendar.SECOND) > 30 && Cal.get(Calendar.SECOND) < 60 && gl.isLoad_datas() == false) {
                    gl.setLoad_datas(true);
                }
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }

    private class iTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Actualizar_contadores();
            calcular_hora calcular_hora_obj = new calcular_hora();
            calcular_hora_obj.start();
        }
    }

    private class busqueda_de_colaboradores extends Thread {

        public void search() {
            DefaultTableModel modelo = (DefaultTableModel) tblCollaborators.getModel();
            int filas = tblCollaborators.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
            String nombre_guardado, nombre_buscado;
            nombre_buscado = txtCollaborator.getText().toLowerCase();
            for (int i = 0; gl.getFilas() > i; i++) {
                Vector Coll = (Vector) gl.getCollaborator_vector().get(i);
                nombre_guardado = "" + Coll.get(2);
                nombre_guardado = nombre_guardado.toLowerCase();
                int res = nombre_guardado.indexOf(nombre_buscado.toLowerCase());
                if (nombre_guardado.contains(nombre_buscado.toLowerCase())) {
                    Object[] fila = new Object[Coll.size()];
                    for (int j = 0; j < Coll.size(); j++) {
                        fila[j] = Coll.get(j);
                    }
                    modelo.addRow(fila);
                }
            }
            tblCollaborators.setRowHeight(40);
        }

        @Override
        public void run() {
            search();
        }
    }

    private class getCollaboratorPhoto extends Thread {

        int collaborator_id;
        DefaultTableModel modelo = null;
        Vector Coll = null;
        int row;
        int column = 1;

        public getCollaboratorPhoto(int collaborator_id, DefaultTableModel modelo, int row) {
            this.collaborator_id = collaborator_id;
            this.modelo = modelo;
            this.row = row;
        }

        public getCollaboratorPhoto(int collaborator_id, Vector Coll) {
            this.collaborator_id = collaborator_id;
            this.Coll = Coll;
        }

        public void getPhoto() {
            OpenERP oerp = hupernikao.BuildOpenERPConnection();
            try {
                String photo_field = "photo_small";
                HashMap<String, Object> Collaborator = oerp.read("kemas.collaborator", this.collaborator_id, new String[]{photo_field});
                byte[] foto = hupernikao.DecodeB64ToBytes(Collaborator.get(photo_field).toString());
                ImageIcon Photo = hupernikao.ReziseImage(foto, gl.size_thumbnails);
                if (this.modelo != null) {
                    this.modelo.setValueAt(new JLabel(Photo), this.row, this.column);
                } else {
                    this.Coll.set(this.column, new JLabel(Photo));
                }
            } catch (Exception ex) {
                Logger.getLogger(frmRegister_attendance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            getPhoto();
        }
    }

    private class colaboradores_del_evento extends Thread {

        private final int event_id;

        public colaboradores_del_evento(int current_event) {
            this.event_id = current_event;
        }

        public void load_collaborator() {
            OpenERP oerp = hupernikao.BuildOpenERPConnection();
            List<Collaborator> Colaboradores = oerp.getEventCollaborators(this.event_id);

            DefaultTableModel modelo = (DefaultTableModel) tblCollaborators.getModel();
            int filas = tblCollaborators.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
            ImageIcon AttendaceINIcon = new ImageIcon(getClass().getResource("/Imagenes/up_28.png"));
            ImageIcon AttendaceOUTIcon = new ImageIcon(getClass().getResource("/Imagenes/down_28.png"));
            for (Collaborator colaborador : Colaboradores) {
                Object[] fila = new Object[7];
                //ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/col.png"));
                ImageIcon person = new ImageIcon(getClass().getResource("/Imagenes/person_dark.png"));
                ImageIcon CollaboratorAvatar = hupernikao.ReziseImage(person.getImage(), gl.size_thumbnails);
                //ImageIcon CollaboratorAvatar = Collaborator.resize_image(colaborador.getPhoto(), gl.size_thumbnails);
                fila[0] = colaborador.getId();
                fila[1] = new JLabel(CollaboratorAvatar);
                fila[2] = colaborador.getName();
                if (colaborador.isRegistrado() != null) {
                    if (!colaborador.isRegistrado().toString().equals("false")) {
                        fila[3] = true;
                        if (colaborador.isRegistrado().get("checkout_id").toString().equals("false")) {
                            fila[6] = new JLabel(AttendaceINIcon);
                        } else {
                            fila[4] = true;
                            fila[6] = new JLabel(AttendaceOUTIcon);
                        }
                    }
                }

                fila[5] = colaborador.getUsername();

                modelo.addRow(fila);
                tblCollaborators.setRowHeight(40);
            }

            int rowCount = modelo.getRowCount();
            int colcount = modelo.getColumnCount();
            Vector<Vector<Object>> copy = new Vector<Vector<Object>>(rowCount);
            for (int row = 0; row < rowCount; row++) {
                Vector<Object> newRow = new Vector<Object>(colcount);
                copy.add(newRow);
                for (int col = 0; col < colcount; col++) {
                    newRow.add(modelo.getValueAt(row, col));
                }
            }
            gl.setCollaborator_vector((Vector) copy.clone());
            gl.setFilas(tblCollaborators.getRowCount());
            calcular_colaboradores_regitrados();

            //Obtener las fotos de los colaboradores
            for (int i = 0; tblCollaborators.getRowCount() > i; i++) {
                int collaborator_id = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                new getCollaboratorPhoto(collaborator_id, modelo, i).start();
            }
            for (int i = 0; gl.getFilas() > i; i++) {
                Vector Coll = (Vector) gl.getCollaborator_vector().get(i);
                int collaborator_id = Integer.parseInt(Coll.get(0).toString());
                new getCollaboratorPhoto(collaborator_id, Coll).start();
            }
        }

        @Override
        public void run() {
            load_collaborator();
        }
    }

    public void calcular_colaboradores_regitrados() {
        int registradores = 0, faltantes = 0;
        for (int i = 0; gl.getFilas() > i; i++) {
            Vector Coll = (Vector) gl.getCollaborator_vector().get(i);

            if (Boolean.parseBoolean("" + Coll.get(3))) {
                registradores++;
            } else {
                faltantes++;
            }
        }

        String res = "<html>Registrados <b>" + registradores + "</b>  |  Faltantes <b>" + faltantes + "</b></html>";
        lblcolaboradores_registrados.setText(res);
    }

    private class colaboradores_registrados extends Thread {

        @SuppressWarnings("empty-statement")
        public void marcar_colaboradores() {
            OpenERP oerp = hupernikao.BuildOpenERPConnection();
            List<HashMap<String, Object>> Colaboradores = oerp.getCollaboratorsRegistereds(gl.Current_event.getId());;

            DefaultTableModel modelo = (DefaultTableModel) tblCollaborators.getModel();
            int id;
            ImageIcon AttendaceINIcon = new ImageIcon(getClass().getResource("/Imagenes/up_28.png"));
            ImageIcon AttendaceOUTIcon = new ImageIcon(getClass().getResource("/Imagenes/down_28.png"));
            for (HashMap<String, Object> Colaborador : Colaboradores) {
                for (int i = 0; tblCollaborators.getRowCount() > i; i++) {
                    id = Integer.parseInt("" + modelo.getValueAt(i, 0));
                    if (id == Integer.parseInt(Colaborador.get("collaborator_id").toString())) {
                        modelo.setValueAt(true, i, 3);
                        if (Colaborador.get("checkout_id").toString().equals("false")) {
                            modelo.setValueAt(new JLabel(AttendaceINIcon), i, 6);
                        } else {
                            modelo.setValueAt(true, i, 4);
                            modelo.setValueAt(new JLabel(AttendaceOUTIcon), i, 6);
                        }
                    }
                }
                for (int i = 0; gl.getFilas() > i; i++) {
                    Vector Coll = (Vector) gl.getCollaborator_vector().get(i);
                    id = Integer.parseInt("" + Coll.get(0));
                    if (id == Integer.parseInt(Colaborador.get("collaborator_id").toString())) {
                        try {
                            Coll.set(3, true);
                            if (Colaborador.get("checkout_id").toString().equals("false")) {
                                Coll.set(6, new JLabel(AttendaceINIcon));
                            } else {
                                Coll.set(4, true);
                                Coll.set(6, new JLabel(AttendaceOUTIcon));
                            }
                            //Coll.setElementAt(true, 3);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            calcular_colaboradores_regitrados();
        }

        @Override
        public void run() {
            marcar_colaboradores();
        }
    }

    private class cargar_eventos_actuales extends Thread {

        @Override
        public void run() {
            ReloadInformation();
        }
    }

    private class calcular_hora extends Thread {

        Timer temp_obj;

        private class Temp implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent ae) {
                set_hour();
            }
        }

        public void set_hour() {
            Calendar Cal = Calendar.getInstance();
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

            lblhora.setText(hora);
            lblmin.setText(minutos);
            lblsec.setText(segundos);
        }

        @Override
        public void run() {
            temp_obj = new Timer(1000, new Temp());
            temp_obj.start();
        }
    }

    /**
     * Creates new form frmRegister_attendance
     */
    public frmRegister_attendance() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                close();
            }
        });
        tblCollaborators.setDefaultRenderer(Object.class, new frmRegister_attendance.IconCellRenderer());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        btnReload = new javax.swing.JButton();
        btnCloseSession = new javax.swing.JButton();
        lblUsuario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblRestante = new javax.swing.JLabel();
        pgRestante = new javax.swing.JProgressBar();
        lblRestante_puntual = new javax.swing.JLabel();
        pgRestante_puntual = new javax.swing.JProgressBar();
        lblBanner = new javax.swing.JLabel();
        pnlHora = new javax.swing.JPanel();
        lblhora = new javax.swing.JLabel();
        lblmin = new javax.swing.JLabel();
        lblsec = new javax.swing.JLabel();
        pnlNext_event = new javax.swing.JPanel();
        lblnext_event = new javax.swing.JLabel();
        lblnext_event1 = new javax.swing.JLabel();
        lblnext_event_remaining = new javax.swing.JLabel();
        pnlcollaborators = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCollaborators = new javax.swing.JTable();
        txtCollaborator = new javax.swing.JTextField();
        lblcolaboradores_registrados = new javax.swing.JLabel();
        btnClearSearch = new javax.swing.JButton();
        frmRegistrar_asistencia = new javax.swing.JInternalFrame();
        txtusername = new javax.swing.JTextField();
        txtpassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstEventos = new jclist.jcList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Shutdown2.png"))); // NOI18N
        jButton1.setToolTipText("Salir");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/gtk-refresh.png"))); // NOI18N
        btnReload.setToolTipText("Recargar los eventos.");
        btnReload.setFocusable(false);
        btnReload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReload.setName(""); // NOI18N
        btnReload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });
        jToolBar1.add(btnReload);

        btnCloseSession.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close_session_16.png"))); // NOI18N
        btnCloseSession.setToolTipText("Cerrar sesión");
        btnCloseSession.setFocusable(false);
        btnCloseSession.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCloseSession.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCloseSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseSessionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCloseSession);

        lblUsuario.setFont(lblUsuario.getFont().deriveFont(lblUsuario.getFont().getSize()-3f));
        lblUsuario.setForeground(new java.awt.Color(148, 136, 136));
        lblUsuario.setText("User");
        jToolBar1.add(lblUsuario);

        jPanel1.setBorder(null);

        lblRestante.setText("Tiempo restante");

        pgRestante.setValue(100);
        pgRestante.setBorderPainted(false);

        lblRestante_puntual.setText("Tiempo restante");

        pgRestante_puntual.setValue(100);
        pgRestante_puntual.setBorderPainted(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblRestante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pgRestante, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
            .addComponent(lblRestante_puntual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pgRestante_puntual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblRestante)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgRestante, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRestante_puntual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgRestante_puntual, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblBanner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Heading image.png"))); // NOI18N

        pnlHora.setBorder(null);

        lblhora.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        lblhora.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblhora.setText("09");

        lblmin.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        lblmin.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblmin.setText("15");

        lblsec.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblsec.setText("15");

        javax.swing.GroupLayout pnlHoraLayout = new javax.swing.GroupLayout(pnlHora);
        pnlHora.setLayout(pnlHoraLayout);
        pnlHoraLayout.setHorizontalGroup(
            pnlHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHoraLayout.createSequentialGroup()
                .addComponent(lblhora)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblmin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblsec, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlHoraLayout.setVerticalGroup(
            pnlHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHoraLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHoraLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblmin, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlHoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblhora)
                        .addComponent(lblsec, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblnext_event.setText("Proximo evento");
        lblnext_event.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblnext_event1.setText("y empezará en:");

        lblnext_event_remaining.setText("Hora");
        lblnext_event_remaining.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout pnlNext_eventLayout = new javax.swing.GroupLayout(pnlNext_event);
        pnlNext_event.setLayout(pnlNext_eventLayout);
        pnlNext_eventLayout.setHorizontalGroup(
            pnlNext_eventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNext_eventLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlNext_eventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblnext_event, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                    .addComponent(lblnext_event1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblnext_event_remaining, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlNext_eventLayout.setVerticalGroup(
            pnlNext_eventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNext_eventLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblnext_event, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblnext_event1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblnext_event_remaining, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlcollaborators.setBorder(javax.swing.BorderFactory.createTitledBorder("Colaboradores"));

        tblCollaborators.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Foto", "Nombre", "In", "Out", "username", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCollaborators.setFocusable(false);
        tblCollaborators.setGridColor(java.awt.Color.white);
        tblCollaborators.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCollaboratorsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblCollaborators);
        if (tblCollaborators.getColumnModel().getColumnCount() > 0) {
            tblCollaborators.getColumnModel().getColumn(0).setMinWidth(0);
            tblCollaborators.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblCollaborators.getColumnModel().getColumn(0).setMaxWidth(0);
            tblCollaborators.getColumnModel().getColumn(1).setMinWidth(40);
            tblCollaborators.getColumnModel().getColumn(1).setPreferredWidth(40);
            tblCollaborators.getColumnModel().getColumn(1).setMaxWidth(40);
            tblCollaborators.getColumnModel().getColumn(3).setMinWidth(0);
            tblCollaborators.getColumnModel().getColumn(3).setPreferredWidth(0);
            tblCollaborators.getColumnModel().getColumn(3).setMaxWidth(0);
            tblCollaborators.getColumnModel().getColumn(4).setMinWidth(0);
            tblCollaborators.getColumnModel().getColumn(4).setPreferredWidth(0);
            tblCollaborators.getColumnModel().getColumn(4).setMaxWidth(0);
            tblCollaborators.getColumnModel().getColumn(5).setMinWidth(0);
            tblCollaborators.getColumnModel().getColumn(5).setPreferredWidth(0);
            tblCollaborators.getColumnModel().getColumn(5).setMaxWidth(0);
            tblCollaborators.getColumnModel().getColumn(6).setMinWidth(40);
            tblCollaborators.getColumnModel().getColumn(6).setPreferredWidth(40);
            tblCollaborators.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        txtCollaborator.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCollaboratorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCollaboratorFocusLost(evt);
            }
        });
        txtCollaborator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCollaboratorKeyReleased(evt);
            }
        });

        lblcolaboradores_registrados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblcolaboradores_registrados.setText("  ");

        btnClearSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Search.png"))); // NOI18N
        btnClearSearch.setToolTipText("Limpiar caja de búsqueda");
        btnClearSearch.setBorderPainted(false);
        btnClearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlcollaboratorsLayout = new javax.swing.GroupLayout(pnlcollaborators);
        pnlcollaborators.setLayout(pnlcollaboratorsLayout);
        pnlcollaboratorsLayout.setHorizontalGroup(
            pnlcollaboratorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlcollaboratorsLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(pnlcollaboratorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblcolaboradores_registrados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnlcollaboratorsLayout.createSequentialGroup()
                        .addComponent(txtCollaborator)
                        .addGap(2, 2, 2)
                        .addComponent(btnClearSearch)))
                .addGap(7, 7, 7))
        );
        pnlcollaboratorsLayout.setVerticalGroup(
            pnlcollaboratorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlcollaboratorsLayout.createSequentialGroup()
                .addGroup(pnlcollaboratorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClearSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCollaborator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblcolaboradores_registrados))
        );

        frmRegistrar_asistencia.setTitle("Registrar asistencia");
        frmRegistrar_asistencia.setVisible(true);

        txtusername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtusernameKeyPressed(evt);
            }
        });

        txtpassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpasswordKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel2.setText("Nombre de Usuario:");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        jLabel3.setText("Contraseña:");

        btnOk.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/STOCK_OK.png"))); // NOI18N
        btnOk.setText("Aceptar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frmRegistrar_asistenciaLayout = new javax.swing.GroupLayout(frmRegistrar_asistencia.getContentPane());
        frmRegistrar_asistencia.getContentPane().setLayout(frmRegistrar_asistenciaLayout);
        frmRegistrar_asistenciaLayout.setHorizontalGroup(
            frmRegistrar_asistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frmRegistrar_asistenciaLayout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
            .addGroup(frmRegistrar_asistenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frmRegistrar_asistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtpassword, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtusername)
                    .addGroup(frmRegistrar_asistenciaLayout.createSequentialGroup()
                        .addGroup(frmRegistrar_asistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        frmRegistrar_asistenciaLayout.setVerticalGroup(
            frmRegistrar_asistenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frmRegistrar_asistenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk)
                .addGap(10, 10, 10))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Eventos del día"));

        lstEventos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstEventos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstEventos.setAlignmentX(2.6F);
        lstEventos.setColorSeleccionado(new java.awt.Color(13, 111, 16));
        lstEventos.setEnabled(false);
        lstEventos.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        lstEventos.setIconoNoSeleccionado(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/gray.png"))); // NOI18N
        lstEventos.setIconoSeleccionado(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/green.png"))); // NOI18N
        jScrollPane1.setViewportView(lstEventos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblBanner, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlNext_event, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(pnlHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pnlcollaborators, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(frmRegistrar_asistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(pnlNext_event, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnlHora, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblBanner, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlcollaborators, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                        .addComponent(frmRegistrar_asistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void centrarVentana() {
        // Se obtienen las dimensiones en pixels de la pantalla.
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        // Se obtienen las dimensiones en pixels de la ventana.
        Dimension ventana = getSize();
        // Una cuenta para situar la ventana en el centro de la pantalla.
        setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
    }

    DefaultListModel modelo = new DefaultListModel();

    void load_next_event() {
        HashMap Evento = gl.getNext_event();
        if (Evento != null) {
            int seconds, minutes, hours, days;
            float sec_base;
            String seconds_base;
            seconds_base = Evento.get("seconds_remaining").toString();
            sec_base = Float.parseFloat(seconds_base) - gl.getSeconds_sum();
            //Seconds-----------------------------------
            seconds = (int) (sec_base % 60);
            sec_base = (int) (sec_base / 60);
            //Minutes-----------------------------------
            minutes = (int) (sec_base % 60);
            sec_base = (int) (sec_base / 60);
            //Horas-------------------------------------
            hours = (int) (sec_base % 24);
            sec_base = (int) (sec_base / 24);
            //Dias--------------------------------------
            days = (int) sec_base;

            String horas_restantes_str, minutos_restantes_str, segundos_restantes_str;

            if (("" + hours).length() < 2) {
                horas_restantes_str = "0" + hours;
            } else {
                horas_restantes_str = "" + hours;
            }
            if (("" + minutes).length() < 2) {
                minutos_restantes_str = "0" + minutes;
            } else {
                minutos_restantes_str = "" + minutes;
            }

            if (seconds < 1) {
                seconds = 0;
            }
            if (("" + seconds).length() < 2) {
                segundos_restantes_str = "0" + seconds;
            } else {
                segundos_restantes_str = "" + seconds;
            }

            String remaining;
            remaining = horas_restantes_str + ":" + minutos_restantes_str + ":" + segundos_restantes_str;
            if (days > 0) {
                String dia;
                if (days == 1) {
                    dia = " día, ";
                } else {
                    dia = " días, ";
                }
                remaining = days + dia + remaining;
            }

            lblnext_event.setText("<html>El próximo evento es <b>" + Evento.get("name") + "</b></html>");
            lblnext_event_remaining.setText("<html>" + remaining + "</html>");
            gl.setSeconds_sum(gl.getSeconds_sum() + 1);

            lblnext_event1.setVisible(true);
            lblnext_event_remaining.setVisible(true);
        } else {
            lblnext_event1.setVisible(false);
            lblnext_event_remaining.setVisible(false);
            lblnext_event.setText("No hay evento próximos.");
        }
    }

    void ReloadInformation() {
        txtCollaborator.setText("");
        Refresh();
        Actualizar_contadores();
        txtusername.requestFocus();
        txtCollaboratorLostocus();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        String nombre_usuario;
        nombre_usuario = gl.user.get("login").toString();
        this.lblUsuario.setText("Odoo - http://" + nombre_usuario + "@" + gl.getHost() + "/" + gl.getDb());
        //Instanciar un el Timer
        timer_obj = new Timer(1000, new iTimer());
        timer_obj.start();

        ///Manejador de Hora
        calcular_hora hora_obj = new calcular_hora();
        hora_obj.start();

        centrarVentana();
        ReloadInformation();
    }//GEN-LAST:event_formWindowOpened

    private boolean validar_cajas() {
        boolean resp;
        if ("".equals(this.txtusername.getText())) {
            JOptionPane.showMessageDialog(null, "Primero Ingresa tu nombre usuario", "Aviso", JOptionPane.WARNING_MESSAGE);
            this.txtusername.requestFocus();
            resp = false;
        } else {
            if ("".equals(this.txtpassword.getText())) {
                JOptionPane.showMessageDialog(null, "Ingresa tu password", "Aviso", JOptionPane.WARNING_MESSAGE);
                this.txtpassword.requestFocus();
                resp = false;
            } else {
                resp = true;
            }
        }
        return resp;
    }

    void llamar_frmok(String reg_username) {
        OpenERP oerp = hupernikao.BuildOpenERPConnection();
        gl.Login_Collaborator = oerp.getCollaborator(reg_username);
        if (gl.Login_Collaborator != null) {
            frmOK frm = new frmOK(this, true);
            frm.setVisible(rootPaneCheckingEnabled);
        }
    }

    void Aceptar() {
        if (this.validar_cajas()) {
            String reg_username = txtusername.getText();
            String reg_password = txtpassword.getText();

            colaboradores_registrados colaboradores_registrados_obj = new colaboradores_registrados();
            OpenERP oerp = hupernikao.BuildOpenERPConnection();
            RespRegistrarAsisitencia resp = oerp.RegisterAttendance(reg_username, reg_password);
            switch (resp) {
                case Error_login:
                    JOptionPane.showMessageDialog(null, "Nombre de Usuario o Password Incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                    this.txtpassword.setText("");
                    this.txtpassword.requestFocus();
                    break;
                case No_Collaborator:
                    JOptionPane.showMessageDialog(null, "Las credenciales ingresadas no pertencen a un Colaborador.", "Error", JOptionPane.ERROR_MESSAGE);
                    this.txtusername.setText("");
                    this.txtpassword.setText("");
                    this.txtusername.requestFocus();
                    break;
                case No_staff:
                    JOptionPane.showMessageDialog(null, "No estas en la lista de colaboradores asignados para este evento.", "Error", JOptionPane.ERROR_MESSAGE);
                    this.txtusername.setText("");
                    this.txtpassword.setText("");
                    this.txtusername.requestFocus();
                    break;
                case No_events:
                    JOptionPane.showMessageDialog(null, "No hay eventos disponibles para registrar tu asistencia.", "Error", JOptionPane.ERROR_MESSAGE);
                    ReloadInformation();
                    break;
                case Already_register:
                    JOptionPane.showMessageDialog(null, "Ya has registrado tu asistencia para este evento.", "Error", JOptionPane.ERROR_MESSAGE);
                    this.txtusername.setText("");
                    this.txtpassword.setText("");
                    this.txtusername.requestFocus();
                    break;
                case Already_checkout:
                    JOptionPane.showMessageDialog(null, "Ya has registrado tu salida en este evento.", "Error", JOptionPane.ERROR_MESSAGE);
                    this.txtusername.setText("");
                    this.txtpassword.setText("");
                    this.txtusername.requestFocus();
                    break;
                case checkin:
                    colaboradores_registrados_obj.start();
                    llamar_frmok(reg_username);
                    this.txtusername.setText("");
                    this.txtpassword.setText("");
                    this.txtusername.requestFocus();
                    break;
                case checkout:
                    colaboradores_registrados_obj.start();
                    this.txtusername.setText("");
                    this.txtpassword.setText("");
                    this.txtusername.requestFocus();
                    break;
            }
        }
    }

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        ReloadInformation();
    }//GEN-LAST:event_btnReloadActionPerformed

    private void close() {
        int resp;
        resp = JOptionPane.showConfirmDialog(rootPane, "¿Desea realmente salir del sistema?", "Salir del sistema", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.close();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        Aceptar();
    }//GEN-LAST:event_btnOkActionPerformed

    private void txtpasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpasswordKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 13) {
            Aceptar();
        }
    }//GEN-LAST:event_txtpasswordKeyPressed

    private void txtusernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtusernameKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 13) {
            if ("".equals(txtpassword.getText())) {
                txtpassword.requestFocus();
            } else {
                Aceptar();
            }
        }
    }//GEN-LAST:event_txtusernameKeyPressed

    void RefreshButtonSearchIcon() {
        if (!txtCollaborator.getText().equals("")) {
            Icon ClearIcon = (Icon) new ImageIcon(getClass().getResource("/Imagenes/gtk-clear.png"));
            btnClearSearch.setIcon(ClearIcon);
        } else {
            Icon RefreshIcon = (Icon) new ImageIcon(getClass().getResource("/Imagenes/Search.png"));
            btnClearSearch.setIcon(RefreshIcon);
        }
    }
    private void txtCollaboratorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCollaboratorKeyReleased
        new busqueda_de_colaboradores().start();
        RefreshButtonSearchIcon();
    }//GEN-LAST:event_txtCollaboratorKeyReleased

    private void tblCollaboratorsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCollaboratorsMouseClicked
        if (evt.getClickCount() == 2) {
            JTable target = (JTable) evt.getSource();
            int column = target.getSelectedColumn();
            if (column == 1 || column == 2) {
                int row = target.getSelectedRow();
                final String Username = (tblCollaborators.getModel().getValueAt(row, 5)).toString();
                txtusername.setText(Username);
                txtpassword.requestFocus();
            }
        }
    }//GEN-LAST:event_tblCollaboratorsMouseClicked

    private void btnCloseSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseSessionActionPerformed
        Object[] opciones = {"Aceptar", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(null,
                "¿Estás seguro de Cerrar la Sesión ahora?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                "Aceptar");
        if (eleccion == JOptionPane.YES_OPTION) {
            this.dispose();
            frmLogin flogin = new frmLogin();
            flogin.setVisible(rootPaneCheckingEnabled);
        }
    }//GEN-LAST:event_btnCloseSessionActionPerformed

    private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
        if (!txtCollaborator.getText().equals("")) {
            txtCollaborator.setText(null);
            new busqueda_de_colaboradores().start();
        }
        txtCollaborator.requestFocus();
        RefreshButtonSearchIcon();
        txtCollaboratorGotFocus();
    }//GEN-LAST:event_btnClearSearchActionPerformed

    void txtCollaboratorGotFocus() {
        Font fntLost = new Font(txtCollaborator.getFont().getName(), Font.ITALIC + Font.BOLD, txtCollaborator.getFont().getSize());
        if (fntLost.equals(txtCollaborator.getFont())) {
            txtCollaborator.setText("");
        }
        Font fntGot = new Font(txtCollaborator.getFont().getName(), Font.PLAIN, txtCollaborator.getFont().getSize());
        txtCollaborator.setFont(fntGot);
        txtCollaborator.setForeground(Color.BLACK);
        txtCollaborator.requestFocus();
    }

    void txtCollaboratorLostocus() {
        if (txtCollaborator.getText().equals("")) {
            txtCollaborator.setText(" Buscar");
            Font fntLost = new Font(txtCollaborator.getFont().getName(), Font.ITALIC + Font.BOLD, txtCollaborator.getFont().getSize());
            txtCollaborator.setFont(fntLost);
            txtCollaborator.setForeground(Color.GRAY);
        } else {
            Font fntGot = new Font(txtCollaborator.getFont().getName(), Font.PLAIN, txtCollaborator.getFont().getSize());
            txtCollaborator.setFont(fntGot);
            txtCollaborator.setForeground(Color.BLACK);
        }
    }
    private void txtCollaboratorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCollaboratorFocusLost
        txtCollaboratorLostocus();
    }//GEN-LAST:event_txtCollaboratorFocusLost

    private void txtCollaboratorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCollaboratorFocusGained
        txtCollaboratorGotFocus();
    }//GEN-LAST:event_txtCollaboratorFocusGained

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
            java.util.logging.Logger.getLogger(frmRegister_attendance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmRegister_attendance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmRegister_attendance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmRegister_attendance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new frmRegister_attendance().setVisible(true);
            }
        });
        gl.setLoad_datas(true);
        gl.setLoad_registereds(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearSearch;
    private javax.swing.JButton btnCloseSession;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnReload;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JInternalFrame frmRegistrar_asistencia;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblBanner;
    private javax.swing.JLabel lblRestante;
    private javax.swing.JLabel lblRestante_puntual;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblcolaboradores_registrados;
    private javax.swing.JLabel lblhora;
    private javax.swing.JLabel lblmin;
    private javax.swing.JLabel lblnext_event;
    private javax.swing.JLabel lblnext_event1;
    private javax.swing.JLabel lblnext_event_remaining;
    private javax.swing.JLabel lblsec;
    private jclist.jcList lstEventos;
    private javax.swing.JProgressBar pgRestante;
    private javax.swing.JProgressBar pgRestante_puntual;
    private javax.swing.JPanel pnlHora;
    private javax.swing.JPanel pnlNext_event;
    private javax.swing.JPanel pnlcollaborators;
    private javax.swing.JTable tblCollaborators;
    private javax.swing.JTextField txtCollaborator;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}

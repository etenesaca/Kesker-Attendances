/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

import com.sun.corba.se.spi.copyobject.CopyobjectDefaults;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Vector;
//========Leer el archivo de parametros
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author edgar
 */
public class gl {
    public static Event Current_event;
    public static Collaborator Login_Collaborator;
    public static HashMap<String, Object> user;              //Usuario logueado
    public static String Host;              //IP a la cual se conecta
    public static int Port;                 //Puerto al que se conecta
    public static String tz;                   //Zona horaria en la que se encuentra el servidor
    public static String Db;                //db
    public static HashMap Next_event;       //Porximo Evento
    public static int seconds_sum;          //segundos que se sumaron a los regundos recibidos
    public static boolean load_datas;       //Le dice si debe cargar los datos o no
    public static boolean load_registereds; //Le dice si debe cargar los colaboradores registrados
    public static Vector collaborator_vector;
    public static int filas;
    public static int size_thumbnails = 36;
    public static boolean connected;        //Indica si se ha podido realizar la conexion al Servidor
    public static String login_status;     //Indica si se ha podido realizar la conexion al Servidor
    
    public static boolean isLoad_registereds() {
        return load_registereds;
    }

    public static void setLoad_registereds(boolean load_registereds) {
        gl.load_registereds = load_registereds;
    }

    public static HashMap getNext_event() {
        return Next_event;
    }

    public static void setNext_event(HashMap Next_event) {
        gl.Next_event = Next_event;
    }
    
    public static int getFilas() {
        return filas;
    }

    public static Vector getCollaborator_vector() {
        return collaborator_vector;
    }

    public static void setCollaborator_vector(Vector collaborator_vector) {
        gl.collaborator_vector = collaborator_vector;
    }

    public static void setFilas(int filas) {
        gl.filas = filas;
    }

    public static int getSeconds_sum() {
        return seconds_sum;
    }

    public static boolean isLoad_datas() {
        return load_datas;
    }

    public static void setLoad_datas(boolean load_datas) {
        gl.load_datas = load_datas;
    }

    public static void setSeconds_sum(int seconds_sum) {
        gl.seconds_sum = seconds_sum;
    }
    public static gl gl_obj = new gl();

    public static String getHost() {
        gl_obj.cargar_propiedades();
        return Host;
    }

    public static void setHost(String Host) throws IOException {
        gl.Host = Host;
        gl_obj.escribir_propiedades("Host", Host);
    }

    public static int getPort() {
        gl_obj.cargar_propiedades();
        return Port;
    }

    public static void setPort(Object Port) throws IOException {
        gl.Port = Integer.parseInt("" + Port);
        gl_obj.escribir_propiedades("Port", "" + Port);
    }

    public static String getDb() {
        gl_obj.cargar_propiedades();
        return Db;
    }

    public static void setDb(String Db) throws IOException {
        gl.Db = Db;
        gl_obj.escribir_propiedades("Db", Db);
    }

    public Properties getProperties() {
        try {
            //se crea una instancia a la clase Properties
            Properties propiedades = new Properties();
            //se leen el archivo .properties
            InputStream in;
            in = getClass().getResourceAsStream("Propiedades.properties");
            propiedades.load(in);
            //si el archivo de propiedades NO esta vacio retornan las propiedes leidas
            if (!propiedades.isEmpty()) {
                return propiedades;
            } else {//sino  retornara NULL
                return null;
            }
        } catch (IOException ex) {
            return null;
        }
    }

    private File get_url_properties_file() {
        File archivo = new File(this.getClass().getResource("Propiedades.properties").getFile().replace("%20", " "));
        return archivo;
    }

    private String subir_nivel(String iPath) {
        String res = "", aux = "";
        Vector<String> Directorio_lst = new Vector<String>();
        for (char car : iPath.toCharArray()) {
            if (car != File.separatorChar) {
                aux += car + "";
            } else {
                if (aux.length() > 0) {
                    Directorio_lst.add(aux.replace("%20", " "));
                    aux = "";
                }
            }
        }
        if ("/".equals(File.separator)) {
            res += File.separator;
        }
        for (int i = 0; i < Directorio_lst.size(); i++) {
            res += Directorio_lst.get(i) + File.separator;
        }
        return res;
    }

    private void _cargar_propiedades() {
        //-----Esto se usa para Cargar los parametros desde el JAR o ruta en desarrollo
        try {
            Properties mispropiedades = new gl().getProperties();
            //se leen las propiedades indicando el KEY (identificador) y se imprime
            gl.Host = mispropiedades.getProperty("Host");
            gl.Port = Integer.parseInt(mispropiedades.getProperty("Port"));
            gl.Db = mispropiedades.getProperty("Db");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    private void cargar_propiedades() {
        URL sistema = getClass().getProtectionDomain().getCodeSource().getLocation();
        String ipath = subir_nivel(sistema.getPath());
        String p_dir = ipath + "Config" + File.separator + "Propiedades.properties";

        Properties props = new Properties();
        try {
            InputStream in = new FileInputStream(p_dir);
            props.load(in);
            gl.Host = props.getProperty("Host");
            gl.Port = Integer.parseInt("" + props.getProperty("Port"));
            gl.Db = props.getProperty("Db");
            System.out.println("Cargado Correctamente");
        } catch (IOException ex) {
            _cargar_propiedades();
        }
    }

    private void _escribir_propiedades(String key, String value) throws FileNotFoundException, IOException {
        gl gl_obj = new gl();
        // Crear el objeto archivo
        File archivo = gl_obj.get_url_properties_file();
        //Crear el objeto properties
        Properties properties = new Properties();
        // Cargar las propiedades del archivo
        properties.load(new FileInputStream(archivo));
        properties.setProperty(key, value);
        // Escribier en el archivo los cambios

        FileOutputStream fos = new FileOutputStream(archivo.toString().replace("\\", "/"));
        properties.store(fos, null);
    }
    
    private void escribir_propiedades(String key, String value) {
        try {
            _escribir_propiedades(key, value);
        } catch (Exception e) {
            URL sistema = getClass().getProtectionDomain().getCodeSource().getLocation();
            String ipath = subir_nivel(sistema.getPath());
            String p_dir = ipath + "Config" + File.separator + "Propiedades.properties";

            Properties props = new Properties();
            try {
                InputStream in = new FileInputStream(p_dir);
                props.load(in);
                props.setProperty(key, value);
                FileOutputStream fos = new FileOutputStream(p_dir);
                props.store(fos, "Cambiado");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }
}

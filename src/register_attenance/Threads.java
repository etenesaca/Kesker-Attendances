/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

//XML-RPC======================================================================Login
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import static register_attenance.OpenERPConnection.XmlRpcClienType.*;
//=====================================================================

/**
 *
 * @author edgar
 */
public class Threads {

    protected static int SecondsToSleep = 500;
    protected static int SecondsToWait = 8000;
    public static ArrayList<String> DbList_GL;

    public void test_connection_execute() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                gl.connected = OpenERP.TestConnection(gl.getHost(), gl.getPort());
            }
        });
        thread.start();
        long endTimeMillis = System.currentTimeMillis() + SecondsToWait;
        while (thread.isAlive()) {
            if (System.currentTimeMillis() > endTimeMillis) {
                gl.connected = false;
                break;
            }
            try {
                Thread.sleep(SecondsToSleep);
            } catch (InterruptedException t) {
            }
        }
    }

    public boolean test_connection() {
        test_connection_execute();
        return gl.connected;
    }

    public void getDBList_excute() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DbList_GL = OpenERP.getDatabases(gl.getHost(), gl.getPort());
            }
        });
        thread.start();
        long endTimeMillis = System.currentTimeMillis() + SecondsToWait;
        while (thread.isAlive()) {
            if (System.currentTimeMillis() > endTimeMillis) {
                DbList_GL = null;
                break;
            }
            try {
                Thread.sleep(SecondsToSleep);
            } catch (InterruptedException t) {
            }
        }
    }

    public ArrayList<String> getDatabaseList(String host, int port) {
        gl.Host = host;
        gl.Port = port;
        getDBList_excute();
        return DbList_GL;
    }

    public boolean is_register_attedance_login(int uid) {
        OpenERP oerp = hupernikao.BuildOpenERPConnection();
        XmlRpcClient client = oerp.build_xmlrcp_client(Object);

        Object[] params = new Object[]{gl.Db, uid, gl.user.get("password").toString(), "kemas.func", "is_in_this_groups", "group_kemas_register_attedance"};
        Object resp = "false";
        try {
            resp = client.execute("execute", params);
        } catch (XmlRpcException e) {
        }
        return Boolean.parseBoolean(resp + "");
    }

    public String login_method(String username, String password, String ip, int port, String db) {
        if (test_connection() == false) {
            return "error_conexion";
        }
        XmlRpcClient cliente = OpenERP.build_xmlrcp_client(ip, port, Common);
        try {
            Object[] params = new Object[]{db, username, password};
            Object res_uid;
            try {
                res_uid = cliente.execute("login", params);
                gl.tz = (String) cliente.execute("timezone_get", params);
            } catch (XmlRpcException e) {
                return "error_conexion";
            }

            if ("false".equals("" + res_uid)) {
                return "error_login";
            }
            //Traer los datos del Usuario Logueado
            int uid = ((Integer) res_uid);
            OpenERP oerp = new OpenERP(ip, port, db, username, password, uid);
            HashMap<String, Object> user = oerp.read("res.users", uid, new String[]{"name", "login", "password", "email", "groups_id"});
            user.put("password", password);
            gl.user = user;
            gl.Db = db;
            gl.Host = ip;
            gl.Port = port;
            String nombre = user.get("name").toString();
            if (!is_register_attedance_login(uid)) {
                return "not_is_consultant_login";
            }
            return nombre;
        } catch (MalformedURLException e) {
            return "error_conexion";
        }
    }

    public void login_execute(final String username, final String password, final String ip, final int port, final String db) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                gl.login_status = login_method(username, password, ip, port, db);
            }
        });
        thread.start();
        long endTimeMillis = System.currentTimeMillis() + SecondsToWait;
        while (thread.isAlive()) {
            if (System.currentTimeMillis() > endTimeMillis) {
                gl.login_status = "error";
                break;
            }
            try {
                Thread.sleep(SecondsToSleep);
            } catch (InterruptedException t) {
            }
        }
    }

    public String login(String username, String password, String ip, int port, String db) {
        login_execute(username, password, ip, port, db);
        return gl.login_status;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

//XML-RPC==============================================================
//========Login
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import static register_attenance.OpenERPConnection.XmlRpcClienType.Common;
import static register_attenance.OpenERPConnection.XmlRpcClienType.DB;
import static register_attenance.OpenERPConnection.XmlRpcClienType.Object;
//=====================================================================

/**
 *
 * @author edgar
 */
public class clsConnection_to_OERP {

    protected static int SecondsToSleep = 500;
    protected static int SecondsToWait = 8000;
    public static ArrayList<String> DbList_GL;
    public static XmlRpcClient xmlrpcDb_GL;
    public static ArrayList<Object> params_GL;

    public boolean test_connection_method() {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
        xmlrpcConfigLogin.setEnabledForExtensions(true);

        try {
            xmlrpcConfigLogin.setServerURL(new URL("http", gl.getHost(), gl.getPort(), "/xmlrpc/common"));
            client.setConfig(xmlrpcConfigLogin);
        } catch (MalformedURLException ex) {
            return false;
        }
        boolean result = false;
        try {
            //Object res = client.execute("check_connectivity", new ArrayList<Object>());
            Object version = client.execute("version", new ArrayList<Object>());
            result = true;
        } catch (XmlRpcException e) {
        }
        return result;
    }

    public void test_connection_execute() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                gl.connected = test_connection_method();
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

    public ArrayList<String> getDBList_method() {
        ArrayList<String> res = null;
        try {
            Object[] db_list = (Object[]) xmlrpcDb_GL.execute("list", params_GL);
            res = new ArrayList<String>();
            for (Object db : db_list) {
                if (db instanceof String) {
                    res.add((String) db);
                }
            }
        } catch (XmlRpcException ex) {
            Logger.getLogger(clsConnection_to_OERP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public void getDBList_excute(XmlRpcClient xmlrpcDb, ArrayList<Object> params) {
        params_GL = params;
        xmlrpcDb_GL = xmlrpcDb;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DbList_GL = getDBList_method();
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
        XmlRpcClient client = OpenERP.build_xmlrcp_client(host, port, DB);
        ArrayList<String> DatabasesList = new ArrayList<String>();
        try {
            ArrayList<Object> params = new ArrayList<Object>();
            this.getDBList_excute(client, params);
            return DbList_GL;
        } catch (Exception e) {
            return DatabasesList;
        }
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

    public static HashMap get_next_event() {
        OpenERP oerp = hupernikao.BuildOpenERPConnection();
        XmlRpcClient client = oerp.build_xmlrcp_client(Object);

        Object[] params = new Object[]{oerp.getDatabase(), oerp.getUserId(), oerp.getPassword(), "kemas.event", "get_next_event"};
        HashMap result = null;
        try {
            Object event = (Object) client.execute("execute", params);
            if (!"false".equals(event.toString())) {
                result = (HashMap) event;
            }
        } catch (XmlRpcException ex) {
            Logger.getLogger(clsConnection_to_OERP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}

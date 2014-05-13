/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

//XML-RPC==============================================================
//========Login
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
//=====================================================================

/**
 *
 * @author edgar
 */
public class clsConnection_to_OERP {

    public boolean test_connection_method() {
        XmlRpcClient xmlrpcLogin = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
        xmlrpcConfigLogin.setEnabledForExtensions(true);
        try {
            xmlrpcConfigLogin.setServerURL(new URL("http", gl.getHost(), gl.getPort(), "/xmlrpc/common"));
            xmlrpcLogin.setConfig(xmlrpcConfigLogin);
        } catch (MalformedURLException ex) {
            return false;
        }
        try {
            Object res = xmlrpcLogin.execute("check_connectivity", new Vector());
            return Boolean.parseBoolean(res + "");
        } catch (Exception e) {
            return false;
        }
    }

    public void test_connection_execute() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                gl.connected = test_connection_method();
            }
        });
        thread.start();
        long endTimeMillis = System.currentTimeMillis() + 10000;
        while (thread.isAlive()) {
            if (System.currentTimeMillis() > endTimeMillis) {
                gl.connected = false;
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException t) {
            }
        }
    }

    public boolean test_connection() {
        test_connection_execute();
        return gl.connected;
    }

    public Vector<String> getDatabaseList(String host, int port) {
        Vector aux = new Vector<String>();
        XmlRpcClient xmlrpcDb = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigDb = new XmlRpcClientConfigImpl();
        xmlrpcConfigDb.setEnabledForExtensions(true);
        try {
            xmlrpcConfigDb.setServerURL(new URL("http", host, port, "/xmlrpc/db"));
            xmlrpcDb.setConfig(xmlrpcConfigDb);
        } catch (MalformedURLException ex) {

            return aux;
        }
        try {
            //Retrieve databases
            Vector<Object> params = new Vector<Object>();
            Object result = xmlrpcDb.execute("list", params);
            Object[] a = (Object[]) result;

            Vector<String> res = new Vector<String>();
            for (int i = 0; i < a.length; i++) {
                if (a[i] instanceof String) {
                    res.addElement((String) a[i]);
                }
            }
            return res;
        } catch (Exception e) {
            return aux;
        }
    }

    public String login_method(String username, String password, String ip, int port, String db) {
        if (test_connection() == false) {
            return "error_conexion";
        }
        XmlRpcClient xmlrpcLogin = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
        xmlrpcConfigLogin.setEnabledForExtensions(true);
        try {
            xmlrpcConfigLogin.setServerURL(new URL("http", ip, port, "/xmlrpc/common"));
        } catch (MalformedURLException ex) {
            return "error_conexion";
        }
        xmlrpcLogin.setConfig(xmlrpcConfigLogin);
        try {
            // Connect
            Vector params = new Vector();
            params.addElement(db);
            params.addElement(username);
            params.addElement(password);

            Object id;
            try {
                id = xmlrpcLogin.execute("login", params);
            } catch (XmlRpcException e) {
                return "error_conexion";
            }

            if ("false".equals("" + id)) {
                return "error_login";
            }
            //Traer los datos del Usuario Logueado
            Vector<Object> user = new Vector<Object>();
            int uid = ((Integer) id).intValue();
            try {
                user = read_user(uid, password, ip, port, db);
            } catch (Exception ex) {
                return "error_conexion";
            }
            gl.user = user;
            gl.Db = db;
            gl.Host = ip;
            gl.Port = port;
            String nombre = ((String) user.get(1)).toString();
            boolean is_consultant_login = false;
            try {
                is_consultant_login = is_register_attedance_login(uid, password, ip, port, db);
            } catch (Exception ex) {
            }
            if (is_consultant_login == false) {
                return "not_is_consultant_login";
            }
            return nombre;
        } catch (Exception e) {
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
        long endTimeMillis = System.currentTimeMillis() + 10000;
        while (thread.isAlive()) {
            if (System.currentTimeMillis() > endTimeMillis) {
                gl.login_status = "error";
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException t) {
            }
        }
    }

    public String login(String username, String password, String ip, int port, String db) {
        login_execute(username, password, ip, port, db);
        return gl.login_status;
    }

    public Vector read_user(int uid, String password, String ip, int port, String db) throws Exception {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setServerURL(new URL("http", ip, port, "/xmlrpc/object"));
        client.setConfig(clientConfig);

        Object[] params2 = {"id", "name", "login", "password", "user_email", "groups_id"};

        Vector<Object> arg = new Vector<Object>();

        arg.add(db);
        arg.add(uid);
        arg.add(password);
        arg.add("res.users");
        arg.add("read");
        arg.add(uid);
        arg.add(params2);

        HashMap ids = (HashMap) client.execute("execute", arg);

        Vector<Object> resp_read = new Vector<Object>();

        Object[] groups = (Object[]) ids.get("groups_id");

        resp_read.add(ids.get("id"));           //[0]
        resp_read.add(ids.get("name"));         //[1]
        resp_read.add(ids.get("login"));        //[2]
        resp_read.add(ids.get("password"));     //[3]
        resp_read.add(ids.get("user_email"));   //[4]
        resp_read.add(ids.get("groups_id"));    //[5]
        resp_read.add(groups);
        return resp_read;
    }

    public boolean is_register_attedance_login(int uid, String password, String ip, int port, String db) throws Exception {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setServerURL(new URL("http", ip, port, "/xmlrpc/object"));
        client.setConfig(clientConfig);

        Vector<Object> arg = new Vector<Object>();

        arg.add(db);
        arg.add(uid);
        arg.add(password);
        arg.add("kemas.func");
        arg.add("is_register_attedance_login");

        Object resp = client.execute("execute", arg);
        return Boolean.parseBoolean(resp + "");
    }

    public boolean is_consultant_login(int uid, String password, String ip, int port, String db) throws Exception {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setServerURL(new URL("http", ip, port, "/xmlrpc/object"));
        client.setConfig(clientConfig);

        Vector<Object> arg = new Vector<Object>();

        arg.add(db);
        arg.add(uid);
        arg.add(password);
        arg.add("kemas.func");
        arg.add("is_consultant_login");

        Object resp = client.execute("execute", arg);
        return Boolean.parseBoolean(resp + "");
    }

    public static HashMap get_next_event(int uid, String password, String ip, int port, String db) throws Exception {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setServerURL(new URL("http", ip, port, "/xmlrpc/object"));
        client.setConfig(clientConfig);

        Vector<Object> arg = new Vector<Object>();

        arg.add(db);
        arg.add(uid);
        arg.add(password);
        arg.add("kemas.event");
        arg.add("get_next_event");

        Object event = (Object) client.execute("execute", arg);
        if (!"false".equals(event.toString())) {
            return (HashMap) event;
        } else {
            return null;
        }
    }
}

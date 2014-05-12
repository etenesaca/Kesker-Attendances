/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

import java.util.*;
import java.text.*;
//XML-RPC==============================================================
//========Login
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
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
            } catch (Exception e) {
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

    public static byte[] decode(byte[] b) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] decodedBytes = decoder.decodeBuffer(new String(b));
            return decodedBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Collaborator read_collaborator(int uid, String password, String ip, int port, String db, int collaborator_id) throws Exception {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setServerURL(new URL("http", ip, port, "/xmlrpc/object"));
        client.setConfig(clientConfig);

        Object[] params2 = {"id", "name", "code", "points", "photo"};

        Vector<Object> arg = new Vector<Object>();

        arg.add(db);
        arg.add(uid);
        arg.add(password);
        arg.add("kemas.collaborator");
        arg.add("read");
        arg.add(collaborator_id);
        arg.add(params2);

        HashMap ids = (HashMap) client.execute("execute", arg);

        String photo = ids.get("photo") + "";
        byte[] dec = clsConnection_to_OERP.decode(photo.getBytes());
        //ImageIcon ii = new ImageIcon(dec);
        Collaborator resp_collaborator = new Collaborator();
        resp_collaborator.setPhoto(dec);
        resp_collaborator.setId("" + ids.get("id"));
        resp_collaborator.setName("" + ids.get("name"));
        resp_collaborator.setCode("" + ids.get("code"));
        resp_collaborator.setPoint(Integer.parseInt("" + ids.get("points")));

        String nick_name;
        resp_collaborator.setName("" + ids.get("name"));
        nick_name = clsConnection_to_OERP.name_get(uid, password, ip, port, db, collaborator_id);
        resp_collaborator.setNickname(nick_name);
        return resp_collaborator;
    }

    public static String name_get(int uid, String password, String ip, int port, String db, int collaborator_id) throws Exception {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setServerURL(new URL("http", ip, port, "/xmlrpc/object"));
        client.setConfig(clientConfig);

        Vector<Object> arg = new Vector<Object>();

        arg.add(db);
        arg.add(uid);
        arg.add(password);
        arg.add("kemas.collaborator");
        arg.add("get_nick_name");
        arg.add(collaborator_id);

        Object resp = client.execute("execute", arg);
        return "" + resp;
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

    public static int get_collaborator_id(int uid, String password, String ip, int port, String db, String username) throws Exception {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setEnabledForExtensions(true);
        clientConfig.setServerURL(new URL("http", ip, port, "/xmlrpc/object"));
        client.setConfig(clientConfig);

        Vector<Object> arg = new Vector<Object>();

        arg.add(db);
        arg.add(uid);
        arg.add(password);
        arg.add("kemas.collaborator");
        arg.add("get_by_user_id");
        arg.add(username);

        Object resp = client.execute("execute", arg);
        return Integer.parseInt("" + resp);
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

    public static Vector<Event> get_today_events(int uid, String password, String ip, int port, String db) throws Exception {
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
        arg.add("get_today_events");

        Object[] events = (Object[]) client.execute("execute", arg);

        Vector<Event> Eventos = new Vector<Event>();
        for (Object event_dic : events) {
            HashMap event = (HashMap) event_dic;

            Event event_ent = new Event();
            event_ent.setId(Integer.parseInt("" + event.get("id")));
            event_ent.setName("" + event.get("name"));
            event_ent.setTime_entry("" + event.get("time_entry"));
            event_ent.setTime_entry_int(Integer.parseInt("" + event.get("time_entry_int")));

            event_ent.setTime_register("" + event.get("time_register"));
            event_ent.setTime_register_int(Integer.parseInt("" + event.get("time_register_int")));

            event_ent.setTime_limit("" + event.get("time_limit"));
            event_ent.setTime_limit_int(Integer.parseInt("" + event.get("time_limit_int")));

            event_ent.setTime_start("" + event.get("time_start"));
            event_ent.setTime_start_int(Integer.parseInt("" + event.get("time_start_int")));
            event_ent.setTime_end("" + event.get("time_end"));
            event_ent.setTime_end_int(Integer.parseInt("" + event.get("time_end_int")));
            event_ent.setCurrent_event(Boolean.parseBoolean("" + event.get("current_event")));

            Eventos.add(event_ent);
        }
        return Eventos;
    }
}

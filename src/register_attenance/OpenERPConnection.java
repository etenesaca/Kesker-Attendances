package register_attenance;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class OpenERPConnection {

    protected static int SecondsToWait = 5000;
    protected String mServer;
    protected Integer mPort;
    protected String mDatabase;
    protected String mUserName;
    protected String mPassword;
    private Integer mUserId;
    protected URL mUrl;

    protected static final String CONNECTOR_NAME = "OpenERPconn";

    public Integer getUserId() {
        return mUserId;
    }

    public String getServer() {
        return mServer;
    }

    public Integer getPort() {
        return mPort;
    }

    public String getDatabase() {
        return mDatabase;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public OpenERPConnection(String server, String port, String db, String user, String pass, String uid) throws MalformedURLException {
        mServer = server;
        mPort = Integer.parseInt(port);
        mDatabase = db;
        mUserName = user;
        mPassword = pass;
        mUserId = Integer.parseInt(uid);
        mUrl = new URL("http", server, Integer.parseInt(port), "/xmlrpc/object");
    }

    public OpenERPConnection(String server, Integer port, String db, String user, String pass, Integer uid) throws MalformedURLException {
        mServer = server;
        mPort = port;
        mDatabase = db;
        mUserName = user;
        mPassword = pass;
        mUserId = uid;
        mUrl = new URL("http", server, port, "/xmlrpc/object");
    }

    public static OpenERPConnection connect(String server, Integer port, String db, String user, String pass) {
        return login(server, port, db, user, pass);
    }

    protected static OpenERPConnection login(String server, Integer port, String db, String user, String pass) {
        OpenERPConnection connection = null;
        try {
            URL loginUrl = new URL("http", server, port, "/xmlrpc/common");
            XmlRpcClient client = new XmlRpcClient();
            XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
            xmlrpcConfigLogin.setEnabledForExtensions(true);
            xmlrpcConfigLogin.setServerURL(loginUrl);
            client.setConfig(xmlrpcConfigLogin);

            // Ejecutar la peticion
            Object[] params = new Object[]{db, user, pass};
            Integer id = (Integer) client.execute("login", params);
            connection = new OpenERPConnection(server, port, db, user, pass, id);
        } catch (XmlRpcException ex) {
            Logger.getLogger(OpenERPConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(OpenERPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public Long create(String model, HashMap<String, ?> values, HashMap<String, ?> context) {
        Long newObjectId = null;
        try {
            XmlRpcClient client = build_xmlrcp_client(mUrl);

            Object[] params = new Object[]{getDatabase(), getUserId(), getPassword(), model, "create", values, context};
            Object result = client.execute("execute", params);
            newObjectId = Long.parseLong(result.toString());
        } catch (XmlRpcException e) {
            Logger.getLogger(OpenERPConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        return newObjectId;
    }

    // Cuando se mande las condiciones como una Arreglo
    public Long[] search(String model, List<Object> conditions) {
        return search(model, false, 0, 0, null, false, conditions);
    }

    public Long[] search(String model, List<Object> conditions, Integer limit) {
        return search(model, false, 0, limit, null, false, conditions);
    }

    public Long[] search(String model, boolean count, List<Object> conditions) {
        return search(model, count, 0, 0, null, false, conditions);
    }

    public Long[] search(String model, boolean count, Integer limit, String order, boolean reverseOrder, List<Object> conditions) {
        return search(model, count, 0, limit, order, reverseOrder, conditions);
    }

    public Long[] search(String model, boolean count, Integer offset, Integer limit, String order, boolean reverseOrder, List<Object> conditions) {
        Object[] ConditionsArray = (Object[]) conditions.toArray();
        return search(model, count, offset, limit, order, reverseOrder, ConditionsArray);
    }

    // Cuando se mande las condiciones como una Arreglo
    public Long[] search(String model, Object[] conditions) {
        return search(model, false, 0, 0, null, false, conditions);
    }

    public Long[] search(String model, Object[] conditions, Integer limit) {
        return search(model, false, 0, limit, null, false, conditions);
    }

    public Long[] search(String model, boolean count, Object[] conditions) {
        return search(model, count, 0, 0, null, false, conditions);
    }

    public Long[] search(String model, boolean count, Integer limit, String order, boolean reverseOrder, Object[] conditions) {
        return search(model, count, 0, limit, order, reverseOrder, conditions);
    }

    public void reverseArray(Long[] array) {
        int minIndex = 0;
        int maxIndex = array.length - 1;
        long minValue;
        while (minIndex < maxIndex) {
            minValue = array[minIndex];
            array[minIndex] = array[maxIndex];
            array[maxIndex] = minValue;
            minIndex++;
            maxIndex--;
        }
    }

    public Long[] search(String model, boolean count, Integer offset, Integer limit, String order, boolean reverseOrder, Object[] conditions) {
        Long[] result = null;
        try {
            XmlRpcClient client = build_xmlrcp_client(mUrl);

            Vector params = new Vector();
            params.add(getDatabase());
            params.add(getUserId());
            params.add(getPassword());
            params.add(model);
            params.add("search");
            params.add(conditions);
            params.add(offset);
            params.add(limit);
            params.add(order);
            params.add(null);
            params.add(count);
            if (count) { // We just want the number of items
                result = new Long[]{((Integer) client.execute("execute", params)).longValue()};

            } else { // Returning the list of matching item id's
                Object[] responseIds = (Object[]) client.execute("execute", params);
                result = new Long[responseIds.length];
                for (int i = 0; i < responseIds.length; i++) {
                    result[i] = ((Integer) responseIds[i]).longValue();
                }
                if (reverseOrder) {
                    reverseArray(result);
                }
            }
        } catch (XmlRpcException e) {
            Logger.getLogger(OpenERPConnection.class.getName()).log(Level.SEVERE, null, e);
        } catch (NullPointerException e) {
            Logger.getLogger(OpenERPConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    public List<HashMap<String, Object>> read(String model, Object[] ids, String[] fields) {
        Long[] res_ids = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            res_ids[i] = Long.parseLong(ids[i] + "");
        }
        return read(model, res_ids, fields);
    }
    
    public List<HashMap<String, Object>> read(String model, List<Long> ids, String[] fields) {
        Object[] object_ids = (Object[]) ids.toArray();
        Long[] res_ids = new Long[object_ids.length];
        for (int i = 0; i < object_ids.length; i++) {
            if (object_ids[i] instanceof Long) {
                res_ids[i] = (Long) object_ids[i];
            }
        }
        return read(model, res_ids, fields);
    }

    public HashMap<String, Object> read(String model, long id, String[] fields) {
        HashMap<String, Object> Record = null;
        try {
            Long[] ids = new Long[]{id};
            List<HashMap<String, Object>> records = read(model, ids, fields);
            Record = records.get(0);
        } catch (Exception e) {
            Logger.getLogger(OpenERPConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        return Record;
    }

    public XmlRpcClient build_xmlrcp_client(URL URL_request) {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
        xmlrpcConfigLogin.setEnabledForExtensions(true);
        xmlrpcConfigLogin.setServerURL(URL_request);
        client.setConfig(xmlrpcConfigLogin);

        return client;
    }

    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> read(String model, Long[] ids, String[] fields) {
        List<HashMap<String, Object>> Records = null;
        try {
            XmlRpcClient client = build_xmlrcp_client(mUrl);
            Object[] cids = new Object[ids.length];
            for (int i = 0; i < ids.length; i++) {
                cids[i] = Integer.parseInt(ids[i] + "");
            }
            Object[] params = new Object[]{getDatabase(), getUserId(), getPassword(), model, "read", cids, fields};
            Object[] responseFields = (Object[]) client.execute("execute", params);
            Records = new ArrayList<HashMap<String, Object>>(responseFields.length);
            for (Object objectFields : responseFields) {
                Records.add((HashMap<String, Object>) objectFields);
            }
        } catch (XmlRpcException e) {
            Logger.getLogger(OpenERPConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        return Records;
    }
}

package register_attenance;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class OpenERP extends OpenERPConnection {
     /**
     * Este Metodo devuelve todos los colaboradores que han registrado asistencia en un evento
     */
    public List<HashMap<String, Object>> getCollaboratorsRegistereds(int event_id) {
        XmlRpcClient client = build_xmlrcp_client(mUrl);

        Vector<Object> params = new Vector<Object>();
        params.add(getDatabase());
        params.add(getUserId());
        params.add(getPassword());
        params.add("kemas.event");
        params.add("get_collaborators_registered");
        params.add(event_id);

        List<HashMap<String, Object>> Collaborators = new ArrayList<HashMap<String, Object>>();
        try {
            Object[] Records = (Object[]) client.execute("execute", params);
            for (Object Record : Records) {
                Collaborators.add((HashMap<String, Object>) Record);
            }
        } catch (XmlRpcException e) {
        }
        return Collaborators;
    }

    /**
     * Constructor con el uid en Integer *
     */
    public OpenERP(String server, Integer port, String db, String user, String pass, Integer uid) throws MalformedURLException {
        super(server, port, db, user, pass, uid);
    }

    /**
     * Constructor con el uid en String *
     */
    public OpenERP(String server, String port, String db, String user, String pass, String uid) throws MalformedURLException {
        super(server, port, db, user, pass, uid);
    }

    /**
     * Redefición del método Connect *
     */
    public static OpenERP connect(String server, Integer port, String db, String user, String pass) {
        return login(server, port, db, user, pass);
    }

    /**
     * Redefinición del Metodo login *
     */
    protected static OpenERP login(String server, Integer port, String db, String user, String pass) {
        OpenERP result = null;
        OpenERPConnection res_login = OpenERPConnection.login(server, port, db, user, pass);
        if (res_login != null) {
            try {
                result = new OpenERP(server, port, db, user, pass, res_login.getUserId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

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

public class OpenERP extends OpenERPConnection {

    public enum RespRegistrarAsisitencia {

        Error_login,
        No_Collaborator,
        No_staff,
        No_events,
        Already_register,
        Already_checkout,
        checkin,
        checkout
    }

    public RespRegistrarAsisitencia RegisterAttendance(String reg_username, String reg_password) {
        XmlRpcClient client = build_xmlrcp_client(mUrl);

        Vector<Object> params = new Vector<Object>();
        HashMap<Object, Object> context = new HashMap<Object, Object>();
        context.put("with_register_type", true);
        params.add(getDatabase());
        params.add(getUserId());
        params.add(getPassword());
        params.add("kemas.attendance");
        params.add("register_attendance");
        params.add(reg_username);
        params.add(reg_password);
        params.add(context);

        RespRegistrarAsisitencia result = null;
        try {
            Object resp = client.execute("execute", params);
            String _resp = "" + resp;
            if ("r_1".equals(_resp)) {
                result = RespRegistrarAsisitencia.Error_login;
            } else if ("r_2".equals(_resp)) {
                result = RespRegistrarAsisitencia.No_Collaborator;
            } else if ("r_3".equals(_resp)) {
                result = RespRegistrarAsisitencia.No_staff;
            } else if ("r_4".equals(_resp)) {
                result = RespRegistrarAsisitencia.No_events;
            } else if ("r_5".equals(_resp)) {
                result = RespRegistrarAsisitencia.Already_register;
            } else if ("r_6".equals(_resp)) {
                result = RespRegistrarAsisitencia.Already_checkout;
            } else {
                HashMap<Object, Object> resp_dict = (HashMap<Object, Object>) resp;
                if (resp_dict.get("register_type").equals("checkin")) {
                    result = RespRegistrarAsisitencia.checkin;
                } else {
                    result = RespRegistrarAsisitencia.checkout;
                }
            }
        } catch (XmlRpcException ex) {
            Logger.getLogger(OpenERP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Este Metodo devuelve todos los colaboradores que han registrado
     * asistencia en un evento
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

    public Vector<Collaborator> getEventCollaborators(int event_id) {
        XmlRpcClient client = build_xmlrcp_client(mUrl);

        Vector<Object> params = new Vector<Object>();
        params.add(getDatabase());
        params.add(getUserId());
        params.add(getPassword());
        params.add("kemas.event");
        params.add("get_collaborators_by_event");
        params.add(event_id);

        Vector<Collaborator> Collaborators = new Vector<Collaborator>();
        try {
            Object[] colls = (Object[]) client.execute("execute", params);
            for (Object collaborator_dic : colls) {
                HashMap current_collaborator = (HashMap) collaborator_dic;
                String id, nombre, username;
                HashMap<Object, Object> registrado = null;

                id = "" + current_collaborator.get("id");
                nombre = "" + current_collaborator.get("name");
                username = "" + current_collaborator.get("username");
                if (!current_collaborator.get("registered").toString().equals("false")) {
                    registrado = (HashMap<Object, Object>) current_collaborator.get("registered");
                }

                Collaborator Collaborator_ent = new Collaborator();
                Collaborator_ent.setId(id);
                Collaborator_ent.setName(nombre);
                Collaborator_ent.setUsername(username);
                Collaborator_ent.setRegistrado(registrado);

                Collaborators.add(Collaborator_ent);
            }
        } catch (XmlRpcException e) {
        }
        return Collaborators;
    }

    public Collaborator getCollaborator(int collaborator_id) {
        String[] fields = {"id", "name", "name_with_nick_name", "code", "points", "user_id"};
        HashMap<String, Object> Collaborator_dict = read("kemas.collaborator", collaborator_id, fields);
        Collaborator resp_collaborator = new Collaborator();
        resp_collaborator.setId(Collaborator_dict.get("id").toString());
        resp_collaborator.setCode(Collaborator_dict.get("code").toString());
        resp_collaborator.setName(Collaborator_dict.get("name").toString());
        resp_collaborator.setShortName(Collaborator_dict.get("name_with_nick_name").toString());
        resp_collaborator.setPoint(Integer.parseInt(Collaborator_dict.get("points").toString()));
        return resp_collaborator;
    }

    public Collaborator getCollaborator(String username) {
        String model = "kemas.collaborator";
        ArrayList<Object> args = new ArrayList<Object>();
        args.add(new Object[]{"user_id.login", "=", username});
        Long[] collaborator_ids = search(model, args);
        return getCollaborator(Integer.parseInt(collaborator_ids[0].toString()));
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

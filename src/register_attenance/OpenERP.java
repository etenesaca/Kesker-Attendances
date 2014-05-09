package register_attenance;

import java.net.MalformedURLException;
import java.net.URL;

public class OpenERP extends OpenERPConnection {

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

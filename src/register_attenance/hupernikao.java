package register_attenance;

import java.net.MalformedURLException;

public class hupernikao {

    /**
     * Este método devuelve una conexión a OpenERP en el caso de que ya se
     * tengan todas las credenciales necesarias
     *
     */
    public static OpenERP BuildOpenERPConnection() {
        OpenERP oerp = null;
        
        int uid = Integer.parseInt("" + gl.user.get(0));
        String username = "" + gl.user.get(2); 
        String password = "" + gl.user.get(3); 
        String server = gl.getHost();
        int port = gl.getPort(); 
        String db = gl.getDb(); 
        
        try {
            oerp = new OpenERP(server, port, db, username, password, uid);
        } catch (MalformedURLException e) {
        }
        return oerp;
    }
}

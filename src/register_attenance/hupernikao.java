package register_attenance;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.ws.commons.util.Base64;

public class hupernikao {

    /**
     * Este método devuelve una conexión a OpenERP en el caso de que ya se
     * tengan todas las credenciales necesarias
     *
     * @return Devuelve la instancia de una conexion al Servidor de OpenERP
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

    public static ImageIcon ReziseImage(byte[] Photo, int min_size) {
        ImageIcon ii = new ImageIcon(Photo);
        Image img = ii.getImage();
        return ReziseImage(img, min_size);
    }

    public static ImageIcon ReziseImage(Image img, int min_size) {
        int img_width = img.getWidth(null);
        int img_height = img.getHeight(null);
        if (img_width != img_height) {
            if (img_width < img_height) {
                img_width = img_width * min_size / img_height;
                img_height = min_size;
            } else {
                img_height = img_height * min_size / img_height;
                img_width = min_size;
            }
        } else {
            img_width = min_size;
            img_height = min_size;
        }
        //------------------------------------
        Image newimg = img.getScaledInstance(img_width, img_height, java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        return newIcon;
    }

    public byte[] getImgBytes(Image image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(getBufferedImage(image), "JPEG", baos);
        } catch (IOException ex) {
            Logger.getLogger(hupernikao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos.toByteArray();
    }

    private BufferedImage getBufferedImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Graphics2D g2d = bi.createGraphics();
        //g2d.drawImage(image, 0, 0, null);
        return bi;
    }

    public static String DecodeB64ToString(String String64) {
        return new String(DecodeB64ToBytes(String64));
    }

    public static byte[] DecodeB64ToBytes(String String64) {
        byte[] result = null;
        Base64 decoder = new Base64();
        try {
            result = decoder.decode(String64);
        } catch (Base64.DecodingException ex) {
            Logger.getLogger(hupernikao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}

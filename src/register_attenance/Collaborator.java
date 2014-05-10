/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author edgar
 */
public class Collaborator {
    private String Id, Name, Username, Nickname, Code;
    private int Point;
    private byte[] Photo;
    private boolean registrado;
    private boolean checkout;

    public boolean isRegistrado() {
        return registrado;
    }
    
    public boolean isCheckout() {
        return checkout;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }

    public byte[] getPhoto() {
        return Photo;
    }

    public void setPhoto(byte[] Photo) {
        this.Photo = Photo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int Point) {
        this.Point = Point;
    }
    
    public static ImageIcon resize_image(byte[] Photo, int min_size){
        ImageIcon ii = new ImageIcon(Photo);
        Image img = ii.getImage();
        int img_width = img.getWidth(null);
        int img_height = img.getHeight(null);
        if (img_width != img_height){
            if (img_width < img_height){
                img_width = img_width * min_size / img_height;
                img_height = min_size;
            }
            else{
                img_height = img_height * min_size / img_height;
                img_width = min_size;
            }
        }
        else{
            img_width = min_size;
            img_height = min_size;
        }
        //------------------------------------
        Image newimg = img.getScaledInstance(img_width, img_height,  java.awt.Image.SCALE_SMOOTH);  
        ImageIcon newIcon = new ImageIcon(newimg);
        return newIcon;
    }
}

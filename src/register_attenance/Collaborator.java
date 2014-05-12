/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author edgar
 */
public class Collaborator {
    private String Id, Name, Username, Nickname, Code;
    private int Point;
    private byte[] Photo;
    private HashMap<Object,Object> registrado;
    private boolean checkout;

    public HashMap<Object,Object> isRegistrado() {
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

    public void setRegistrado(HashMap<Object,Object> registrado) {
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
}

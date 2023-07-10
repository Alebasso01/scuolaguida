package com.example.scuolaguida.models;

public class MyUser {
    private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();

    private String giorno,capitolo,orario,userid, lezioneid;

    public MyUser(){}

    public String getCapitolo() {return capitolo;}

    public String getGiorno() {return giorno;}

    public String getOrario() {return orario;}

    public String getUserid(){return auth.getUid();}

    public String getLezioneid() {return lezioneid;}

    public void setCapitolo(String capitolo) {this.capitolo = capitolo;}

    public void setGiorno(String giorno) {this.giorno = giorno;}

    public void setOrario(String orario) {this.orario = orario;}

    public void setUserid(String userid){this.userid = auth.getUid();}

    public void setLezioneid(String lezioneid) {this.lezioneid = lezioneid;}
}

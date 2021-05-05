package com.ceft.perto.Model;

public class Annonce {

     private String  iduser, titre , type,ville, description,Tel,img_annomca;


     public Annonce(){};

    public Annonce(String iduser, String titre, String type,String description,String ville, String img_annomca,String Tel) {
        this.iduser=iduser;
        this.titre = titre;
        this.type = type;
        this.description= description;
        this.ville=ville;
        this.img_annomca = img_annomca;
        this.Tel= Tel;
    }
    public Annonce(String titre, String type,String ville, String img_annomca) {
        this.titre = titre;
        this.description= description;
        this.type = type;
        this.ville=ville;
        this.img_annomca = img_annomca;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_annomca() {
        return img_annomca;
    }

    public void setImg_annomca(String img_annomca) {
        this.img_annomca = img_annomca;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }
}

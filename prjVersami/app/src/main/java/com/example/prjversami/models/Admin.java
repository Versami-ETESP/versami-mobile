package com.example.prjversami.models;

public class Admin extends Perfil{
    private Boolean admPermission;

    public Admin(){

    }

    public Admin(String name, String login, String email, String pass, Boolean admPermission){
        this.setUserName(name);
        this.setUserLogin(login);
        this.setUserEmail(email);
        this.setUserPass(pass);
        this.setAdmPermission(admPermission);
    }

    public boolean isAdmPermission() {
        return admPermission;
    }

    public void setAdmPermission(boolean admPermission) {
        this.admPermission = admPermission;
    }
}

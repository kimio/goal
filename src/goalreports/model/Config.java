/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goalreports.model;

import java.io.Serializable;

/**
 *
 * @author felipekn
 */
public class Config implements Serializable{
    private String usuario = "";
    private String projeto = "";
    private String nomeTime = "";
    private String credencialJson = "";
    private String idSituationWall = "";
    private String pastaFirefoxInstalado = "";
    
    public String getUsuario(){
        return usuario;
    }
    public String getProjeto(){
        return projeto;
    }
    public String getNomeTime(){
        return nomeTime;
    }
    public String getCredencialJson(){
        return credencialJson;
    }
    public String getIdSituationWall(){
        return idSituationWall;
    }
    public String getPastaFirefoxInstalado(){
        return pastaFirefoxInstalado;
    }
    
    public void setUsuario(String usuario){
        this.usuario = usuario;
    }
    public void setProjeto(String projeto){
        this.projeto = projeto;
    }
    public void setNomeTime(String nomeTime){
        this.nomeTime = nomeTime;
    }
    public void setCredencialJson(String credencialJson){
        this.credencialJson = credencialJson;
    }
    public void setIdSituationWall(String idSituationWall){
        this.idSituationWall = idSituationWall;
    }
    public void setPastaFirefoxInstalado(String pastaFirefoxInstalado){
        this.pastaFirefoxInstalado = pastaFirefoxInstalado;
    }
}

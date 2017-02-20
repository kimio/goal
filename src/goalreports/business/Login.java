/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goalreports.business;

import goalreports.helper.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author felipekimionishikaku
 */
public class Login  extends SeleniumHelper{

    public Login(WebDriver driver) {
        super(driver);
    }
    public void logar(String user,String password){
        openUrl("http://goal.cit.com.br");
        waitForPageLoad();
        setValueByIdElement("txt_login",user);
        setValueByIdElement("txt_pass",password);
        setValueByIdElement("txt_pass",Keys.ENTER);
        waitForPageLoad();
    }
    
    
}

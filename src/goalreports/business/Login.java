package goalreports.business;

import goalreports.helper.SeleniumHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        sleep(3);
        setValueByIdElement("txt_pass",Keys.ENTER);
        waitForPageLoad();
    }
    
    
}

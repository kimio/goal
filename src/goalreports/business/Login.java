package goalreports.business;

import goalreports.helper.SeleniumHelper;
import java.awt.event.KeyEvent;
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
        setValueByIdElement("txt_pass",KeyEvent.VK_ENTER);
        waitForPageLoad();
    }
    
    
}

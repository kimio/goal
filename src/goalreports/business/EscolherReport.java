package goalreports.business;

import goalreports.helper.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author felipekimionishikaku
 */
public class EscolherReport extends SeleniumHelper{

    public EscolherReport(WebDriver driver) {
        super(driver);
    }
    public void escolherReport(String frente){
        
        fluentWait(By.id("ctl00_menup_reports"));
        openUrl("http://goal.cit.com.br/GoalReports.aspx?Mode=Cards");
        waitForPageLoad();
        clicarNoProjeto(frente);
    }
    private void clicarNoProjeto(String frente){
        if(fluentWait(By.className("dropdown-toggle"))==null){
            clicarNoProjeto(frente);
        }else{
            String jsOpenReportMenu = "$('.multiselect.dropdown-toggle.btn.btn-link')[$('.multiselect.dropdown-toggle.btn.btn-link').length-1].click();";
            String jsProjectReport = "for(i = 1;i < $('label[class=checkbox]').length;i++){"+
               "if($('label[class=checkbox]')[i].innerText=='"+frente+"'){"+
                   "$('label[class=checkbox]')[i].click();"+
                   "break;"+ 
               "}"+
            "}";
            callJs(jsOpenReportMenu);
            sleep(3);
            callJs(jsProjectReport);
        }
    }
    
}

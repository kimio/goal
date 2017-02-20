/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goalreports.business;

import goalreports.helper.SeleniumHelper;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author felipekimionishikaku
 */
public class CapturarReport extends SeleniumHelper{
    private String currentReport = "";
    public static class ReportConfig{
        public String dateFrom;
        public String dateTo;
        public String nameSquad;
    }
    private ReportConfig config;
    public CapturarReport(WebDriver driver) {
        super(driver);
    }
    private void clickOnReport(String reportName){
        String jsClickReport = "for(i = 1;i < $('h1').length;i++){" +
            "if($('h1')[i].innerText.trim()=='"+reportName+"'){"+
               "$('h1')[i].click();"+
               "break;"+ 
           "}"+
        "}";
        callJs(jsClickReport);
    }
    private void setDateFrom(){
        callJs("$(\"#date_from\").val('"+config.dateFrom+"');");
    }
    private void setDateTo(){
        callJs("$(\"#date_to\").val('"+config.dateTo+"');");
    }
    private void setNameSquad(){
        callJs("$(\"#SprintFilter\").val('"+config.nameSquad+"');");
    }
    
    public void setConfigReport(ReportConfig config){
        this.config = config;
    }
    public void entrarEmCPDelivered() throws InterruptedException{
        Thread.sleep(5000);
        currentReport = "CP Delivered";
        clickOnReport(currentReport);
        fluentWait(By.id("SprintFilter"));
        setDateFrom();
        setDateTo();
        setNameSquad();  
        getImageReport();
    }
    public void entrarEmProdutividadeBurnPotencial() throws InterruptedException{
        Thread.sleep(5000);
        currentReport = "Produtividade Burn - Potencial";
        clickOnReport(currentReport);
        fluentWait(By.id("selMoreFilters_chosen"));
        
        String jsChooseSprintFilter = "$(\"#selMoreFilters\").val(\"23\");" +
"        $(GoalReports.DOM.moreFiltersDropdown).trigger(\"chosen:updated\");" +
"        Goal.sendRequest(GoalFilters.URI.GetFilters," +
"                   GoalFilters.renderFilters," +
"                   Goal.handleError," +
"                   JSON.stringify(" +
"                   {" +
"                       filters: [23]," +
"                       projects: Goal.selectedProjects()" +
"                   }));";
        
        callJs(jsChooseSprintFilter);
        fluentWait(By.id("SprintFilter"));
        setDateFrom();
        setDateTo();
        setNameSquad();  
        getImageReport();
    }
    
            
    private void getImageReport(){
        fluentWait(By.className("google-visualization-table-table"));
        callJs("GoalReports.btnGenerateClick();");
        waitForReportSquad();
    }
    private void waitForReportSquad(){
        fluentWait(By.className("google-visualization-table-table"));
        boolean isCurrentSquadReport = false;
        List<WebElement> lista = driver.findElements(By.className("google-visualization-table-td"));
        for(WebElement element : lista){
            if(element.getText().toLowerCase().contains(config.nameSquad.toLowerCase())){
                captureScreen(By.id("visualization"),currentReport);
                isCurrentSquadReport = true;
                break;
            }
        }
        if(!isCurrentSquadReport){
           waitForReportSquad();
        }
    }
    
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goalreports.business;

import goalreports.helper.SeleniumHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CapturarReport.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public String entrarEmCPDelivered(){
        currentReport = "CP Delivered";
        clickOnReport(currentReport);
        fluentWait(By.id("SprintFilter"));
        setDateFrom();
        setDateTo();
        setNameSquad(); 
        fluentWait(By.className("google-visualization-table-table"));
        return getImageReport();
    }
    public String entrarEmProdutividadeBurnPotencial(){
        currentReport = "Produtividade Burn - Potencial";
        clickOnReport(currentReport);
        fluentWait(By.id("selMoreFilters_chosen"));
        callSprintFilter();
        setDateFrom();
        setDateTo();
        setNameSquad();  
        return getImageReport();
    }
    public String entrarEmPerformanceWorklog(){
        currentReport = "Performance Worklog";
        clickOnReport(currentReport);
        fluentWait(By.id("SprintFilter"));
                
        setDateFrom();
        setDateTo();
        setNameSquad();  
        fluentWait(By.className("google-visualization-table-table"));
        return getImageReport();
    }
    public String entrarEmQualityDEVPotencial(){
        currentReport = "Produtividade Burn - Potencial";
        clickOnReport(currentReport);
        currentReport = "Quality DEV Potential";
        
        fluentWait(By.id("ctl00_cphContent_selReport"));
        String jsChooseReport = "$('#ctl00_cphContent_selReport').val('190');" +
"        $(GoalReports.DOM.ReportSel).trigger('chosen:updated');";
        callJs(jsChooseReport);

        String jsChooseGroupBy = "$('#ctl00_cphContent_ddlGroupBy').val('4');" +
"        $(GoalReports.DOM.RangesDropdown).trigger('chosen:updated');";
        callJs(jsChooseGroupBy);
        
        callSprintFilter();
        setDateFrom();
        setDateTo();
        setNameSquad();  
        return getImageReport();
    }
    public String entrarEmDefeitosPorFase(){
        currentReport = "Defeitos por Fase";
        clickOnReport(currentReport);
        callSprintFilter();
                
        setDateFrom();
        setDateTo();
        setNameSquad();  
        fluentWait(By.className("google-visualization-table-table"));
        return getImageReport();
    }
    public String entrarEmRetrabalho(){
        currentReport = "Retrabalho";
        clickOnReport(currentReport);
        callSprintFilter();
                
        setDateFrom();
        setDateTo();
        setNameSquad();  
        fluentWait(By.className("google-visualization-table-table"));
        return getImageReport();
    }
    
    private void callSprintFilter(){
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
    }
    private String getImageReport(){
        callJs("GoalReports.btnGenerateClick();");
        int numberColumnSquadName = (currentReport.equals("Retrabalho"))?2:1;
        return waitForReportSquad(numberColumnSquadName);
    }
    private String waitForReportSquad(int numberColumnSquadName){
        fluentWait(By.className("google-visualization-table-table"));
        boolean isCurrentSquadReport = false;
        String report = "";
        WebElement itemList = driver.findElements(By.className("google-visualization-table-td")).get(numberColumnSquadName);
        try{
            if(itemList.getText().toLowerCase().contains(config.nameSquad.toLowerCase())){
                fluentWait(By.id("visualization"));
                report = captureScreen(By.id("visualization"),currentReport);
                isCurrentSquadReport = true;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        if(!isCurrentSquadReport){
           report = waitForReportSquad(numberColumnSquadName);
        }else{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CapturarReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return report;
    }
    
}


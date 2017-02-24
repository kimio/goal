package goalreports.business;

import goalreports.helper.SeleniumHelper;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import util.Util;

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
    private final String 
        CP_DELIVERED = "CP Delivered",
        PRODUTIVIDADE_BURN_POTENCIAL = "Produtividade Burn - Potencial",
        PERFOMANCE_WORKLOG = "Performance Worklog",
        QUALITY_DEV_POTENCIAL = "Quality DEV Potential",
        DEFEITOS_POR_FASE = "Defeitos por Fase",
        RETRABALHO = "Retrabalho";
    
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
        currentReport = CP_DELIVERED;
        clickOnReport(currentReport);
        fluentWait(By.id("SprintFilter"));
        setDateFrom();
        setDateTo();
        setNameSquad(); 
        fluentWait(By.className("google-visualization-table-table"));
        return getImageReport();
    }
    public String entrarEmProdutividadeBurnPotencial(){
        currentReport = PRODUTIVIDADE_BURN_POTENCIAL;
        clickOnReport(currentReport);
        fluentWait(By.id("selMoreFilters_chosen"));
        callSprintFilter();
        setDateFrom();
        setDateTo();
        setNameSquad();  
        return getImageReport();
    }
    public String entrarEmPerformanceWorklog(){
        currentReport = PERFOMANCE_WORKLOG;
        clickOnReport(currentReport);
        fluentWait(By.id("SprintFilter"));
                
        setDateFrom();
        setDateTo();
        setNameSquad();  
        fluentWait(By.className("google-visualization-table-table"));
        return getImageReport();
    }
    public String entrarEmQualityDEVPotencial(){
        currentReport = PRODUTIVIDADE_BURN_POTENCIAL;
        clickOnReport(currentReport);
        currentReport = QUALITY_DEV_POTENCIAL;
        
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
        currentReport = DEFEITOS_POR_FASE;
        clickOnReport(currentReport);
        callSprintFilter();
                
        setDateFrom();
        setDateTo();
        setNameSquad();  
        fluentWait(By.className("google-visualization-table-table"));
        return getImageReport();
    }
    public String entrarEmRetrabalho(){
        currentReport = RETRABALHO;
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
    private String getReportLastInformation(){
        List<WebElement> list = driver.findElements(By.className("google-visualization-table-td"));
        int lastIndexItem = list.size()-1;
        
        switch(currentReport){
            case CP_DELIVERED:
                return "Real CP - "+list.get(lastIndexItem-1).getText()+" Potencial CP - "+list.get(lastIndexItem).getText();
            case PRODUTIVIDADE_BURN_POTENCIAL:
                return "Burn - "+list.get(lastIndexItem-6).getText()+" Accumulated - "+list.get(lastIndexItem-5).getText()+" Goal - "+list.get(lastIndexItem-4).getText();
            case PERFOMANCE_WORKLOG:
                return "Rework - "+list.get(lastIndexItem-3).getText()+" Burn - "+list.get(lastIndexItem-2).getText()+" Build - "+list.get(lastIndexItem-1).getText()+" All Work - "+list.get(lastIndexItem).getText();
            case QUALITY_DEV_POTENCIAL:
                return "DEV - "+list.get(lastIndexItem-6).getText()+" DEV Acc - "+list.get(lastIndexItem-5).getText()+" Goal - "+list.get(lastIndexItem-4).getText();
            case DEFEITOS_POR_FASE:
                return "DEV - "+list.get(lastIndexItem-2).getText()+" PROD - "+list.get(lastIndexItem-1).getText()+" UAT - "+list.get(lastIndexItem).getText();
            case RETRABALHO:
                return "Rework(%) - "+list.get(lastIndexItem-6).getText()+" Rework(%) Acc - "+list.get(lastIndexItem-5).getText();
                
        }
        return "";
    }
    private String getImageReport(){
        callJs("GoalReports.btnGenerateClick();");
        int numberColumnSquadName = (currentReport.equals("Retrabalho"))?2:1;
        return waitForReportSquad(numberColumnSquadName);
    }
    private void updateReportImageWithTitleText(String report) throws IOException{
        File file = new File(report);
        Util.ImageText text = new Util.ImageText();
        Util.ImageText.value = currentReport+" - "+config.nameSquad;
        Util.ImageText.posX = 5;
        Util.ImageText.posY = 25;
        Util.writeTextInImage(file,text);
    }
    private void updateReportImageWithSubTitleText(String report) throws IOException{
        File file = new File(report);
        Util.ImageText text = new Util.ImageText();
        Util.ImageText.value = getReportLastInformation();
        Util.ImageText.posX = 5;
        Util.ImageText.posY = 47;
        Util.ImageText.font = new Font("Arial Black", Font.PLAIN, 16);
        Util.writeTextInImage(file,text);
    }
    private String waitForReportSquad(int numberColumnSquadName){
        fluentWait(By.className("google-visualization-table-table"));
        boolean isCurrentSquadReport = false;
        String report = "";
        WebElement itemList = driver.findElements(By.className("google-visualization-table-td")).get(numberColumnSquadName);
        try{
            if(itemList.getText().toLowerCase().contains(config.nameSquad.toLowerCase())){
                fluentWait(By.id("visualization"));
                
                //screen capture and insert a text
                report = captureScreen(By.id("visualization"),currentReport);
                updateReportImageWithTitleText(report);
                updateReportImageWithSubTitleText(report);
                
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


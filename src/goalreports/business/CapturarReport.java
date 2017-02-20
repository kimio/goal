/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goalreports.business;

import goalreports.helper.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author felipekimionishikaku
 */
public class CapturarReport extends SeleniumHelper{
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
            "if($('h1')[i].innerText=='"+reportName+"'){"+
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
        clickOnReport("CP Delivered");
        fluentWait(By.id("SprintFilter"));
        
        
        
//        callJs("$(\"#selMoreFilters_chosen\").children()[0].click();");
        
        
//           String jsFilters = "for(i = 1;i < $('.active-result').length;i++){"+
//           "if($('.active-result')[i].innerText=='"+frente+"'){"+
//               "$('.active-result')[i].click();"+
//               "break;"+ 
//           "}"+
//        "}";
        System.out.println("TEste");
        setDateFrom();
        setDateTo();
        setNameSquad();  
        
        fluentWait(By.className("google-visualization-table-table"));
        System.out.println("TEste1");
        callJs("GoalReports.btnGenerateClick();");
        fluentWait(By.className("google-visualization-table-table"));
        System.out.println("tirar screen shot");
        captureScreen(By.id("visualization"));
        
    }
    
}


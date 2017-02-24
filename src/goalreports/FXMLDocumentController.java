/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goalreports;

import goalreports.business.CapturarReport;
import goalreports.business.CapturarReport.ReportConfig;
import goalreports.business.EscolherReport;
import goalreports.business.Login;
import goalreports.business.SituationWall;
import goalreports.helper.GoogleApiHelper;
import goalreports.helper.SeleniumHelper;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


/**
 *
 * @author felipekimionishikaku
 */
public class FXMLDocumentController implements Initializable {
    
    
    @FXML
    private TextField usuario;
    @FXML
    private PasswordField senha;
    @FXML
    private TextField frente;
    @FXML
    private DatePicker data_inicial;
    @FXML
    private DatePicker data_final;
    @FXML
    private TextField time;
    @FXML
    private TextField credencial_json;
    @FXML
    private TextField id_situation_wall;
    
    private List<String> reports;
    @FXML
    private void handleButtonAction(ActionEvent event) throws InterruptedException {
        //get all reports
        getAllReports();
        
        GoogleApiHelper googleapi = new GoogleApiHelper("Goal", credencial_json.getText());
        SituationWall situationWall = new SituationWall(googleapi);
        //upload reports to google drive account
        situationWall.uploadReports(reports);
        //update situation wall data
        situationWall.setDataInSituationWall(id_situation_wall.getText());
        
    }
    private void getAllReports(){
        SeleniumHelper selenium = new SeleniumHelper(SeleniumHelper.Browser.Firefox);
        new Login(selenium.driver).logar(usuario.getText(), senha.getText());
        
        reports = new ArrayList<>();
        CapturarReport capturarReport = escolherReport(selenium);
        reports.add(capturarReport.entrarEmCPDelivered());
        
        capturarReport = escolherReport(selenium);
        reports.add(capturarReport.entrarEmProdutividadeBurnPotencial());

        capturarReport = escolherReport(selenium);
        reports.add(capturarReport.entrarEmPerformanceWorklog());
        
        capturarReport = escolherReport(selenium);
        reports.add(capturarReport.entrarEmQualityDEVPotencial());
        
        capturarReport = escolherReport(selenium);
        reports.add(capturarReport.entrarEmDefeitosPorFase());
        
        capturarReport = escolherReport(selenium);
        reports.add(capturarReport.entrarEmRetrabalho());
    }
    
    private CapturarReport escolherReport(SeleniumHelper selenium){
        new EscolherReport(selenium.driver).escolherReport(frente.getText());
        CapturarReport capturarReport = new CapturarReport(selenium.driver);

        ReportConfig config = new ReportConfig();
        config.dateFrom = getDate(data_inicial);
        config.dateTo = getDate(data_final);
        config.nameSquad = time.getText();
        capturarReport.setConfigReport(config);
        
        return capturarReport;
    }
    private String getDate(DatePicker date){
        LocalDate localDate = date.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        return format1.format(Date.from(instant));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void escolherCredencial(ActionEvent event) throws InterruptedException {
        List<File> files = util.Util.openFileChooser(GoalReports.currentStage, "Selecione o Credencial.json", false);
        files.forEach((file_import) -> {
            credencial_json.setText(file_import.getAbsolutePath());
        });
    }
    
    
}

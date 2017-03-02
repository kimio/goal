/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goalreports;

import com.google.gson.Gson;
import goalreports.business.CapturarReport;
import goalreports.business.CapturarReport.ReportConfig;
import goalreports.business.EscolherReport;
import goalreports.business.Login;
import goalreports.business.SituationWall;
import goalreports.helper.GoogleApiHelper;
import goalreports.helper.SeleniumHelper;
import goalreports.model.Config;
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
import util.Util;

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
    private Config configuration;
    private final String fileConfig = "config/config.json";
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws InterruptedException {
        Thread thread = new Thread(){
            @Override
            public void run(){
                //update configuration
                updateConfiguration();

                //get all reports
                getAllReports();
                
                util.Util.showAlertInformation("Informação",
                        "Reports",
                        "Todos Reports capturados.");
            
                GoogleApiHelper googleapi = new GoogleApiHelper("Goal", credencial_json.getText());
                SituationWall situationWall = new SituationWall(googleapi);
                //upload reports to google drive account
                if(situationWall.uploadReports(reports)!=null){
                    util.Util.showAlertInformation("Informação",
                            "Reports",
                            "Todos Reports enviados para o Google Drive.");

                }
                //update situation wall data
                if(situationWall.setDataInSituationWall(id_situation_wall.getText())){
                    
                    util.Util.showAlertInformation("Informação",
                            "Reports",
                            "Data do Situation Wall atualizada.");
                }
            }
        };
        thread.start();
    }
    private void updateConfiguration(){
        
        configuration.setCredencialJson(credencial_json.getText());
        configuration.setIdSituationWall(id_situation_wall.getText());
        configuration.setNomeTime(time.getText());
        configuration.setProjeto(frente.getText());
        configuration.setUsuario(usuario.getText());
        
        Gson gson = new Gson();
        String json = gson.toJson(configuration);
        util.Util.writeFile(fileConfig, json);
    }
    private void getAllReports(){
        SeleniumHelper selenium = new SeleniumHelper(SeleniumHelper.Browser.Firefox,configuration.getPastaFirefoxInstalado());
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
        getConfiguration();
    }    
    private void getConfiguration(){
        String config = Util.readFile(fileConfig);
        Gson gson = new Gson();
        configuration = gson.fromJson(config, Config.class);
        
        credencial_json.setText(configuration.getCredencialJson());
        id_situation_wall.setText(configuration.getIdSituationWall());
        time.setText(configuration.getNomeTime());
        frente.setText(configuration.getProjeto());
        usuario.setText(configuration.getUsuario());
    }
    @FXML
    private void escolherCredencial(ActionEvent event) throws InterruptedException {
        List<File> files = util.Util.openFileChooser(GoalReports.currentStage, "Selecione o Credencial.json", false);
        files.forEach((file_import) -> {
            credencial_json.setText(file_import.getAbsolutePath());
        });
    }
    
    
}

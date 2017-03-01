package goalreports.business;

import goalreports.helper.GoogleApiHelper;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author felipekn
 */
public class SituationWall {
    GoogleApiHelper googleapi=null;
    public SituationWall(GoogleApiHelper googleapi){
        this.googleapi = googleapi;
    }
    public List<com.google.api.services.drive.model.File> uploadReports(List<String> reports){
        List<com.google.api.services.drive.model.File> googleFiles = new ArrayList<>();
        for(String report : reports){
            File file = new File(report);
            googleFiles.add(googleapi.uploadDriveFile(file));
        }
        return googleFiles;
    }
    public boolean setDataInSituationWall(String idSpreadSheet){
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        
        try {
            googleapi.setSpreadSheetCellValue(idSpreadSheet, "M1", "Last Update:\n"+dateFormat.format(date));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SituationWall.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

package timotei;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

//Controls start window.
public class FXMLStartController implements Initializable {

    @FXML
    private Button newButton;
    @FXML
    private Button existingButton;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void onCreateNewDatabaseClicked(ActionEvent event) {
        openMainWindow( true );
    }

    @FXML
    private void onUseExistingDatabaseClicked(ActionEvent event) {
        openMainWindow( false );
    }
    
    private void openMainWindow( boolean newDB ){
        try {
            existingButton.disarm();
            newButton.disarm();
            Stage start = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDoc.fxml"));
            
            //Sends info to new window.
            fxmlLoader.getNamespace().put( "input", newDB);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(TIMOTEI.class.getResource( "MainGUIStyle.css" ).toExternalForm());
            start.setTitle( "TIMOTEI Smart Post" );
            start.setScene(scene);
            start.setResizable( false );
            start.show();
            
            //Defines which function to run on close event.
            FXMLDocumentController ref = (FXMLDocumentController) fxmlLoader.getController();
            start.setOnCloseRequest(event ->{
                ref.onClose();
            });
            
            Stage self = ( Stage ) newButton.getScene().getWindow();
            self.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}

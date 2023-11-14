
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainSceneController {

    @FXML
    private TextField txTitle;

    @FXML
    void btnOKClicked(ActionEvent event) {
         Stage mainWindow = (Stage) txTitle.getScene().getWindow();
         String title = txTitle.getText();
         mainWindow.setTitle(title);
    }

}

    


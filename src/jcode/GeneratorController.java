package jcode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class GeneratorController {

    MainController mc;

    @FXML
    Spinner<Integer> nField;

    @FXML
    Spinner<Integer> mField;

    Stage getCurrentStage(){
        return  (Stage) mField.getScene().getWindow();
    }

    @FXML
    void onGenerateButton() throws Exception{
        int n = nField.getValue();
        int m = mField.getValue();

        Matrix x = AlgoFunc.generateStencil(2*n,2*m);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save stencil as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Kardano stencil", "*.kstn"));
        File selectedFile = fileChooser.showSaveDialog(getCurrentStage());
        if(selectedFile==null){
            return;
        }
        if (IOOperations.writeToFile(AlgoFunc.generateStencil(2*n,2*m),selectedFile.getAbsolutePath())==0) {
            String s = selectedFile.getAbsolutePath();
            if(IOOperations.writeToFile(x,s)!=0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Operation error");
                alert.setHeaderText("Stensil generation operation");
                alert.setContentText("Stansil generation error.");
                alert.showAndWait();
                return;
            };
        }

        mc.setDialogRes(true);
        mc.stencilPath.setText(selectedFile.getAbsolutePath());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Stensil generation operation");
        alert.setContentText("Stansil generated.");
        alert.showAndWait();
        getCurrentStage().close();
    }

    void init(MainController mc){
        this.mc = mc;
    }


}

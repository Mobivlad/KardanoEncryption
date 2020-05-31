package jcode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class DatabaseViewController {
    MainController mc;
    @FXML
    ListView<String> stencilsList;

    Stage getCurrentStage(){
        return  (Stage) stencilsList.getScene().getWindow();
    }

    @FXML
    void onSelectButton(){
        if(stencilsList.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Stencil selection error.");
            alert.setHeaderText("Select stencil from list.");
            alert.setContentText("Stencil is not selected. Please select stencil from list.");
            alert.showAndWait();
            return;
        }
        String name = stencilsList.getSelectionModel().getSelectedItems().get(0);
        mc.stencilPath.setText("#database:"+name);
        mc.setDialogRes2(true);
        getCurrentStage().close();
    }

    @FXML
    void onCancelButton(){
        getCurrentStage().close();
    }

    @FXML
    void onAddButton(){
        String stencilPath = selectStencil();
        if(stencilPath==null)return;
        Matrix stencil=null;
        try{
            stencil = IOOperations.readFromFile(stencilPath);
        } catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't find a file.");
            alert.setContentText("Stencil file is open or don't exist.");
            alert.showAndWait();
            return;
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't read stencil file.");
            alert.setContentText("Check stencil file correction or change stencil file.");
            alert.showAndWait();
            return;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Error in stencil reading process.");
            alert.setContentText("Check stencil file correction or change stencil file.");
            alert.showAndWait();
            return;
        }
        if(!AlgoFunc.checkStencil(stencil)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Error in stencil file.");
            alert.setContentText("Stencil file is incorrect. Check stencil validation or choose other.");
            alert.showAndWait();
            return;
        }
        String message = "Enter stencil name:";
        while (true){
            TextInputDialog dialog = new TextInputDialog("StencilName");

            dialog.setTitle("Adding stencil file to database.");
            dialog.setHeaderText(message);
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                Matrix finalStencil = stencil;
                int n=stencilsList.getItems().size();
                boolean found = false;
                String name = result.get();
                for(int i=0;i<n;i++){
                    if(stencilsList.getItems().get(i).equals(name)){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    try {
                        SQLAccessor sql = new SQLAccessor();
                        sql.writeMatrix(finalStencil,name);
                        sql.finish();
                        refresh();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                message = "Entered name exists. Enter stencil name:";
            } else {
                break;
            }
        }
    }

    @FXML
    void onRemoveButton(){
        if(stencilsList.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Stencil selection error.");
            alert.setHeaderText("Select stencil from list.");
            alert.setContentText("Stencil is not selected. Please select stencil from list.");
            alert.showAndWait();
            return;
        }
        String name = stencilsList.getSelectionModel().getSelectedItems().get(0);
        ButtonType foo = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType bar = new ButtonType("No", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Do you want to remove selected key?", foo, bar);
        alert.setTitle("Remove confirmation.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(bar) == foo) {
            try {
                SQLAccessor sql = new SQLAccessor();
                sql.removeMatrix(name);
                sql.finish();
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void init(MainController mc){
        this.mc = mc;
        refresh();
    }

    void refresh(){
        try{
            SQLAccessor sql = new SQLAccessor();
            ObservableList<String> list = FXCollections.observableList(sql.readNames());
            stencilsList.setItems(list);
            stencilsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            sql.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String selectStencil(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open stencil file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Kardano stencil", "*.kstn"));
        File selectedFile = fileChooser.showOpenDialog(getCurrentStage());
        if(selectedFile==null)return null;
        return selectedFile.getAbsolutePath();
    }
}

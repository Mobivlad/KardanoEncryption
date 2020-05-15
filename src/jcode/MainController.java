package jcode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainController {
    private static final String pathStartIcon = "/ui/img/icons/icon_";
    private boolean dialogRes = false;

    @FXML
    RadioMenuItem hMenuButton;

    @FXML
    ToggleGroup dir2;

    @FXML
    RadioMenuItem vMenuButton;

    @FXML
    RadioButton hRadioButton;

    @FXML
    RadioButton vRadioButton;

    @FXML
    TextField stencilPath;

    @FXML
    TextField filePath;

    @FXML
    ChoiceBox<String> operationChoose;

    @FXML
    void onStencilChooseButton(){
        stencilPath.setText(selectStencil());
    }

    @FXML
    void onNewStencilButton() throws IOException {
        openGenerator();
    }

    @FXML
    void onFileChooseButton(){
        filePath.setText(selectFile());
    }

    @FXML
    void onGoButton() {
        if(stencilPath.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Choose a stencil file.");
            alert.setContentText("Stencil file path is empty. Choose stencil file.");
            alert.showAndWait();
            return;
        }
        if(filePath.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Choose a "+operationChoose.getValue().toLowerCase()+ " file.");
            alert.setContentText(operationChoose.getValue()+ " file path is empty. Choose file.");
            alert.showAndWait();
            return;
        }
        AlgoModel am;
        if(operationChoose.getValue().equals("Encryption")){
            System.out.println("e");
            am = new EncryptModel();
        } else {
            System.out.println("d");
            am = new DecryptModel();
        }
        goFunction(am,stencilPath.getText(),filePath.getText());
    }

    @FXML
    void onAboutMenuButton() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/about.fxml"));
        Parent root = fxmlLoader.load();
        for (String x: new String[]{"16","32","64","128","256","512"}) {
            stage.getIcons().add(new Image(pathStartIcon+x+".png"));
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setResizable(false);
        stage.setTitle("About");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    @FXML
    void onQuitMenuButton(){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onHorizontalMenuButton(){
        hRadioButton.setSelected(true);
    }

    @FXML
    void onVerticalMenuButton(){
        vRadioButton.setSelected(true);
    }

    @FXML
    void onEncryptMenuButton(){
        String filePath = selectFile();
        if(filePath==null)return;
        String stencilPath = selectStencil();
        if(stencilPath==null)return;
        goFunction(new EncryptModel(),stencilPath,filePath);
    }

    @FXML
    void onDecryptMenuButton(){
        String filePath = selectFile();
        if(filePath==null)return;
        String stencilPath = selectFile();
        if(stencilPath==null)return;
        goFunction(new DecryptModel(),stencilPath,filePath);
    }

    @FXML
    void setHDirection(){
        hMenuButton.setSelected(true);
    }

    @FXML
    void setVDirection(){
        vMenuButton.setSelected(true);
    }

    Stage getCurrentStage(){
        return  (Stage) filePath.getScene().getWindow();
    }

    void openGenerator() throws IOException {
        dialogRes = false;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/generate.fxml"));
        Parent root = fxmlLoader.load();
        GeneratorController gc = fxmlLoader.getController();
        gc.init(this);
        for (String x: new String[]{"16","32","64","128","256","512"}) {
            stage.getIcons().add(new Image(pathStartIcon+x+".png"));
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setResizable(false);
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        System.out.println(dialogRes);
    }

    String selectFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(getCurrentStage());
        if(selectedFile==null)return null;
        return selectedFile.getAbsolutePath();
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

    void goFunction(AlgoModel am,String stencilPathText,String filePathText){
        boolean direction = ((RadioButton)(dir2.getSelectedToggle())).getText().equals("Vertical");
        Matrix stencil=null;
        try{
            stencil = IOOperations.readFromFile(stencilPathText);
        } catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't find an file.");
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
        String text="";
        try {
            text = new String ( Files.readAllBytes( Paths.get(filePathText) ) );
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't find an file.");
            alert.setContentText("File is open or don't exist.");
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
        String s = am.getAlgoRes(text,stencil,direction);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save result file as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(getCurrentStage());
        try {
            FileWriter fw = new FileWriter(selectedFile);
            fw.write(s);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Operation done.");
        alert.setContentText("Text was "+ ((RadioButton)(dir2.getSelectedToggle())).getText().toLowerCase() +"ed!");
        alert.showAndWait();
    }

    public void setDialogRes(boolean res){
        dialogRes = res;
    }
}

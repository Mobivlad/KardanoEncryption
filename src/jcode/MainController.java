package jcode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class MainController {
    private static final String pathStartIcon = "/ui/img/icons/icon_";
    private boolean dialogRes = false;
    private boolean dialogRes2 = false;

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
    TitledPane tpane;

    @FXML
    TextArea plainTextField;

    @FXML
    TextArea chiperTextField;

    @FXML
    ChoiceBox<String> operationChoose;

    @FXML
    void onStencilChooseButton() {
        stencilPath.setText(selectStencil());
    }

    @FXML
    void onNewStencilButton() throws IOException {
        openGenerator();
    }

    @FXML
    void onFileChooseButton() {
        filePath.setText(selectFile());
    }

    @FXML
    void onGoButton() {
        if (stencilPath.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Choose a stencil file.");
            alert.setContentText("Stencil file path is empty. Choose stencil file.");
            alert.showAndWait();
            return;
        }
        if (filePath.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Choose a " + operationChoose.getValue().toLowerCase() + " file.");
            alert.setContentText(operationChoose.getValue() + " file path is empty. Choose file.");
            alert.showAndWait();
            return;
        }
        AlgoModel am;
        if (operationChoose.getValue().equals("Encryption")) {
            System.out.println("e");
            am = new EncryptModel();
        } else {
            System.out.println("d");
            am = new DecryptModel();
        }
        goFunction(am, stencilPath.getText(), filePath.getText());
    }

    @FXML
    void onAboutMenuButton() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/about.fxml"));
        Parent root = fxmlLoader.load();
        for (String x : new String[]{"16", "32", "64", "128", "256", "512"}) {
            stage.getIcons().add(new Image(pathStartIcon + x + ".png"));
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setResizable(false);
        stage.setTitle("About");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    @FXML
    void onQuitMenuButton() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onHorizontalMenuButton() {
        hRadioButton.setSelected(true);
    }

    @FXML
    void onVerticalMenuButton() {
        vRadioButton.setSelected(true);
    }

    @FXML
    void onEncryptMenuButton() {
        String filePath = selectFile();
        if (filePath == null) return;
        String stencilPath = selectStencil();
        if (stencilPath == null) return;
        goFunctionForMenu(new EncryptModel(), stencilPath, filePath);
    }

    @FXML
    void onDecryptMenuButton() {
        String filePath = selectFile();
        if (filePath == null) return;
        String stencilPath = selectFile();
        if (stencilPath == null) return;
        goFunctionForMenu(new DecryptModel(), stencilPath, filePath);
    }

    @FXML
    void setHDirection() {
        hMenuButton.setSelected(true);
    }

    @FXML
    void setVDirection() {
        vMenuButton.setSelected(true);
    }

    Stage getCurrentStage() {
        return (Stage) filePath.getScene().getWindow();
    }

    void openGenerator() throws IOException {
        dialogRes = false;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/generate.fxml"));
        Parent root = fxmlLoader.load();
        GeneratorController gc = fxmlLoader.getController();
        gc.init(this);
        for (String x : new String[]{"16", "32", "64", "128", "256", "512"}) {
            stage.getIcons().add(new Image(pathStartIcon + x + ".png"));
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setResizable(false);
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        System.out.println(dialogRes);
    }

    String selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(getCurrentStage());
        if (selectedFile == null) return null;
        return selectedFile.getAbsolutePath();
    }

    String selectStencil() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open stencil file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Kardano stencil", "*.kstn"));
        File selectedFile = fileChooser.showOpenDialog(getCurrentStage());
        if (selectedFile == null) return null;
        return selectedFile.getAbsolutePath();
    }

    @FXML
    void onShowButtonClick() throws IOException {
        //
        String stencilPathText = stencilPath.getText();
        Matrix stencil = getStencil(stencilPathText);
        if(stencil==null)return;
        boolean en = operationChoose.getValue().equals("Encryption");
        boolean direction = ((RadioButton) (dir2.getSelectedToggle())).getText().equals("Vertical");
        //
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/stencil.fxml"));
        Parent root = fxmlLoader.load();
        StencilViewController gc = fxmlLoader.getController();
        gc.setData(stencil, direction, en);
        for (String x : new String[]{"16", "32", "64", "128", "256", "512"}) {
            stage.getIcons().add(new Image(pathStartIcon + x + ".png"));
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setResizable(false);
        stage.setTitle("Algorithm's matrix view");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    void goFunction(AlgoModel am, String stencilPathText, String filePathText) {
        boolean direction = ((RadioButton) (dir2.getSelectedToggle())).getText().equals("Vertical");
        Matrix stencil = getStencil(stencilPathText);
        if (stencil == null) return;
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(filePathText)));
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't find an file.");
            alert.setContentText("File is open or don't exist.");
            alert.showAndWait();
            return;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Error in stencil reading process.");
            alert.setContentText("Check stencil file correction or change stencil file.");
            alert.showAndWait();
            return;
        }
        String s = "";
        try {
            s = am.getAlgoRes(text, stencil, direction, true);
        } catch (AlgorithmException e) {
            ButtonType foo = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType bar = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Chipertext can be incorrect. Do you want to continue a decryption?", foo, bar);
            alert.setTitle("Decryption warning");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(bar) == foo) {
                try {
                    s = am.getAlgoRes(text, stencil, direction, false);
                } catch (AlgorithmException algorithmException) {
                    algorithmException.printStackTrace();
                }
            } else {
                return;
            }
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save result file as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(getCurrentStage());
        if (selectedFile == null) {
            return;
        }
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
        alert.setContentText("Text was " + ((RadioButton) (dir2.getSelectedToggle())).getText().toLowerCase() + "ed!");
        alert.showAndWait();
    }

    void goFunctionForMenu(AlgoModel am, String stencilPathText, String filePathText) {
        boolean direction = ((RadioButton) (dir2.getSelectedToggle())).getText().equals("Vertical");
        Matrix stencil = null;
        try {
            stencil = IOOperations.readFromFile(stencilPathText);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't find a file.");
            alert.setContentText("Stencil file is open or don't exist.");
            alert.showAndWait();
            return;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't read stencil file.");
            alert.setContentText("Check stencil file correction or change stencil file.");
            alert.showAndWait();
            return;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Error in stencil reading process.");
            alert.setContentText("Check stencil file correction or change stencil file.");
            alert.showAndWait();
            return;
        }
        if (!AlgoFunc.checkStencil(stencil)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Error in stencil file.");
            alert.setContentText("Stencil file is incorrect. Check stencil validation or choose other.");
            alert.showAndWait();
            return;
        }
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(filePathText)));
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Can't find an file.");
            alert.setContentText("File is open or don't exist.");
            alert.showAndWait();
            return;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Encryption operation");
            alert.setHeaderText("Error in stencil reading process.");
            alert.setContentText("Check stencil file correction or change stencil file.");
            alert.showAndWait();
            return;
        }
        String s = "";
        try {
            s = am.getAlgoRes(text, stencil, direction, true);
        } catch (AlgorithmException e) {
            ButtonType foo = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType bar = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Chipertext can be incorrect. Do you want to continue a decryption?", foo, bar);
            alert.setTitle("Decryption warning");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(bar) == foo) {
                try {
                    s = am.getAlgoRes(text, stencil, direction, false);
                } catch (AlgorithmException algorithmException) {
                    algorithmException.printStackTrace();
                }
            } else {
                return;
            }
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save result file as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(getCurrentStage());
        if (selectedFile == null) {
            return;
        }
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
        alert.setContentText("Text was " + ((RadioButton) (dir2.getSelectedToggle())).getText().toLowerCase() + "ed!");
        alert.showAndWait();
    }

    @FXML
    void onCopyButton() {
        String myString = chiperTextField.getText();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @FXML
    void onSaveAsButton() {
        if (chiperTextField.getText().isEmpty()) {
            ButtonType foo = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType bar = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Chipertext field is empty, so saved file will be empty.\n Do you want to continue?", foo, bar);
            alert.setTitle("Save file operation");
            alert.setHeaderText("Empty text field.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(bar) != foo) {
                return;
            }
        }
        String s = chiperTextField.getText();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save result file as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(getCurrentStage());
        if (selectedFile == null) {
            return;
        }
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
        alert.setContentText("Text was " + ((RadioButton) (dir2.getSelectedToggle())).getText().toLowerCase() + "ed!");
        alert.showAndWait();
    }

    @FXML
    void onTextGoButton() {
        if (stencilPath.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Choose a stencil file.");
            alert.setContentText("Stencil file path is empty. Choose stencil file.");
            alert.showAndWait();
            return;
        }
        AlgoModel am;
        if (operationChoose.getValue().equals("Encryption")) {
            System.out.println("e");
            am = new EncryptModel();
        } else {
            System.out.println("d");
            am = new DecryptModel();
        }
        String stencilPathText = stencilPath.getText();
        boolean direction = ((RadioButton) (dir2.getSelectedToggle())).getText().equals("Vertical");
        Matrix stencil = getStencil(stencilPathText);
        if(stencil==null)return;
        String text = plainTextField.getText();
        String s = "";
        try {
            s = am.getAlgoRes(text, stencil, direction, true);
        } catch (AlgorithmException e) {
            ButtonType foo = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType bar = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Chipertext can be incorrect. Do you want to continue a decryption?", foo, bar);
            alert.setTitle("Decryption warning");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(bar) == foo) {
                try {
                    s = am.getAlgoRes(text, stencil, direction, false);
                } catch (AlgorithmException algorithmException) {
                    algorithmException.printStackTrace();
                }
            } else {
                return;
            }
        }
        chiperTextField.setText(s);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Operation done.");
        alert.setContentText("Text was " + ((RadioButton) (dir2.getSelectedToggle())).getText().toLowerCase() + "ed!");
        alert.showAndWait();
    }

    public void setDialogRes(boolean res) {
        dialogRes = res;
    }

    public void setDialogRes2(boolean res) {
        dialogRes2 = res;
    }

    @FXML
    void onDBclick() throws IOException {
        dialogRes2 = false;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/DatabaseView.fxml"));
        Parent root = fxmlLoader.load();
        DatabaseViewController gc = fxmlLoader.getController();
        gc.init(this);
        for (String x : new String[]{"16", "32", "64", "128", "256", "512"}) {
            stage.getIcons().add(new Image(pathStartIcon + x + ".png"));
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setResizable(false);
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        System.out.println(dialogRes);
    }

    Matrix getStencil(String stencilPathText) {
        Matrix stencil = null;
        if (stencilPathText.startsWith("#database:")) {
            stencilPathText = stencilPathText.replaceFirst("#database:","");
            try {
                SQLAccessor sqlAccessor = new SQLAccessor();
                if(!sqlAccessor.exist(stencilPathText)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Stencil loading error");
                    alert.setHeaderText("Can't find a stencil.");
                    alert.setContentText("Stencil with name "+stencilPathText+" is not exist.");
                    alert.showAndWait();
                    return null;
                } else {
                    stencil = sqlAccessor.readMatrix(stencilPathText);
                }
                sqlAccessor.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                stencil = IOOperations.readFromFile(stencilPathText);
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Encryption operation");
                alert.setHeaderText("Can't find a file.");
                alert.setContentText("Stencil file is open or don't exist.");
                alert.showAndWait();
                return null;
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Encryption operation");
                alert.setHeaderText("Can't read stencil file.");
                alert.setContentText("Check stencil file correction or change stencil file.");
                alert.showAndWait();
                return null;
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Encryption operation");
                alert.setHeaderText("Error in stencil reading process.");
                alert.setContentText("Check stencil file correction or change stencil file.");
                alert.showAndWait();
                return null;
            }
            if (!AlgoFunc.checkStencil(stencil)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Encryption operation");
                alert.setHeaderText("Error in stencil file.");
                alert.setContentText("Stencil file is incorrect. Check stencil validation or choose other.");
                alert.showAndWait();
                return null;
            }
        }
        return stencil;
    }
}

package jcode;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String mainUIPath = "/ui/fxml/main.fxml";
    private static final String pathStartIcon = "/ui/img/icons/icon_";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(mainUIPath));
        for (String x: new String[]{"16","32","64","128","256","512"}) {
            primaryStage.getIcons().add(new Image(pathStartIcon+x+".png"));
        }
        primaryStage.setTitle("Encryption by method of Kardano");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            SQLAccessor sql = new SQLAccessor();
            sql.createTables();
            sql.finish();
        }catch (Exception e){
            e.printStackTrace();
        }
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
    }
}

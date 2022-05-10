import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    // Global variables available to all classes
    private Stage stage;
    private BorderPane root;
    private BorderPane top;

    @Override
    public void start(Stage stage) throws Exception {
        // Move stage to global variable
        this.stage = stage;

        // Create menu from help method createMenu()
        VBox vboxMenu = createMenu();
        HBox hboxButtons = createButtonPane();

        // Create BorderPanes for layout
        this.root = new BorderPane();
        this.top = new BorderPane();

        // Fill top BorderPane with components
        top.setTop(vboxMenu);
        top.setCenter(hboxButtons);

        // Add top BorderPane to top in root
        root.setTop(top);

        // Mandatory code for start
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    ///////////////////////////////////////
    ////////////// Handlers ///////////////
    ///////////////////////////////////////
    class NewMapHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            double rootHeight = root.getHeight();
            //double topHeight = top.getTop().maxHeight();
            Image map = new Image("file:/Users/Julius/Documents/IntelliJProjects/prog2_del2/src/europa.gif");
            ImageView imageView = new ImageView(map);
            root.setCenter(imageView);

            // Change window size so map fits
            // Detta måste vi fixa så att det inte är hårdkodat
            double mapWidth = map.getWidth();
            double mapHeight = map.getHeight() + rootHeight + 30;

            stage.setWidth(mapWidth);
            stage.setHeight(mapHeight);
        }
    }


    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "OpenHandler");
            alert.showAndWait();
        }
    }

    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "SaveHandler");
            alert.showAndWait();
        }
    }

    class SaveImageHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "SaveImageHandler");
            alert.showAndWait();
        }
    }

    class ExitHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    class FindPathHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "FindPathHandler");
            alert.showAndWait();
        }
    }

    class ShowConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ShowConnectionHandler");
            alert.showAndWait();
        }
    }

    class NewPlaceHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "NewPlaceHandler");
            alert.showAndWait();
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "NewConnectionHandler");
            alert.showAndWait();
        }
    }

    class ChangeConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ChangeConnectionHandler");
            alert.showAndWait();
        }
    }


    ///////////////////////////////////////
    //////////// Help methods /////////////
    ///////////////////////////////////////
    private VBox createMenu(){
        VBox vboxMenu = new VBox();
        MenuBar menuBar = new MenuBar();
        vboxMenu.getChildren().add(menuBar);

        Menu archiveMenu = new Menu("File");
        menuBar.getMenus().add(archiveMenu);

        MenuItem newMapItem = new MenuItem("New Map");
        archiveMenu.getItems().add(newMapItem);
        newMapItem.setOnAction(new NewMapHandler());

        MenuItem openItem = new MenuItem("Open");
        archiveMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());

        MenuItem saveItem = new MenuItem("Save");
        archiveMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());

        MenuItem saveImageItem = new MenuItem("Save Image");
        archiveMenu.getItems().add(saveImageItem);
        saveImageItem.setOnAction(new SaveImageHandler());

        MenuItem exitItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitItem);
        exitItem.setOnAction(new ExitHandler());

        return vboxMenu;
    }

    private HBox createButtonPane(){
        HBox hboxButtons = new HBox();

        Button findPathBtn = new Button("Find Path");
        Button showConnectionBtn = new Button("Show Connection");
        Button newPlaceBtn = new Button("New Place");
        Button newConnectionBtn = new Button("New Connection");
        Button changeConnectionBtn = new Button("Change Connection");

        findPathBtn.setOnAction(new FindPathHandler());
        showConnectionBtn.setOnAction(new ShowConnectionHandler());
        newPlaceBtn.setOnAction(new NewPlaceHandler());
        newConnectionBtn.setOnAction(new NewConnectionHandler());
        changeConnectionBtn.setOnAction(new ChangeConnectionHandler());

        hboxButtons.getChildren().addAll(findPathBtn, showConnectionBtn, newPlaceBtn, newConnectionBtn, changeConnectionBtn);

        hboxButtons.setAlignment(Pos.CENTER);
        hboxButtons.setPadding(new Insets(15, 15, 15, 15));

        hboxButtons.setSpacing(10);

        return hboxButtons;
    }

    ///////////////////////////////////////
    //////// Main method to start /////////
    ///////////////////////////////////////
    public static void main(String[] args) {
        launch(args);
    }
}

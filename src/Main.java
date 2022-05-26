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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.*;

public class Main extends Application {
    // Global variables available to all classes
    private Stage stage;
    private BorderPane root;
    private Pane pane;
    private BorderPane top;
    private ImageView imageView;
    private ListGraph<City> listGraph;

    @Override
    public void start(Stage stage) throws Exception {
        listGraph = new ListGraph();
        pane = new Pane();

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
            imageView = createMap();
        }
    }


    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try{
                File file = new File("/Users/Julius/Documents/IntelliJProjects/prog2_del2/src/europa.graph");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                imageView = createMap();
                pane.getChildren().add(imageView);
                root.setCenter(pane);

                String data;
                String[] nodeInfo = null;
                int lineCounter = 0;

                //Loop som kör så länge det finns data kvar att läsa från filen
                while ((data = br.readLine()) != null){
                    nodeInfo = data.split(";");

                    if(lineCounter == 0){
                        // Här säger vi vilken fil
                    }

                    if(lineCounter == 1){
                        // Här skapar vi städerna
                        for(int i = 0; i < nodeInfo.length; i = i+3){
                            String stad = nodeInfo[i];
                            double xCordinate = Double.parseDouble(nodeInfo[i+1]);
                            double yCordinate = Double.parseDouble(nodeInfo[i+2]);

                            // Skapa staden
                            City city = new City(stad, xCordinate, yCordinate);
                            listGraph.add(city);

                            // Skapa cirkel
                            Circle circleForCity = new Circle();
                            circleForCity.setCenterX(xCordinate);
                            circleForCity.setCenterY(yCordinate);
                            circleForCity.setRadius(10.0f);
                            circleForCity.setFill(Color.BLUE);

                            // Skapa label
                            Label label = new Label(stad);
                            label.setLayoutX(xCordinate - 10);
                            label.setLayoutY(yCordinate + 10);

                            // Lägg till label och cirkel i pane
                            pane.getChildren().add(label);
                            pane.getChildren().add(circleForCity);
                        }
                    }

                    if(lineCounter > 1){
                        // Här skapar vi edges
                        for(int i = 0; i < nodeInfo.length; i=i+4){
                            String city1Name = nodeInfo[i];
                            String city2Name = nodeInfo[i+1];
                            String edgeName = nodeInfo[i+2];
                            int weight = Integer.parseInt(nodeInfo[i+3]);

                            City city1 = null;
                            City city2 = null;

                            Set set = listGraph.getNodes();

                            Iterator<City> itr = set.iterator();

                            while (itr.hasNext()) {

                                //itr.next() får bara förekomma en gång per iteration.
                                City temp = itr.next();

                                if(temp.getName().equals(city1Name)) {
                                    city1 = temp;
                                    System.out.println("1 " + city1.getName());
                                }
                                if(temp.getName().equals(city2Name)){
                                    city2 = temp;
                                    System.out.println("2 " + city2.getName());
                                }
                                if(city1 != null && city2 != null) {
                                    System.out.println("city1 != null && city2 != null");
                                    listGraph.connect(city1, city2, edgeName, weight);
                                }
                            }

                            /*
                            Set<City> set = listGraph.getNodes();
                            for(City temp : set){
                                if(temp.getName().equals(city1Name)){
                                    city1 = temp;
                                }
                                if(temp.getName().equals(city2Name)){
                                    city2 = temp;
                                }
                            }
                             */
                        }
                    }
                    lineCounter++;
                }



            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "file not found, " + e);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    private ImageView createMap(){
        double rootHeight = root.getHeight();
        Image map = new Image("file:/Users/Julius/Documents/IntelliJProjects/prog2_del2/src/europa.gif");
        imageView = new ImageView(map);
        root.setCenter(imageView);

        // Change window size so map fits
        // Detta måste vi fixa så att det inte är hårdkodat
        double mapWidth = map.getWidth();
        double mapHeight = map.getHeight() + rootHeight + 30;

        stage.setWidth(mapWidth);
        stage.setHeight(mapHeight);

        return imageView;
    }

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

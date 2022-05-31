import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class PathFinder extends Application {
    // Global variables available to all classes
    private Stage stage;
    private BorderPane root;
    private Pane pane;
    private BorderPane top;
    private ImageView imageView;
    private ListGraph<City> listGraph;
    private Scene scene;
    private Button newPlaceBtn;
    private Button findPathBtn;
    private Button showConnectionBtn;
    private Button newConnectionBtn;
    private Button changeConnectionBtn;
    private CreateCityHandler createCityHandler;
    private City cityFrom;
    private City cityTo;
    private TextField nameField;
    private TextField timeField;
    private boolean hasUnsavedChanges;

    @Override
    public void start(Stage stage) throws Exception {
        createCityHandler = new CreateCityHandler();
        listGraph = new ListGraph();

        // Move stage to global variable
        this.stage = stage;

        // Create menu from help method createMenu()
        VBox vboxMenu = createMenu();
        vboxMenu.setId("menuFile");
        HBox hboxButtons = createButtonPane();

        // Create BorderPanes for layout
        this.root = new BorderPane();
        this.top = new BorderPane();

        pane = new Pane();
        root.setCenter(pane);
        pane.setId("outputArea");

        // Fill top BorderPane with components
        top.setTop(vboxMenu);
        top.setCenter(hboxButtons);

        // Add top BorderPane to top in root
        root.setTop(top);

        // Mandatory code for start
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("PathFinder");
        stage.show();
    }



    ///////////////////////////////////////
    ////////////// Handlers ///////////////
    ///////////////////////////////////////
    class NewMapHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            imageView = createMap();
            pane.getChildren().add(imageView);
            root.setCenter(pane);
            changeButtonCondition(false);
        }
    }


    class OpenHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            if (hasUnsavedChanges) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Osparade ändringar. Avsluta ändå?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get().equals(ButtonType.CANCEL)) {
                    actionEvent.consume();
                    return;
                }
            }
            try{
                File file = new File("/home/gustavwalter/Documents/prog2_del2/src/europa.graph");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                imageView = createMap();
                pane.getChildren().add(imageView);
                root.setCenter(pane);
                changeButtonCondition(false);

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
                            double xcordinate = Double.parseDouble(nodeInfo[i+1]);
                            double ycordinate = Double.parseDouble(nodeInfo[i+2]);

                            // Skapa staden
                            City city = new City(stad, xcordinate, ycordinate);
                            city.setId(city.getName());
                            city.setOnMouseClicked(new MakeCityClickable());
                            listGraph.add(city);

                            // Skapa label
                            Label label = new Label(stad);
                            label.setLayoutX(xcordinate - 10);
                            label.setLayoutY(ycordinate + 10);

                            // Lägg till label och cirkel i pane
                            pane.getChildren().add(label);
                            pane.getChildren().add(city);
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
                                }
                                if(temp.getName().equals(city2Name)){
                                    city2 = temp;
                                }
                                if(city1 != null && city2 != null) {
                                    if(listGraph.getEdgeBetween(city1, city2) == null){
                                        listGraph.connect(city1, city2, edgeName, weight);

                                        // Skapa linje
                                        Line line = new Line(city1.getxCordinate(), city1.getyCordinate(), city2.getxCordinate(), city2.getyCordinate());
                                        line.setFill(Color.BLACK);
                                        line.setStrokeWidth(3);
                                        line.setDisable(true);

                                        pane.getChildren().add(line);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    lineCounter++;
                    hasUnsavedChanges = false;
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
            Set nodes = listGraph.getNodes();
            Iterator<City> nodesIterator = nodes.iterator();
                try {
                    File file = new File("/home/gustavwalter/Documents/prog2_del2/src/europa.graph");
                    FileWriter myWriter = new FileWriter(file);
                    myWriter.write("file:" + file.getName() + "\n");

                    // Create nodes on line 2
                    while(nodesIterator.hasNext()) {
                        City city = nodesIterator.next();
                        myWriter.write(city.getName() + ";" + city.getxCordinate() + ";" + city.getyCordinate() + ";");
                    }

                    // Get edges and create them
                    myWriter.write("\n");

                    nodesIterator = nodes.iterator();
                    while(nodesIterator.hasNext()) {
                        City city = nodesIterator.next();
                        Collection<Edge<City>> edges = listGraph.getEdgesFrom(city);
                        for(Edge edge : edges){
                            myWriter.write(city.getName() + ";" + edge.getDestination() + ";" + edge.getName() + ";" + edge.getWeight() + "\n");
                        }
                    }

                    myWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    class SaveImageHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                WritableImage screenShot = root.snapshot(null, null);
                BufferedImage bufferedScreenShot = SwingFXUtils.fromFXImage(screenShot, null);
                ImageIO.write(bufferedScreenShot, "png", new File("src/capture.png"));
            } catch(IOException ioe) {
                Alert ioImageAlert = new Alert(Alert.AlertType.ERROR, "IO-error!" + ioe.getMessage());
                ioImageAlert.showAndWait();
            }
        }
    }

    class ExitHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent actionEvent){
            if (hasUnsavedChanges) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Osparade ändringar. Avsluta ändå?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get().equals(ButtonType.CANCEL)) {
                    actionEvent.consume();
                }  else {
                    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                }
            }  else {
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        }
    }

    class FindPathHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("Two places must be selected!");
                alert.showAndWait();
                return;
            }

            List<Edge<City>> path = listGraph.getPath(cityFrom, cityTo);
            if(path == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("There is no path between these two nodes!");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Message");
            alert.setHeaderText("The path from " + cityFrom.getName() + " to " + cityTo.getName());
            StringBuilder sb = new StringBuilder();
            int total = 0;
            for(Edge t : path){
                sb.append("to " + t.getDestination() + " by " + t.getName() + " takes " + t.getWeight() + "\n");
                total += t.getWeight();
            }
            sb.append("Total " + total);
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }
    }

    class ShowConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("Two places must be selected!");
                alert.showAndWait();
                return;
            }

            if(listGraph.getEdgeBetween(cityFrom, cityTo) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("These two nodes does not have a connection!");
                alert.showAndWait();
                return;
            }

            NewConnectionDialog dialog = new NewConnectionDialog();
            dialog.setHeaderText("Connection from " + cityFrom.getName() + " to " + cityTo.getName());

            nameField.setEditable(false);
            timeField.setEditable(false);
            Edge edge = listGraph.getEdgeBetween(cityFrom, cityTo);
            nameField.setText(edge.getName());
            timeField.setText(Integer.toString(edge.getWeight()));

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK){
                return;
            }
        }
    }

    class NewPlaceHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            scene.setCursor(Cursor.CROSSHAIR);
            newPlaceBtn.setDisable(true);
            pane.setOnMouseClicked(createCityHandler);
            hasUnsavedChanges = true;
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("Two places must be selected!");
                alert.showAndWait();
                return;
            }

            if(listGraph.getEdgeBetween(cityFrom, cityTo) == null){
                try {
                    NewConnectionDialog dialog = new NewConnectionDialog();
                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent() && result.get() != ButtonType.OK)
                        return;
                    String namn = dialog.getName();
                    int time = dialog.getTime();

                    // Kontrollera att namn fältet har text och tid fältet endast har siffror
                    if(namn.length() < 1){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                        alert.setContentText("Please enter a name!");
                        alert.showAndWait();
                        return;
                    }

                    // Skapa new connection
                    listGraph.connect(cityFrom, cityTo, namn, time);

                    // Skapa linje
                    Line line = new Line(cityFrom.getxCordinate(), cityFrom.getyCordinate(), cityTo.getxCordinate(), cityTo.getyCordinate());
                    line.setFill(Color.BLACK);
                    line.setStrokeWidth(3);
                    line.setDisable(true);
                    pane.getChildren().add(line);

                    hasUnsavedChanges = true;
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                    alert.setContentText("Please enter a time to create a connection!");
                    alert.showAndWait();
                }
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("These two nodes already have a connection!");
                alert.showAndWait();
            }
        }
    }

    class NewConnectionDialog extends Alert{
         NewConnectionDialog() {
            super(AlertType.CONFIRMATION);

            nameField = new TextField();
            timeField = new TextField();
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(10));
            grid.setHgap(5);
            grid.setVgap(10);
            grid.addRow(0, new Label("Name:"), nameField);
            grid.addRow(1, new Label("Time:"), timeField);
            setHeaderText(null);
            getDialogPane().setContent(grid);
        }
        public String getName() {
            return nameField.getText();
        }
        public int getTime() {
            return Integer.parseInt(timeField.getText());
        }
    }

    class ChangeConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityFrom == null || cityTo == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("Two places must be selected!");
                alert.showAndWait();
                return;
            }

            if(listGraph.getEdgeBetween(cityFrom, cityTo) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("These two nodes does not have a connection!");
                alert.showAndWait();
                return;
            }
            try {
                Edge edge1 = listGraph.getEdgeBetween(cityFrom, cityTo);
                Edge edge2 = listGraph.getEdgeBetween(cityTo, cityFrom);
                NewConnectionDialog dialog = new NewConnectionDialog();
                nameField.setText(edge1.getName());
                nameField.setEditable(false);
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK)
                    return;
                int time = dialog.getTime();

                edge1.setWeight(time);
                edge2.setWeight(time);

                hasUnsavedChanges = true;
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error!");
                alert.setContentText("Please enter a time to create a connection!");
                alert.showAndWait();
            }
        }
    }

    class CreateCityHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            // Skapa dialog
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Name");
            dialog.setContentText("Name of place");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                // Skapa label
                Label label = new Label(result.get());
                label.setLayoutX(event.getX() - 10);
                label.setLayoutY(event.getY() + 10);

                City city = new City(result.get(), event.getX(), event.getY());
                city.setId(city.getName());
                city.setOnMouseClicked(new MakeCityClickable());
                listGraph.add(city);

                // Lägg till label och cirkel i pane
                pane.getChildren().add(label);
                pane.getChildren().add(city);

                // Gå tbx till normal stage
                scene.setCursor(Cursor.DEFAULT);
                newPlaceBtn.setDisable(false);
                pane.setOnMouseClicked(null);

                hasUnsavedChanges = true;
            }
        }
    }


    ///////////////////////////////////////
    //////////// Help methods /////////////
    ///////////////////////////////////////
    class MakeCityClickable implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            City city = (City)mouseEvent.getSource();
            if(city.getFill() == Color.BLUE){
                // Inte markerad
                if(cityFrom == null){
                    cityFrom = city;
                    city.setFill(Color.RED);
                } else if(cityTo == null){
                    cityTo = city;
                    city.setFill(Color.RED);
                }
            } else{
                if(cityFrom == city){
                    city.setFill(Color.BLUE);
                    cityFrom = null;
                } else if(cityTo == city){
                    city.setFill(Color.BLUE);
                    cityTo = null;
                }
            }
        }
    }

    private ImageView createMap(){
        double rootHeight = root.getHeight();
        Image map = new Image("file:///home/gustavwalter/Documents/prog2_del2/src/europa.gif");
        imageView = new ImageView(map);

        // Change window size so map fits
        // Detta måste vi fixa så att det inte är hårdkodat
        double mapWidth = map.getWidth();
        double mapHeight = map.getHeight() + 120;

        stage.setWidth(mapWidth);
        stage.setHeight(mapHeight);

        // Resetta alla värden
        cityFrom = null;
        cityTo = null;
        pane.getChildren().clear();

        Set set = listGraph.getNodes();

        Iterator<City> itr = set.iterator();

        while (itr.hasNext()) {
            listGraph.remove(itr.next());
        }

        return imageView;
    }

    private VBox createMenu(){
        VBox vboxMenu = new VBox();
        MenuBar menuBar = new MenuBar();
        vboxMenu.getChildren().add(menuBar);

        Menu archiveMenu = new Menu("File");
        menuBar.getMenus().add(archiveMenu);

        MenuItem newMapItem = new MenuItem("New Map");
        newMapItem.setId("menuNewMap");
        archiveMenu.getItems().add(newMapItem);
        newMapItem.setOnAction(new NewMapHandler());

        MenuItem openItem = new MenuItem("Open");
        openItem.setId("menuOpenFile");
        archiveMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setId("menuSaveFile");
        archiveMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());

        MenuItem saveImageItem = new MenuItem("Save Image");
        saveImageItem.setId("menuSaveImage");
        archiveMenu.getItems().add(saveImageItem);
        saveImageItem.setOnAction(new SaveImageHandler());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setId("menuExit");
        archiveMenu.getItems().add(exitItem);
        exitItem.setOnAction(new ExitHandler());

        menuBar.setId("menu");

        return vboxMenu;
    }

    private void changeButtonCondition(boolean value){
        findPathBtn.setDisable(value);
        showConnectionBtn.setDisable(value);
        newPlaceBtn.setDisable(value);
        newConnectionBtn.setDisable(value);
        changeConnectionBtn.setDisable(value);
    }

    private HBox createButtonPane(){
        HBox hboxButtons = new HBox();

        findPathBtn = new Button("Find Path");
        findPathBtn.setId("btnFindPath");
        showConnectionBtn = new Button("Show Connection");
        showConnectionBtn.setId("btnShowConnection");
        newPlaceBtn = new Button("New Place");
        newPlaceBtn.setId("btnNewPlace");
        newConnectionBtn = new Button("New Connection");
        newConnectionBtn.setId("btnNewConnection");
        changeConnectionBtn = new Button("Change Connection");
        changeConnectionBtn.setId("btnChangeConnection");

        // Stänger av alla knappar
        changeButtonCondition(true);

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

package maze;

import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Maze extends Application{

    private Stage primaryStage;
    private BorderPane border;
    private VBox vBox;
    private Scene scene;
    private GridPane gridPane;
    private Hra hra;
    private MenuBar menuBar;
    
    private Menu game; 
    private MenuItem newG;
    private MenuItem saveG;
    private MenuItem openG;
    private MenuItem exitG;
    
    private Menu tools;
    private MenuItem search;
    private MenuItem addWall;
    private MenuItem addRandomWalls;
    private MenuItem removeWall;
    private MenuItem addShaft;
    private MenuItem addShaftToNextFloor;
    private MenuItem removeShaft;
    private MenuItem removeShaftFromFloor;
    private MenuItem resetVisited;
    private MenuItem resetFloor;
    
    private ContextMenu cmTools;
    private MenuItem cmiSearch;
    private MenuItem cmiAddWall;
    private MenuItem cmiAddRandomWalls;
    private MenuItem cmiRemoveWall;
    private MenuItem cmiAddShaft;
    private MenuItem cmiAddShaftToNextFloor;
    private MenuItem cmiRemoveShaft;
    private MenuItem cmiRemoveShaftFromFloor;
    private MenuItem cmiResetVisited;
    private MenuItem cmiResetFloor;
    
    public void newGame(){
        NewGame dialog = new NewGame();
        dialog.showNewGame();
        if(dialog.isCreated()){
            hra = new Hra(dialog.getRows(), dialog.getColumns(), dialog.getFloors(), null, cmTools);
            vBox.getChildren().clear();
            for(int i = 0; i < dialog.getFloors(); i++){
                Button button = new Button(String.valueOf(i+1));
                int index = i;
                button.setOnAction((ActionEvent) -> {
                    for(int j = 0; j < vBox.getChildren().size(); j++){
                        vBox.getChildren().get(j).setDisable(false);
                    }      
                    hra.getFloorToGrid(index, gridPane);
                    primaryStage.sizeToScene();
                    button.setDisable(true);
                });
                if(i == 0){
                    button.setDisable(true);
                }
                vBox.getChildren().add(button);
            }
            hra.getFloorToGrid(0, gridPane);
            tools.setDisable(false);
            saveG.setDisable(false);
        }
    }
    
    private FileChooser prepareFileChooser() {
        FileChooser fileChooser = new FileChooser();                                
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));  
        fileChooser.getExtensionFilters().addAll(                                   
                new FileChooser.ExtensionFilter("Textové súbory (*.txt)", "*.txt"), 
                new FileChooser.ExtensionFilter("Všetky súbory (*.*)", "*.*"));
        return fileChooser;                                                        
    }
    
    private File chooseFileToSave() {
        FileChooser fileChooser = prepareFileChooser();
        fileChooser.setTitle("Ulozit");
        File file = fileChooser.showSaveDialog(primaryStage);
        return file;
    }
    
    private void save() throws IOException{
        PrintStream out = new PrintStream(chooseFileToSave());
        hra.save(out);
        out.close();
    }
    
    private File chooseFileToOpen() {
        FileChooser fileChooser = prepareFileChooser();
        fileChooser.setTitle("Otvorit");
        File file = fileChooser.showOpenDialog(primaryStage);
        return file;
    }
    
    private void open() throws IOException{
        Scanner scanner = new Scanner(chooseFileToOpen());
        ArrayList<String> string = new ArrayList<>();
        int rows = scanner.nextInt();
        int columns = scanner.nextInt();
        int floor = scanner.nextInt();
        while(scanner.hasNext()){
            string.add(scanner.next());
        }
        gridPane.getChildren().clear();
        hra = new Hra(rows, columns, floor, string, cmTools);
        
        vBox.getChildren().clear();
        for(int i = 0; i < floor; i++){
            Button button = new Button(String.valueOf(i+1));
            int index = i;
            button.setOnAction((ActionEvent) -> {
                for(int j = 0; j < vBox.getChildren().size(); j++){
                    vBox.getChildren().get(j).setDisable(false);
                }      
                hra.getFloorToGrid(index, gridPane);
                primaryStage.sizeToScene();
                button.setDisable(true);
            });
            if(i == 0){
                button.setDisable(true);
            }
            vBox.getChildren().add(button);
        }
        hra.getFloorToGrid(0, gridPane);
    
        scanner.close();
    }
    
    private void createCM(){
        cmTools = new ContextMenu();
        cmiSearch = new MenuItem("Search");
        cmiSearch.setOnAction((ActionEvent) -> {
            clear();
            hra.setSearch();
            cmiSearch.setDisable(true);
            search.setDisable(true);
        });
        cmiAddWall = new MenuItem("Add Wall");
        cmiAddWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddWall();
            cmiAddWall.setDisable(true);
            addWall.setDisable(true);
        });
        cmiAddRandomWalls = new MenuItem("Add Random Wall");
        cmiAddRandomWalls.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddRandomWalls();
            cmiAddRandomWalls.setDisable(true);
            addRandomWalls.setDisable(true);
        });
        cmiRemoveWall = new MenuItem("Remove Wall");
        cmiRemoveWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveWall();
            cmiRemoveWall.setDisable(true);
            removeWall.setDisable(true);
        });
        cmiAddShaft = new MenuItem("Add Shaft");
        cmiAddShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaft();
            cmiAddShaft.setDisable(true);
            addShaft.setDisable(true);
        });
        cmiAddShaftToNextFloor = new MenuItem("Add Shaft To Next Floor");
        cmiAddShaftToNextFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaftToNextFloor();
            cmiAddShaftToNextFloor.setDisable(true);
            addShaftToNextFloor.setDisable(true);
        });
        cmiRemoveShaft = new MenuItem("Remove Shaft");
        cmiRemoveShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaft();
            cmiRemoveShaft.setDisable(true);
            removeShaft.setDisable(true);
        });
        cmiRemoveShaftFromFloor = new MenuItem("Remove Shaft From Floor");
        cmiRemoveShaftFromFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaftFromFloor();
            cmiRemoveShaftFromFloor.setDisable(true);
            removeShaftFromFloor.setDisable(true);
        });
        cmiResetVisited = new MenuItem("Reset Visited");
        cmiResetVisited.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetVisited();
            cmiResetVisited.setDisable(true);
            resetVisited.setDisable(true);
        });
        cmiResetFloor = new MenuItem("Reset Floor");
        cmiResetFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetFloor();
            cmiResetFloor.setDisable(true);
            resetFloor.setDisable(true);
        });
        cmTools.getItems().addAll(cmiSearch, cmiAddWall, cmiAddRandomWalls, 
                cmiRemoveWall, cmiAddShaft, cmiAddShaftToNextFloor, cmiRemoveShaft, 
                cmiRemoveShaftFromFloor, cmiResetVisited, cmiResetFloor);
    }
    
    private void createM(){
        menuBar = new MenuBar();
        game = new Menu("Game");
        newG = new MenuItem("New");
        newG.setOnAction((ActionEvent) -> {
            newGame();
            primaryStage.sizeToScene();
        });
        saveG = new MenuItem("Save");
        saveG.setDisable(true);
        saveG.setOnAction((ActionEvent) -> {
            try{
                save();
                saveG.setDisable(true);
            } catch(Throwable e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText(null);
                alert.setContentText("Nastala chyba pri ukladani!");
                alert.showAndWait();
            }
        });
        openG = new MenuItem("Open");
        openG.setOnAction((ActionEvent) -> {
            try{
                open();
                tools.setDisable(false);
                saveG.setDisable(false);
                primaryStage.sizeToScene();
            } catch(Throwable e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText(null);
                alert.setContentText("Nastala chyba pri otvarani!");
                alert.showAndWait();
            }
        });
        exitG = new MenuItem("Exit");
        exitG.setOnAction((ActionEvent) -> {
            primaryStage.close();
        });
        game.getItems().addAll(newG, saveG, openG, exitG);
        
        tools = new Menu("Tools");
        search = new MenuItem("Search");
        search.setOnAction((ActionEvent) -> {
            clear();
            hra.setSearch();
            cmiSearch.setDisable(true);
            search.setDisable(true);
        });
        addWall = new MenuItem("Add Wall");
        addWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddWall();
            cmiAddWall.setDisable(true);
            addWall.setDisable(true);
        });
        addRandomWalls = new MenuItem("Add Random Wall");
        addRandomWalls.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddRandomWalls();
            cmiAddRandomWalls.setDisable(true);
            addRandomWalls.setDisable(true);
        });
        removeWall = new MenuItem("Remove Wall");
        removeWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveWall();
            cmiRemoveWall.setDisable(true);
            removeWall.setDisable(true);
        });
        addShaft = new MenuItem("Add Shaft");
        addShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaft();
            cmiAddShaft.setDisable(true);
            addShaft.setDisable(true);
        });
        addShaftToNextFloor = new MenuItem("Add Shaft To Next Floor");
        addShaftToNextFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaftToNextFloor();
            cmiAddShaftToNextFloor.setDisable(true);
            addShaftToNextFloor.setDisable(true);
        });
        removeShaft = new MenuItem("Remove Shaft");
        removeShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaft();
            cmiRemoveShaft.setDisable(true);
            removeShaft.setDisable(true);
        });
        removeShaftFromFloor = new MenuItem("Remove Shaft From Floor");
        removeShaftFromFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaftFromFloor();
            cmiRemoveShaftFromFloor.setDisable(true);
            removeShaftFromFloor.setDisable(true);
        });
        resetVisited = new MenuItem("Reset Visited");
        resetVisited.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetVisited();
            cmiResetVisited.setDisable(true);
            resetVisited.setDisable(true);
        });
        resetFloor = new MenuItem("Reset Floor");
        resetFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetFloor();
            cmiResetFloor.setDisable(true);
            resetFloor.setDisable(true);
        });
        tools.getItems().addAll(search, addWall, addRandomWalls, 
                removeWall, addShaft, addShaftToNextFloor, removeShaft, 
                removeShaftFromFloor, resetVisited, resetFloor);
        tools.setDisable(true);
        menuBar.getMenus().addAll(game, tools);
    }
    
    private void clear(){
        search.setDisable(false);
        cmiSearch.setDisable(false);    
            
        addWall.setDisable(false);
        cmiAddWall.setDisable(false);
        
        addRandomWalls.setDisable(false);
        cmiAddRandomWalls.setDisable(false);
        
        removeWall.setDisable(false);
        cmiRemoveWall.setDisable(false);
        
        addShaft.setDisable(false);
        cmiAddShaft.setDisable(false);
        
        addShaftToNextFloor.setDisable(false);
        cmiAddShaftToNextFloor.setDisable(false);
        
        removeShaft.setDisable(false);
        cmiRemoveShaft.setDisable(false);
        
        removeShaftFromFloor.setDisable(false);
        cmiRemoveShaftFromFloor.setDisable(false);
        
        resetVisited.setDisable(false);
        cmiResetVisited.setDisable(false);
        
        resetFloor.setDisable(false);
        cmiResetFloor.setDisable(false);
    }
    
    @Override
    public void start(Stage stage){
        primaryStage = stage;
        border = new BorderPane();
        gridPane = new GridPane();
        vBox = new VBox();
        
        createCM();
        createM();
        
        border.setCenter(gridPane);
        border.setRight(vBox);
        border.setTop(menuBar);
        
        scene = new Scene(border);
        scene.getStylesheets().add("maze/resources/styly.css");
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Maze");
        primaryStage.sizeToScene();
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

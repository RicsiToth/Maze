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
    private MenuItem addFloor;
    private MenuItem removeFloor;
    private MenuItem addRow;
    private MenuItem removeRow;
    private MenuItem addColumn;
    private MenuItem removeColumn;
    
    private ContextMenu cmTools;
    private MenuItem cmSearch;
    private MenuItem cmAddWall;
    private MenuItem cmAddRandomWalls;
    private MenuItem cmRemoveWall;
    private MenuItem cmAddShaft;
    private MenuItem cmAddShaftToNextFloor;
    private MenuItem cmRemoveShaft;
    private MenuItem cmRemoveShaftFromFloor;
    private MenuItem cmResetVisited;
    private MenuItem cmResetFloor;
    private MenuItem cmAddFloor;
    private MenuItem cmRemoveFloor;
    private MenuItem cmAddRow;
    private MenuItem cmRemoveRow;
    private MenuItem cmAddColumn;
    private MenuItem cmRemoveColumn;
    
    public void newGame(){
        NewGame dialog = new NewGame();
        dialog.showNewGame();
        if(dialog.isCreated()){
            hra = new Hra(dialog.getRows(), dialog.getColumns(), dialog.getFloors(), null, cmTools);
            vBox.getChildren().clear();
            for(int i = 0; i < dialog.getFloors(); i++){
                addButton(i);
            }
            hra.getFloorToGrid(0, gridPane);
            tools.setDisable(false);
            saveG.setDisable(false);
        }
    }
    
    private void addButton(int floor){
        Button button = new Button(String.valueOf(floor+1));
        button.setOnAction((ActionEvent) -> {
            for(int j = 0; j < vBox.getChildren().size(); j++){
                vBox.getChildren().get(j).setDisable(false);
            }      
            hra.getFloorToGrid(floor, gridPane);
            primaryStage.sizeToScene();
            button.setDisable(true);
        });
        if(floor == 0){
            button.setDisable(true);
        }
        vBox.getChildren().add(button);
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
    
    private void createCMenu(){
        cmSearch = new MenuItem("Search");
        cmSearch.setOnAction((ActionEvent) -> {
            clear();
            hra.setSearch();
            cmSearch.setDisable(true);
            search.setDisable(true);
        });
        cmAddWall = new MenuItem("Add Wall");
        cmAddWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddWall();
            cmAddWall.setDisable(true);
            addWall.setDisable(true);
        });
        cmAddRandomWalls = new MenuItem("Add Random Wall");
        cmAddRandomWalls.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddRandomWalls();
            cmAddRandomWalls.setDisable(true);
            addRandomWalls.setDisable(true);
        });
        cmRemoveWall = new MenuItem("Remove Wall");
        cmRemoveWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveWall();
            cmRemoveWall.setDisable(true);
            removeWall.setDisable(true);
        });
        cmAddShaft = new MenuItem("Add Shaft");
        cmAddShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaft();
            cmAddShaft.setDisable(true);
            addShaft.setDisable(true);
        });
        cmAddShaftToNextFloor = new MenuItem("Add Shaft To Next Floor");
        cmAddShaftToNextFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaftToNextFloor();
            cmAddShaftToNextFloor.setDisable(true);
            addShaftToNextFloor.setDisable(true);
        });
        cmRemoveShaft = new MenuItem("Remove Shaft");
        cmRemoveShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaft();
            cmRemoveShaft.setDisable(true);
            removeShaft.setDisable(true);
        });
        cmRemoveShaftFromFloor = new MenuItem("Remove Shaft From Floor");
        cmRemoveShaftFromFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaftFromFloor();
            cmRemoveShaftFromFloor.setDisable(true);
            removeShaftFromFloor.setDisable(true);
        });
        cmResetVisited = new MenuItem("Reset Visited");
        cmResetVisited.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetVisited();
            cmResetVisited.setDisable(true);
            resetVisited.setDisable(true);
        });
        cmResetFloor = new MenuItem("Reset Floor");
        cmResetFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetFloor();
            cmResetFloor.setDisable(true);
            resetFloor.setDisable(true);
        });
        cmAddFloor = new MenuItem("Add Floor");
        cmAddFloor.setOnAction((ActionEvent) -> {
            clear();
            int floor = hra.addFloor(cmTools);
            if(floor != -1){
                addButton(floor);
                primaryStage.sizeToScene();
            } else { 
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa pridat dalsie poschodie, budova by to nezvladla");
                alert.showAndWait();
            }
        });
        cmRemoveFloor = new MenuItem("Remove Floor");
        cmRemoveFloor.setOnAction((ActionEvent) -> {
            clear();
            int floor = hra.removeFloor();
            if(floor != -1){
                if(vBox.getChildren().get(floor).isDisabled()){
                    hra.getFloorToGrid(floor-1, gridPane);
                    vBox.getChildren().get(floor-1).setDisable(true);
                }
                vBox.getChildren().remove(floor);
                primaryStage.sizeToScene();
            } else { 
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa zburat dalsie poschodie lebo dalsie uz nie je");
                alert.showAndWait();
            }
        });
        cmAddRow = new MenuItem("Add Row");
        cmAddRow.setOnAction((ActionEvent) -> {
            clear();
            if(hra.addRow(cmTools)){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zvacsit (v dlzke)");
                alert.showAndWait();
            }
        });
        cmRemoveRow = new MenuItem("Remove Row");
        cmRemoveRow.setOnAction((ActionEvent) -> {
            clear();
            if(hra.removeRow()){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zmensit (v dlzke)");
                alert.showAndWait();
            }
        });
        cmAddColumn = new MenuItem("Add Column");
        cmAddColumn.setOnAction((ActionEvent) -> {
            clear();
            if(hra.addColumn(cmTools)){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zvacsit (v sirke)");
                alert.showAndWait();
            }
        });
        cmRemoveColumn = new MenuItem("Remove Column");
        cmRemoveColumn.setOnAction((ActionEvent) -> {
            clear();
            if(hra.removeColumn()){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zmensit (v sirke)");
                alert.showAndWait();
            }
        });
        cmTools = new ContextMenu();
        cmTools.getItems().addAll(cmSearch, cmAddWall, cmAddRandomWalls, 
                cmRemoveWall, cmAddShaft, cmAddShaftToNextFloor, cmRemoveShaft, 
                cmRemoveShaftFromFloor, cmAddFloor, cmRemoveFloor, cmAddRow,
                cmRemoveRow, cmAddColumn, cmRemoveColumn, cmResetVisited, cmResetFloor);
    }
    
    private void createMenus(){
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
            search.setDisable(true);
            cmSearch.setDisable(true);
        });
        addWall = new MenuItem("Add Wall");
        addWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddWall();
            addWall.setDisable(true);
            cmAddWall.setDisable(true);
        });
        addRandomWalls = new MenuItem("Add Random Wall");
        addRandomWalls.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddRandomWalls();
            addRandomWalls.setDisable(true);
            cmAddRandomWalls.setDisable(true);
        });
        removeWall = new MenuItem("Remove Wall");
        removeWall.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveWall();
            removeWall.setDisable(true);
            cmRemoveWall.setDisable(true);
        });
        addShaft = new MenuItem("Add Shaft");
        addShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaft();
            addShaft.setDisable(true);
            cmAddShaft.setDisable(true);

        });
        addShaftToNextFloor = new MenuItem("Add Shaft To Next Floor");
        addShaftToNextFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setAddShaftToNextFloor();
            addShaftToNextFloor.setDisable(true);
            cmAddShaftToNextFloor.setDisable(true);
        });
        removeShaft = new MenuItem("Remove Shaft");
        removeShaft.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaft();
            removeShaft.setDisable(true);
            cmRemoveShaft.setDisable(true);
        });
        removeShaftFromFloor = new MenuItem("Remove Shaft From Floor");
        removeShaftFromFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setRemoveShaftFromFloor();
            removeShaftFromFloor.setDisable(true);
            cmRemoveShaftFromFloor.setDisable(true);
        });
        resetVisited = new MenuItem("Reset Visited");
        resetVisited.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetVisited();
            resetVisited.setDisable(true);
            cmResetVisited.setDisable(true);
        });
        resetFloor = new MenuItem("Reset Floor");
        resetFloor.setOnAction((ActionEvent) -> {
            clear();
            hra.setResetFloor();
            resetFloor.setDisable(true);
            cmResetFloor.setDisable(true);
        });
        addFloor = new MenuItem("Add Floor");
        addFloor.setOnAction((ActionEvent) -> {
            clear();
            int floor = hra.addFloor(cmTools);
            if(floor != -1){
                addButton(floor);
                primaryStage.sizeToScene();
            } else { 
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa pridat dalsie poschodie, budova by to nezvladla");
                alert.showAndWait();
            }
        });
        removeFloor = new MenuItem("Remove Floor");
        removeFloor.setOnAction((ActionEvent) -> {
            clear();
            int floor = hra.removeFloor();
            if(floor != -1){
                if(vBox.getChildren().get(floor).isDisabled()){
                    hra.getFloorToGrid(floor-1, gridPane);
                    vBox.getChildren().get(floor-1).setDisable(true);
                }
                vBox.getChildren().remove(floor);
                primaryStage.sizeToScene();
            } else { 
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa zburat dalsie poschodie lebo dalsie uz nie je");
                alert.showAndWait();
            }
        });
        addRow = new MenuItem("Add Row");
        addRow.setOnAction((ActionEvent) -> {
            clear();
            if(hra.addRow(cmTools)){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zvacsit (v dlzke)");
                alert.showAndWait();
            }
        });
        removeRow = new MenuItem("Remove Row");
        removeRow.setOnAction((ActionEvent) -> {
            clear();
            if(hra.removeRow()){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zmensit (v dlzke)");
                alert.showAndWait();
            }
        });
        addColumn = new MenuItem("Add Column");
        addColumn.setOnAction((ActionEvent) -> {
            clear();
            if(hra.addColumn(cmTools)){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zvacsit (v sirke)");
                alert.showAndWait();
            }
        });
        removeColumn = new MenuItem("Remove Column");
        removeColumn.setOnAction((ActionEvent) -> {
            clear();
            if(hra.removeColumn()){
                for(int i = 0; i < vBox.getChildren().size(); i++){
                    if(vBox.getChildren().get(i).isDisabled()){
                        hra.getFloorToGrid(i, gridPane);
                        primaryStage.sizeToScene();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hups");
                alert.setHeaderText(null);
                alert.setContentText("Neda sa budovu zmensit (v sirke)");
                alert.showAndWait();
            }
        });
        
        tools.getItems().addAll(search, addWall, addRandomWalls, 
                removeWall, addShaft, addShaftToNextFloor, removeShaft, 
                removeShaftFromFloor, addFloor, removeFloor, addRow,
                removeRow, addColumn, removeColumn, resetVisited, resetFloor);
        tools.setDisable(true);
        menuBar.getMenus().addAll(game, tools);
        
        cmTools = new ContextMenu();
        cmTools.getItems().addAll(search, addWall, addRandomWalls, 
                removeWall, addShaft, addShaftToNextFloor, removeShaft, 
                removeShaftFromFloor, addFloor, removeFloor, addRow,
                removeRow, addColumn, removeColumn, resetVisited, resetFloor);
    }
    
    private void clear(){
        search.setDisable(false);
        addWall.setDisable(false);
        addRandomWalls.setDisable(false);
        removeWall.setDisable(false);
        addShaft.setDisable(false);
        addShaftToNextFloor.setDisable(false);
        removeShaft.setDisable(false);
        removeShaftFromFloor.setDisable(false);
        resetVisited.setDisable(false);
        resetFloor.setDisable(false);
        
        cmSearch.setDisable(false);
        cmAddWall.setDisable(false);
        cmAddRandomWalls.setDisable(false);
        cmRemoveWall.setDisable(false);
        cmAddShaft.setDisable(false);
        cmAddShaftToNextFloor.setDisable(false);
        cmRemoveShaft.setDisable(false);
        cmRemoveShaftFromFloor.setDisable(false);
        cmResetVisited.setDisable(false);
        cmResetFloor.setDisable(false);
    }
    
    @Override
    public void start(Stage stage){
        primaryStage = stage;
        border = new BorderPane();
        gridPane = new GridPane();
        vBox = new VBox();
        
        createMenus();
        createCMenu();
        
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

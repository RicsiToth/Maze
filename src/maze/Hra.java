package maze;

import java.io.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class Hra {
    
    private int rows;
    private int columns;
    private int floors;
    private ArrayList<ArrayList<ArrayList<Label>>> labels;
    
    private boolean search;
    private boolean addWall;
    private boolean addRandomWalls;
    private boolean removeWall;
    private boolean addShaft;
    private boolean addShaftToNextFloor;
    private boolean removeShaft;
    private boolean removeShaftFromFloor;
    private boolean resetVisited;
    private boolean resetFloor;
    
    private ContextMenu tools;
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
    
    public Hra(int rows, int columns, int floors, ArrayList<String> string){
        this.rows = rows;
        this.columns = columns;
        this.floors = floors;
        labels = new ArrayList<>();
        
        tools = new ContextMenu();
        cmiSearch = new MenuItem("Search");
        cmiSearch.setOnAction((ActionEvent) -> {
            clear();
            cmiSearch.setDisable(true);
            search = true;
        });
        cmiAddWall = new MenuItem("Add Wall");
        cmiAddWall.setOnAction((ActionEvent) -> {
            clear();
            cmiAddWall.setDisable(true);
            addWall = true;
        });
        cmiAddRandomWalls = new MenuItem("Add Random Wall");
        cmiAddRandomWalls.setOnAction((ActionEvent) -> {
            clear();
            cmiAddRandomWalls.setDisable(true);
            addRandomWalls = true;
        });
        cmiRemoveWall = new MenuItem("Remove Wall");
        cmiRemoveWall.setOnAction((ActionEvent) -> {
            clear();
            cmiRemoveWall.setDisable(true);
            removeWall = true;
        });
        cmiAddShaft = new MenuItem("Add Shaft");
        cmiAddShaft.setOnAction((ActionEvent) -> {
            clear();
            cmiAddShaft.setDisable(true);
            addShaft = true;
        });
        cmiAddShaftToNextFloor = new MenuItem("Add Shaft To Next Floor");
        cmiAddShaftToNextFloor.setOnAction((ActionEvent) -> {
            clear();
            cmiAddShaftToNextFloor.setDisable(true);
            addShaftToNextFloor = true;
        });
        cmiRemoveShaft = new MenuItem("Remove Shaft");
        cmiRemoveShaft.setOnAction((ActionEvent) -> {
            clear();
            cmiRemoveShaft.setDisable(true);
            removeShaft = true;
        });
        cmiRemoveShaftFromFloor = new MenuItem("Remove Shaft From Floor");
        cmiRemoveShaftFromFloor.setOnAction((ActionEvent) -> {
            clear();
            cmiRemoveShaftFromFloor.setDisable(true);
            removeShaftFromFloor = true;
        });
        cmiResetVisited = new MenuItem("Reset Visited");
        cmiResetVisited.setOnAction((ActionEvent) -> {
            clear();
            cmiResetVisited.setDisable(true);
            resetVisited = true;
        });
        cmiResetFloor = new MenuItem("Reset Floor");
        cmiResetFloor.setOnAction((ActionEvent) -> {
            clear();
            cmiResetFloor.setDisable(true);
            resetFloor = true;
        });
        tools.getItems().addAll(cmiSearch, cmiAddWall, cmiAddRandomWalls, 
                cmiRemoveWall, cmiAddShaft, cmiAddShaftToNextFloor, cmiRemoveShaft, 
                cmiRemoveShaftFromFloor, cmiResetVisited, cmiResetFloor);
        
        int index = 0;
        for(int i = 0; i < this.floors; i++){
            labels.add(new ArrayList<>());
            for(int j = 0; j < this.rows; j++){
                labels.get(i).add(new ArrayList<Label>());
                for(int k = 0; k < this.columns; k++){
                    Label label = new Label();
                    label.setContextMenu(tools);
                    if(string != null){
                        if(string.get(index).equals(".")){
                            label.setId("Free");
                        } else if(string.get(index).equals("#")){
                            label.setId("Wall");
                        } else if(string.get(index).equals("V")){
                            label.setId("Shaft");
                        }
                        index++;
                    } else {
                        label.setId("Free");
                    }
                    int row = j; 
                    int column = k;
                    int floor = i;
                    label.setOnMouseClicked((ActionEvent) -> {
                        MouseButton btn = ActionEvent.getButton();
                        if(btn == MouseButton.PRIMARY){
                            if(search){
                                colourIt(floor, row, column);
                            }
                            if(addShaft){
                                placeShaft(row, column);
                            }
                            if(addShaftToNextFloor){
                                addShaftToNextFloor(row, column, floor);
                            }
                            if(removeShaft){
                                if(label.getId().equals("Shaft")){
                                    removeShaft(row, column);
                                }
                            }
                            if(removeShaftFromFloor){
                                if(label.getId().equals("Shaft")){
                                    label.setId("Free");
                                }
                            }
                            if(addRandomWalls){
                                generateWalls(floor);
                            }
                            if(addWall){
                                if(!label.getId().equals("Shaft")){
                                    label.setId("Wall");
                                }
                            }
                            if(removeWall){
                                if(label.getId().equals("Wall")){
                                    label.setId("Free");
                                }
                            }
                            if(resetVisited){
                                resetVisited(floor);
                            }
                            if(resetFloor){
                                resetFloor(floor);
                            }
                        }
                    });
                    labels.get(i).get(j).add(label);
                }
            }
        }
    }
    
    private void clear(){
        search = false;
        cmiSearch.setDisable(false);
        addWall = false;
        cmiAddWall.setDisable(false);
        addRandomWalls = false;
        cmiAddRandomWalls.setDisable(false);
        removeWall = false;
        cmiRemoveWall.setDisable(false);
        addShaft = false;
        cmiAddShaft.setDisable(false);
        addShaftToNextFloor = false;
        cmiAddShaftToNextFloor.setDisable(false);
        removeShaft = false;
        cmiRemoveShaft.setDisable(false);
        removeShaftFromFloor = false;
        cmiRemoveShaftFromFloor.setDisable(false);
        resetVisited = false;
        cmiResetVisited.setDisable(false);
        resetFloor = false;
        cmiResetFloor.setDisable(false);
    }
    
    private void colourIt(int floor, int row, int column){
        if(labels.get(floor).get(row).get(column).getId().equals("Visited")){
            return;
        }
        if(labels.get(floor).get(row).get(column).getId().equals("Shaft")){
            for(int i = 0; i < floors; i++){
                if(floor != i && labels.get(i).get(row).get(column).getId().equals("Shaft")){
                    if(row+1 < rows && !labels.get(i).get(row+1).get(column).getId().equals("Wall")){
                        colourIt(i, row+1, column);
                    }
                    if(row-1 >= 0 && !labels.get(i).get(row-1).get(column).getId().equals("Wall")){
                        colourIt(i, row-1, column);
                    }
                    if(column+1 < columns && !labels.get(i).get(row).get(column+1).getId().equals("Wall")){
                        colourIt(i, row, column+1);
                    }
                    if(column-1 >= 0 && !labels.get(i).get(row).get(column-1).getId().equals("Wall")){
                        colourIt(i, row, column-1);
                    }
                }
            }
        }
          
        if(row+1 < rows && !labels.get(floor).get(row+1).get(column).getId().equals("Wall")){
            if(!labels.get(floor).get(row).get(column).getId().equals("Shaft")){
                labels.get(floor).get(row).get(column).setId("Visited");
            }
            colourIt(floor, row+1, column);
        }
        if(row-1 >= 0 && !labels.get(floor).get(row-1).get(column).getId().equals("Wall")){
            if(!labels.get(floor).get(row).get(column).getId().equals("Shaft")){
                labels.get(floor).get(row).get(column).setId("Visited");
            }
            colourIt(floor, row-1, column);
        }
        if(column+1 < columns && !labels.get(floor).get(row).get(column+1).getId().equals("Wall")){
            if(!labels.get(floor).get(row).get(column).getId().equals("Shaft")){
                labels.get(floor).get(row).get(column).setId("Visited");
            }
            colourIt(floor, row, column+1);
        }
        if(column-1 >= 0 && !labels.get(floor).get(row).get(column-1).getId().equals("Wall")){
            if(!labels.get(floor).get(row).get(column).getId().equals("Shaft")){
                labels.get(floor).get(row).get(column).setId("Visited");
            }
            colourIt(floor, row, column-1);
        }
    }
    
    private void placeShaft(int row, int column){
        for(int i = 0; i < floors; i++){
            labels.get(i).get(row).get(column).setId("Shaft");
        }
    }
    private void removeShaft(int row, int column){
         for(int i = 0; i < floors; i++){
            labels.get(i).get(row).get(column).setId("Free");
        }
    }
    
    private void addShaftToNextFloor(int row, int column, int floor){
        if(floor+1 < floors){
            labels.get(floor).get(row).get(column).setId("Shaft");
            labels.get(floor+1).get(row).get(column).setId("Shaft");
        }
    }
    
    private void generateWalls(int floor){
        NumberOfRandomWalls dialog = new NumberOfRandomWalls(rows, columns);
        dialog.showRandom();
        if(dialog.isCreated()){
            Random random = new Random();
            int row;
            int column;
            int numOfWalls = dialog.getNumber();
            while(numOfWalls != 0){
                row = random.nextInt(rows);
                column = random.nextInt(columns);
                if(labels.get(floor).get(row).get(column).getId().equals("Free") || labels.get(floor).get(row).get(column).getId().equals("Wall")){
                    labels.get(floor).get(row).get(column).setId("Wall");
                    numOfWalls--;
                }
            }
        }
    }
    
    private void resetFloor(int floor){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                labels.get(floor).get(i).get(j).setId("Free");
            }
        }
    }
    
    private void resetVisited(int floor){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(labels.get(floor).get(i).get(j).getId().equals("Visited")){
                    labels.get(floor).get(i).get(j).setId("Free");
                }
            }
        }
    }
    
    public void getFloorToGrid(int floor, GridPane gridPane){
        gridPane.getChildren().clear();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                gridPane.add(labels.get(floor).get(i).get(j), j, i);
            }
        }
    }
    
    public void save(PrintStream out){
        out.print(rows + " " + columns + " " + floors);
        out.println();
        for(int i = 0; i < floors; i++){
            for(int j = 0; j < rows; j++){
                for(int k = 0; k < columns; k++){
                    if(labels.get(i).get(j).get(k).getId().equals("Free") || labels.get(i).get(j).get(k).getId().equals("Visited")){
                        out.print(". ");
                    }
                    if(labels.get(i).get(j).get(k).getId().equals("Wall")){
                        out.print("# ");
                    }
                    if(labels.get(i).get(j).get(k).getId().equals("Shaft")){
                        out.print("V ");
                    }
                }
                out.println();
            }
            out.println();
        }
    }
    
    public void setSearch(){
        clear();
        cmiSearch.setDisable(true);
        search = true;
    }
    
    public void setAddWall(){
        clear();
        cmiAddWall.setDisable(true);
        addWall = true;
    }
    
    public void setAddRandomWalls(){
        clear();
        cmiAddRandomWalls.setDisable(true);
        addRandomWalls = true;
    }
    
    public void setRemoveWall(){
        clear();
        cmiRemoveWall.setDisable(true);
        removeWall = true;
    }
    
    public void setAddShaft(){
        clear();
        cmiAddShaft.setDisable(true);
        addShaft = true;
    }
    
    public void setAddShaftToNextFloor(){
        clear();
        cmiAddShaftToNextFloor.setDisable(true);
        addShaftToNextFloor = true;
    }
    
    public void setRemoveShaft(){
        clear();
        cmiRemoveShaft.setDisable(true);
        removeShaft = true;
    }
    
    public void setRemoveShaftFromFloor(){
        clear();
        cmiRemoveShaftFromFloor.setDisable(true);
        removeShaftFromFloor = true;
    }
    
    public void setResetVisited(){
        clear();
        cmiResetVisited.setDisable(true);
        resetVisited = true;
    }
    
    public void setResetFloor(){
        clear();
        cmiResetFloor.setDisable(true);
        resetFloor = true;
    }
}

package maze;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/** Okno nové ktoré sa otvorí pri zvolení New Game. Užívatel zadá parametre na
 * vytvorenie novej hry a ak sú správne tak sa uložia a nastaví created na TRUE*/
class NewGame{
    
    private Stage newGameStage;
    private VBox vBox;
    private Label labelRows;
    private TextField textRows;
    private Label labelColumns;
    private TextField textColumns;
    private Label labelFloors;
    private TextField textFloors;
    private Button ok;
    
    private int rows;
    private int columns;
    private int floors;
    
    private boolean created;
    
    public NewGame(){
        newGameStage = new Stage();
        newGameStage.setTitle("New Game");
        newGameStage.setOnCloseRequest((WindowEvent)->{
            created = false;
            newGameStage.close();
        });
        
        labelRows = new Label("Rows (3-35)");
        textRows = new TextField();
        
        labelColumns = new Label("Columns (3-35)");
        textColumns = new TextField();
        
        labelFloors = new Label("Floors (1-10)");
        textFloors = new TextField();
        
        ok = new Button("OK");
        ok.setOnAction((ActionEvent)->{
            if(filledOut()){
                rows = Integer.parseInt(textRows.getText());
                columns = Integer.parseInt(textColumns.getText());
                floors = Integer.parseInt(textFloors.getText());
                created = true;
                newGameStage.close();
            }
        });
        
        vBox = new VBox();
        vBox.getChildren().addAll(labelRows, textRows, labelColumns, textColumns, labelFloors, textFloors, ok);
        vBox.setMinWidth(300);
        
        Scene newGameScene = new Scene(vBox);
        newGameScene.getStylesheets().add("maze/resources/styly2.css");
        newGameStage.setScene(newGameScene);
        newGameStage.sizeToScene();
    }
    
    private boolean filledOut(){
        if(textRows.getLength() != 0 && Integer.parseInt(textRows.getText()) > 2 && Integer.parseInt(textRows.getText()) < 36){
            if(textColumns.getLength() != 0 && Integer.parseInt(textColumns.getText()) > 2 && Integer.parseInt(textColumns.getText()) < 36){
                if(textFloors.getLength() != 0 && Integer.parseInt(textFloors.getText()) > 0 && Integer.parseInt(textFloors.getText()) < 11){
                    return true;
                }
            }
        }           
        return false;
    }
    
    public int getRows(){
        return rows;
    }
    
    public int getColumns(){
        return columns;
    }
    
    public int getFloors(){
        return floors;
    }
    
    public boolean isCreated(){
        return created;
    }
    
    public void showNewGame(){
        newGameStage.showAndWait();
    }
}

/** Okno nové ktoré sa otvorí pri zvolení rozmiestenia náhodných stien. Užívatel 
 * zadá počet stien a ak je pod počtom max tak sa uloži a nastaví created na TRUE*/
class NumberOfRandomWalls{
    private Stage randomStage;
    private VBox vBox;
    private Label labelNum;
    private TextField textNum;
    private Button ok;
    
    private int number;
    private boolean created;
    
    public NumberOfRandomWalls(int rows, int columns){
        randomStage = new Stage();
        randomStage.setTitle("Number of walls");
        randomStage.setOnCloseRequest((WindowEvent)->{
            created = false;
            randomStage.close();
        });
        
        labelNum = new Label("Insert the number of walls to generate " + rows*columns);
        textNum = new TextField();
        
        ok = new Button("OK");
        ok.setOnAction((ActionEvent)->{
            if(filledOut(rows, columns)){
                number = Integer.parseInt(textNum.getText());
                created = true;
                randomStage.close();
            }
        });
        
        vBox = new VBox();
        vBox.getChildren().addAll(labelNum, textNum, ok);
        vBox.setMinWidth(300);
        
        Scene randomScene = new Scene(vBox);
        randomScene.getStylesheets().add("maze/resources/styly2.css");
        randomStage.setScene(randomScene);
        randomStage.sizeToScene();
    }
    
    private boolean filledOut(int rows, int columns){
        if(textNum.getLength() != 0 && Integer.parseInt(textNum.getText()) > 0 && Integer.parseInt(textNum.getText()) < (rows*columns)+1){
            return true;
        }           
        return false;
    }
    
    public int getNumber(){
        return number;
    }
    
    public boolean isCreated(){
        return created;
    }
    
    public void showRandom(){
        randomStage.showAndWait();
    }
}
package maze;

import java.io.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class Hra {
    
    /**Premenná, ktorá má uložené počet riadkov*/
    private int rows;
    
    /**Premenná, ktorá má uložené počet stĺpcov*/
    private int columns;
    
    /**Premenná, ktorá má uložené počet poschodí*/
    private int floors;
    
    /**Trojrozmerné pole v ktorom sú uložené jednotlivé políčka*/
    private ArrayList<ArrayList<ArrayList<Label>>> labels;
    
    /** Premenná, ktorá obsahuje aktuálnu zvolenú funkciu*/
    private SelectedTool status;
    
    /** Enum v ktorom su definované názvy jednotlivých funkcii*/
    private enum SelectedTool{
        SEARCH,
        ADDWALL,
        ADDRANDOMWALLS,
        REMOVEWALL,
        ADDSHAFT,
        ADDSHAFTTONEXTFLOOR,
        REMOVESHAFT,
        REMOVESHAFTFROMFLOOR,
        RESETVISITED,
        RESETFLOOR,
        NOTHING,
    }
    
    
    /**Konštruktor v ktorom sa vykonajú všetky potrebne operácie na vytvorenie hry*/
    public Hra(int rows, int columns, int floors, ArrayList<String> string, ContextMenu tools){
        this.rows = rows;
        this.columns = columns;
        this.floors = floors;
        labels = new ArrayList<>();
        //Defaultne sa funkcia nastaví na nic
        status = SelectedTool.NOTHING;
        int index = 0;
        //For cykli na vytvorenie všetkých políčok
        for(int i = 0; i < this.floors; i++){
            labels.add(new ArrayList<>());
            for(int j = 0; j < this.rows; j++){
                labels.get(i).add(new ArrayList<Label>());
                for(int k = 0; k < this.columns; k++){
                    Label label = new Label();
                    label.setContextMenu(tools);
                    //Zistí či je pole Stringov ak hej načíta z nej ak nie nastaví sa Free 
                    if(string != null){
                        if(string.get(index).equals(".")){
                            //Volné políčko
                            label.setId("Free");
                        } else if(string.get(index).equals("#")){
                            //Stena
                            label.setId("Wall");
                        } else if(string.get(index).equals("V")){
                            //Výtah    
                            label.setId("Shaft");
                        }
                        index++;
                    } else {
                        label.setId("Free");
                    }
                    createLabel(label, i, j, k);
                    labels.get(i).get(j).add(label);
                }
            }
        }
    }
    
    /**Pomocná metóda na nastavenie jednotlivých operácii na dané políčko ktoré
     * je definované riadkom stlpcom a poschodím*/
    private void createLabel(Label label, int floor, int row, int column){
        label.setOnMouseClicked((ActionEvent) -> {
            //Uloží, ktoré tlačítko sme na myši stlačili
            MouseButton btn = ActionEvent.getButton();
            //Odfiltrujeme len lavé tlačítko aby sa to nebilo s ContextMenu
            if(btn == MouseButton.PRIMARY){
                switch(status){
                    case SEARCH:
                        colourIt(floor, row, column);
                        break;
                    case ADDWALL:
                        if(!label.getId().equals("Shaft")){
                            label.setId("Wall");
                        }
                        break;
                    case ADDRANDOMWALLS:
                        generateWalls(floor);
                        break;
                    case REMOVEWALL:
                        if(label.getId().equals("Wall")){
                            label.setId("Free");
                        }
                        break;
                    case ADDSHAFT:
                        placeShaft(row, column);
                        break;
                    case ADDSHAFTTONEXTFLOOR:
                        addShaftToNextFloor(row, column, floor);
                        break;
                    case REMOVESHAFT:
                        if(label.getId().equals("Shaft")){
                            removeShaft(row, column);
                        }
                        break;
                    case REMOVESHAFTFROMFLOOR:
                        if(label.getId().equals("Shaft")){
                            label.setId("Free");
                        }
                        break;
                    case RESETVISITED:
                        resetVisited();
                        break;
                    case RESETFLOOR:
                        resetFloor(floor);
                        break;
                }
            }
        });
    }
    
    /** Metóda, ktorá rekurzívne prehladáva celú plochu hry od zvoleného bodu*/
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
    
    /**Metóda ktorá vloží výtah na kazdé poschodie*/
    private void placeShaft(int row, int column){
        for(int i = 0; i < floors; i++){
            labels.get(i).get(row).get(column).setId("Shaft");
        }
    }
    
    /**Metóda, ktorá zmaže zvolený výťah z každého poschodia*/
    private void removeShaft(int row, int column){
         for(int i = 0; i < floors; i++){
            labels.get(i).get(row).get(column).setId("Free");
        }
    }
    
    /**Metóda, ktorá vloží na zvolené a ďalšie poschodie výťah*/
    private void addShaftToNextFloor(int row, int column, int floor){
        if(floor+1 < floors){
            labels.get(floor).get(row).get(column).setId("Shaft");
            labels.get(floor+1).get(row).get(column).setId("Shaft");
        }
    }
    
    /**Metóda, ktorá vygeneruje N stien na danom poschodí náhodne rozmiestnené.
     * Vznikne okno v ktorom používatel zadá počet stien ktoré chce rozmiestnit a
     * nasledne sa rozmiestna nahodne ale ak sa nejaka stena chce vložit na miesto,
     * kde je už stena tak sa tam vloži -> je to aby sa nezacyklilo*/
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
    
    /**Metóda, ktorá vyprázdni cele poschodie*/
    private void resetFloor(int floor){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                labels.get(floor).get(i).get(j).setId("Free");
            }
        }
    }
    
    /** Metóda, ktorá obnoví navštívené políčka na prázdne*/
    private void resetVisited(){
        for(int i = 0; i < floors; i++){
            for(int j = 0; j < rows; j++){
                for(int k = 0; k < columns; k++){
                    if(labels.get(i).get(j).get(k).getId().equals("Visited")){
                        labels.get(i).get(j).get(k).setId("Free");
                    }
                }
            }
        }
    }
    
    /**Metóda ktorá načíta políčka, ktoré sú na danom poschodí do gridPane*/
    public void getFloorToGrid(int floor, GridPane gridPane){
        gridPane.getChildren().clear();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                gridPane.add(labels.get(floor).get(i).get(j), j, i);
            }
        }
    }
    
    /**Metóda, ktorá uloží do súboru údaje o hre. Na prvý riadok => pocet riadkov, 
     * počet stĺpcov, počet poschodí. NaďalŠie riadky sa vypíše hracia plocha.*/
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
    
    /**Metóda, ktorá nastaví funkciu vyhľadávanie*/
    public void setSearch(){
        status = SelectedTool.SEARCH;
    }
    
    /**Metóda, ktorá nastaví funkciu pridaj stenu*/
    public void setAddWall(){
        status = SelectedTool.ADDWALL;
    }
    
    /**Metóda, ktorá nastaví funkciu pridaj nahodne steny*/
    public void setAddRandomWalls(){
        status = SelectedTool.ADDRANDOMWALLS;
    }
    
    /**Metóda, ktorá nastaví funkciu odstrán stenu*/
    public void setRemoveWall(){
        status = SelectedTool.REMOVEWALL;
    }
    
    /**Metóda, ktorá nastaví funkciu pridaj výťah*/
    public void setAddShaft(){
        status = SelectedTool.ADDSHAFT;
    }
    
    /**Metóda, ktorá nastaví funkciu pridaj výťah na ďalšie poschodie*/
    public void setAddShaftToNextFloor(){
        status = SelectedTool.ADDSHAFTTONEXTFLOOR;
    }
    
    /**Metóda, ktorá nastaví funkciu odstráň výťah*/
    public void setRemoveShaft(){
        status = SelectedTool.REMOVESHAFT;
    }
    
    /**Metóda, ktorá nastaví funkciu odstráň výťah s daného poschodia*/
    public void setRemoveShaftFromFloor(){
        status = SelectedTool.REMOVESHAFTFROMFLOOR;
    }
    
    /**Metóda, ktorá nastaví funkciu reset navštívených*/
    public void setResetVisited(){
        status = SelectedTool.RESETVISITED;
    }
    
    /**Metóda, ktorá nastaví funkciu reset poschodia*/
    public void setResetFloor(){
        status = SelectedTool.RESETFLOOR;
    }
    
    /**Metóda, ktorá pridá ďalšie poschodie ak sa dá. V novom poschodí budú volné
     * políčka */
    public int addFloor(ContextMenu tools){
        status = SelectedTool.NOTHING;
        if((floors+1) < 11){
            floors++;
            labels.add(new ArrayList<>());
            for(int i = 0; i < rows; i++){
                labels.get(floors-1).add(new ArrayList<Label>());
                for(int j = 0; j < columns; j++){
                    Label label = new Label();
                    label.setContextMenu(tools);
                    label.setId("Free");
                    createLabel(label, floors-1, i, j);
                    labels.get(floors-1).get(i).add(label);
                }
            }
            return floors-1;
        } else {
            return -1;
        }
    }
    
    /**Metóda, ktorá odstráni najvyššie poschodie ak sa dá*/
    public int removeFloor(){
        status = SelectedTool.NOTHING;
        if(floors-1 > 0){
            labels.get(floors-1).clear();
            floors--;
            return floors;
        } else {
            return -1;
        }
    }
    
    /**Metóda, ktorá pridá ďaľší riadok na každé poschodie. Každý nový riadok 
     * má voľné políčka */
    public boolean addRow(ContextMenu tools){
        status = SelectedTool.NOTHING;
        if(rows+1 < 101){
            rows++;
            for(int i = 0; i < floors; i++){
                labels.get(i).add(new ArrayList<>());
                for(int j = 0; j < columns; j++){
                    Label label = new Label();
                    label.setContextMenu(tools);
                    label.setId("Free");
                    createLabel(label, i, rows-1, j);
                    labels.get(i).get(rows-1).add(label);
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /** Metóda, ktorá odstráni posledný riadok*/
    public boolean removeRow(){
        status = SelectedTool.NOTHING;
        if(rows-1 >= 3){
            for(int i = 0; i < floors; i++){
                labels.get(i).get(rows-1).clear();
            }
            rows--;
            return true;
        } else {
            return false;
        }
    }
    
    /** Metóda, ktorá pridá nový stlpec na každé poschodie. V každom stĺpci sú
     * políčka voľné */
    public boolean addColumn(ContextMenu tools){
        status = SelectedTool.NOTHING;
        if(columns+1 < 101){
            columns++;
            for(int i = 0; i < floors; i++){
                for(int j = 0; j < rows; j++){
                    Label label = new Label();
                    label.setContextMenu(tools);
                    label.setId("Free");
                    createLabel(label, i, j, columns-1);
                    labels.get(i).get(j).add(label);
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**Metóda, ktorá odstráni posledný stĺpec*/
    public boolean removeColumn(){
        status = SelectedTool.NOTHING;
        if(columns-1 >= 3){
            for(int i = 0; i < floors; i++){
                for(int j = 0; j < rows; j++){
                    labels.get(i).get(j).remove(columns-1);
                }
            }
            columns--;
            return true;
        } else {
            return false;
        }
    }
}

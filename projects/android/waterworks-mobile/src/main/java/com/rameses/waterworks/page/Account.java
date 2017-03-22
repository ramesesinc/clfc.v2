package com.rameses.waterworks.page;

import com.rameses.Main;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Account {
    
    private VBox root;
    
    public Account(){
        Label appno_lb = new Label("Application No.");
        appno_lb.getStyleClass().add("account-label");
        
        Label name_lb = new Label("Name");
        name_lb.getStyleClass().add("account-label");
        
        Label address_lb = new Label("Address");
        address_lb.getStyleClass().add("account-label");
        
        Label sector_lb = new Label("Sector");
        sector_lb.getStyleClass().add("account-label");
        
        Label zone_lb = new Label("Zone No.");
        zone_lb.getStyleClass().add("account-label");
        
        TextField appno_tf = new TextField();
        appno_tf.getStyleClass().add("account-textfield");
        appno_tf.setEditable(false);
        
        TextField name_tf = new TextField();
        name_tf.getStyleClass().add("account-textfield");
        name_tf.setEditable(false);
        
        TextField address_tf = new TextField();
        address_tf.getStyleClass().add("account-textfield");
        address_tf.setEditable(false);
        
        TextField sector_tf = new TextField();
        sector_tf.getStyleClass().add("account-textfield");
        sector_tf.setEditable(false);
        
        TextField zone_tf = new TextField();
        zone_tf.getStyleClass().add("account-textfield");
        zone_tf.setEditable(false);
        
        GridPane accountGrid = new GridPane();
        accountGrid.setHgap(10);
        accountGrid.setVgap(10);
        
        accountGrid.add(appno_lb, 0, 0);
        accountGrid.add(name_lb, 0, 1);
        accountGrid.add(address_lb, 0, 2);
        accountGrid.add(sector_lb, 0, 3);
        accountGrid.add(zone_lb, 0, 4);
        
        accountGrid.add(appno_tf, 1, 0);
        accountGrid.add(name_tf, 1, 1);
        accountGrid.add(address_tf, 1, 2);
        accountGrid.add(sector_tf, 1, 3);
        accountGrid.add(zone_tf, 1, 4);
        
        TitledPane accountPane = new TitledPane();
        accountPane.setText("Account Information");
        accountPane.setContent(accountGrid);
        accountPane.setCollapsible(false);
        
        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(accountPane);
        
    }
    
    public Node getLayout(){
        return root;
    }
    
}

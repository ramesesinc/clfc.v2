package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.layout.Header;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AccountList {
    
    private VBox root;
    private TableView<Account> table;
    private TextField search;
    
    public AccountList(){
        Header.TITLE.setText("Accounts");
        
        search = new TextField();
        search.setId("search-account");
        search.setPromptText("Search");
        search.setFocusTraversable(false);
        search.setMaxWidth(Main.WIDTH * 0.75);
        
        double colSize = (Main.WIDTH - 20) / 3;
        
        TableColumn appnoCol = new TableColumn("Application No.");
        appnoCol.setCellValueFactory(new PropertyValueFactory<AccountList,String>("appno"));
        appnoCol.setPrefWidth(colSize);
        
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<AccountList,String>("name"));
        nameCol.setPrefWidth(colSize);
        
        TableColumn addressCol = new TableColumn("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<AccountList,String>("address"));
        addressCol.setPrefWidth(colSize);
        
        table = new TableView<Account>();
        table.setPrefHeight(Main.HEIGHT);
        table.getColumns().addAll(appnoCol, nameCol, addressCol);
        table.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() ==  2){
                    Main.ROOT.setCenter(new Account().getLayout());
                }
            }
        });
        
        double buttonSize = (Main.WIDTH - 30) / 2;
        
        Button download = new Button("Download Data from Server");
        download.setPrefWidth(buttonSize);
        
        Button upload = new Button("Upload Data to Server");
        upload.setPrefWidth(buttonSize);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(download,upload);
        
        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(search, table, buttonContainer);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    Main.ROOT.setCenter(new Home().getLayout());
                }
            }
        });
    }
    
    public Node getLayout(){
        return root;
    }
    
}

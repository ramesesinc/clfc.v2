package com.rameses.waterworks.sample;

import com.rameses.waterworks.bluetooth.BluetoothPlatformFactory;
import com.rameses.waterworks.bluetooth.BluetoothPort;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class PrintSample {
    
    private VBox root;
    public static TextArea textarea;
    
    public PrintSample(){
        Button button = new Button("PRINT");
        button.setStyle("-fx-font-size: 18px;");
        button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                BluetoothPort port = BluetoothPlatformFactory.getPlatform().getBluetoothPrinter();
                port.setPrinter("00:80:37:2E:EB:83");
                port.print(textarea.getText());
            }
        });
        
        textarea = new TextArea();
        textarea.setStyle("-fx-font-size: 20px;");
        
        root = new VBox(3);
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(textarea,button);
    }
    
    public Node getLayout(){
        return root;
    }
    
}

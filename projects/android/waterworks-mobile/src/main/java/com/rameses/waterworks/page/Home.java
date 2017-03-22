package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.layout.Header;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Home {
    
    private VBox root;
    private ImageView logo;
    private FlowPane pane;
    
    public Home(){
        Header.TITLE.setText("Waterworks System");
        Header.LOGO.setImage(new Image("icon/ic_launcher.png"));
        Header.LOGO.setFitWidth(50);
        Header.LOGO.setFitHeight(50);
        Header.showSetting(false);
        
        MenuItem accountItem = new MenuItem("icon/account.png","Accounts","View the list of account information including its meter and application information.");
        Node account = accountItem.getLayout();
        accountItem.getImage().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new AccountList().getLayout());
            }
        });
        accountItem.getImage().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                Main.ROOT.setCenter(new AccountList().getLayout());
            }
        });
        
        MenuItem sheetItem = new MenuItem("icon/list.png","Reading Sheet","Update the the account's meter reading history by capturing its new meter reading.");
        Node sheet = sheetItem.getLayout();
        sheetItem.getImage().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Sheet");
            }
        });
        sheetItem.getImage().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                System.out.println("Sheet");
            }
        });
        
        MenuItem settingItem = new MenuItem("icon/mysetting.png","Setting","Manage your user account and connection setting.");
        Node setting = settingItem.getLayout();
        settingItem.getImage().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Setting");
            }
        });
        settingItem.getImage().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                System.out.println("Setting");
            }
        });
        
        
        root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(50));
        root.getChildren().addAll(account,sheet,setting);
    }
    
    private class MenuItem {
        
        private HBox container;
        private ImageView image;
        private Text title;
        private Label desc;
        
        public MenuItem(String image_url, String title, String desc){
            this.image = new ImageView(new Image(image_url));
            
            VBox imageContainer = new VBox();
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.getChildren().add(this.image);
            
            this.title = new Text(title);
            this.title.getStyleClass().add("home-title");
            
            this.desc = new Label(desc);
            this.desc.getStyleClass().add("home-desc");
            this.desc.setWrapText(true);
            
            VBox textContainer = new VBox(10);
            textContainer.setAlignment(Pos.CENTER_LEFT);
            textContainer.getChildren().addAll(this.title,this.desc);
            
            container = new HBox(20);
            container.getChildren().addAll(imageContainer,textContainer);
        }
  
        public Node getLayout(){
            return container;
        }
        
        public ImageView getImage(){
            return image;
        }
    }
    
    public Node getLayout(){
        return root;
    }
    
}

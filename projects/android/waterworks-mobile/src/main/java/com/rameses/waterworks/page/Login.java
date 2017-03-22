package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.LoginService;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Login {
    
    private GridPane grid;
    private TextField username;
    private PasswordField password;
    private Button login;
    
    public Login(){
        Header.TITLE.setText("User Login");
        Header.LOGO.setImage(new Image("icon/key.png"));
        
        ImageView image = new ImageView(new Image("icon/userlogin.png"));
        
        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getChildren().add(image);
        
        username = new TextField();
        username.getStyleClass().add("login-field");
        username.setPrefWidth(300);
        
        password = new PasswordField();
        password.getStyleClass().add("login-field");
        password.setPrefWidth(300);
        
        Label username_lb = new Label("Username");
        username_lb.getStyleClass().add("login-label");
        
        Label password_lb = new Label("Password");
        password_lb.getStyleClass().add("login-label");
        
        login = new Button("Login");
        login.setId("login-button");
        login.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                String usrname = username.getText();
                String pwd = password.getText();
                if(usrname.isEmpty() || pwd.isEmpty()) return;
                
                Map parameter = new HashMap();
                parameter.put("username", usrname);
                parameter.put("password", pwd);
                
                LoginService service = new LoginService();
                Map account = service.login(parameter);
                
                if(account != null){
                    Main.ROOT.setCenter(new Home().getLayout());
                }else{
                    Main.LOG.error("LoginService", "Invalid username or password.");
                }
            }
        });
        
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.getChildren().add(login);
        
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(50));
        //grid.setGridLinesVisible(true);
        grid.setVgap(10);
        grid.setHgap(10);
        
        grid.add(imageContainer, 0, 0, 2, 1);
        grid.add(username_lb, 0, 1);
        grid.add(username, 1, 1);
        grid.add(password_lb, 0, 2);
        grid.add(password, 1, 2);
        grid.add(buttonContainer, 1, 3);
    }
    
    public Node getLayout(){
        return grid;
    }
    
}

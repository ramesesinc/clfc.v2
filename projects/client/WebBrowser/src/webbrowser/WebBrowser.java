/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbrowser;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import netscape.javascript.JSObject;

/**
 *
 * @author louie
 */
public class WebBrowser extends Application {
    
    public void xstart(Stage primaryStage) {
        
        PrinterService printerService = new PrinterService();
        printerService.printString("SANEI SK1-21S", "\n\n testing testing 1 2 3eeeee \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        
//        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
//SANEI SK1-21S
//        for (PrintService printer : printServices) {
//            System.out.println("Printer: " + printer.getName()); 
//        }
    }
    
    @Override
    public void start(Stage primaryStage) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html>");
//        sb.append("<head>");
//        sb.append("<script>");
//        sb.append("function sayHello() {");
//        sb.append("alert('say hello');");
//        sb.append("}");
//        sb.append("</script>");
//        sb.append("</head>");
//        sb.append("<body>");
//        sb.append("<h1>What Can JavaScript Do?</h1>");
//        sb.append("<p id=\"demo\">JavaScript can change HTML content.</p>");
//        sb.append("<input type=\"button\" value=\"Hello Javascript\" onclick=\"sayHello()\">");
//        //sb.append("onclick=\"document.getElementById('demo').innerHTML = 'Hello JavaScript!'\">");
//        //sb.append("Click Me!</button>");
//        sb.append("</body>");
//        sb.append("</html>");
        
        WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.load("http://localhost:8080/index");
//        webEngine.load("http://192.168.254.4:8080/index");
//        JSObject jsobj = (JSObject) webEngine.executeScript("window");
//        jsobj.setMember("bridge", new Bridge());
        webEngine.getLoadWorker().stateProperty().addListener(
               new ChangeListener<Worker.State>() {
                   @Override
                   public void changed( ObservableValue<? extends Worker.State> ov,
                                        Worker.State oldState, Worker.State newState ) {
//                       println("new state " + newState);
                       if ( newState == Worker.State.SUCCEEDED ) {
                           JSObject jsobj = (JSObject) webEngine.executeScript("window");
                           jsobj.setMember("handler", new BridgeHandler());
//                           println("reload");
//                           println("member " + jsobj.getMember("bridge"));
//                           Element printSenior = webEngine.getDocument().getElementById("print-senior");
//                           if (printSenior != null) {
//                               printSenior.
//                           }
//                           println("print-senior element " + printSenior);
//                           Element emailField = webEngine.getDocument().getElementById( "Email" );
//                           if ( emailField != null ) {
//                               emailField.setAttribute( "Value", username );
//                           }
//                           Element passwordField = webEngine.getDocument().getElementById( "Passwd" );
//                           if ( emailField != null ) {
//                               passwordField.setAttribute( "Value", password );
//                           }
//                           HTMLFormElement gaia_loginform = ( HTMLFormElement ) engine.getDocument().getElementById( "gaia_loginform" );
//                           if ( gaia_loginform != null ) {
//                               if ( !submitted.get() ) {
//                                   submitted.set( true );
//                                   gaia_loginform.submit();
//                               }
//                           }
                       } else if ( oldState == Worker.State.SUCCEEDED ) {
                           JSObject jsobj = (JSObject) webEngine.executeScript("window");
                           jsobj.removeMember("bridge");
                       }
                   }
               }
        );
        
//        Hyperlink hpl = new Hyperlink("java2s.com");
//        hpl.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent e) {
//                webEngine.load("http://192.168.254.11:8080/index");
//            }
//        });
//        webEngine.loadContent(sb.toString());
//        webEngine.setOnAlert(new EventHandler<WebEvent<String>>(){
//            @Override
//            public void handle(WebEvent<String> event) {
//                System.out.println("Alert Message > " + event.getData());
//            }
//        });

//        StackPane root = new StackPane();
//        root.getChildren().add(browser);
        VBox root = new VBox();
        root.getChildren().addAll(browser);
        
        Scene scene = new Scene(root);
       
//        scene.setOnMouseClicked(new EventHandler() {
//            @Override
//            public void handle(Event event) {
//                println("event target " + event.getTarget());
////                println("mouse clicked " + event.getSource());
////                println("event type " + event.getEventType());
////                System.out.println("mouse click detected! "+event.getSource());
//            }
//        });
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        
//        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
//        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
    
    void println( String msg ) {
        System.out.println(msg);
    }
    
//    @Override
//    public void start(Stage primaryStage) {
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
//        
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        
//        Scene scene = new Scene(root, 300, 250);
//        
//        primaryStage.setTitle("Hello World!");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.Toolkit.getDefaultToolkit();
        System.setProperty("javafx.macosx.embedded", "true");
        System.setProperty("java.awt.headless", "true");
//        -Dglass.platform=Monocle -Dmonocle.platform=Headless -Dprism.order=sw
        
//        System.setProperty("glass.platform", "Monocle");
//        System.setProperty("monocle.platform", "Headless");
//        System.setProperty("prism.order", "sw");
//        System.setProperty("java.awt.headless", "false");
        
        Application.launch(args);
    }
    
    public class BridgeHandler {

        void println(String str) {
            System.out.println(str);
        }

        public void onclick() {
            println("doPrint w/o params");
            
//            try {
//                PrinterService printerService = new PrinterService();
//                printerService.printString("SANEI SK1-21S", "Test Printing\n\n\n\n\n\n\n\n\n\n");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }
        
        public void onclick(Object obj) {
        }
    }
    
    private class PrinterService implements Printable {
        @Override
        public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
            if (page > 0) { /* We have only one page, and 'page' is zero-based */
                return NO_SUCH_PAGE;
            }

            /*
             * User (0,0) is typically outside the imageable area, so we must
             * translate by the X and Y values in the PageFormat to avoid clipping
             */
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            /* Now we perform our rendering */

            g.setFont(new Font("Roman", 0, 8));
            g.drawString("Hello world !", 0, 10);

            return PAGE_EXISTS;
        }
        
        private PrintService findPrintService(String printerName) {
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

            PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, pras);
            
            for (PrintService service : services) {
                if (service.getName().equalsIgnoreCase(printerName)) {
                    return service;
                }
            }

            return null;
        }
        
        public void printString(String printerName, String text) {

           // find the printService of name printerName
           DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
           PrintService service = findPrintService(printerName);

           DocPrintJob job = service.createPrintJob();

           try {

               byte[] bytes;

               // important for umlaut chars
               bytes = text.getBytes("CP437");

               Doc doc = new SimpleDoc(bytes, flavor, null);


               job.print(doc, null);

           } catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           
        }
        
    }
}


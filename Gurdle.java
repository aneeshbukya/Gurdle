package gurdle.gui;

import gurdle.CharChoice;
import gurdle.Model;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * The graphical user interface to the Wordle game model in
 * {@link Model}.
 *
 * @author Aneesh Bukya
 */
public class Gurdle extends Application
        implements Observer< Model, String > {

    private Model model;
    private ArrayList<Character> guessList = new ArrayList<>();

    private Label statement;
    private ArrayList<Button> buttonList = new ArrayList<>();
    private GridPane guessGrid = new GridPane();
    private Label [][] labels;
    private Label guessNum;
    private HashMap<Character,Button> keypadButtons = new HashMap<>();
    private Label cheatWord = new Label("Secret:");



    @Override public void init() {
        this.model = new Model();
        model.newGame();
        model.addObserver(this);
        labels = new Label[6][5];

    }

    @Override
    public void start( Stage mainStage ) {
        // the main borderpane
        BorderPane mainPane = new BorderPane();
        //top of borderpane
        FlowPane topMain = new FlowPane();
        guessNum = new Label("No. of Guesses:"+0);
        statement = new Label("Make a guess!");
        guessNum.setStyle( "-fx-font: 14px Menlo" );
        statement.setStyle( "-fx-font: 14px Menlo" );
        cheatWord.setStyle( "-fx-font: 14px Menlo" );
        HBox box = new HBox();
        box.getChildren().addAll(guessNum,statement,cheatWord);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(75);
        topMain.getChildren().add(box);
        mainPane.setTop(topMain);

        // bottom of borderpane
        FlowPane bottomMain = new FlowPane();
        Button newGame = new Button("New Game");
        Button cheatButton = new Button("CHEAT");
        newGame.setStyle( "-fx-font: 14px Menlo" );
        cheatButton.setStyle( "-fx-font: 14px Menlo" );
        HBox box1 = new HBox();
        box1.getChildren().addAll(newGame,cheatButton);
        box1.setAlignment(Pos.TOP_CENTER);
        bottomMain.getChildren().add(box1);
        bottomMain.setAlignment(Pos.TOP_CENTER);
        mainPane.setBottom(bottomMain);

        //center of borderpane
        BorderPane centerMain = new BorderPane();
        mainPane.setCenter(centerMain);

        //top of centerMain
        Label label;

        for (int row=0; row<6; row++) {
            for (int col = 0; col < 5; col++) {
                label = new Label();
                label.setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                """);
                guessGrid.add(label,col,row);
                labels[row][col] = label;
            }
        }
        guessGrid.setAlignment(Pos.CENTER);
        centerMain.setCenter(guessGrid);

        // right of centerMain
        VBox box2 = new VBox();
        Button enterButton = new Button("ENTER");
        enterButton.setStyle( "-fx-font: 14px Menlo" );
        box2.getChildren().add(enterButton);
        enterButton.setAlignment(Pos.BOTTOM_CENTER);
        mainPane.setRight(enterButton);


        //center of centerMain
        String [] lst = {"Q","W","E","R","T","Y","U","I","O","P", "A","S", "D","F","G","H","J","K","L","Z","X","C","V","B","N","M"};
        FlowPane flowpane = new FlowPane();
        for (String alphabet: lst){
            Button button = new Button(alphabet);
            button.setPrefSize(45,45);
            flowpane.getChildren().add(button);
            button.setOnAction((event) -> {
                this.model.enterNewGuessChar(alphabet.charAt(0));
                guessList.add(alphabet.charAt(0));
                buttonList.add(button);
                keypadButtons.put(alphabet.charAt(0),button);
            });

        }
        flowpane.setAlignment(Pos.CENTER);
        centerMain.setBottom(flowpane);

        Scene scene = new Scene(mainPane);
        mainStage.setScene( scene );
        mainStage.setTitle("GURDLE");
        mainStage.setHeight(600);
        mainStage.setWidth(550);
        mainStage.show();

        cheatButton.setOnAction((event) -> cheatWord.setText("Secret:"+this.model.secret()));
        newGame.setOnAction((event) -> {
            model.newGame();
            cheatWord.setText("Secret:");
            for (Character key : keypadButtons.keySet()){
                keypadButtons.get(key).setStyle( "-fx-font: 14px Menlo" );
            }
        });
        enterButton.setOnAction((event) -> model.confirmGuess());

    }

    @Override
    public void update( Model model, String message ) {
        guessNum.setText("No. of Guesses:"+model.numAttempts());
        statement.setText(message);
        for (int r =0; r<6; r++){
            for (int c=0; c<5; c++){
                CharChoice cc = model.get(r,c);
                labels[r][c].setText(String.valueOf(cc.getChar()));
                if (cc.getStatus().equals(CharChoice.Status.WRONG)){
                    labels[r][c].setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                            -fx-background-color: grey;

                """);
                    if (model.usedLetter(cc.getChar())){
                        keypadButtons.get(cc.getChar()).setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 2;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                            -fx-background-color: grey;

                """);
                    }
                }
                if (cc.getStatus().equals(CharChoice.Status.WRONG_POS)){
                    labels[r][c].setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                            -fx-background-color: orange;
                """);
                    if (model.usedLetter(cc.getChar())){
                        keypadButtons.get(cc.getChar()).setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 2;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                            -fx-background-color: orange;

                """);
                    }
                }
                if (cc.getStatus().equals(CharChoice.Status.RIGHT_POS)) {
                    labels[r][c].setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                           -fx-background-color: green;

                """);
                    if (model.usedLetter(cc.getChar())){
                        keypadButtons.get(cc.getChar()).setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 2;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                            -fx-background-color: green;

                """);
                    }
                }
                if (cc.getStatus().equals(CharChoice.Status.EMPTY)){
                    labels[r][c].setStyle( """
                            -fx-padding: 12;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                           -fx-background-color: white;

                """);

                }
            }
        }
    }

    public static void main( String[] args ) {
        if ( args.length > 1 ) {
            System.err.println( "Usage: java Gurdle [1st-secret-word]" );
        }
        Application.launch( args );
    }
}

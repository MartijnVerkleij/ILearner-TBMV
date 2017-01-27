/**
 * Created by Ties on 26-1-2017.
 */
package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends Application {

    ArrayList<CategoryDirectory> directories = new ArrayList<>();
    ArrayList<CategoryDirectory> testingDirectories = new ArrayList<>();
    ObservableList<String> directoriesTexts = FXCollections.observableArrayList();
    ObservableList<String> testingNames = FXCollections.observableArrayList();
    ObservableList<String> categoryNames = FXCollections.observableArrayList();

    GridPane grid;
    int highestRow = 2;

    NaiveMultinomialClassifier nmc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ILearner");

        TabPane tabs = new TabPane();

        Tab tab1 = new Tab();
        tab1.setText("Training");
        tab1.setClosable(false);

        Tab tab2 = new Tab();
        tab2.setText("Testing");
        tab2.setClosable(false);


        /////////////////////////// tab1
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(tabs, 300, 275);
        primaryStage.setScene(scene);

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Category:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Button btnAddCategory = new Button("+");
        btnAddCategory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = userTextField.getText();
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                chooser.setTitle("Choose training folder");
                File chosen = chooser.showDialog(primaryStage);
                if (chosen != null && !name.equals("")) {
                    directories.add(new CategoryDirectory(new Category(name), chosen));
                    directoriesTexts.add(name + " - " + chosen.getAbsolutePath());
                    categoryNames.add(name);
                    userTextField.clear();
                }
            }
        });
        grid.add(btnAddCategory, 2, 1);

        ListView listView = new ListView(directoriesTexts);
        grid.add(listView, 0, 2);

        Button btn = new Button("Train");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 0);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Document> docs = new ArrayList<Document>();
                ArrayList<Category> cats = new ArrayList<Category>();
                for (CategoryDirectory cD : directories) {
                    cats.add(cD.getCategory());
                    Utils.scanFile(cD.getDirectory(), cD.getCategory(), docs);
                }
                nmc = new NaiveMultinomialClassifier(cats, docs, 1.0);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ILearner");
                alert.setHeaderText(null);
                alert.setContentText("Training completed!");

                alert.showAndWait();
            }
        });

        tab1.setContent(grid);
        tabs.getTabs().add(tab1);

        //////////////////////////////////////tab2
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label("Category:");
        grid2.add(label, 0, 1);

        ComboBox comboBox = new ComboBox(categoryNames);
        grid2.add(comboBox, 1, 1);

        Button btnAddCategory2 = new Button("+");
        btnAddCategory2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = (String) comboBox.getValue();
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                chooser.setTitle("Choose testing folder");
                File chosen = chooser.showDialog(primaryStage);
                testingDirectories.add(new CategoryDirectory(new Category(name), chosen));
                testingNames.add(name + " - " + chosen.getAbsolutePath());
            }
        });
        grid2.add(btnAddCategory2, 2, 1);

        ListView listView2 = new ListView(testingNames);
        grid2.add(listView2, 0, 2);

        Button btn2 = new Button("Test");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btn2);
        grid2.add(hbBtn2, 1, 0);

        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<Category, CategoryScores> scores = new HashMap<>();

                ArrayList<Document> documents = new ArrayList<Document>();
                for (CategoryDirectory testingDirectory : testingDirectories) {
                    Utils.scanFile(testingDirectory.getDirectory(), testingDirectory.getCategory(), documents);
                }

                for (Document document : documents) {
                    if (!scores.containsKey(document.getCategory())) {
                        scores.put(document.getCategory(), new CategoryScores(document.getCategory()));
                    }

                    scores.get(document.getCategory()).incAmount();
                    Category tested = nmc.apply(document);
                    if (tested.toString().equals(document.getCategory().toString())) {
                        scores.get(document.getCategory()).incCorrect();
                    }
                }

                String resultText = "";
                for (Category category : scores.keySet()) {
                    resultText += category.toString() + ": " + scores.get(category).getCorrect() + " correct out of " + scores.get(category).getAmount() + "\n";
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ILearner");
                alert.setHeaderText("Test results");
                alert.setContentText(resultText);

                alert.showAndWait();
            }
        });

        tab2.setContent(grid2);
        tabs.getTabs().add(tab2);

        primaryStage.show();
    }

    private int incrementRow() {
        return ++highestRow;
    }
}

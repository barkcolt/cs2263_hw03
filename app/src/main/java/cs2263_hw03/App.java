package cs2263_hw03;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Course Overview");
        GridPane gridPane = createApp();
        addUIControls(gridPane);
        Scene scene = new Scene(gridPane, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private GridPane createApp() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        Label headerLabel = new Label("Course Management");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        Label departmentLabel = new Label("Department: ");
        gridPane.add(departmentLabel, 0,4);

        //Add Department ChoiceBox
        ChoiceBox<String> department = new ChoiceBox<>();
        department.getItems().add("Select");
        for (int i=0; i<Course.departmentCodes.length; i++) {
            department.getItems().add(Course.departmentCodes[i]);
        }
        gridPane.add(department, 1, 4);
        department.setValue("Select");

        // Add Name Label
        Label nameLabel = new Label("Course Name: ");
        gridPane.add(nameLabel, 0,1);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1,1);


        // Add Credit Label
        Label credit = new Label("Total Credits: ");
        gridPane.add(credit, 0, 2);

        // Add Credit Text Field
        TextField creditField = new TextField();
        creditField.setPrefHeight(40);
        gridPane.add(creditField, 1, 2);

        // Add Course Number Label
        Label courseLabel = new Label("Course number: ");
        gridPane.add(courseLabel, 0, 3);

        // Add Course Number Field
        TextField idField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(idField, 1,3);

        // Add save Button
        Button saveButton = new Button("Save");
        saveButton.setPrefHeight(40);
        saveButton.setDefaultButton(true);
        saveButton.setPrefWidth(100);
        gridPane.add(saveButton, 0, 5, 2, 1);
        GridPane.setHalignment(saveButton, HPos.CENTER);
        GridPane.setMargin(saveButton, new Insets(10, 0,10,0));

        saveButton.setOnAction(event -> {
            if(department.getValue().equals("Select")) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please select the department");
                return;
            }
            if(nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter the course name");
                return;
            }
            if(creditField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter the credit value");
                return;
            }
            else if(intCheck(creditField.getText())) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter an integer in the credit field");
                return;
            }
            if(idField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a course number");
                return;
            }
            else if(intCheck(idField.getText())) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter an integer in the ID field");
                return;
            }
            Course entered = new Course(Integer.parseInt(idField.getText()), nameField.getText(), Integer.parseInt(creditField.getText()), department.getValue());
            boolean fileWrite = entered.saveJSON();
            if(!fileWrite) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Unable to save changes");
                return;
            }
            showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Addition Successful!", "Added " + nameField.getText());
        });

        Button listCourses = new Button("List Courses");
        listCourses.setPrefHeight(40);
        listCourses.setPrefWidth(100);
        gridPane.add(listCourses, 0, 6, 2, 1);
        GridPane.setHalignment(listCourses, HPos.CENTER);
        GridPane.setMargin(listCourses, new Insets(0, 0,10,0));

        listCourses.setOnAction(event -> {
            showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Course List", readJSON());
        });

        Button exit = new Button("Exit");
        exit.setPrefHeight(40);
        exit.setPrefWidth(100);
        gridPane.add(exit, 0, 7, 2, 1);
        GridPane.setHalignment(exit, HPos.CENTER);
        GridPane.setMargin(exit, new Insets(0, 0,10,0));

        exit.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setContentText("Are you sure you want to close the app?");
            if (confirm.showAndWait().get()==ButtonType.OK) {
                Platform.exit();
            }
        });

    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    protected String readJSON() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("courses.json"));
            Course course = gson.fromJson(reader,Course.class);
            reader.close();
            return course.toString();
        } catch (Exception ex) {
            return "Couldn't read json";
        }
    }

    public boolean intCheck(String toCheck) {
        try {
            Integer.valueOf(toCheck);
            return false;
        }
        catch (NumberFormatException i) {
            return true;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
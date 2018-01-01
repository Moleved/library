package sample;

import entity.ActiveModel;
import entity.BooksEntity;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

public class Main extends Application {
    private static final String[] columnHeaders;
    private String tableSelection;
    private GridPane pane = new GridPane();
    private TableView<BooksEntity> table = new TableView<>();
    private TextField searchInput = new TextField();

    static {
        columnHeaders = new String[] { "id", "name", "state", "date"};
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");

        Button searchButton = new Button("Search");
        Button releaseButton = new Button("Release");
        Button returnedButton = new Button("Returned");

        addNewButton(primaryStage);

        searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                search();
            }
        });

        table.setMinWidth(600);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(600);

        List<ActiveModel> ents = BooksEntity.getAll(BooksEntity.class);
        BooksEntity[] list = new BooksEntity[ents.size()];
        ents.toArray(list);

        table.getItems().addAll(list);
        for (String name : columnHeaders) {
            TableColumn<BooksEntity, String> column = new TableColumn<>(name);

            if (name.equals("id")) column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId()));
            if (name.equals("name")) column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
            if (name.equals("state")) column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getState()));
            if (name.equals("date")) column.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getReleaseDate()));

            table.getColumns().add(column);
        }

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                tableSelection = newItem.getId();
            }
        });

        releaseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleDownButtonsPress(0);
            }
        });

        returnedButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleDownButtonsPress(1);
            }
        });

        pane.add(searchInput, 0, 0);
        pane.add(searchButton, 1, 0);
        pane.add(table, 0, 2, 6, 2);
        pane.add(returnedButton, 5, 5);
        pane.add(releaseButton, 6, 5);

        primaryStage.setScene(new Scene(pane, 640, 480));
        primaryStage.show();
    }

    private void addNewButton(Stage parentStage) {
        Button button = new Button("New");
        pane.add(button, 3, 0, 1, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {

                Stage stage = new Stage();

                stage.setTitle("New");
                stage.initOwner(parentStage);
                stage.initModality(Modality.APPLICATION_MODAL);

                VBox vbox = new VBox();

                TextField textField = new TextField();
                Button addButton = new Button("Add book");

                addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        BooksEntity entity = new BooksEntity();
                        entity.setName(textField.getText());
                        entity.save();

                        refreshTable();

                        stage.close();
                    }
                });

                vbox.getChildren().addAll(textField, addButton);

                Scene scene = new Scene(vbox, 300, 270);

                stage.setScene(scene);
                stage.showAndWait();
            }
        });
    }

    public void handleDownButtonsPress(int state) {
        BooksEntity entity = (BooksEntity) BooksEntity.getById(Long.parseLong(tableSelection), BooksEntity.class);
        entity.setState(state);
        entity.setReleaseDate(new Date());
        entity.update();
        refreshTable();
    }

    public void refreshTable() {
        table.getItems().clear();

        List<ActiveModel> ents = BooksEntity.getAll(BooksEntity.class);
        BooksEntity[] list = new BooksEntity[ents.size()];
        ents.toArray(list);

        table.getItems().addAll(list);
    }

    public void search() {
        table.getItems().clear();

        List<ActiveModel> ents = BooksEntity.getByName(searchInput.getText(), BooksEntity.class);
        BooksEntity[] list = new BooksEntity[ents.size()];
        ents.toArray(list);

        table.getItems().addAll(list);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

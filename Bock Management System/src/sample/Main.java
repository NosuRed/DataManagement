package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller = new Controller();
    private static final String title = "Book management system";


    private final TableView<Book> booksTable = new TableView<>();








    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        controller.setFilename("bookDatabase.db");

        //ObservableList<Book> data = FXCollections.observableArrayList(controller.getBookDataObList());

        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idColumn.setMinWidth(110.0);
        TableColumn<Book, String> bookTitle = new TableColumn<>("Title");
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookTitle.setMinWidth(110.0);
        TableColumn<Book, String> authorName = new TableColumn<>("Author");
        authorName.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorName.setMinWidth(110.0);

        booksTable.setItems(controller.getBookDataObList());
        booksTable.getColumns().addAll(idColumn, bookTitle, authorName);

        root.getChildren().addAll(booksTable);

        // new PropertyValueFactory<>("ID") => getID
        // new PropertyValueFactory<>("BookID") => getBookID





        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

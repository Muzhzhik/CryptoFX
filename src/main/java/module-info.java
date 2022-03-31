module com.example.cryptofx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cryptofx to javafx.fxml;
    exports com.example.cryptofx;
}
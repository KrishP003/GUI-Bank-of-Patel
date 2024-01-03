module com.example.bank_of_patel {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bank_of_patel to javafx.fxml;
    exports com.example.bank_of_patel;
}
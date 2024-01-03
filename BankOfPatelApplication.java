package com.example.bank_of_patel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * This is the runner that launches the GUI
 * @author Dharmik Patel and Krish Patel
 *
 */
/**
 * This is the Runner for the GUI.
 * @author Dharmik Patel and Krish Patel
 *
 */
public class BankOfPatelApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BankOfPatelApplication.class.
                getResource("bank-of-patel-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 540, 600);
        stage.setTitle("Bank Of Patel");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
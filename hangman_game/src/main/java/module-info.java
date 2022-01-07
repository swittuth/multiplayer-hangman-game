module hangman_code {
    requires javafx.controls;
    requires javafx.fxml;


    opens hangman_code to javafx.fxml;
    exports hangman_code;
}
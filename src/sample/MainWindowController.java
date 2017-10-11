package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class MainWindowController {
    File dir;
    @FXML private Text messageToViewer;
    @FXML
    private Button closeButton;
    @FXML
    private Button renameButton;

    /**
     * Handles Choose Directory Button Click
     * Opens a directory chooser
     * After the user selects a directory, display rename button
     * @param event
     */
    @FXML protected void handleChooseDirectoryButtonAction(ActionEvent event) {
        try {
            messageToViewer.setText("");

            DirectoryChooser chooser = new DirectoryChooser();
            dir = chooser.showDialog(null);
            if (dir == null) {
                return;
            }
            messageToViewer.setText(Paths.get(dir.getAbsolutePath()).toString());

            //enable rename button
            renameButton.setDisable(false);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Calls renameAllFilesInADirectory function to rename the files
     * Displays message to the user that the file has been renamed
     * Resets the program
     * @param event
     */
    @FXML protected void handleRenameButtonAction(ActionEvent event) {
        try {
            renameAllFilesInADirectory(dir);
            messageToViewer.setText("Files renamed");
            reset();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Closes the application
     * @param event
     */
    @FXML protected void close(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Loops through all the files in the directory and renames them incrementally
     * @param dir
     */
    private void renameAllFilesInADirectory(File dir) {
        int counter = 1;
        String newFileName;
        for (final File file : dir.listFiles()) {
            //if the file is a directory skip
            if (file.isDirectory()) {
                continue;
            }
            try {
                String[] fileNameSplits = file.getName().split("\\.");
                // extension is assumed to be the last part
                int extensionIndex = fileNameSplits.length - 1;
                // add extension to id

                newFileName = counter++ + "." + fileNameSplits[fileNameSplits.length - 1];

                File newfile = new File(Paths.get(dir.getAbsolutePath()).toString() + "//" + newFileName);
                file.renameTo(newfile);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    /**
     * Reset the program
     * set dir to null
     * disable the renameButton
     */
    private void reset() {
        try {
            dir = null;
            //disable rename button
            renameButton.setDisable(true);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Displays alert message to the user
     * @param e
     */
    private void handleException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }

}

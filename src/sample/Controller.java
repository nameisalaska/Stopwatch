package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    private Label label;
    @FXML
    private Button buttonPause;

    private long sec = 0;
    private long min = 0;
    private long hour = 0;

   private boolean isPaused; // for button "Pause/Play"
   private boolean isThread ; // for stop count time
   private boolean isStarted ;  // for button "Start"

   private static Thread threadforUpdate;

    public void showTimeMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Time");
        alert.setHeaderText("Your time");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateTheClock() {
        if (sec < 60) {
            sec++;
        } else {
            if (min < 60) {
                sec = 0;
                min++;
            } else {
                min = 0;
                hour++;
            }
        }
    }

    @FXML
    public void start() {
        if(!isStarted) {
            threadforUpdate = new Thread(new AffableThread());
            threadforUpdate.start();
            isStarted = true;
        }
        isThread = true;
    }

    @FXML
    public void stop() {
        isThread = false;
        showTimeMessage(String.format("%02d", hour) + " : " + String.format("%02d", min) + " : " + String.format("%02d", sec));
        refresh();
    }

    @FXML
    public void pause() {
        if (!isPaused) {
            isThread = false;
            buttonPause.setText("Play");
            isPaused = true;
        } else {
            start();
            buttonPause.setText("Pause");
            isPaused = false;
        }
    }

    @FXML
    public void refresh() {
        sec = 0; min = 0; hour = 0;
        label.setText(String.format("%02d", hour) + " : " + String.format("%02d", min) + " : " + String.format("%02d", sec));
    }

    class AffableThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } if (isThread) {
                    Platform.runLater(() -> {
                        updateTheClock();
                        label.setText(String.format("%02d", hour) + " : " + String.format("%02d", min) + " : " + String.format("%02d", sec));
                    });
                }
            }
        }
    }
}

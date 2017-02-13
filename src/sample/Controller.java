package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Controller implements Initializable{
    @FXML
    private Label label;
    @FXML
    private Pane pane;
    @FXML
    private Button buttonPause;

    long sec = 0;
    long min = 0;
    long hour = 0;

    long fixhour;
    long fixmin;
    long fixsec;

    boolean isControl = true;
    boolean isPaused = true;
  //  boolean isThread = true;
    LocalDateTime timeStart;
    LocalDateTime timeStop;
    LocalDateTime timePause;
    static  Thread threadforUpdate ;

    public void showTimeMessage(String message) {
        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "Time" );
        alert.setHeaderText( "Your time" );
        alert.setContentText( message );
        alert.showAndWait();
    }
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {  }

    public void updateTheClock(){
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
    public void start(){

        if(!isPaused){refreshFixed();}
        startTime();
        threadforUpdate =  new Thread(new AffableThread());
       // isThread = true;
        threadforUpdate.start();
    }
    public void refreshFixed(){
        fixhour = 0;
        fixmin = 0;
        fixsec = 0;
    }
    public void countTime(){
//        fixhour += hour;
//        fixmin += min;
//        fixsec += sec;
    }

    public void startTime(){ timeStart = LocalDateTime.now(); }

    @FXML
    public void stop(){
            findTime(timeStart, timeStop);
            countTime();
            showTimeMessage(String.format("%02d", fixhour) + " : " + String.format("%02d", fixmin) + " : " + String.format("%02d", fixsec));
            refresh();
    }

    public void findTime(LocalDateTime fromTime, LocalDateTime toTime){
        threadforUpdate.interrupt();
        // isThread = false;
        if(!threadforUpdate.interrupted()){System.out.println (" NO NO NO.NO Interrupted");}
        toTime = LocalDateTime.now();
        hour = fromTime.until(toTime, ChronoUnit.HOURS);
        fromTime = timeStart.plusHours(hour);
        min = timeStart.until(toTime, ChronoUnit.MINUTES);
        fromTime = timeStart.plusHours(min);
        sec = timeStart.until(toTime, ChronoUnit.SECONDS);
    }




    @FXML
    public void pause(){
        if(isControl) {
            findTime(timeStart, timePause);
            countTime();
            buttonPause.setText("Play");
            isControl = false;
            isPaused = true;
        }
        else{
            start();
            buttonPause.setText("Pause");
            isControl = true;
            isPaused = false;
        }
    }

    @FXML
    public void refresh(){
        startTime();
        sec = 0;  min = 0; hour = 0;
        label.setText(String.format("%02d", hour) + " : " +String.format("%02d", min) + " : " + String.format("%02d", sec) );
    }

    class AffableThread implements Runnable {
        @Override
        public void run() {
            // System.out.println("Привет из побочного потока!");
            while (true) {
                if (threadforUpdate.isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        updateTheClock();
                        label.setText(String.format("%02d", hour) + " : " + String.format("%02d", min) + " : " + String.format("%02d", sec));
                        System.out.println("Alaska");
                    });
                } else {
                    System.out.println("Kate");
                }
            }
        }
    }
}

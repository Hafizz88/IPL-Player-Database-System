package com.lastpart.lastpart;

import javafx.application.Platform;

public class RefreshThread implements Runnable {
    private ClubHomeController clubHomeController;
    Thread thread;

    public RefreshThread(ClubHomeController clubHomeController) {
        this.clubHomeController = clubHomeController;
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Update the UI on the JavaFX Application Thread
                Platform.runLater(clubHomeController::loadTransferWindow);

                // Sleep for the specified refresh rate
                Thread.sleep(ClubHomeController.getRefreshRate() * 1000L);
            } catch (InterruptedException e) {
                // Handle interruption (e.g., stop the thread gracefully)
                System.out.println("Refresh thread interrupted. Stopping...");
                break; // Exit the loop if the thread is interrupted
            } catch (Exception e) {
                // Handle other exceptions (e.g., log the error)
                e.printStackTrace();
            }
        }
    }
    public Thread getThread() {
        return thread;
    }
}


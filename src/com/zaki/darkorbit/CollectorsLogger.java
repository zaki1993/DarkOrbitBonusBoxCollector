package com.zaki.darkorbit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CollectorsLogger {

    private int collectedObjects;

    private double red;

    private double green;

    private double blue;

    private final String loggerFileName;

    public CollectorsLogger() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;
        this.collectedObjects = 0;
        this.loggerFileName = "logs" + File.separator + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss").format(LocalDateTime.now()) + "_log_session.log";
    }

    public void onCollect(double red, double green, double blue) {

        System.out.println("Collecting rgb(" + red + ", " + green + ", " + blue + ")");

        this.red += red;
        this.green += green;
        this.blue += blue;
        this.collectedObjects++;

        // Logs information on every 20 objects collected
        if (collectedObjects != 0 && collectedObjects % 20 == 0) {
            try {
                log();
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }
        }
    }

    public void log() throws IOException {

        File logFile = new File(loggerFileName);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        String logMessage = "Collected boxes: " + collectedObjects / 3 + ", rgb(" + red / collectedObjects + ", " + green / collectedObjects + ", " + blue / collectedObjects + ")";
        try (FileWriter out = new FileWriter(logFile, true)) {
            out.write(logMessage);
            out.write("\n");
        }
    }
}
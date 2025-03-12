package com.lastpart.lastpart;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileOperation {
    private static final Logger logger = Logger.getLogger(FileOperation.class.getName());
    private String inputFileName;
    private String outputFileName;

    public FileOperation() {
        inputFileName = outputFileName = "players.txt";
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void readFromFile(Datahouse db) throws IOException {
        // Load players.txt from resources folder
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFileName);
        if (inputStream == null) {
            logger.severe("File not found: " + inputFileName);
            throw new FileNotFoundException("File not found in resources: " + inputFileName);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 5) {
                    logger.warning("Invalid line format: " + line + " - Skipping.");
                    continue;
                }

                try {
                    Player player = new Player();
                    player.setName(tokens[0].trim());
                    player.setCountry(tokens[1].trim());
                    player.setAge(!tokens[2].trim().isEmpty() ? Integer.parseInt(tokens[2].trim()) : -1);
                    player.setHeight(!tokens[3].trim().isEmpty() ? Double.parseDouble(tokens[3].trim()) : -1.0);
                    player.setClub(tokens[4].trim());
                    player.setPosition(tokens[5].trim());
                    player.setNumber(tokens.length > 6 && !tokens[6].trim().isEmpty() ? Integer.parseInt(tokens[6].trim()) : -1);
                    player.setSalary(tokens.length > 7 && !tokens[7].trim().isEmpty() ? Double.parseDouble(tokens[7].trim()) : 0.0);

                    int check = db.addPlayer(player);
                    if (check != 1) {
                        logger.warning("Failed to add player to Datahouse: " + tokens[0]);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    logger.warning("Error parsing line: " + line + " - " + e.getMessage());
                }
            }
        }
    }

    public void writeToFile(List<Player> players, boolean append) throws IOException {
        // Write output to players.txt in the working directory
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName, append))) {
            for (Player player : players) {
                bw.write(player.getName() + ",");
                bw.write(player.getCountry() + ",");
                bw.write(player.getAge() + ",");
                bw.write(player.getHeight() + ",");
                bw.write(player.getClub() + ",");
                bw.write(player.getPosition() + ",");
                bw.write(player.getNumber() + ",");
                bw.write(player.getSalary() + "");
                bw.newLine();
            }
            logger.info("Players data successfully written to file: " + outputFileName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to file: " + outputFileName, e);
            throw e;
        }
    }
}

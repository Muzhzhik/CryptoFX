package com.example.cryptofx.dao;

import com.example.cryptofx.utils.ConsoleColors;
import com.example.cryptofx.utils.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManagerDAO implements DataDAO{

    Logger logger = new Logger();

    @Override
    public String getData(String sourcePath) {
        String result = null;
        Path path = Path.of(sourcePath);
        if (Files.isRegularFile(path)) {
            byte[] bytes = new byte[0];
            try {
                bytes = Files.readAllBytes(path);
            } catch (IOException e) {
                logger.error("Cant read file\n");
            }
            result = new String(bytes);
        } else {
            logger.error("Cant find file\n");
        }
        return result;
    }

    @Override
    public void writeData(String sourcePath, String data) {
        try {
            Files.write(Path.of(sourcePath), data.getBytes());
        } catch (IOException e) {
            logger.error("Error: Cant write file");
        }
        logger.info("File created " + sourcePath, ConsoleColors.GREEN_BOLD_BRIGHT);
    }



    private String createDateString() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH-mm-ss");
        return dateTime.format(dateTimeFormatter);
    }
}
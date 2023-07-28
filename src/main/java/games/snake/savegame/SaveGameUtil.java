package games.snake.savegame;

import utils.Settings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveGameUtil {
    public static void saveDtoToFile(SavedGameDTO savedGameDTO) {
        String filename = Settings.SAVE_GAME_PATH + savedGameDTO.fileName;
        try (FileOutputStream fileOut = new FileOutputStream(filename); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(savedGameDTO);
            System.out.println("save game created");
        } catch (IOException exception) {
            System.out.println("Could not save save game." + exception);
        }
    }

    public static SavedGameDTO loadObjectFromFile(String filename) {
        try (FileInputStream fileIn = new FileInputStream(filename); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (SavedGameDTO) in.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Could not load saved game." + exception);
            return new SavedGameDTO();
        }
    }

    public static String getCurrentDateTimeAsString() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-HHmmss");
        return currentDateTime.format(formatter);
    }

    public static void saveSettingsToFile() {
        String filePath = Settings.VIDEO_BASE_PATH + getSaveGameDirectoryName() + "settings.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(settingsToString());
        } catch (IOException e) {
            System.err.println("Error while saving settings to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createSaveGameDirectory() {
        File directory = new File(Settings.VIDEO_BASE_PATH + getSaveGameDirectoryName());
        directory.mkdirs();
    }

    /**
     * Creates name of directory for movies based on current configuration.
     *
     * @return name of directory
     */
    public static String getSaveGameDirectoryName() {
        return "pl" + Settings.NUM_OF_PLAYERS + "_" +
                "sigh" + Settings.SNAKE_SIGHT + "_" +
                "dp" + Settings.DEATH_PENALTY + "_" +
                "crps" + getShortBool(Settings.LEAVE_CORPSE) + "_" +
                "wall" + getShortBool(Settings.HAS_WALL) + "_" +
                "coll" + getShortBool(Settings.SELF_COLLISION) + "_" +
                "ev" + getShortBool(Settings.IS_FREE_EVOLUTION) + "/";
    }

    private static String settingsToString() {
        return String.format("%-35s %s%n", "NUM_OF_PLAYERS", Settings.NUM_OF_PLAYERS) +
                String.format("%-35s %s%n", "NUM_OF_GENERATIONS", Settings.NUM_OF_GENERATIONS) +
                String.format("%-35s %s%n", "GRID_COLUMN_PIXELS", Settings.GRID_COLUMN_PIXELS) +
                String.format("%-35s %s%n", "GRID_ROW_PIXELS", Settings.GRID_ROW_PIXELS) +
                String.format("%-35s %s%n", "PIXELS_PER_SQUARE", Settings.PIXELS_PER_SQUARE) +
                String.format("%-35s %s%n", "SNAKE_SIGHT", Settings.SNAKE_SIGHT) +
                String.format("%-35s %s%n", "DEATH_PENALTY", Settings.DEATH_PENALTY) +
                String.format("%-35s %s%n", "MAX_NUM_OF_FOOD", Settings.MAX_NUM_OF_FOOD) +
                String.format("%-35s %s%n", "PROTECTED_AGE", Settings.PROTECTED_AGE) +
                String.format("%-35s %s%n", "FREQUENCY_OF_SPECIATION", Settings.FREQUENCY_OF_SPECIATION) +
                String.format("%-35s %s%n", "LEAVE_CORPSE", Settings.LEAVE_CORPSE) +
                String.format("%-35s %s%n", "HAS_WALL", Settings.HAS_WALL) +
                String.format("%-35s %s%n", "SELF_COLLISION", Settings.SELF_COLLISION) +
                String.format("%-35s %s%n", "SAVE_EVERY_N_GENERATIONS", Settings.SAVE_EVERY_N_GENERATIONS) +
                String.format("%-35s %s%n", "SNAKE_SIGHT_TYPE", Settings.SNAKE_SIGHT_TYPE) +
                String.format("%-35s %s%n", "CHANCE_ADD_NODE", Settings.CHANCE_ADD_NODE) +
                String.format("%-35s %s%n", "CHANCE_ADD_CONNECTION", Settings.CHANCE_ADD_CONNECTION) +
                String.format("%-35s %s%n", "CHANCE_SWITCH_CONNECTION_ENABLED", Settings.CHANCE_SWITCH_CONNECTION_ENABLED) +
                String.format("%-35s %s%n", "OFFSPRING_THRESHOLD", Settings.OFFSPRING_THRESHOLD) +
                String.format("%-35s %s%n", "STEPS_TO_REDUCTION", Settings.STEPS_TO_REDUCTION) +
                String.format("%-35s %s%n", "IS_FOOD_GUARANTEED", Settings.IS_FOOD_GUARANTEED) +
                String.format("%-35s %s%n", "MIN_PARENT_LENGTH_FOR_OFFSPRING", Settings.MIN_PARENT_LENGTH_FOR_OFFSPRING) +
                String.format("%-35s %s%n", "IS_FREE_EVOLUTION", Settings.IS_FREE_EVOLUTION);
    }

    /**
     * Shortens boolean values to single letter
     *
     * @param bool boolean value
     * @return "T" for True and "F" for False
     */
    private static String getShortBool(boolean bool) {
        if (bool)
            return "T";
        return "F";
    }
}

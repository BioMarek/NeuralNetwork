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
        return "NUM_OF_PLAYERS = " + Settings.NUM_OF_PLAYERS + "\n" +
                "GRID_COLUMN_PIXELS = " + Settings.GRID_COLUMN_PIXELS + "\n" +
                "GRID_ROW_PIXELS = " + Settings.GRID_ROW_PIXELS + "\n" +
                "PIXELS_PER_SQUARE = " + Settings.PIXELS_PER_SQUARE + "\n" +
                "SNAKE_SIGHT = " + Settings.SNAKE_SIGHT + "\n" +
                "DEATH_PENALTY = " + Settings.DEATH_PENALTY + "\n" +
                "MAX_NUM_OF_FOOD = " + Settings.MAX_NUM_OF_FOOD + "\n" +
                "LEAVE_CORPSE = " + Settings.LEAVE_CORPSE + "\n" +
                "HAS_WALL = " + Settings.HAS_WALL + "\n" +
                "SELF_COLLISION = " + Settings.SELF_COLLISION + "\n" +
                "SAVE_EVERY_N_GENERATIONS = " + Settings.SAVE_EVERY_N_GENERATIONS + "\n" +
                "SNAKE_SIGHT_TYPE = " + Settings.SNAKE_SIGHT_TYPE + "\n" +
                "CHANCE_ADD_NODE = " + Settings.CHANCE_ADD_NODE + "\n" +
                "CHANCE_ADD_CONNECTION = " + Settings.CHANCE_ADD_CONNECTION + "\n" +
                "OFFSPRING_THRESHOLD = " + Settings.OFFSPRING_THRESHOLD + "\n" +
                "SNAKE_SIGHT_TYPE = " + Settings.SNAKE_SIGHT_TYPE + "\n" +
                "STEPS_TO_REDUCTION = " + Settings.STEPS_TO_REDUCTION + "\n" +
                "IS_FOOD_GUARANTEED = " + Settings.IS_FOOD_GUARANTEED + "\n" +
                "IS_FREE_EVOLUTION = " + Settings.IS_FREE_EVOLUTION;
    }

    /**
     * Shortes boolean values to single letter
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

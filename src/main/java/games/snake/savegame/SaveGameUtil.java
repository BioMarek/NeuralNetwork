package games.snake.savegame;

import utils.Settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveGameUtil {
    public static void saveObjectToFile(SavedGameDTO savedGameDTO) {
        String filename = Settings.SAVE_GAME_PATH + "savegame.sav";
        try (FileOutputStream fileOut = new FileOutputStream(filename); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(savedGameDTO);
            System.out.println("save game created");
        } catch (IOException e) {
            System.out.println("Could not save save game.");
        }
    }

    public static SavedGameDTO loadObjectFromFile(String filename) {
        try (FileInputStream fileIn = new FileInputStream(filename); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (SavedGameDTO) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load saved game.");
            return new SavedGameDTO();
        }
    }
}

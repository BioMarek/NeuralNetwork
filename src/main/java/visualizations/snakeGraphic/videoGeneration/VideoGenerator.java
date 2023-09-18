package visualizations.snakeGraphic.videoGeneration;

import games.snake.savegame.SaveGameUtil;
import games.snake.savegame.SavedGameDTO;
import org.jcodec.api.SequenceEncoder;
import org.jcodec.common.Codec;
import org.jcodec.common.Format;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;
import org.jcodec.scale.AWTUtil;
import utils.Settings;
import visualizations.snakeGraphic.GridVisualization;
import visualizations.snakeGraphic.SnakeVisualization;
import visualizations.snakeGraphic.explanations.SnakeIntroduction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VideoGenerator {
    private GridVisualization gridVisualization;

    public void generateSavedGameVideo(String filename) {
        var path = Settings.SAVE_GAME_PATH + filename;
        var savedGameDTO = SaveGameUtil.loadSaveGameFromFile(path);
        gridVisualization = new SnakeVisualization(savedGameDTO);
        createMP4();
    }

    public void generateSavedGameVideo(SavedGameDTO savedGameDTO) {
        gridVisualization = new SnakeVisualization(savedGameDTO);
        SaveGameUtil.createSaveGameDirectory();
        SaveGameUtil.saveSettingsToFile();
        createMP4();
    }

    public void generateSnakeIntroduction() {
        gridVisualization = new SnakeIntroduction();
        createMP4();
    }

    /**
     * Generates *.mp4 from current {@link games.snake.savegame.SavedGameDTO}.
     */
    private void createMP4() {
        ImageIterator imageIterator = new ImageIterator(gridVisualization);
        try {
            SequenceEncoder encoder = new SequenceEncoder(NIOUtils.writableChannel(new File(Settings.VIDEO_BASE_PATH + Settings.HASH + "_" + SaveGameUtil.getSaveGameDirectoryName() + gridVisualization.name().replace(".sav", "") + ".mp4")),
                    Rational.R(Settings.VIDEO_FPS, 1), Format.MOV, Codec.PNG, null);
            while (imageIterator.hasNext()) {
                encoder.encodeNativeFrame(AWTUtil.fromBufferedImageRGB(imageIterator.next()));
            }
            for (int i = 0; i < Settings.VIDEO_REPEAT_LAST_FRAME; i++) {
                encoder.encodeNativeFrame(AWTUtil.fromBufferedImageRGB(imageIterator.last()));
            }
            encoder.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * For debugging.
     *
     * @param image    to save
     * @param filePath where to save image
     */
    public static void saveAsJpg(BufferedImage image, String filePath) {
        try {
            File outputImage = new File(filePath + ".jpg");
            ImageIO.write(image, "jpg", outputImage);
        } catch (IOException e) {
            System.err.println("Error saving the image: " + e.getMessage());
        }
    }
}

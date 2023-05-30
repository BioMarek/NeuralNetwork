package visualizations.snakeGraphic.videoGeneration;

import games.snake.savegame.SaveGameUtil;
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

import java.io.File;
import java.io.IOException;

public class VideoGenerator {
    private GridVisualization gridVisualization;

    public void generateSavedGameVideo(String filename) {
        var path = Settings.SAVE_GAME_PATH + filename;
        var savedGameDTO = SaveGameUtil.loadObjectFromFile(path);
        gridVisualization = new SnakeVisualization(savedGameDTO);
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
            SequenceEncoder encoder = new SequenceEncoder(NIOUtils.writableChannel(new File(Settings.VIDEO_BASE_PATH + gridVisualization.name().replace(".sav", "") + ".mp4")),
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
}

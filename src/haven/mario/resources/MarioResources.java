package haven.mario.resources;

import haven.Debug;
import haven.Resource;
import haven.Tex;
import haven.TexI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MarioResources {
    private static final Path THEME_DIRECTORY =
            Path.of("themes", "default");

    private MarioResources() {
    }

    public static Tex loadThemeTex(String name) {
        Path imagePath = Path.of(
                "themes",
                "default",
                name + ".png"
        ).toAbsolutePath();

        Path logPath = Path.of("mario-theme-debug.txt").toAbsolutePath();

        try {
            Files.writeString(
                    logPath,
                    "Requested texture: " + name + System.lineSeparator() +
                            "PNG path: " + imagePath + System.lineSeparator() +
                            "PNG exists: " + Files.isRegularFile(imagePath) + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
            );

            if (Files.isRegularFile(imagePath)) {
                BufferedImage image = ImageIO.read(imagePath.toFile());

                if (image == null) {
                    Files.writeString(
                            logPath,
                            "ImageIO could not decode the PNG." + System.lineSeparator(),
                            java.nio.file.StandardOpenOption.CREATE,
                            java.nio.file.StandardOpenOption.APPEND
                    );
                } else {
                    Tex original = Resource.loadtex(name);

                    Files.writeString(
                            logPath,
                            "PNG loaded successfully." + System.lineSeparator() +
                                    "Custom PNG Size : " +
                                    image.getWidth() + " x " + image.getHeight() +
                                    System.lineSeparator() +
                                    "Original Tex Size: " +
                                    original.sz().x + " x " + original.sz().y +
                                    System.lineSeparator(),
                            java.nio.file.StandardOpenOption.CREATE,
                            java.nio.file.StandardOpenOption.APPEND
                    );

                    return new TexI(image);
                }
            }

            Files.writeString(
                    logPath,
                    "Falling back to Haven resource." +
                            System.lineSeparator() +
                            System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            try {
                Files.writeString(
                        logPath,
                        "ERROR: " + e +
                                System.lineSeparator() +
                                System.lineSeparator(),
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND
                );
            } catch (IOException ignored) {
            }
        }

        return Resource.loadtex(name);
    }

}

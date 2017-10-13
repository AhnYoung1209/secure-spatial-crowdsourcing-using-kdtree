import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/8/5 16:21
 */
public class FileUtils {
    public static Set<Point2D> generatePointSet(String fileName, int limit) {
        Stream<String> stream;
        try {
            stream = Files.lines(Paths.get(fileName));
            return stream.map(s -> {
                String[] strs = s.split("\t");
                return new Point2D(strs[0], strs[1], strs[2]);
            }).limit(limit).collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<Point2D> generatePointSet(String fileName) {
        Stream<String> stream;
        try {
            stream = Files.lines(Paths.get(fileName));
            return stream.map(s -> {
                String[] strs = s.split("\t");
                return new Point2D(strs[0], strs[1], strs[2]);
            }).collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

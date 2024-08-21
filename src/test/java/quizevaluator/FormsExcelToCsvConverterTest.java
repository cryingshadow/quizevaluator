package quizevaluator;

import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.testng.Assert.*;

public class FormsExcelToCsvConverterTest {

    @Test
    public void testConvert() throws IOException {
        String quizAuthor = "Hans Mustermann";
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(quizAuthor + "_ Topic of Talk(1-10).xlsx");
        File excelFile = createFileFromInputStream(inputStream, quizAuthor + "_", ".xlsx");

        File targetFile = new FormsExcelToCsvConverter().convert(excelFile);

        List<String> lines = Files.readAllLines(targetFile.toPath());
        assertEquals(lines.get(0), quizAuthor);
        assertEquals(lines.get(1), "First Name; 1:b, 2:b, 3:b, 4:, 5:c, 6:c, 7:b, 8:d, 9:d, 10:b");
    }

    private static File createFileFromInputStream(InputStream inputStream, String prefix, String suffix) throws IOException {
        File tempFile = Files.createTempFile(prefix, suffix).toFile();
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) != -1) {
                out.write(buffer);
            }
        }
        return tempFile;
    }

}
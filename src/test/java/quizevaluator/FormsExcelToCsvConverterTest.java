package quizevaluator;

import static org.testng.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.testng.annotations.*;

public class FormsExcelToCsvConverterTest {

    @Test
    public void testConvert() throws IOException {
        final String quizAuthor = "Hans Mustermann";
        final InputStream inputStream =
            this.getClass().getClassLoader().getResourceAsStream(quizAuthor + "_ Topic of Talk(1-10).xlsx");
        final File excelFile = createFileFromInputStream(inputStream, quizAuthor + "_", ".xlsx");
        final File targetFile = FormsExcelToCsvConverter.convertToCSV(excelFile);
        final List<String> lines = Files.readAllLines(targetFile.toPath());
        assertEquals(lines.get(0), quizAuthor);
        assertEquals(lines.get(1), "First Name; 1:b, 2:b, 3:b, 4:, 5:c, 6:c, 7:b, 8:d, 9:d, 10:b");
    }

    private static File createFileFromInputStream(
        final InputStream inputStream,
        final String prefix,
        final String suffix
    ) throws IOException {
        File tempFile = Files.createTempFile(prefix, suffix).toFile();
        tempFile.deleteOnExit();
        try (final FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) != -1) {
                out.write(buffer);
            }
        }
        return tempFile;
    }

}
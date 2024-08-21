package quizevaluator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FormsExcelToCsvConverter {

    private static final int PARTICIPANT_COLUMN = 4;
    private static final int ANSWER_COLUMNS_OFFSET = 6;
    private static final int NR_OF_ANSWER_COLUMNS = 10;

    public FormsExcelToCsvConverter() {
    }

    public boolean isExcelFile(File file) {
        return file.getName().endsWith(".xlsx");
    }

    /**
     * Converts an Excel file as can be downloaded from a MS Forms quiz into our answer file format.
     * The file's filename must begin with the author of the quiz - the easiest way to achieve this is
     * by letting the form have a name such as "Christian Soltenborn: The Topic of my Talk" - if the
     * form's Excel file is downloaded, it will have the correct file name.
     *
     * @param excelFile
     * @return
     * @throws IOException
     */
    public File convert(File excelFile) throws IOException {
        String filename = excelFile.getName();
        String quizAuthor = filename.substring(0, filename.indexOf('_'));

        File targetFile = File.createTempFile(quizAuthor, ".csv");
        targetFile.deleteOnExit();

        List<String> lines = new ArrayList<>();
        lines.add(quizAuthor);

        FileInputStream fileInputStream = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int rowNumber = 1; rowNumber < sheet.getPhysicalNumberOfRows(); rowNumber++) {
            Row row = sheet.getRow(rowNumber);
            String participant = row.getCell(PARTICIPANT_COLUMN).getStringCellValue();

            StringBuilder line = new StringBuilder(participant);
            line.append("; ");
            line.append(String.join(", ", getAnswers(row)));

            lines.add(line.toString());
        }

        workbook.close();
        fileInputStream.close();

        Files.write(Path.of(targetFile.toURI()), lines);
        return targetFile;
    }

    private List<String> getAnswers(Row row) {
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < NR_OF_ANSWER_COLUMNS; i++) {
            int column = i + ANSWER_COLUMNS_OFFSET;
            Cell cell = row.getCell(column);
            String answer = (i + 1) + ":";
            if (cell != null) {
                String cellValue = cell.getStringCellValue().trim();
                if (!cellValue.isBlank()) {
                    answer += String.valueOf(cellValue.charAt(0)).toLowerCase();
                }
            }
            answers.add(answer);
        }
        return answers;
    }
}
package quizevaluator;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class FormsExcelToCsvConverter {

    private static final int ANSWER_COLUMNS_OFFSET = 6;

    private static final int NR_OF_ANSWER_COLUMNS = 10;

    private static final int PARTICIPANT_COLUMN = 4;

    /**
     * Converts an Excel file as can be downloaded from a MS Forms quiz into our answer file format.
     * The file's filename must begin with the author of the quiz - the easiest way to achieve this is
     * by letting the form have a name such as "Christian Soltenborn: The Topic of my Talk" - if the
     * form's Excel file is downloaded, it will have the correct file name.
     *
     * @param formsResultsExcelFile
     * @return
     * @throws IOException
     */
    public static File convertToCSV(final File formsResultsExcelFile) throws IOException {
        final String quizAuthor = FormsExcelToCsvConverter.extractQuizAuthor(formsResultsExcelFile);
        final File targetFile = File.createTempFile(quizAuthor, ".csv");
        targetFile.deleteOnExit();
        final List<String> lines = new ArrayList<>();
        lines.add(quizAuthor);
        try (final Workbook workbook = new XSSFWorkbook(new FileInputStream(formsResultsExcelFile))) {
            final Sheet sheet = workbook.getSheetAt(0);
            for (int rowNumber = 1; rowNumber < sheet.getPhysicalNumberOfRows(); rowNumber++) {
                lines.add(FormsExcelToCsvConverter.convertAnswerRow(sheet.getRow(rowNumber)));
            }
        }
        Files.write(targetFile.toPath(), lines);
        return targetFile;
    }

    public static boolean isExcelFile(final File file) {
        return file.getName().endsWith(".xlsx");
    }

    private static String convertAnswerRow(final Row row) {
        final String participant = row.getCell(FormsExcelToCsvConverter.PARTICIPANT_COLUMN).getStringCellValue();
        final StringBuilder line = new StringBuilder(participant);
        line.append("; ");
        line.append(String.join(", ", FormsExcelToCsvConverter.getAnswers(row)));
        return line.toString();
    }

    private static String extractQuizAuthor(final File formsResultsExcelFile) throws IOException {
        final String filename = formsResultsExcelFile.getName();
        final Pattern defaultFormsNamePattern = Pattern.compile("Multiple-Choice-Quiz zum Thema ((\\w|\\s)+)\\(.*");
        final Matcher defaultFormsNameMatcher = defaultFormsNamePattern.matcher(filename);
        if (defaultFormsNameMatcher.matches()) {
            return FormsExcelToCsvConverter.findQuizAuthorByTopic(
                defaultFormsNameMatcher.group(1),
                formsResultsExcelFile
            );
        } else if (filename.matches("[^_]+_.+")) {
            return filename.substring(0, filename.indexOf('_'));
        }
        throw new IllegalArgumentException("File name does not match expected pattern!");
    }

    private static String findQuizAuthorByTopic(
        final String topic,
        final File formsResultsExcelFile
    ) throws IOException {
        final Pattern assignmentPattern = Pattern.compile("([^>]+) -> \\(\\d+\\) (.+)");
        return
            Files.lines(
                formsResultsExcelFile.getAbsoluteFile().toPath().getParent().getParent().resolve("assignment.txt")
            ).map(line -> {
                final Matcher matcher = assignmentPattern.matcher(line);
                if (matcher.matches() && matcher.group(2).trim().equals(topic)) {
                    return matcher.group(1);
                }
                return "";
            }).filter(line -> !line.isBlank())
            .findAny()
            .get();
    }

    private static List<String> getAnswers(final Row row) {
        final List<String> answers = new ArrayList<>();
        for (int i = 0; i < FormsExcelToCsvConverter.NR_OF_ANSWER_COLUMNS; i++) {
            final int column = i + FormsExcelToCsvConverter.ANSWER_COLUMNS_OFFSET;
            final Cell cell = row.getCell(column);
            String answer = (i + 1) + ":";
            if (cell != null) {
                final String cellValue = cell.getStringCellValue().trim();
                if (!cellValue.isBlank()) {
                    answer += String.valueOf(cellValue.charAt(0)).toLowerCase();
                }
            }
            answers.add(answer);
        }
        return answers;
    }
}
package quizevaluator;

import java.io.*;
import java.util.*;

import org.testng.*;
import org.testng.annotations.Test;

class MainTest {

    @Test
    void outputTest() throws IOException {
        final AnswerDataByQuizMasterAndParticipant answers = new AnswerDataByQuizMasterAndParticipant();
        SolutionsByQuizMaster solutionsByQuizMaster;
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.SOLUTIONS))) {
            solutionsByQuizMaster = new SolutionsByQuizMaster(reader);
        }
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER2))) {
            answers.parseAnswers(reader, solutionsByQuizMaster);
        }
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER3))) {
            answers.parseAnswers(reader, solutionsByQuizMaster);
        }
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER1))) {
            answers.parseAnswers(reader, solutionsByQuizMaster);
        }
        final StringWriter output = new StringWriter();
        try (BufferedWriter writer = new BufferedWriter(output)) {
            final ResultsByQuizMasterAndParticipant results =
                new ResultsByQuizMasterAndParticipant(
                    solutionsByQuizMaster,
                    answers,
                    new OldSchoolMCResultComputation()
                );
            new CSVWriter(writer).writeCSV(
                results,
                Main.EVALUATIONS_OLD,
                Collections.emptyMap(),
                Collections.emptyList()
            );
        }
        Assert.assertEquals(
            output.toString(),
            ";x;y;z;Punkte gesamt;Punkte Prozent;Bestanden gesamt;Bestanden Prozent;Gut bestanden gesamt;Gut bestanden Prozent;Bonuspunkte"
            + System.lineSeparator()
            + "x;;10;8;18;90,00;2;100,00;2;100,00;3"
            + System.lineSeparator()
            + "y;2;;10;12;60,00;1;50,00;1;50,00;1"
            + System.lineSeparator()
            + "z;0;0;;0;0,00;0;0,00;0;0,00;0"
            + System.lineSeparator()
            + "Punkte gesamt;2;10;18"
            + System.lineSeparator()
            + "Punkte Prozent;10,00;50,00;90,00"
            + System.lineSeparator()
            + "Bestanden gesamt;0;1;2"
            + System.lineSeparator()
            + "Bestanden Prozent;0,00;50,00;100,00"
            + System.lineSeparator()
            + "Bestanden Bewertung;0;6;10"
            + System.lineSeparator()
            + "Gut bestanden gesamt;0;1;2"
            + System.lineSeparator()
            + "Gut bestanden Prozent;0,00;50,00;100,00"
            + System.lineSeparator()
            + "Bonus;0;0;3"
            + System.lineSeparator()
            + System.lineSeparator()
            + "Bewertung;3;7;13"
            + System.lineSeparator()
        );
    }

}

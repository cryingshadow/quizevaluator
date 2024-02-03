package quizevaluator;

import java.io.*;

import org.testng.*;
import org.testng.annotations.Test;

class ResultsByQuizMasterAndParticipantTest {

    @Test
    void outputTest() throws IOException {
        final AnswersByQuizMasterAndParticipant answers = new AnswersByQuizMasterAndParticipant();
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER2))) {
            answers.parseAnswers(reader, "dummyfile");
        }
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER3))) {
            answers.parseAnswers(reader, "dummyfile");
        }
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER1))) {
            answers.parseAnswers(reader, "dummyfile");
        }
        final StringWriter output = new StringWriter();
        try (
            BufferedReader reader = new BufferedReader(new StringReader(Data.SOLUTIONS));
            BufferedWriter writer = new BufferedWriter(output);
        ) {
            final ResultsByQuizMasterAndParticipant results =
                new ResultsByQuizMasterAndParticipant(new SolutionsByQuizMaster(reader), answers);
            results.output(writer);
        }
        Assert.assertEquals(
            output.toString(),
            ";y;z;x;Punkte gesamt;Punkte Prozent;Bestanden gesamt;Bestanden Prozent;Gut bestanden gesamt;Gut bestanden Prozent;Bonuspunkte"
            + System.lineSeparator()
            + "y;;10;2;12;60,00;1;50,00;1;50,00;1"
            + System.lineSeparator()
            + "z;0;;0;0;0,00;0;0,00;0;0,00;0"
            + System.lineSeparator()
            + "x;10;8;;18;90,00;2;100,00;2;100,00;3"
            + System.lineSeparator()
            + "Punkte gesamt;10;18;2"
            + System.lineSeparator()
            + "Punkte Prozent;50,00;90,00;10,00"
            + System.lineSeparator()
            + "Bestanden gesamt;1;2;0"
            + System.lineSeparator()
            + "Bestanden Prozent;50,00;100,00;0,00"
            + System.lineSeparator()
            + "Bestanden Bewertung;6;10;0"
            + System.lineSeparator()
            + "Gut bestanden gesamt;1;2;0"
            + System.lineSeparator()
            + "Gut bestanden Prozent;50,00;100,00;0,00"
            + System.lineSeparator()
            + "Bonus;0;3;0"
            + System.lineSeparator()
            + System.lineSeparator()
            + "Bewertung;7;13;3"
            + System.lineSeparator()
        );
    }

}

package quizevaluator;

import java.io.*;
import java.util.*;

import org.testng.*;
import org.testng.Assert.*;
import org.testng.annotations.Test;

class AnswersByQuizMasterAndParticipantTest {

    @Test
    void parseAnswersTest() throws IOException {
        final AnswersByQuizMasterAndParticipant answers = new AnswersByQuizMasterAndParticipant();
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER1))) {
            answers.parseAnswers(reader, "dummyfile");
        }
        Assert.assertEquals(answers.size(), 1);
        Assert.assertTrue(answers.containsKey("x"));
        final Map<String, Map<Integer, Integer>> answersForX = answers.get("x");
        Assert.assertEquals(answersForX.size(), 2);
        Assert.assertTrue(answersForX.containsKey("y"));
        Assert.assertTrue(answersForX.containsKey("z"));
        final Map<Integer, Integer> answersForXFromZ = answersForX.get("z");
        Assert.assertEquals(answersForXFromZ.size(), 2);
        Assert.assertTrue(answersForXFromZ.containsKey(Integer.valueOf(4)));
        Assert.assertTrue(answersForXFromZ.containsKey(Integer.valueOf(1)));
        Assert.assertEquals(answersForXFromZ.get(Integer.valueOf(4)), Integer.valueOf(0));
        Assert.assertEquals(answersForX.get("y").get(Integer.valueOf(5)), Integer.valueOf(3));
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.ANSWER2))) {
            answers.parseAnswers(reader, "dummyfile");
        }
        Assert.assertEquals(answers.size(), 2);
        Assert.assertTrue(answers.containsKey("x"));
        Assert.assertTrue(answers.containsKey("y"));
        Assert.assertEquals(answers.get("x").get("y").get(Integer.valueOf(5)), Integer.valueOf(3));
        Assert.assertEquals(answers.get("y").get("z").get(Integer.valueOf(3)), Integer.valueOf(2));
    }

    @Test
    void parseEmptyAnswersTest() throws IOException {
        final AnswersByQuizMasterAndParticipant answers = new AnswersByQuizMasterAndParticipant();
        try (BufferedReader reader = new BufferedReader(new StringReader(""))) {
            Assert.assertThrows(new ThrowingRunnable () {

                @Override
                public void run() throws IOException {
                    answers.parseAnswers(reader, "dummyfile");
                }

            });
        }
    }

}

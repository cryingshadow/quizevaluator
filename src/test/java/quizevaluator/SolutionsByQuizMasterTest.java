/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package quizevaluator;

import java.io.*;
import java.util.*;

import org.testng.*;
import org.testng.annotations.*;

class SolutionsByQuizMasterTest {

    @Test
    void parseEmptyText() throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(""))) {
            final SolutionsByQuizMaster result = new SolutionsByQuizMaster(reader);
            Assert.assertTrue(result.isEmpty());
        }
    }

    @Test
    void parseSolutions() throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(Data.SOLUTIONS))) {
            final SolutionsByQuizMaster result = new SolutionsByQuizMaster(reader);
            Assert.assertEquals(result.size(), 3);
            Assert.assertTrue(result.containsKey("x"));
            Assert.assertTrue(result.containsKey("y"));
            Assert.assertTrue(result.containsKey("z"));
            final Map<Integer, Integer> solution = result.get("y");
            Assert.assertEquals(solution.size(), 10);
            final int[] expectedSolution = new int[] {0,0,0,1,1,1,1,3,3,2};
            for (int i = 1; i <= 10; i++) {
                final Integer key = Integer.valueOf(i);
                Assert.assertTrue(solution.containsKey(key));
                Assert.assertEquals(solution.get(key), Integer.valueOf(expectedSolution[i - 1]));
            }
        }
    }

}

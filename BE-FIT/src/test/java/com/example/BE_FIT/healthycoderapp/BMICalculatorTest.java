package com.example.BE_FIT.healthycoderapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class BMICalculatorTest {

    private String environment = "DEV";


    @BeforeAll // invoke this method before all unit tests, exactly once.
    static void printUnitTestingStarts() {
        System.out.println("Unit testing starts for class DietPlanner");
        // we can use it in something like setting up DB connection, start servers, ..
    }

    @AfterAll // invoke this method after all tests finished, exactly once.
    static void printAllDone() {
        System.out.println("All unit tests finished");
        // we can use it in something like close DB connection, stop server, ..
    }

    @Nested
    class IsDietRecommendedTest {

        @ParameterizedTest
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void Should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight) {

            // given
            double weight = coderWeight;
            double height = coderHeight;

            // when
            boolean dietRecommended = BMICalculator.isDietRecommended(weight, height);

            // then
            assertTrue(dietRecommended);
        }

        @ParameterizedTest(name = "weight= {0}, height= {1}") // show this values in developer-friendly way
        @CsvSource(value = {"60.0 , 1.6", "70.0, 1.9", "76.5, 1.8"})
        void Should_ReturnFalse_When_DietNotRecommended(Double coderWeight, Double coderHeight) {

            // given
            double weight = coderWeight;
            double height = coderHeight;

            // when
            boolean dietRecommended = BMICalculator.isDietRecommended(weight, height);

            // then
            assertFalse(dietRecommended);
        }

        @Test
        void Should_ThrowArithmeticException_When_HeightZero() {

            // given
            double weight = 65;
            double height = 0;

            // when
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            // then
            assertThrows(ArithmeticException.class, executable);
        }
    }

    @Nested
    class findCoderWithWorstBMITest {

        @Test // simple performance unit test.
        @DisplayName(">> Performance Test") // to customize the name of your unit test in the output.
        @Disabled // to disable this test, not fail, not pass, and not ignored. just disabled
        void Should_ReturnWorstBMICoderIn1MS_When_CodersListHas10000Element() {

            //given
            assumeTrue(BMICalculatorTest.this.environment.equals("PROD")); // run this test only if the environment is PRODUCTION, else it will be ignored, unlike assert()

            List<Coder> coders = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }

            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertTimeout(Duration.ofMillis(100), executable);
        }

        @Test
        void Should_ReturnWorstBMICoder_When_CodersListNotEmpty() {

            //given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 120.0));
            coders.add(new Coder(1.60, 65));
            coders.add(new Coder(1.90, 90.0));

            //when
            Coder coderWithWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertAll(
                    () -> assertEquals(1.80, coderWithWorstBMI.getHeight()),
                    () -> assertEquals(120.0, coderWithWorstBMI.getWeight())
            );
        }

        @Test
        void Should_ReturnNullWorstBMICoder_When_CodersListEmpty() {

            // given
            List<Coder> coders = new ArrayList<>();

            // when
            Coder coderWithWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertNull(coderWithWorstBMI);
        }
    }

    @Nested
    class GetBMIScoresTest {

        @Test
        void Should_ReturnCorrectBMIScoresArray_When_CoderListNotEmpty() {

            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));
            double[] expectedValues = new double[]{18.52, 29.59, 19.53};

            // when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            // then
            assertArrayEquals(expectedValues, bmiScores);
        }
    }

}

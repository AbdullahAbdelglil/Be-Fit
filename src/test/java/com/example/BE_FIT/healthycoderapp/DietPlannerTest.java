package com.example.BE_FIT.healthycoderapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
public class DietPlannerTest {

    private DietPlanner dietPlanner;

    @BeforeEach // invoke this method before each test
    void setup() {
        // A new instance will be created before each unit test
        dietPlanner = new DietPlanner(30, 30, 40);
    }

    @AfterEach // invoke this method after each test
    void printMsg() {
        System.out.println("A unit test is finished");
    }

    @ParameterizedTest
    @CsvSource(value = {"40, 40, 30", "30, 30, 30"})
    void Should_ThrowRuntimeException_When_TotalPercentageNotEqualHundred(Integer protein, Integer fat, Integer carb) {

        // given
        int proteinPercentage = protein;
        int fatPercentage = fat;
        int carbPercentage = carb;

        // when
        Executable executable = () -> new DietPlanner(proteinPercentage, fatPercentage, carbPercentage);

        // then
        assertThrows(RuntimeException.class, executable);
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    // repeat this test 5 times. each repetition is treated as a separate unit test.
    // WARN: use repeated test only when makes sense, like: your method generates random nums, change its state in each run, and so on.
    void Should_Return_CorrectDietPlan_When_CorrectCoder() {

        // given
        Coder coder = new Coder(1.6, 65, 22, Gender.MALE);
        DietPlan expectedPlan = new DietPlan(1937, 145, 65, 194);

        // when
        DietPlan actualPlan = dietPlanner.calculateDiet(coder);

        // then
        assertAll(
                () -> assertEquals(expectedPlan.getCalories(), actualPlan.getCalories()),
                () -> assertEquals(expectedPlan.getProtein(), actualPlan.getProtein()),
                () -> assertEquals(expectedPlan.getFat(), actualPlan.getFat()),
                () -> assertEquals(expectedPlan.getCarbohydrate(), actualPlan.getCarbohydrate())
        );
    }
}

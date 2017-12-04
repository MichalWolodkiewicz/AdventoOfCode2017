package com.quipu.advent_of_code;

class Day3 {

    Day3() {
        System.out.println("Day 3");
    }

    Day3 runPart1() {
        int numberOfSteps = findNumberOfSteps(277678);
        System.out.println(String.format("Number of steps = %d", numberOfSteps));
        return this;
    }

    Day3 runPart2() {
        int result = new SquareSpiralMatrix().findFirstBiggerThan(12, 277678);
        System.out.println("First bigger than " + 277678 + " is " + result);
        return this;
    }

    Day3 runPart1Test() {
        runPart1Test(1, 0);
        runPart1Test(12, 3);
        runPart1Test(23, 2);
        runPart1Test(1024, 31);
        System.out.println("Test success!");
        return this;
    }

    private void runPart1Test(int input, int expected) {
        int numberOfSteps = findNumberOfSteps(input);
        if (numberOfSteps != expected) {
            throw new AssertionError(String.format("Wrong number of steps. Expected %d, actual %d", expected, input));
        }
    }

    Day3 runPart2Test() {
        return this;
    }

    private int findNumberOfSteps(int from) {
        if (from == 1) {
            return 0;
        }
        return new SquareSpiralMatrix().findStepsToNumberNumber(from);
    }
}

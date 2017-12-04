package com.quipu.advent_of_code;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.LongAdder;

class SquareSpiralMatrix {
    private enum DIRECTION {LEFT, RIGHT, UP, DOWN}

    private int[][] matrix;

    private int matrixOriginX;
    private int matrixOriginY;

    SquareSpiralMatrix() {
    }

    private void initMatrix(int dimension) {
        matrix = new int[dimension][dimension];
        matrixOriginX = dimension / 2;
        matrixOriginY = dimension / 2;
        clearMatrix(dimension);
    }

    private void clearMatrix(int dimension) {
        for (int row = 0; row < dimension; row++) {
            matrix[row] = new int[dimension];
            for (int i = 0; i < dimension; i++) {
                matrix[row][i] = 0;
            }
        }
    }

    int findFirstBiggerThan(int dimension, int number) {
        initMatrix(dimension);
        Point actualPosition = new Point(matrixOriginX, matrixOriginY + 1);
        matrix[matrixOriginX][matrixOriginY] = 1;
        matrix[matrixOriginX][matrixOriginY + 1] = 1;
        int result = 0;
        int innerMatrixDimension = 3;
        Iterator<DIRECTION> route = createRoute(innerMatrixDimension);
        while (result < 1) {
            if (route.hasNext()) {
                actualPosition = translatePoint(actualPosition, route.next());
                int sum = calculateAdjacentSquaresSum(actualPosition.x, actualPosition.y);
                matrix[actualPosition.x][actualPosition.y] = sum;
                if (sum > number) {
                    result = sum;
                }
            } else {
                innerMatrixDimension += 2;
                actualPosition = new Point(actualPosition.x, actualPosition.y + 1);
                matrix[actualPosition.x][actualPosition.y] = calculateAdjacentSquaresSum(actualPosition.x,
                        actualPosition.y);
                route = createRoute(innerMatrixDimension);
            }
        }
        return result;
    }

    int findStepsToNumberNumber(int number) {
        Point actualPosition = new Point(matrixOriginX, matrixOriginY + 1);
        int actualNumber = 1;
        int innerMatrixDimension = 3;
        Iterator<DIRECTION> route = createRoute(innerMatrixDimension);
        while (true) {
            ++actualNumber;
            if (route.hasNext()) {
                actualPosition = translatePoint(actualPosition, route.next());
            } else {
                innerMatrixDimension += 2;
                actualPosition = new Point(actualPosition.x, actualPosition.y + 1);
                route = createRoute(innerMatrixDimension);
            }
            if (actualNumber == number - 1) {
                break;
            }
        }
        return Math.abs(actualPosition.x - matrixOriginX) + Math.abs(actualPosition.y - matrixOriginY);
    }

    private Iterator<DIRECTION> createRoute(int innerMatrixDimension) {
        Collection<DIRECTION> route = new ArrayList<>();
        for (int i = 0; i < innerMatrixDimension - 2; i++) {
            route.add(DIRECTION.UP);
        }
        for (int i = 0; i < innerMatrixDimension - 1; i++) {
            route.add(DIRECTION.LEFT);
        }
        for (int i = 0; i < innerMatrixDimension - 1; i++) {
            route.add(DIRECTION.DOWN);
        }
        for (int i = 0; i < innerMatrixDimension - 1; i++) {
            route.add(DIRECTION.RIGHT);
        }
        return route.iterator();
    }

    private Point translatePoint(Point point, DIRECTION dir) {
        Point p = new Point(point);
        switch (dir) {
            case LEFT:
                p.y -= 1;
                break;
            case RIGHT:
                p.y += 1;
                break;
            case UP:
                p.x -= 1;
                break;
            case DOWN:
                p.x += 1;
                break;
        }
        return p;
    }

    private int calculateAdjacentSquaresSum(int x, int y) {
        LongAdder adder = new LongAdder();
        adder.add(getValueFromMatrixOrZero(x + 1, y));
        adder.add(getValueFromMatrixOrZero(x + 1, y + 1));
        adder.add(getValueFromMatrixOrZero(x, y + 1));
        adder.add(getValueFromMatrixOrZero(x - 1, y + 1));
        adder.add(getValueFromMatrixOrZero(x - 1, y));
        adder.add(getValueFromMatrixOrZero(x - 1, y - 1));
        adder.add(getValueFromMatrixOrZero(x, y - 1));
        adder.add(getValueFromMatrixOrZero(x + 1, y - 1));
        return adder.intValue();
    }

    private long getValueFromMatrixOrZero(int x, int y) {
        if (x > -1 && x < matrix.length && y > -1 && y < matrix.length) {
            return matrix[x][y];
        }
        return 0;
    }

}

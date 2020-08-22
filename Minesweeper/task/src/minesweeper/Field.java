package minesweeper;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Field {
    private final int fieldSize = 9;
    private final char[][] field = new char[fieldSize][fieldSize];


    // useful constants
    final static char emptyCell = '.';
    final static char checkedEmptyCell = '/';
    final static char specialMark = '*';
    final static char mineCell = 'X';

    public Field() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {

                field[i][j] = emptyCell;
            }
        }
    }


    public char[][] getField() {
        return field;
    }

    void fillField(int minesNumber, int userRow, int userColumn) {

        Random randomRow = new Random();
        Random randomColumn = new Random();

        int minesCount = 0;
        while (minesCount < minesNumber) {

            int rowIndex = randomRow.nextInt(fieldSize);
            int columnIndex = randomColumn.nextInt(fieldSize);

            if (field[rowIndex][columnIndex] != mineCell && addMine(rowIndex, columnIndex, userRow, userColumn)) {
                minesCount++;
            } else {
                for (int i = rowIndex - 1; i <= rowIndex + 1; i++) {
                    for (int j = columnIndex - 1; j <= columnIndex + 1; j++) {

                        // check if in field and addMine = true
                        if  (i >= 0 && i < fieldSize && j >= 0 && j < fieldSize && field[rowIndex][columnIndex] != mineCell && addMine(i, j, userRow, userColumn)) {
                            minesCount++;
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean addMine(int rowIndex, int columnIndex, int userRow, int userColumn) {
        if (rowIndex != userRow && columnIndex != userColumn
                && rowIndex + 1 != userRow && rowIndex - 1 != userRow && columnIndex + 1 != userColumn
                && columnIndex - 1 != userColumn) {

            field[rowIndex][columnIndex] = mineCell;

            return true;
        }

        return false;
    }


    void printField() {
        System.out.println("\n |123456789|");
        System.out.println("—|—————————|");

        int i = 1;
        for (char[] symbols : field) {

            System.out.print(i + "|");

            for (char symbol : symbols) {
                System.out.print(symbol);
            }

            i++;
            System.out.println("|");
        }
        System.out.println("—|—————————|");
    }


    private int checkCellsAround(char[][] fieldWithMines, int indexRow, int indexColumn) {

        int mineCount = 0;


        for (int i = indexRow - 1; i <= indexRow + 1; i++) {
            for (int j = indexColumn - 1; j <= indexColumn + 1; j++) {

                // check if in field
                if (i >= 0 && i < fieldSize && j >= 0 && j < fieldSize) {

                    // check if it is mine and not itself
                    if (fieldWithMines[i][j] == mineCell && !(i == indexRow && j == indexColumn)) {

                        mineCount++;
                    }
                }
            }
        }

        return mineCount;
    }

    void addMark(int indexRow, int indexColumn) {

        field[indexRow][indexColumn] = specialMark;
    }

    void removeMark(int indexRow, int indexColumn) {

        field[indexRow][indexColumn] = emptyCell;
    }

    private void freeAroundCell(Queue<CellCoordinates> queueFreeCells, char[][] fieldWithMines) {

        if (queueFreeCells.peek() != null) {

            int indexRow = queueFreeCells.peek().indexRow;

            int indexColumn = queueFreeCells.peek().indexColumn;
            queueFreeCells.poll();

            for (int i = indexRow - 1; i <= indexRow + 1; i++) {
                for (int j = indexColumn - 1; j <= indexColumn + 1; j++) {

                    // check if in field
                    if (i >= 0 && i < fieldSize && j >= 0 && j < fieldSize) {

                        // check if it is empty or *
                        if ((field[i][j] == emptyCell || field[i][j] == specialMark) && fieldWithMines[i][j] != mineCell) {

                            int mineCount = checkCellsAround(fieldWithMines, i, j);

                            if (mineCount > 0) {
                                field[i][j] = Character.forDigit(mineCount, 10);
                            } else if (field[i][j] == emptyCell || field[i][j] == specialMark) {

                                field[i][j] = checkedEmptyCell;
                                queueFreeCells.offer(new CellCoordinates(i, j));
                            }

                        } else if ((field[i][j] == emptyCell || field[i][j] == specialMark) && fieldWithMines[i][j] == mineCell) {

                            field[i][j] = checkedEmptyCell;
                            queueFreeCells.offer(new CellCoordinates(i, j));
                        }
                    }
                }
            }
        }
    }

    private static class CellCoordinates {
        int indexRow;
        int indexColumn;

        public CellCoordinates(int indexRow, int indexColumn) {
            this.indexRow = indexRow;
            this.indexColumn = indexColumn;
        }
    }


    void freeCells(char[][] fieldWithMines, int indexRow, int indexColumn) {
        Queue<CellCoordinates> queueFreeCells = new ArrayDeque<>();


        int mineCount = checkCellsAround(fieldWithMines, indexRow, indexColumn);

        if (mineCount > 0) {
            field[indexRow][indexColumn] = Character.forDigit(mineCount, 10);
        } else if (field[indexColumn][indexColumn] != checkedEmptyCell) {
            queueFreeCells.offer(new CellCoordinates(indexRow, indexColumn));
        }

        while (!queueFreeCells.isEmpty()) {
            freeAroundCell(queueFreeCells, fieldWithMines);
        }
    }

    void bumMines(char[][] fieldWithMines) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (fieldWithMines[i][j] == mineCell) {
                    field[i][j] = mineCell;
                }
            }
        }
    }
}

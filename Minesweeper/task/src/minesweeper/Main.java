package minesweeper;

import java.util.Scanner;

import static minesweeper.Field.*;


public class Main {

    static int minesNumber = -1;


    public static void main(String[] args) {

        Field fieldWithMines = new Field();

        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");

        minesNumber = scanner.nextInt();


        // print empty
        fieldWithMines.printField();


        Field fieldUser = new Field();


        boolean endGame = false;

        boolean isFirstTime = true;

        while (!endGame) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");

            // array starts with 0, but coordinates with 1
            int indexColumn = scanner.nextInt() - 1;
            int indexRow = scanner.nextInt() - 1;


            String command = scanner.next();

            // to initialise
            if (command.contains("free") && isFirstTime) {
                isFirstTime = false;

                fieldWithMines.fillField(minesNumber, indexRow, indexColumn);
            }


            if (command.contains("free") && !isFirstTime) {
                endGame = freeCommand(fieldUser, fieldWithMines, indexRow, indexColumn);


            } else if (command.contains("mine") ) {

                endGame = mineCommand(fieldUser, fieldWithMines, indexRow, indexColumn);

            } else {
                System.out.println("Wrong input!");
            }
        }
    }


    private static boolean freeCommand(Field fieldUser, Field fieldWithMines, int indexRow, int indexColumn) {

        if (fieldWithMines.getField()[indexRow][indexColumn] != mineCell) {
            fieldUser.freeCells(fieldWithMines.getField(), indexRow, indexColumn);
        } else {

            fieldUser.bumMines(fieldWithMines.getField());

            // print
            fieldUser.printField();

            System.out.println("You stepped on a mine and failed!");
            return true;
        }

        // print
        fieldUser.printField();


        return false;
    }


    private static boolean mineCommand(Field fieldUser, Field fieldWithMines, int indexRow, int indexColumn) {

        boolean endGame = false;

        char currentSymbol = fieldUser.getField()[indexRow][indexColumn];

        if (currentSymbol == emptyCell) {
            fieldUser.addMark(indexRow, indexColumn);

            // print
            fieldUser.printField();

            // check if end
            endGame = isEndGame(fieldWithMines, fieldUser, minesNumber);
        } else if (currentSymbol == specialMark) {
            fieldUser.removeMark(indexRow, indexColumn);

            // print
            fieldUser.printField();

            // check if end
            endGame = isEndGame(fieldWithMines, fieldUser, minesNumber);
        } else {
            System.out.println("You can't put number here!");
        }

        return endGame;
    }


    private static boolean isEndGame(Field fieldWihMines, Field fieldUser, int minesNumber) {

        int minesCount = 0;


        for (int i = 0; i < fieldUser.getField().length; i++) {
            for (int j = 0; j < fieldUser.getField().length; j++) {

                if (fieldUser.getField()[i][j] == specialMark && fieldWihMines.getField()[i][j] == mineCell) {
                    minesCount++;
                }
            }
        }


        if (minesCount == minesNumber) {
            System.out.println("Congratulations! You found all mines!");
            return true;
        } else {
            return false;
        }
    }
}

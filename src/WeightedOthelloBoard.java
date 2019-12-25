import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class WeightedOthelloBoard implements IBoard {
    public int size;
    public char[][] boardGame;
    public int[][] boardRewards;
    public int cellsLeft;
    public char player;


    public WeightedOthelloBoard(String problem) {
        importInstance(problem);
        player = '1';
    }

    public WeightedOthelloBoard(int size, char player) {
        this.size = size;
        cellsLeft = size * size-4;
        this.player = player;
        createNewBoard();
        createNewRewardsBoard();
    }


    public WeightedOthelloBoard(int size, char[][] boardGame, int[][] boardRewards, int cellsLeft, char player) {
        this.size = size;
        this.cellsLeft = cellsLeft;
        this.player = player;
        this.boardGame = new char[size][size];
        this.boardRewards = new int[size][size];
        for(int row = 0; row < this.size; row++)
            for(int col = 0; col < this.size; col++) {
                this.boardGame[row][col] = boardGame[row][col];
                this.boardRewards[row][col] = boardRewards[row][col];
            }
    }


    public WeightedOthelloBoard(WeightedOthelloBoard toCopy) {
        this(toCopy.size, toCopy.boardGame, toCopy.boardRewards, toCopy.cellsLeft, toCopy.player);
    }


    private void importInstance(String problemName) {
        // Get the file
        File file = new File(problemName);
        Scanner sc;
        try {
            sc = new Scanner(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Read the file
        while(sc.hasNextLine()) {
            String cuurentLine = sc.nextLine();
            if (cuurentLine.contains("Size:"))                    // Board size
            {
                cuurentLine = sc.nextLine();
                size = Integer.parseInt(cuurentLine);
                boardGame = new char[size][size];
                boardRewards = new int[size][size];
            } else if (cuurentLine.contains("Board:"))            // Board instance
            {
                for(int row = 0; row < size; row++) {
                    cuurentLine = sc.nextLine();
                    String[] tokens = cuurentLine.split("\\|");
                    for(int col = 0; col < size; col++) {
                        boardGame[row][col] = tokens[col].charAt(0);
                        if (boardGame[row][col] == '0')
                            cellsLeft++;
                    }
                }
            } else if (cuurentLine.contains("Rewards:"))            // Board rewards
            {
                for(int row = 0; row < size; row++) {
                    cuurentLine = sc.nextLine();
                    String[] tokens = cuurentLine.split("\\|");
                    for(int col = 0; col < size; col++) {
                        boardRewards[row][col] = Integer.parseInt(tokens[col]);
                    }
                }
            }
        }
        sc.close();
    }


    public IBoard copyBoard() {
        return new WeightedOthelloBoard(this);
    }


    private void createNewBoard() {
        boardGame = new char[size][size];
        for(int row = 0; row < size; row++)
            for(int col = 0; col < size; col++) {
                boardGame[row][col] = '0';
            }
        boardGame[(size / 2)-1][(size / 2)-1] = '1';
        boardGame[(size / 2)][(size / 2)] = '1';
        boardGame[(size / 2)-1][(size / 2)] = '2';
        boardGame[(size / 2)][(size / 2)-1] = '2';
    }


    private void createNewRewardsBoard() {
        boardRewards = new int[size][size];
        Random random = new Random();
        int[] randomNumbersArray = new int[size * size];
        int randomIndex;
        for(int i = 0; i < size * size; i++)
            randomNumbersArray[i] = i;
        int counter = 0;
        for(int row = 0; row < size; row++)
            for(int col = 0; col < size; col++) {
                randomIndex = random.nextInt(size * size-counter);
                boardRewards[row][col] = randomNumbersArray[randomIndex];
                randomNumbersArray[randomIndex] = randomNumbersArray[size * size-counter-1];
                counter++;
            }
    }

    public IBoard getNewChildBoard(IMove move) {
        if (move instanceof WeightedOthelloMove) {
            WeightedOthelloBoard childBoard = new WeightedOthelloBoard(this);
            childBoard.executePlayersMove(move);
            childBoard.player = this.getNextPlayer();
            if (childBoard.getLegalMoves().size() == 0) {
                childBoard.player = childBoard.getNextPlayer();
            }
            if (childBoard.getLegalMoves().size() == 0) {
                childBoard.player = 'n';
            }
            return childBoard;
        }
        return null;
    }


    @Override
    public boolean equals(Object otherBoard) {
        return this.hashCode() == otherBoard.hashCode();
    }


    @Override
    public int hashCode() {
        int hashCode = 0;
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                if (boardGame[i][j] == '1')
                    hashCode += (i+1) * 2+(j+1) * 3;
                else if (boardGame[i][j] == '2')
                    hashCode += (i+1) * 5+(j+1) * 7;
            }

        return hashCode;
    }

    @Override
    public String getBoardName() {
        return "Othello "+size+" x "+size;
    }


    @Override
    public String toString() {
        return Arrays.deepToString(boardGame);
    }


    public boolean isTheGameOver() {
        if (cellsLeft == 0)
            return true;
        if (player == 'n')
            return true;
        return false;
    }


    public double getScore() {
        int player1score = 0;
        int player2score = 0;
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                if (boardGame[i][j] == '1')
                    player1score += boardRewards[i][j];
                else if (boardGame[i][j] == '2')
                    player2score += boardRewards[i][j];
        return (double) (player1score-player2score);
    }


    public ArrayList<IMove> getLegalMoves() {
        ArrayList<IMove> legalMoves = new ArrayList<IMove>();
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                WeightedOthelloMove currentMove = new WeightedOthelloMove(i, j);
                if (isLegalMove(currentMove))
                    legalMoves.add(currentMove);
            }
        return legalMoves;
    }


    public boolean isLegalMove(IMove move) {
        if (move instanceof WeightedOthelloMove) {
            int row = ((WeightedOthelloMove) move).row;
            int col = ((WeightedOthelloMove) move).col;

            if (row > size-1 ||
                    col > size-1 ||
                    row < 0 ||
                    col < 0 ||
                    boardGame[row][col] != '0')
                return false;

            if (isLegalDown(row, col) ||
                    isLegalUp(row, col) ||
                    isLegalRight(row, col) ||
                    isLegalLeft(row, col) ||
                    isLegalDiagonalDownLeft(row, col) ||
                    isLegalDiagonalDownRight(row, col) ||
                    isLegalDiagonalUpLeft(row, col) ||
                    isLegalDiagonalUpRight(row, col))
                return true;
        }
        return false;
    }


    public boolean executePlayersMove(IMove move) {
        if (move instanceof WeightedOthelloMove) {
            int row = ((WeightedOthelloMove) move).row;
            int col = ((WeightedOthelloMove) move).col;

            if (!isLegalMove(move))
                return false;

            boardGame[row][col] = player;

            if (isLegalDown(row, col))
                executeDown(row, col);

            if (isLegalUp(row, col))
                executeUp(row, col);

            if (isLegalRight(row, col))
                executeRight(row, col);

            if (isLegalLeft(row, col))
                executeLeft(row, col);

            if (isLegalDiagonalDownLeft(row, col))
                executeDiagonalDownLeft(row, col);

            if (isLegalDiagonalDownRight(row, col))
                executeDiagonalDownRight(row, col);

            if (isLegalDiagonalUpLeft(row, col))
                executeDiagonalUpLeft(row, col);

            if (isLegalDiagonalUpRight(row, col))
                executeDiagonalUpRight(row, col);

            cellsLeft--;
            return true;
        }
        return false;
    }


    public char getCurrentPlayer() {
        return player;
    }


    public char getNextPlayer() {
        if (player == '1')
            return '2';
        if (player == '2')
            return '1';
        return 'n';
    }


    private boolean isLegalDown(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int i = row+1; i < size; i++) {
            if (boardGame[i][col] == '0')
                return false;
            if (boardGame[i][col] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[i][col] == player)
                return true;
            else if (boardGame[i][col] == player)
                return false;
        }
        return false;
    }


    private boolean isLegalUp(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int i = row-1; i >= 0; i--) {
            if (boardGame[i][col] == '0')
                return false;
            if (boardGame[i][col] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[i][col] == player)
                return true;
            else if (boardGame[i][col] == player)
                return false;
        }
        return false;
    }


    private boolean isLegalRight(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int j = col+1; j < size; j++) {
            if (boardGame[row][j] == '0')
                return false;
            if (boardGame[row][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[row][j] == player)
                return true;
            else if (boardGame[row][j] == player)
                return false;
        }
        return false;
    }


    private boolean isLegalLeft(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int j = col-1; j >= 0; j--) {
            if (boardGame[row][j] == '0')
                return false;
            if (boardGame[row][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[row][j] == player)
                return true;
            else if (boardGame[row][j] == player)
                return false;
        }
        return false;
    }


    private boolean isLegalDiagonalUpRight(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int i = row-1, j = col+1; i >= 0 && j < size; i--, j++) {
            if (boardGame[i][j] == '0')
                return false;
            if (boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[i][j] == player)
                return true;
            else if (boardGame[i][j] == player)
                return false;
        }
        return false;
    }


    private boolean isLegalDiagonalUpLeft(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int i = row-1, j = col-1; i >= 0 && j >= 0; i--, j--) {
            if (boardGame[i][j] == '0')
                return false;
            if (boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[i][j] == player)
                return true;
            else if (boardGame[i][j] == player)
                return false;
        }
        return false;
    }


    private boolean isLegalDiagonalDownRight(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int i = row+1, j = col+1; i < size && j < size; i++, j++) {
            if (boardGame[i][j] == '0')
                return false;
            if (boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[i][j] == player)
                return true;
            else if (boardGame[i][j] == player)
                return false;
        }
        return false;
    }


    private boolean isLegalDiagonalDownLeft(int row, int col) {
        boolean otherPlayerFlag = false;
        for(int i = row+1, j = col-1; i < size && j >= 0; i++, j--) {
            if (boardGame[i][j] == '0')
                return false;
            if (boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && boardGame[i][j] == player)
                return true;
            else if (boardGame[i][j] == player)
                return false;
        }
        return false;
    }


    private boolean executeDown(int row, int col) {
        for(int i = row+1; i < size; i++) {
            if (boardGame[i][col] == getNextPlayer())
                boardGame[i][col] = player;
            else if (boardGame[i][col] == player)
                return true;
        }
        return false;
    }


    private boolean executeUp(int row, int col) {
        for(int i = row-1; i >= 0; i--) {
            if (boardGame[i][col] == getNextPlayer())
                boardGame[i][col] = player;
            else if (boardGame[i][col] == player)
                return true;
        }
        return false;
    }


    private boolean executeRight(int row, int col) {
        for(int j = col+1; j < size; j++) {
            if (boardGame[row][j] == getNextPlayer())
                boardGame[row][j] = player;
            else if (boardGame[row][j] == player)
                return true;
        }
        return false;
    }


    private boolean executeLeft(int row, int col) {
        for(int j = col-1; j >= 0; j--) {
            if (boardGame[row][j] == getNextPlayer())
                boardGame[row][j] = player;
            else if (boardGame[row][j] == player)
                return true;
        }
        return false;
    }


    private boolean executeDiagonalUpRight(int row, int col) {
        for(int i = row-1, j = col+1; i >= 0 && j < size; i--, j++) {
            if (boardGame[i][j] == getNextPlayer())
                boardGame[i][j] = player;
            else if (boardGame[i][j] == player)
                return true;
        }
        return false;
    }


    private boolean executeDiagonalUpLeft(int row, int col) {
        for(int i = row-1, j = col-1; i >= 0 && j >= 0; i--, j--) {
            if (boardGame[i][j] == getNextPlayer())
                boardGame[i][j] = player;
            else if (boardGame[i][j] == player)
                return true;
        }
        return false;
    }


    private boolean executeDiagonalDownRight(int row, int col) {
        for(int i = row+1, j = col+1; i < size && j < size; i++, j++) {
            if (boardGame[i][j] == getNextPlayer())
                boardGame[i][j] = player;
            else if (boardGame[i][j] == player)
                return true;
        }
        return false;
    }


    private boolean executeDiagonalDownLeft(int row, int col) {
        for(int i = row+1, j = col-1; i < size && j >= 0; i++, j--) {
            if (boardGame[i][j] == getNextPlayer())
                boardGame[i][j] = player;
            else if (boardGame[i][j] == player)
                return true;
        }
        return false;
    }


    @Override
    public void printBoard() {
        String s = "";
        int rows = size;
        int cols = size;
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                if (boardGame[row][col] == '1')
                    s += " | 1";
                else if (boardGame[row][col] == '2')
                    s += " | 2";
                else
                    s += " |  ";
            }
            s += " |\n";
        }
        System.out.println(s);
        printRewards();
    }

    private void printRewards() {
        String s = "";
        int rows = size;
        int cols = size;
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                s += "|"+boardRewards[row][col];

            }
            s += "|\n";
        }
        System.out.println(s);
    }


}

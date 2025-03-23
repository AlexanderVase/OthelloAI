import java.util.List;
import java.util.Scanner;

public class OthelloGame {
    private OthelloBoard board;
    private int currentPlayer;
    private boolean gameOver;
    private int turnCount; // Add turn counter
    private static final int MAX_TURNS = 50; // Prevent infinite loops
    
    public OthelloGame() {
        board = new OthelloBoard();
        currentPlayer = OthelloBoard.BLACK; // Human plays as black
        gameOver = false;
        turnCount = 0;
    }
    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        
        while (!gameOver) {
            turnCount++; // Increment turn counter
            
            // Prevent infinite game
            if (turnCount > MAX_TURNS) {
                System.out.println("Game ended due to maximum turn limit.");
                printFinalScore();
                break;
            }
            
            printBoard();
            
            if (currentPlayer == OthelloBoard.BLACK) {
                // Human player's turn
                List<int[]> validMoves = board.getValidMoves(currentPlayer);
                
                if (validMoves.isEmpty()) {
                    System.out.println("No valid moves for Black. Skipping turn.");
                    switchPlayer();
                    continue;
                }
                
                System.out.println("Your valid moves:");
                for (int[] move : validMoves) {
                    System.out.println("Row: " + move[0] + ", Col: " + move[1]);
                }
                
                System.out.print("Enter row (0-3): ");
                int row = scanner.nextInt();
                System.out.print("Enter column (0-3): ");
                int col = scanner.nextInt();
                
                if (board.isValidMove(row, col, currentPlayer)) {
                    board.makeMove(row, col, currentPlayer);
                    switchPlayer();
                } else {
                    System.out.println("Invalid move. Try again.");
                    continue; // Stay on the same player's turn
                }
            } else {
                // AI player's turn
                List<int[]> validMoves = board.getValidMoves(currentPlayer);
                
                if (validMoves.isEmpty()) {
                    System.out.println("No valid moves for AI. Skipping turn.");
                    switchPlayer();
                    continue;
                }
                
                int depth = 3; // Reduced depth for 4x4 board
                int[] aiMove = OthelloAI.getBestMove(board, currentPlayer, depth);
                
                System.out.println("AI Move:");
                System.out.println("Row: " + aiMove[0] + ", Col: " + aiMove[1]);
                System.out.println("Depth: " + aiMove[3]);
                System.out.println("Nodes Examined: " + aiMove[2]);
                
                if (board.isValidMove(aiMove[0], aiMove[1], currentPlayer)) {
                    board.makeMove(aiMove[0], aiMove[1], currentPlayer);
                    switchPlayer();
                } else {
                    System.out.println("AI found an invalid move. Skipping turn.");
                    switchPlayer();
                }
            }
            
            // More robust game over condition
            List<int[]> blackMoves = board.getValidMoves(OthelloBoard.BLACK);
            List<int[]> whiteMoves = board.getValidMoves(OthelloBoard.WHITE);
            
            if (blackMoves.isEmpty() && whiteMoves.isEmpty()) {
                gameOver = true;
                printFinalScore();
            } else if (board.countPieces(OthelloBoard.BLACK) + board.countPieces(OthelloBoard.WHITE) == 16) {
                // Board is full
                gameOver = true;
                printFinalScore();
            }
        }
        
        scanner.close();
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == OthelloBoard.BLACK) ? 
                         OthelloBoard.WHITE : OthelloBoard.BLACK;
    }
    
    private void printBoard() {
        int[][] gameBoard = board.getBoard();
        System.out.println("\nCurrent Board (Turn " + turnCount + "):");
        System.out.print("  0 1 2 3\n");
        
        for (int i = 0; i < 4; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 4; j++) {
                switch (gameBoard[i][j]) {
                    case OthelloBoard.EMPTY:
                        System.out.print(". ");
                        break;
                    case OthelloBoard.BLACK:
                        System.out.print("B ");
                        break;
                    case OthelloBoard.WHITE:
                        System.out.print("W ");
                        break;
                }
            }
            System.out.println();
        }
        
        System.out.println("\nBlack Pieces: " + board.countPieces(OthelloBoard.BLACK));
        System.out.println("White Pieces: " + board.countPieces(OthelloBoard.WHITE));
    }
    
    private void printFinalScore() {
        int blackPieces = board.countPieces(OthelloBoard.BLACK);
        int whitePieces = board.countPieces(OthelloBoard.WHITE);
        
        System.out.println("\nGame Over!");
        System.out.println("Black Pieces: " + blackPieces);
        System.out.println("White Pieces: " + whitePieces);
        
        if (blackPieces > whitePieces) {
            System.out.println("Black Wins!");
        } else if (whitePieces > blackPieces) {
            System.out.println("White Wins!");
        } else {
            System.out.println("It's a Tie!");
        }
    }
    
}
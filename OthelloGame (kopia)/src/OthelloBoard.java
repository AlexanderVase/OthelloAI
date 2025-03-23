import java.util.ArrayList;
import java.util.List;

public class OthelloBoard {
    private int[][] board;
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    
    public OthelloBoard() {
        board = new int[4][4];
        // Initial board setup
        board[1][1] = WHITE;
        board[1][2] = BLACK;
        board[2][1] = BLACK;
        board[2][2] = WHITE;
    }
    
    public void setBoard(int[][] newBoard) {
        this.board = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.board[i][j] = newBoard[i][j];
            }
        }
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public boolean isValidMove(int row, int col, int player) {
        // Check if the move is within board and the cell is empty
        if (row < 0 || row >= 4 || col < 0 || col >= 4 || board[row][col] != EMPTY) {
            return false;
        }
        
        // Check all 8 directions for capturing opponent's pieces
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        
        for (int[] dir : directions) {
            if (checkDirection(row, col, dir[0], dir[1], player)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean checkDirection(int row, int col, int dRow, int dCol, int player) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        int r = row + dRow;
        int c = col + dCol;
        
        // First step must be opponent's piece
        if (r < 0 || r >= 4 || c < 0 || c >= 4 || board[r][c] != opponent) {
            return false;
        }
        
        // Continue in direction until find player's piece or board edge
        while (r >= 0 && r < 4 && c >= 0 && c < 4 && board[r][c] == opponent) {
            r += dRow;
            c += dCol;
        }
        
        // Check if we found player's piece
        return (r >= 0 && r < 4 && c >= 0 && c < 4 && board[r][c] == player);
    }
    
    public void makeMove(int row, int col, int player) {
        if (!isValidMove(row, col, player)) {
            throw new IllegalArgumentException("Invalid move");
        }
        
        board[row][col] = player;
        
        // Flip pieces in all directions
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        
        for (int[] dir : directions) {
            flipPiecesInDirection(row, col, dir[0], dir[1], player);
        }
    }
    
    private void flipPiecesInDirection(int row, int col, int dRow, int dCol, int player) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        int r = row + dRow;
        int c = col + dCol;
        List<int[]> toFlip = new ArrayList<>();
        
        // Collect opponent pieces to potentially flip
        while (r >= 0 && r < 4 && c >= 0 && c < 4 && board[r][c] == opponent) {
            toFlip.add(new int[]{r, c});
            r += dRow;
            c += dCol;
        }
        
        // If we found player's piece, flip the collected pieces
        if (r >= 0 && r < 4 && c >= 0 && c < 4 && board[r][c] == player) {
            for (int[] piece : toFlip) {
                board[piece[0]][piece[1]] = player;
            }
        }
    }
    
    public List<int[]> getValidMoves(int player) {
        List<int[]> validMoves = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (isValidMove(i, j, player)) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }
        
        return validMoves;
    }
    
    public int countPieces(int player) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public OthelloBoard clone() {
        OthelloBoard newBoard = new OthelloBoard();
        newBoard.setBoard(this.board);
        return newBoard;
    }
}
import java.util.List;

public class OthelloAI {
    private static int nodesExamined = 0;
    
    public static int[] getBestMove(OthelloBoard board, int player, int depth) {
        nodesExamined = 0;
        int[] move = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player, true);
        return new int[]{move[0], move[1], nodesExamined, depth};
    }
    
    private static int[] minimax(OthelloBoard board, int depth, int alpha, int beta, int player, boolean isMaximizing) {
        nodesExamined++;
        
        // Terminal conditions
        List<int[]> validMoves = board.getValidMoves(player);
        if (depth == 0 || validMoves.isEmpty()) {
            return new int[]{-1, -1, evaluateBoard(board, player)};
        }
        
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = new int[]{-1, -1, bestScore};
        
        for (int[] move : validMoves) {
            OthelloBoard newBoard = board.clone();
            newBoard.makeMove(move[0], move[1], player);
            
            int opponent = (player == OthelloBoard.BLACK) ? OthelloBoard.WHITE : OthelloBoard.BLACK;
            int[] result = minimax(newBoard, depth - 1, alpha, beta, opponent, !isMaximizing);
            int score = result[2];
            
            if (isMaximizing) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new int[]{move[0], move[1], bestScore};
                }
                alpha = Math.max(alpha, bestScore);
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = new int[]{move[0], move[1], bestScore};
                }
                beta = Math.min(beta, bestScore);
            }
            
            // Pruning
            if (beta <= alpha) {
                break;
            }
        }
        
        return bestMove;
    }
    
    private static int evaluateBoard(OthelloBoard board, int player) {
        int opponent = (player == OthelloBoard.BLACK) ? OthelloBoard.WHITE : OthelloBoard.BLACK;
        
        // Count pieces
        int playerPieces = board.countPieces(player);
        int opponentPieces = board.countPieces(opponent);
        
        // Bonus for corner control
        int cornerBonus = evaluateCorners(board, player);
        
        // Mobility factor
        int playerMoves = board.getValidMoves(player).size();
        int opponentMoves = board.getValidMoves(opponent).size();
        
        return playerPieces - opponentPieces + cornerBonus + (playerMoves - opponentMoves);
    }
    
    private static int evaluateCorners(OthelloBoard board, int player) {
        int[][] corners = {{0,0}, {0,3}, {3,0}, {3,3}};
        int cornerScore = 0;
        
        for (int[] corner : corners) {
            if (board.getBoard()[corner[0]][corner[1]] == player) {
                cornerScore += 5;
            }
        }
        
        return cornerScore;
    }
}
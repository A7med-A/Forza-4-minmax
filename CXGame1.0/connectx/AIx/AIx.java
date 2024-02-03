package connectx.AIx;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import connectx.CXCellState;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import java.util.List;
import java.util.ArrayList;



public class AIx implements CXPlayer {
    private Random rand;
	private int MAX_DEPTH; // Profondità massima dell'albero di ricerca
    private int M, N, X;
    private boolean first;
    private int timeout_in_secs;

    private long START;

    private int AInum;
    private int HUMANnum;
    private CXCellState AI;
    private CXCellState HUMAN;
    private CXGameState AIwin;
    private CXGameState HUMANwin;
	
	public AIx() {
		/* costruttore di dafault che deve rimanere vuoto */
	}

	public void initPlayer(int M, int N, int X,  boolean first, int timeout_in_secs) {
		this.M = M;
        this.N = N;
        this.X = X;
        this.first = first;
        this.timeout_in_secs = timeout_in_secs;
        rand = new Random(System.currentTimeMillis());

        if(M <= 7 && N <= 7){
            MAX_DEPTH = 7;
        }
        else if (M <= 30 && N <= 30){
            MAX_DEPTH = 3;
        }
        else{
            MAX_DEPTH = 2;
        }

        if (this.first == true){
            AI = CXCellState.P1;
            AInum = 1;
            HUMAN = CXCellState.P2;
            HUMANnum = 2;
            AIwin = CXGameState.WINP1;
            HUMANwin = CXGameState.WINP2;
        }
        else{
            AI = CXCellState.P2;
            AInum = 2;
            HUMAN = CXCellState.P1;
            HUMANnum = 1;
            AIwin = CXGameState.WINP2;
            HUMANwin = CXGameState.WINP1;
        }

		
		
	}

	public int selectColumn(CXBoard B) {
        START = System.currentTimeMillis();

        return minimax(B, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true, START, this.timeout_in_secs).getColumn();

	}

	///////////////////////////////CODICE///////////////////////////////////
 
    private int evaluateScore(int[] window, int AInum, int HUMANnum){
        int score = 0;

        int AIpieceCount = (int) Arrays.stream(window).filter(x -> x == AInum).count();
        int HUMANpieceCount = (int) Arrays.stream(window).filter(x -> x == HUMANnum).count();
        int freeCount = (int) Arrays.stream(window).filter(x -> x == 0).count();
        if (this.X == 4){
            if(AIpieceCount == this.X){
                score += 100;
            }else if(AIpieceCount == this.X - 1 && freeCount == 1){
                score += 13;
            }else if(AIpieceCount == this.X - 2 && freeCount == 2){
                score += 6;
            }
            else if(AIpieceCount == this.X - 3 && freeCount == 3){
                score += 2;
            }


            if(HUMANpieceCount == this.X){
                score -= 100;
            }
            else if(HUMANpieceCount == this.X - 1 && freeCount == 1){
                score -= 10;
            }
            else if(HUMANpieceCount == this.X - 2 && freeCount == 2){
                score -= 4;
            }
            else if(HUMANpieceCount == this.X - 3 && freeCount == 3){
                score -= 1;
            }
        }
        else if (this.X == 5){
            if(AIpieceCount == this.X){
                score += 100;
            }else if(AIpieceCount == this.X - 1 && freeCount == 1){
                score += 23;
            }else if(AIpieceCount == this.X - 2 && freeCount == 2){
                score += 14;
            }
            else if(AIpieceCount == this.X - 3 && freeCount == 3){
                score += 5;
            }
            else if(AIpieceCount == this.X - 4 && freeCount == 4){
                score += 2;
            }

            if(HUMANpieceCount == this.X){
                score -= 100;
            }
            else if(HUMANpieceCount == this.X - 1 && freeCount == 1){
                score -= 20;
            }
            else if(HUMANpieceCount == this.X - 2 && freeCount == 2){
                score -= 10;
            }
            else if(HUMANpieceCount == this.X - 3 && freeCount == 3){
                score -= 4;
            }
            else if(HUMANpieceCount == this.X - 4 && freeCount == 4){
                score -= 1;
            }
        }
        else if (this.X == 10){
            if(AIpieceCount == this.X){
                score += 100000;
            }else if(AIpieceCount == this.X - 1 && freeCount == 1){
                score += 100;
            }else if(AIpieceCount == this.X - 2 && freeCount == 2){
                score += 90;
            }
            else if(AIpieceCount == this.X - 3 && freeCount == 3){
                score += 60;
            }
            else if(AIpieceCount == this.X - 4 && freeCount == 4){
                score += 50;
            }
            else if(AIpieceCount == this.X - 5 && freeCount == 5){
                score += 40;
            }
            else if(AIpieceCount == this.X - 6 && freeCount == 6){
                score += 30;
            }
            else if(AIpieceCount == this.X - 7 && freeCount == 7){
                score += 20;
            }
            else if(AIpieceCount == this.X - 8 && freeCount == 8){
                score += 10;
            }
            else if(AIpieceCount == this.X - 9 && freeCount == 9){
                score += 2;
            }

            if(HUMANpieceCount == this.X){
                score -= 100000;
            }
            else if(HUMANpieceCount == this.X - 1 && freeCount == 1){
                score -= 98;
            }
            else if(HUMANpieceCount == this.X - 2 && freeCount == 2){
                score -= 88;
            }
            else if(HUMANpieceCount == this.X - 3 && freeCount == 3){
                score -= 59;
            }
            else if(HUMANpieceCount == this.X - 4 && freeCount == 4){
                score -= 48;
            }
            else if(HUMANpieceCount == this.X - 5 && freeCount == 5){
                score -= 37;
            }
            else if(HUMANpieceCount == this.X - 6 && freeCount == 6){
                score -= 29;
            }
            else if(HUMANpieceCount == this.X - 7 && freeCount == 7){
                score -= 19;
            }
            else if(HUMANpieceCount == this.X - 8 && freeCount == 8){
                score -= 9;
            }
            else if(HUMANpieceCount == this.X - 9 && freeCount == 9){
                score -= 2;
            }
        }
        return score;
    
    }

    private int scorePosition(CXBoard board){

        int score = 0;

        CXCellState[][] B = board.getBoard();

        int [][] stateArray = new int[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j <  N; j++) {
                if (B[i][j] == CXCellState.FREE) stateArray[i][j] = 0;
                else if (B[i][j] == CXCellState.P1) stateArray[i][j] = 1;
                else if (B[i][j] == CXCellState.P2) stateArray[i][j] = 2;
            }
        }
        
        // Score center column
        int centerColumn = N / 2;
        int centerCount = 0;
        for(int r = 0; r < M; r++) {
            if(stateArray[r][centerColumn] == AInum) {
                centerCount++;
            }
        }
        score += centerCount * 3;


        // horizontal check
        for(int r = 0; r < M ; r++) {
            for(int c = 0; c <= N - X ; c++) {
                int[] window = Arrays.copyOfRange(stateArray[r], c, c + X);
                score += evaluateScore(window,AInum,HUMANnum);
            }   
        }
        
        // vertical check
        for(int c = 0; c < N; c++) {
            for(int r = 0; r <= M - X; r++) {
                int[] window = new int[X];
                for(int k = 0; k < X; k++) {
                    window[k] = stateArray[r + k][c];
                }
                score += evaluateScore(window, AInum, HUMANnum);
            }
        }

        // positive diagonal check
        for(int r = 0; r <= M - X ; r++) {
            for(int c = 0; c <= N - X ; c++) {
                int[] window = new int[X];
                for(int k = 0; k < X; k++) {
                    window[k] = stateArray[r + k][c + k];
                }
                score += evaluateScore(window,AInum,HUMANnum);
            }
        }

        // negative diagonal check
        for(int r = X - 1; r < M ; r++) {
            for(int c = X - 1; c < N ; c++) {
                int[] window = new int[X];
                for(int k = 0; k < X; k++) {
                    window[k] = stateArray[r - k][c - k];
                }
                score += evaluateScore(window,AInum,HUMANnum);
            }
        }
        
        return score;
    }


    class Pair<C, V> {
        private final C column;
        private final V value;

        public Pair(C column, V value) {
            this.column = column;
            this.value = value;
        }

        public C getColumn() {
            return column;
        }

        public V getValue() {
            return value;
        }
    }



    private Pair<Integer, Integer> minimax(CXBoard board, int depth, int alpha, int beta, boolean maximizingPlayer, long startTime, int timeout) {
        List<Integer> validLocations = Arrays.asList(board.getAvailableColumns());
        boolean isTerminal = board.gameState() != CXGameState.OPEN;

        if ((System.currentTimeMillis() - startTime) / 1000.0 >= timeout * (95.0 / 100.0)) {
            int randomColumn = validLocations.get(rand.nextInt(validLocations.size()));
            return new Pair<>(randomColumn, 0);
        }

        if (depth == 0 || isTerminal) {
            if (isTerminal) {
                if (board.gameState() == AIwin) {
                    return new Pair<>(null, 1000000000);
                } else if (board.gameState() == HUMANwin) {
                    return new Pair<>(null, -1000000000);
                } else {
                    return new Pair<>(null, 0);
                }
            } else {
                return new Pair<>(null, scorePosition(board));
            }
        }

        if (maximizingPlayer) {
            int value = Integer.MIN_VALUE;
            int column = validLocations.get(rand.nextInt(validLocations.size()));
            for (int col : validLocations) {
                board.markColumn(col);
                Pair<Integer, Integer> newScore = minimax(board, depth - 1, alpha, beta, false, startTime, timeout);
                board.unmarkColumn();
                if (newScore.getValue() > value) {
                    value = newScore.getValue();
                    column = col;
                }
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return new Pair<>(column, value);
        } else {
            int value = Integer.MAX_VALUE;
            int column = validLocations.get(rand.nextInt(validLocations.size()));
            for (int col : validLocations) {
                board.markColumn(col);
                Pair<Integer, Integer> newScore = minimax(board, depth - 1, alpha, beta, true, startTime, timeout);
                board.unmarkColumn();
                if (newScore.getValue() < value) {
                    value = newScore.getValue();
                    column = col;
                }
                beta = Math.min(beta, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return new Pair<>(column, value);
        }
    }

    ///////////////////////////////////FINE////////////////////////////////////

	public String playerName() {
		return "AIx";
	}
}
	



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


/* LE FUNZIONI DELLA MIA LIBRERIA 
// Resetta la tabella di gioco
public void reset();

// Stato della cella i,j della matrice
public CXCellState cellState(int i, int j);

// Ritorna true se la colonna col `e piena
public boolean fullColumn(int col);

// Ritorna l’ultima mossa effettuata
public CXCell getLastMove();

// Ritorna lo stato del gioco (WIN1, WIN2, DRAW, OPEN)
public CXGameState gameState();

// Giocatore a cui tocca la prossima mossa
public int currentPlayer();

// Numero di celle ancora libere nella matrice
public int numOfFreeCells();

// Numero di celle gi`a occupate nella matrice
public int numOfMarkedCells();

// Il giocatore corrente gioca sulla colonna indicata
public CXGameState markColumn(int col);

// Elimina l’ultima mossa giocata
public void unmarkColumn();

// Ritorna la lista di mosse gi`a giocate, in ordine
public CXCell[] getMarkedCells();

// Ritorna la lista di colonne non ancora piene
public Integer[] getAvailableColumns();

// Ritorna la matrice di gioco attuale
public CXCellState[][] getBoard();

// Crea una copia dell’oggetto CXBoard
public CXBoard copy();
 */




public class AIx implements CXPlayer {
	// Inizializza il giocatore software
	// M = numero di righe nella scacchiera
	// N = numero di colonne nella scacchiera
	// X = numero di gettoni da allineare
	// first = true se `e il primo a giocare
	// timeout_in_secs = numero massimo di secondi per una mossa
    private Random rand;
	private static final int MAX_DEPTH = 4; // Profondità massima dell'albero di ricerca
    private int M, N, X;
    private boolean first;
    private int timeout_in_secs;

	
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
		
		
	}

	public int selectColumn(CXBoard B) {
		
        return pickBestMove(B);
        

	}

	///////////////////////////////CODICE///////////////////////////////////
 
    private int evaluateScore(int[] window, int AInum, int HUMANnum){
        int score = 0;

        int AIpieceCount = (int) Arrays.stream(window).filter(x -> x == AInum).count();
        int HUMANpieceCount = (int) Arrays.stream(window).filter(x -> x == HUMANnum).count();
        int freeCount = (int) Arrays.stream(window).filter(x -> x == 0).count();

        if(AIpieceCount == this.X){
            score += 100;
        }else if(AIpieceCount == this.X - 1 && freeCount == 1){
            score += 10;
        }else if(AIpieceCount == this.X - 2 && freeCount == 2){
            score += 5;
        }

        if(HUMANpieceCount == this.X - 1 && freeCount == 1){
            score -= 20;
        }
        return score;
    
    }

    private int scorePosition(CXBoard board){

        int score = 0;
        
        CXCellState AI;
        int AInum;
        int HUMANnum;
        CXCellState HUMAN;
        CXCellState[][] B = board.getBoard();

        int [][] stateArray = new int[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j <  N; j++) {
                if (B[i][j] == CXCellState.FREE) stateArray[i][j] = 0;
                else if (B[i][j] == CXCellState.P1) stateArray[i][j] = 1;
                else if (B[i][j] == CXCellState.P2) stateArray[i][j] = 2;
            }
        }

        if (this.first == true){
            AI = CXCellState.P1;
            AInum = 1;
            HUMAN = CXCellState.P2;
            HUMANnum = 2;
        }
        else{
            AI = CXCellState.P2;
            AInum = 2;
            HUMAN = CXCellState.P1;
            HUMANnum = 1;
        }

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
            for(int c = 0; c <= N - X ; c++) {
                int[] window = new int[X];
                for(int k = 0; k < X; k++) {
                    window[k] = stateArray[r - k][c + k];
                }
                score += evaluateScore(window,AInum,HUMANnum);
            }
        }
        
        return score;
    }


    /* 
    private int pickBestMove(CXBoard board){
        int bestScore = -1000;
        List<Integer> bestCols = new ArrayList<>(); // List to store the best columns
        
        for (int col = 0; col < N; col++){
            CXBoard copy = board.copy();
            if (copy.gameState() == CXGameState.OPEN && !copy.fullColumn(col)) {
                    copy.markColumn(col);
                    int score = scorePosition(copy);
                    if (score > bestScore){
                        bestScore = score;
                        
                        bestCols.clear(); // Clear the list as we found a better score
                        bestCols.add(col); // Add the column to the list
                    }else if (score == bestScore){
                        bestCols.add(col); // Add the column to the list
                    }
            }
        }

        // If there are multiple best columns, pick one randomly
        int bestCol = 0;
        if (!bestCols.isEmpty()) {
            bestCol = bestCols.get(rand.nextInt(bestCols.size()));
        }
        return bestCol;
    }*/


    private int minmax(CXBoard board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.gameState() != CXGameState.OPEN) {
            return scorePosition(board);
        }

        if (maximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;
            for (int col : board.getAvailableColumns()) {
                CXBoard copy = board.copy();
                copy.markColumn(col);
                int score = minmax(copy, depth - 1, alpha, beta, false);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int col : board.getAvailableColumns()) {
                CXBoard copy = board.copy();
                copy.markColumn(col);
                int score = minmax(copy, depth - 1, alpha, beta, true);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }
            return minScore;
        }
    }

    private int pickBestMove(CXBoard board) {
        int bestScore = Integer.MIN_VALUE;
        List<Integer> bestCols = new ArrayList<>();

        for (int col : board.getAvailableColumns()) {
            CXBoard copy = board.copy();
            copy.markColumn(col);
            int score = minmax(copy, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (score > bestScore) {
                bestScore = score;
                bestCols.clear();
                bestCols.add(col);
            } else if (score == bestScore) {
                bestCols.add(col);
            }
        }

        int bestCol = 0;
        if (!bestCols.isEmpty()) {
            bestCol = bestCols.get(rand.nextInt(bestCols.size()));
        }
        return bestCol;
    }

    




    ///////////////////////////////////FINE////////////////////////////////////

	public String playerName() {
		return "AIx";
	}
}
	



// =====================================================
// SNAKE AND LADDER GAME SYSTEM
// =====================================================

import java.util.*;
import java.util.stream.Collectors;

// Main Class - Must be first and public
public class SnakeAndLadderGame {
    public static void main(String[] args) {
        System.out.println("=== SNAKE AND LADDER GAME DEMO ===");
        System.out.println("==================================\n");
        
        // Create game with default board size (100)
        Game game = new Game(100);
        
        // Add players
        System.out.println("1. Adding players...");
        game.addPlayer("Alice");
        game.addPlayer("Bob");
        game.addPlayer("Charlie");
        game.addPlayer("Diana");
        
        // Add snakes
        System.out.println("\n2. Adding snakes...");
        game.addSnake(99, 54);   // Near the end
        game.addSnake(95, 75);
        game.addSnake(88, 24);
        game.addSnake(62, 19);
        game.addSnake(48, 26);
        game.addSnake(36, 6);
        game.addSnake(32, 10);
        
        // Add ladders
        System.out.println("\n3. Adding ladders...");
        game.addLadder(2, 38);   // Early boost
        game.addLadder(7, 14);
        game.addLadder(8, 31);
        game.addLadder(15, 26);
        game.addLadder(28, 84);  // Big ladder
        game.addLadder(21, 42);
        game.addLadder(51, 67);
        game.addLadder(71, 91);
        game.addLadder(78, 98);
        
        // Display game setup
        game.displayGameInfo();
        
        // Play the game
        System.out.println("\n4. Starting the game...");
        game.playGame();
        
        // Display final statistics
        game.displayGameStats();
        
        System.out.println("\n=== GAME DEMO COMPLETED ===");
        
        // Demo 2: Custom game
        System.out.println("\n\n=== CUSTOM GAME DEMO ===");
        Game customGame = new Game(50); // Smaller board
        
        customGame.addPlayer("Player1");
        customGame.addPlayer("Player2");
        
        // Add fewer snakes and ladders for faster game
        customGame.addSnake(47, 25);
        customGame.addSnake(35, 12);
        customGame.addLadder(5, 15);
        customGame.addLadder(20, 40);
        
        customGame.displayGameInfo();
        customGame.playGame();
        customGame.displayGameStats();
    }
}

// Enums
enum CellType {
    NORMAL, SNAKE_HEAD, SNAKE_TAIL, LADDER_BOTTOM, LADDER_TOP
}

enum GameStatus {
    NOT_STARTED, IN_PROGRESS, COMPLETED
}

// Cell class representing each square on the board
class Cell {
    private int position;
    private CellType type;
    private int jumpTo; // For snakes and ladders
    
    public Cell(int position) {
        this.position = position;
        this.type = CellType.NORMAL;
        this.jumpTo = -1;
    }
    
    public void setSnake(int tailPosition) {
        this.type = CellType.SNAKE_HEAD;
        this.jumpTo = tailPosition;
    }
    
    public void setLadder(int topPosition) {
        this.type = CellType.LADDER_BOTTOM;
        this.jumpTo = topPosition;
    }
    
    // Getters
    public int getPosition() { return position; }
    public CellType getType() { return type; }
    public int getJumpTo() { return jumpTo; }
    public boolean hasJump() { return jumpTo != -1; }
}

// Snake class
class Snake {
    private int head;
    private int tail;
    
    public Snake(int head, int tail) {
        if (head <= tail) {
            throw new IllegalArgumentException("Snake head must be greater than tail");
        }
        this.head = head;
        this.tail = tail;
    }
    
    // Getters
    public int getHead() { return head; }
    public int getTail() { return tail; }
    
    @Override
    public String toString() {
        return "Snake(" + head + " -> " + tail + ")";
    }
}

// Ladder class
class Ladder {
    private int bottom;
    private int top;
    
    public Ladder(int bottom, int top) {
        if (bottom >= top) {
            throw new IllegalArgumentException("Ladder bottom must be less than top");
        }
        this.bottom = bottom;
        this.top = top;
    }
    
    // Getters
    public int getBottom() { return bottom; }
    public int getTop() { return top; }
    
    @Override
    public String toString() {
        return "Ladder(" + bottom + " -> " + top + ")";
    }
}

// Player class
class Player {
    private String name;
    private int currentPosition;
    private int totalMoves;
    private int snakesBitten;
    private int laddersClimbed;
    private List<Integer> moveHistory;
    private boolean hasWon;
    
    public Player(String name) {
        this.name = name;
        this.currentPosition = 0;
        this.totalMoves = 0;
        this.snakesBitten = 0;
        this.laddersClimbed = 0;
        this.moveHistory = new ArrayList<>();
        this.hasWon = false;
    }
    
    public void makeMove(int diceRoll, int newPosition) {
        totalMoves++;
        moveHistory.add(diceRoll);
        currentPosition = newPosition;
    }
    
    public void encounterSnake() {
        snakesBitten++;
    }
    
    public void climbLadder() {
        laddersClimbed++;
    }
    
    public void win() {
        hasWon = true;
    }
    
    public void displayStats() {
        System.out.println("Player: " + name);
        System.out.println("  Final Position: " + currentPosition);
        System.out.println("  Total Moves: " + totalMoves);
        System.out.println("  Snakes Bitten: " + snakesBitten);
        System.out.println("  Ladders Climbed: " + laddersClimbed);
        System.out.println("  Won: " + (hasWon ? "Yes" : "No"));
        
        if (!moveHistory.isEmpty()) {
            String recentMoves = moveHistory.stream()
                    .skip(Math.max(0, moveHistory.size() - 10))
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            System.out.println("  Recent Moves: " + recentMoves);
        }
    }
    
    // Getters and setters
    public String getName() { return name; }
    public int getCurrentPosition() { return currentPosition; }
    public void setCurrentPosition(int position) { this.currentPosition = position; }
    public int getTotalMoves() { return totalMoves; }
    public int getSnakesBitten() { return snakesBitten; }
    public int getLaddersClimbed() { return laddersClimbed; }
    public boolean hasWon() { return hasWon; }
    public List<Integer> getMoveHistory() { return moveHistory; }
}

// Dice class
class Dice {
    private Random random;
    private int sides;
    private int totalRolls;
    private Map<Integer, Integer> rollCounts;
    
    public Dice(int sides) {
        this.sides = sides;
        this.random = new Random();
        this.totalRolls = 0;
        this.rollCounts = new HashMap<>();
        
        // Initialize roll counts
        for (int i = 1; i <= sides; i++) {
            rollCounts.put(i, 0);
        }
    }
    
    public Dice() {
        this(6); // Default 6-sided dice
    }
    
    public int roll() {
        int result = random.nextInt(sides) + 1;
        totalRolls++;
        rollCounts.put(result, rollCounts.get(result) + 1);
        return result;
    }
    
    public void displayStats() {
        System.out.println("\n=== Dice Statistics ===");
        System.out.println("Total Rolls: " + totalRolls);
        System.out.println("Roll Distribution:");
        for (int i = 1; i <= sides; i++) {
            int count = rollCounts.get(i);
            double percentage = totalRolls > 0 ? (count * 100.0 / totalRolls) : 0;
            System.out.println("  " + i + ": " + count + " times (" + 
                             String.format("%.1f", percentage) + "%)");
        }
    }
    
    // Getters
    public int getSides() { return sides; }
    public int getTotalRolls() { return totalRolls; }
}

// Game Board
class GameBoard {
    private int size;
    private Cell[] cells;
    private List<Snake> snakes;
    private List<Ladder> ladders;
    
    public GameBoard(int size) {
        this.size = size;
        this.cells = new Cell[size + 1]; // 1-indexed
        this.snakes = new ArrayList<>();
        this.ladders = new ArrayList<>();
        
        // Initialize cells
        for (int i = 1; i <= size; i++) {
            cells[i] = new Cell(i);
        }
    }
    
    public boolean addSnake(int head, int tail) {
        // Validate snake position
        if (head <= tail || head > size || tail < 1) {
            System.out.println("Invalid snake position: " + head + " -> " + tail);
            return false;
        }
        
        // Check if position already has a snake or ladder
        if (cells[head].hasJump()) {
            System.out.println("Position " + head + " already has a snake or ladder");
            return false;
        }
        
        Snake snake = new Snake(head, tail);
        snakes.add(snake);
        cells[head].setSnake(tail);
        
        System.out.println("Added snake: " + snake);
        return true;
    }
    
    public boolean addLadder(int bottom, int top) {
        // Validate ladder position
        if (bottom >= top || bottom < 1 || top > size) {
            System.out.println("Invalid ladder position: " + bottom + " -> " + top);
            return false;
        }
        
        // Check if position already has a snake or ladder
        if (cells[bottom].hasJump()) {
            System.out.println("Position " + bottom + " already has a snake or ladder");
            return false;
        }
        
        Ladder ladder = new Ladder(bottom, top);
        ladders.add(ladder);
        cells[bottom].setLadder(top);
        
        System.out.println("Added ladder: " + ladder);
        return true;
    }
    
    public int movePlayer(int currentPosition, int diceRoll) {
        int newPosition = currentPosition + diceRoll;
        
        // Check if move goes beyond board
        if (newPosition > size) {
            return currentPosition; // Stay at current position
        }
        
        // Check for snake or ladder at new position
        Cell cell = cells[newPosition];
        if (cell.hasJump()) {
            newPosition = cell.getJumpTo();
        }
        
        return newPosition;
    }
    
    public Cell getCell(int position) {
        if (position < 1 || position > size) {
            return null;
        }
        return cells[position];
    }
    
    public void displayBoard() {
        System.out.println("\n=== Game Board ===");
        System.out.println("Board Size: " + size);
        System.out.println("Snakes: " + snakes.size());
        for (Snake snake : snakes) {
            System.out.println("  " + snake);
        }
        
        System.out.println("Ladders: " + ladders.size());
        for (Ladder ladder : ladders) {
            System.out.println("  " + ladder);
        }
    }
    
    public void displayBoardGrid(List<Player> players) {
        System.out.println("\n=== Current Board State ===");
        
        // Display board in 10x10 grid (for 100 cell board)
        int cols = 10;
        int rows = (size + cols - 1) / cols;
        
        for (int row = rows - 1; row >= 0; row--) {
            // Print row numbers
            for (int col = 0; col < cols; col++) {
                int cellNumber;
                if (row % 2 == 0) {
                    // Even rows: left to right
                    cellNumber = row * cols + col + 1;
                } else {
                    // Odd rows: right to left (snake pattern)
                    cellNumber = row * cols + (cols - col);
                }
                
                if (cellNumber <= size) {
                    System.out.printf("%3d", cellNumber);
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
            
            // Print player positions and special cells
            for (int col = 0; col < cols; col++) {
                int cellNumber;
                if (row % 2 == 0) {
                    cellNumber = row * cols + col + 1;
                } else {
                    cellNumber = row * cols + (cols - col);
                }
                
                if (cellNumber <= size) {
                    String cellContent = "  .";
                    
                    // Check for players
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getCurrentPosition() == cellNumber) {
                            cellContent = " P" + (i + 1);
                            break;
                        }
                    }
                    
                    // Check for snakes and ladders
                    Cell cell = cells[cellNumber];
                    if (cell.getType() == CellType.SNAKE_HEAD) {
                        cellContent = " S↓";
                    } else if (cell.getType() == CellType.LADDER_BOTTOM) {
                        cellContent = " L↑";
                    }
                    
                    System.out.print(cellContent);
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
            System.out.println();
        }
        
        // Legend
        System.out.println("Legend: P1,P2,P3,P4 = Players, S↓ = Snake, L↑ = Ladder");
    }
    
    // Getters
    public int getSize() { return size; }
    public List<Snake> getSnakes() { return snakes; }
    public List<Ladder> getLadders() { return ladders; }
}

// Main Game class
class Game {
    private GameBoard board;
    private List<Player> players;
    private Dice dice;
    private GameStatus status;
    private int currentPlayerIndex;
    private Player winner;
    private int totalTurns;
    
    public Game(int boardSize) {
        this.board = new GameBoard(boardSize);
        this.players = new ArrayList<>();
        this.dice = new Dice();
        this.status = GameStatus.NOT_STARTED;
        this.currentPlayerIndex = 0;
        this.totalTurns = 0;
    }
    
    public boolean addPlayer(String playerName) {
        if (players.size() >= 4) {
            System.out.println("Maximum 4 players allowed");
            return false;
        }
        
        // Check for duplicate names
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                System.out.println("Player name already exists: " + playerName);
                return false;
            }
        }
        
        Player player = new Player(playerName);
        players.add(player);
        System.out.println("Added player: " + playerName + " (Player " + players.size() + ")");
        return true;
    }
    
    public boolean addSnake(int head, int tail) {
        return board.addSnake(head, tail);
    }
    
    public boolean addLadder(int bottom, int top) {
        return board.addLadder(bottom, top);
    }
    
    public void playGame() {
        if (players.size() < 2) {
            System.out.println("Need at least 2 players to start the game");
            return;
        }
        
        status = GameStatus.IN_PROGRESS;
        System.out.println("\nGame started with " + players.size() + " players!");
        
        while (status == GameStatus.IN_PROGRESS) {
            playTurn();
            
            // Prevent infinite games
            if (totalTurns > 1000) {
                System.out.println("\nGame terminated after 1000 turns to prevent infinite loop");
                break;
            }
        }
    }
    
    private void playTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        int diceRoll = dice.roll();
        int oldPosition = currentPlayer.getCurrentPosition();
        int newPosition = board.movePlayer(oldPosition, diceRoll);
        
        totalTurns++;
        
        System.out.println("\nTurn " + totalTurns + ": " + currentPlayer.getName() + 
                          " rolled " + diceRoll);
        System.out.println("  Position: " + oldPosition + " -> " + (oldPosition + diceRoll));
        
        // Check what happened at the new position
        Cell newCell = board.getCell(oldPosition + diceRoll);
        if (oldPosition + diceRoll > board.getSize()) {
            System.out.println("  Cannot move beyond board! Staying at position " + oldPosition);
        } else if (newCell != null && newCell.hasJump()) {
            if (newCell.getType() == CellType.SNAKE_HEAD) {
                System.out.println("  Oops! Bitten by snake at " + (oldPosition + diceRoll) + 
                                 " -> sliding down to " + newPosition);
                currentPlayer.encounterSnake();
            } else if (newCell.getType() == CellType.LADDER_BOTTOM) {
                System.out.println("  Great! Found ladder at " + (oldPosition + diceRoll) + 
                                 " -> climbing up to " + newPosition);
                currentPlayer.climbLadder();
            }
        } else {
            System.out.println("  Normal move to position " + newPosition);
        }
        
        currentPlayer.makeMove(diceRoll, newPosition);
        
        // Check for win condition
        if (newPosition == board.getSize()) {
            currentPlayer.win();
            winner = currentPlayer;
            status = GameStatus.COMPLETED;
            System.out.println("\n🎉 " + currentPlayer.getName() + " WINS! 🎉");
            return;
        }
        
        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        
        // Display current positions every 10 turns
        if (totalTurns % 10 == 0) {
            displayCurrentPositions();
        }
    }
    
    public void displayCurrentPositions() {
        System.out.println("\n--- Current Positions ---");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String indicator = (i == currentPlayerIndex) ? " [CURRENT]" : "";
            System.out.println("Player " + (i + 1) + " (" + player.getName() + "): " + 
                             player.getCurrentPosition() + indicator);
        }
    }
    
    public void displayGameInfo() {
        System.out.println("\n=== Game Information ===");
        System.out.println("Players: " + players.size());
        for (int i = 0; i < players.size(); i++) {
            System.out.println("  Player " + (i + 1) + ": " + players.get(i).getName());
        }
        
        board.displayBoard();
        
        if (players.size() <= 4 && board.getSize() <= 100) {
            board.displayBoardGrid(players);
        }
    }
    
    public void displayGameStats() {
        System.out.println("\n=== Game Statistics ===");
        System.out.println("Total Turns: " + totalTurns);
        System.out.println("Winner: " + (winner != null ? winner.getName() : "None"));
        System.out.println("Game Status: " + status);
        
        System.out.println("\n=== Player Statistics ===");
        for (Player player : players) {
            player.displayStats();
            System.out.println();
        }
        
        dice.displayStats();
        
        // Additional game analytics
        displayGameAnalytics();
    }
    
    private void displayGameAnalytics() {
        System.out.println("\n=== Game Analytics ===");
        
        // Total snakes encountered
        int totalSnakes = players.stream().mapToInt(Player::getSnakesBitten).sum();
        System.out.println("Total Snakes Encountered: " + totalSnakes);
        
        // Total ladders climbed
        int totalLadders = players.stream().mapToInt(Player::getLaddersClimbed).sum();
        System.out.println("Total Ladders Climbed: " + totalLadders);
        
        // Average moves per player
        double avgMoves = players.stream().mapToInt(Player::getTotalMoves).average().orElse(0);
        System.out.println("Average Moves per Player: " + String.format("%.1f", avgMoves));
        
        // Find player with most snakes
        Player mostSnakes = players.stream()
                .max((p1, p2) -> Integer.compare(p1.getSnakesBitten(), p2.getSnakesBitten()))
                .orElse(null);
        if (mostSnakes != null && mostSnakes.getSnakesBitten() > 0) {
            System.out.println("Most Unlucky (Most Snakes): " + mostSnakes.getName() + 
                             " (" + mostSnakes.getSnakesBitten() + " snakes)");
        }
        
        // Find player with most ladders
        Player mostLadders = players.stream()
                .max((p1, p2) -> Integer.compare(p1.getLaddersClimbed(), p2.getLaddersClimbed()))
                .orElse(null);
        if (mostLadders != null && mostLadders.getLaddersClimbed() > 0) {
            System.out.println("Most Lucky (Most Ladders): " + mostLadders.getName() + 
                             " (" + mostLadders.getLaddersClimbed() + " ladders)");
        }
    }
    
    // Getters
    public GameBoard getBoard() { return board; }
    public List<Player> getPlayers() { return players; }
    public GameStatus getStatus() { return status; }
    public Player getWinner() { return winner; }
    public int getTotalTurns() { return totalTurns; }
}

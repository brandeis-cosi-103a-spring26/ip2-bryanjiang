package edu.brandeis.cosi103a.ip2;
import java.util.*;

public class Dominion {
    public static void main(String[] args) {
        GameState game = new GameState();
        game.initializeGame();
        game.playGame();
    }
}

// ==================== CARD CLASSES ====================

abstract class Card {
    protected String name;
    protected int cost;
    protected String type; // "Cryptocurrency" or "Automation"
    
    public Card(String name, int cost, String type) {
        this.name = name;
        this.cost = cost;
        this.type = type;
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public String getType() { return type; }
    
    public abstract void play(Player player, GameState gameState);
}

class CryptocurrencyCard extends Card {
    private int cryptoValue;
    
    public CryptocurrencyCard(String name, int cost, int cryptoValue) {
        super(name, cost, "Cryptocurrency");
        this.cryptoValue = cryptoValue;
    }
    
    public int getCryptoValue() { return cryptoValue; }
    
    @Override
    public void play(Player player, GameState gameState) {
        player.addCoins(cryptoValue);
    }
}

class AutomationCard extends Card {
    private int automationValue;
    
    public AutomationCard(String name, int cost, int automationValue) {
        super(name, cost, "Automation");
        this.automationValue = automationValue;
    }
    
    public int getAutomationValue() { return automationValue; }
    
    @Override
    public void play(Player player, GameState gameState) {
        // Automation cards don't have play effects - they contribute to end-game score
    }
}

// ==================== PLAYER CLASS ====================

class Player {
    private String name;
    private ArrayList<Card> hand;
    private ArrayList<Card> deck;
    private ArrayList<Card> discardPile;
    private int coins;
    private int actions;
    private int buys;
    
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.coins = 0;
        this.actions = 0;
        this.buys = 0;
    }
    
    public String getName() { return name; }
    public ArrayList<Card> getHand() { return hand; }
    public ArrayList<Card> getDeck() { return deck; }
    public ArrayList<Card> getDiscardPile() { return discardPile; }
    
    public void addCoins(int amount) { this.coins += amount; }
    public void addActions(int amount) { this.actions += amount; }
    public void addBuys(int amount) { this.buys += amount; }
    
    public int getCoins() { return coins; }
    public int getActions() { return actions; }
    public int getBuys() { return buys; }
    
    public void drawCard() {
        if (deck.isEmpty() && !discardPile.isEmpty()) {
            // Shuffle discard pile into deck
            deck.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(deck);
        }
        
        if (!deck.isEmpty()) {
            hand.add(deck.remove(0));
        }
    }
    
    public void playCard(Card card) {
        hand.remove(card);
        card.play(this, null);
        // Cryptocurrency cards remain in deck; they go to discard on cleanup
        if (card instanceof CryptocurrencyCard) {
            discardPile.add(card);
        }
    }
    
    public void discardCard(Card card) {
        hand.remove(card);
        discardPile.add(card);
    }
    
    public void buyCard(Card card) {
        getDiscardPile().add(card);
    }
    
    public int calculateAutomationPoints() {
        int automationPoints = 0;
        for (Card card : discardPile) {
            if (card instanceof AutomationCard) {
                automationPoints += ((AutomationCard) card).getAutomationValue();
            }
        }
        for (Card card : deck) {
            if (card instanceof AutomationCard) {
                automationPoints += ((AutomationCard) card).getAutomationValue();
            }
        }
        for (Card card : hand) {
            if (card instanceof AutomationCard) {
                automationPoints += ((AutomationCard) card).getAutomationValue();
            }
        }
        return automationPoints;
    }
}

// ==================== CARD SUPPLY CLASS ====================

class CardSupply {
    private Map<String, List<Card>> supply;
    
    public CardSupply() {
        this.supply = new HashMap<>();
        initializeSupply();
    }
    
    private void initializeSupply() {
        // Cryptocurrency cards
        supply.put("Bitcoin", createStack(new CryptocurrencyCard("Bitcoin", 0, 1), 60));
        supply.put("Ethereum", createStack(new CryptocurrencyCard("Ethereum", 3, 2), 40));
        supply.put("Dogecoin", createStack(new CryptocurrencyCard("Dogecoin", 6, 3), 30));
        
        // Automation cards
        supply.put("Method", createStack(new AutomationCard("Method", 2, 1), 14));
        supply.put("Module", createStack(new AutomationCard("Module", 5, 3), 8));
        supply.put("Framework", createStack(new AutomationCard("Framework", 8, 6), 8));
    }
    
    private List<Card> createStack(Card cardTemplate, int quantity) {
        List<Card> stack = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            try {
                if (cardTemplate instanceof CryptocurrencyCard) {
                    CryptocurrencyCard cc = (CryptocurrencyCard) cardTemplate;
                    stack.add(new CryptocurrencyCard(cc.getName(), cc.getCost(), cc.getCryptoValue()));
                } else if (cardTemplate instanceof AutomationCard) {
                    AutomationCard ac = (AutomationCard) cardTemplate;
                    stack.add(new AutomationCard(ac.getName(), ac.getCost(), ac.getAutomationValue()));
                }
            } catch (Exception e) {
                stack.add(cardTemplate);
            }
        }
        return stack;
    }
    
    public Card getCard(String cardName) {
        List<Card> stack = supply.get(cardName);
        if (stack != null && !stack.isEmpty()) {
            return stack.remove(0);
        }
        return null;
    }
    
    public Card peekCard(String cardName) {
        List<Card> stack = supply.get(cardName);
        if (stack != null && !stack.isEmpty()) {
            return stack.get(0);
        }
        return null;
    }
    
    public int cardsRemaining(String cardName) {
        List<Card> stack = supply.get(cardName);
        return stack != null ? stack.size() : 0;
    }
}

// ==================== TURN CLASS ====================

class Turn {
    private Player currentPlayer;
    private GameState gameState;
    
    public Turn(Player player, GameState gameState) {
        this.currentPlayer = player;
        this.gameState = gameState;
    }
    
    public void executeTurn() {
        System.out.println("\n" + currentPlayer.getName() + "'s Turn");
        System.out.println("Hand: " + currentPlayer.getHand().size() + " cards");
        
        // Draw 5 cards for this turn
        for (int i = 0; i < 5; i++) {
            currentPlayer.drawCard();
        }
        System.out.println("Drew 5 cards. Hand size: " + currentPlayer.getHand().size());
        
        buyPhase();
        cleanupPhase();
    }
    
    private void buyPhase() {
        System.out.println(currentPlayer.getName() + " - Buy Phase");
        
        // Play all cryptocurrency cards from hand and calculate total value
        int totalCoins = 0;
        ArrayList<Card> hand = new ArrayList<>(currentPlayer.getHand());
        for (Card card : hand) {
            if (card instanceof CryptocurrencyCard) {
                CryptocurrencyCard cc = (CryptocurrencyCard) card;
                totalCoins += cc.getCryptoValue();
                currentPlayer.playCard(card);
            }
        }
        
        System.out.println("Played cryptocurrency cards. Total coins: " + totalCoins);
        
        // Buy up to 1 card if possible
        if (totalCoins > 0) {
            Card cardToBuy = chooseBestCard(totalCoins);
            if (cardToBuy != null) {
                currentPlayer.buyCard(cardToBuy);
                System.out.println("Bought: " + cardToBuy.getName());
            } else {
                System.out.println("No affordable cards available.");
            }
        }
    }
    
    private Card chooseBestCard(int availableCoins) {
        // AI logic: prefer automation cards, then cryptocurrency cards
        // First, try to find an automation card we can afford
        String[] automationCards = {"Framework", "Module", "Method"};
        for (String cardName : automationCards) {
            Card card = gameState.getSupply().peekCard(cardName);
            if (card != null && card.getCost() <= availableCoins) {
                return gameState.getSupply().getCard(cardName);
            }
        }
        
        // Then try cryptocurrency cards
        String[] cryptoCards = {"Dogecoin", "Ethereum", "Bitcoin"};
        for (String cardName : cryptoCards) {
            Card card = gameState.getSupply().peekCard(cardName);
            if (card != null && card.getCost() <= availableCoins) {
                return gameState.getSupply().getCard(cardName);
            }
        }
        
        return null;
    }
    
    private void cleanupPhase() {
        System.out.println(currentPlayer.getName() + " - Cleanup Phase");
        
        // Discard remaining hand
        ArrayList<Card> hand = new ArrayList<>(currentPlayer.getHand());
        for (Card card : hand) {
            currentPlayer.discardCard(card);
        }
        System.out.println("Discarded remaining hand.");
        
        // Reshuffle discard pile into deck if deck is empty
        if (currentPlayer.getDeck().isEmpty() && !currentPlayer.getDiscardPile().isEmpty()) {
            ArrayList<Card> discardPile = currentPlayer.getDiscardPile();
            currentPlayer.getDeck().addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(currentPlayer.getDeck());
            System.out.println("Reshuffled discard pile into deck.");
        }
    }
}

// ==================== GAME STATE CLASS ====================

class GameState {
    private ArrayList<Player> players;
    private CardSupply supply;
    private int currentPlayerIndex;
    private boolean gameOver;
    
    public GameState() {
        this.players = new ArrayList<>();
        this.supply = new CardSupply();
        this.currentPlayerIndex = 0;
        this.gameOver = false;
    }
    
    public void initializeGame() {
        // Create 2 automated players
        for (int i = 1; i <= 2; i++) {
            Player player = new Player("Player " + i);
            players.add(player);
            setupStarterDeck(player);
        }
        
        // Choose random starting player
        currentPlayerIndex = new java.util.Random().nextInt(2);
        System.out.println("Game initialized with 2 players.");
        System.out.println(players.get(currentPlayerIndex).getName() + " goes first.");
    }
    
    private void setupStarterDeck(Player player) {
        // Starter deck: 7 Bitcoins and 3 Methods
        for (int i = 0; i < 7; i++) {
            player.getDeck().add(supply.getCard("Bitcoin"));
        }
        for (int i = 0; i < 3; i++) {
            player.getDeck().add(supply.getCard("Method"));
        }
        
        // Shuffle the deck
        Collections.shuffle(player.getDeck());
        
        // Deal 5 cards to initial hand
        for (int i = 0; i < 5; i++) {
            player.drawCard();
        }
        
        System.out.println(player.getName() + " draws initial hand.");
    }
    
    public void playGame() {
        int round = 1;
        while (!gameOver) {
            System.out.println("\n========== ROUND " + round + " ==========");
            
            for (int i = 0; i < players.size(); i++) {
                Player currentPlayer = players.get(currentPlayerIndex);
                Turn turn = new Turn(currentPlayer, this);
                turn.executeTurn();
                
                // Move to next player
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
            
            // Check win condition: all Framework cards purchased
            if (supply.cardsRemaining("Framework") == 0) {
                gameOver = true;
                System.out.println("\n========== All Framework cards have been purchased! ==========");
            }
            
            round++;
        }
        
        endGame();
    }
    
    private void endGame() {
        System.out.println("\n========== GAME OVER ==========");
        players.sort((p1, p2) -> Integer.compare(
            p2.calculateAutomationPoints(), 
            p1.calculateAutomationPoints()));
        
        for (Player player : players) {
            System.out.println(player.getName() + ": " + 
                             player.calculateAutomationPoints() + " APs");
        }
    }
    
    public CardSupply getSupply() { return supply; }
    public ArrayList<Player> getPlayers() { return players; }
}


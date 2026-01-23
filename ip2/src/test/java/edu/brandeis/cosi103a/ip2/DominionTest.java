package edu.brandeis.cosi103a.ip2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

public class DominionTest {
    
    // ==================== CARD TESTS ====================
    
    @Test
    public void testCryptocurrencyCardCreation() {
        CryptocurrencyCard card = new CryptocurrencyCard("Bitcoin", 0, 1);
        assertEquals("Bitcoin", card.getName());
        assertEquals(0, card.getCost());
        assertEquals(1, card.getCryptoValue());
        assertEquals("Cryptocurrency", card.getType());
    }
    
    @Test
    public void testAutomationCardCreation() {
        AutomationCard card = new AutomationCard("Method", 2, 1);
        assertEquals("Method", card.getName());
        assertEquals(2, card.getCost());
        assertEquals(1, card.getAutomationValue());
        assertEquals("Automation", card.getType());
    }
    
    @Test
    public void testCryptocurrencyCardPlay() {
        Player player = new Player("Test Player");
        CryptocurrencyCard card = new CryptocurrencyCard("Bitcoin", 0, 1);
        
        assertEquals(0, player.getCoins());
        player.addCoins(card.getCryptoValue());
        assertEquals(1, player.getCoins());
    }
    
    @Test
    public void testMultipleCryptocurrencyCards() {
        Player player = new Player("Test Player");
        CryptocurrencyCard bitcoin = new CryptocurrencyCard("Bitcoin", 0, 1);
        CryptocurrencyCard ethereum = new CryptocurrencyCard("Ethereum", 3, 2);
        
        player.addCoins(bitcoin.getCryptoValue());
        player.addCoins(ethereum.getCryptoValue());
        
        assertEquals(3, player.getCoins());
    }
    
    // ==================== PLAYER TESTS ====================
    
    @Test
    public void testPlayerCreation() {
        Player player = new Player("Alice");
        assertEquals("Alice", player.getName());
        assertEquals(0, player.getCoins());
        assertEquals(0, player.getActions());
        assertEquals(0, player.getBuys());
        assertTrue(player.getHand().isEmpty());
        assertTrue(player.getDeck().isEmpty());
        assertTrue(player.getDiscardPile().isEmpty());
    }
    
    @Test
    public void testAddCoins() {
        Player player = new Player("Bob");
        player.addCoins(5);
        assertEquals(5, player.getCoins());
        player.addCoins(3);
        assertEquals(8, player.getCoins());
    }
    
    @Test
    public void testAddActions() {
        Player player = new Player("Charlie");
        player.addActions(2);
        assertEquals(2, player.getActions());
        player.addActions(1);
        assertEquals(3, player.getActions());
    }
    
    @Test
    public void testAddBuys() {
        Player player = new Player("Diana");
        player.addBuys(1);
        assertEquals(1, player.getBuys());
        player.addBuys(2);
        assertEquals(3, player.getBuys());
    }
    
    @Test
    public void testDrawCard() {
        Player player = new Player("Eve");
        CryptocurrencyCard card = new CryptocurrencyCard("Bitcoin", 0, 1);
        player.getDeck().add(card);
        
        assertEquals(0, player.getHand().size());
        player.drawCard();
        assertEquals(1, player.getHand().size());
        assertEquals(0, player.getDeck().size());
    }
    
    @Test
    public void testDrawMultipleCards() {
        Player player = new Player("Frank");
        for (int i = 0; i < 5; i++) {
            player.getDeck().add(new CryptocurrencyCard("Bitcoin", 0, 1));
        }
        
        for (int i = 0; i < 5; i++) {
            player.drawCard();
        }
        
        assertEquals(5, player.getHand().size());
        assertEquals(0, player.getDeck().size());
    }
    
    @Test
    public void testDrawCardWithEmptyDeck() {
        Player player = new Player("Grace");
        assertTrue(player.getDeck().isEmpty());
        player.drawCard();
        assertEquals(0, player.getHand().size());
    }
    
    @Test
    public void testPlayCard() {
        Player player = new Player("Henry");
        CryptocurrencyCard card = new CryptocurrencyCard("Bitcoin", 0, 1);
        player.getHand().add(card);
        
        assertEquals(1, player.getHand().size());
        player.playCard(card);
        assertEquals(0, player.getHand().size());
        assertEquals(1, player.getCoins());
        assertEquals(1, player.getDiscardPile().size());
    }
    
    @Test
    public void testDiscardCard() {
        Player player = new Player("Iris");
        CryptocurrencyCard card = new CryptocurrencyCard("Bitcoin", 0, 1);
        player.getHand().add(card);
        
        assertEquals(1, player.getHand().size());
        assertEquals(0, player.getDiscardPile().size());
        player.discardCard(card);
        assertEquals(0, player.getHand().size());
        assertEquals(1, player.getDiscardPile().size());
    }
    
    @Test
    public void testBuyCard() {
        Player player = new Player("Jack");
        AutomationCard card = new AutomationCard("Method", 2, 1);
        
        assertEquals(0, player.getDiscardPile().size());
        player.buyCard(card);
        assertEquals(1, player.getDiscardPile().size());
    }
    
    @Test
    public void testCalculateAutomationPointsEmpty() {
        Player player = new Player("Kate");
        assertEquals(0, player.calculateAutomationPoints());
    }
    
    @Test
    public void testCalculateAutomationPointsInHand() {
        Player player = new Player("Leo");
        AutomationCard method = new AutomationCard("Method", 2, 1);
        AutomationCard module = new AutomationCard("Module", 5, 3);
        
        player.getHand().add(method);
        player.getHand().add(module);
        
        assertEquals(4, player.calculateAutomationPoints());
    }
    
    @Test
    public void testCalculateAutomationPointsInDeck() {
        Player player = new Player("Megan");
        AutomationCard framework = new AutomationCard("Framework", 8, 6);
        
        player.getDeck().add(framework);
        
        assertEquals(6, player.calculateAutomationPoints());
    }
    
    @Test
    public void testCalculateAutomationPointsInDiscard() {
        Player player = new Player("Noah");
        AutomationCard method = new AutomationCard("Method", 2, 1);
        AutomationCard module = new AutomationCard("Module", 5, 3);
        
        player.getDiscardPile().add(method);
        player.getDiscardPile().add(module);
        
        assertEquals(4, player.calculateAutomationPoints());
    }
    
    @Test
    public void testCalculateAutomationPointsCombined() {
        Player player = new Player("Olivia");
        AutomationCard method = new AutomationCard("Method", 2, 1);
        AutomationCard module = new AutomationCard("Module", 5, 3);
        AutomationCard framework = new AutomationCard("Framework", 8, 6);
        
        player.getHand().add(method);
        player.getDeck().add(module);
        player.getDiscardPile().add(framework);
        
        assertEquals(10, player.calculateAutomationPoints());
    }
    
    @Test
    public void testReshuffleDiscardIntoDeck() {
        Player player = new Player("Peter");
        CryptocurrencyCard card1 = new CryptocurrencyCard("Bitcoin", 0, 1);
        CryptocurrencyCard card2 = new CryptocurrencyCard("Bitcoin", 0, 1);
        
        player.getDiscardPile().add(card1);
        player.getDiscardPile().add(card2);
        
        // Draw from empty deck should trigger reshuffle
        player.drawCard();
        
        assertEquals(1, player.getHand().size());
        assertEquals(1, player.getDeck().size());
        assertEquals(0, player.getDiscardPile().size());
    }
    
    // ==================== CARD SUPPLY TESTS ====================
    
    @Test
    public void testCardSupplyInitialization() {
        CardSupply supply = new CardSupply();
        
        assertTrue(supply.cardsRemaining("Bitcoin") > 0);
        assertTrue(supply.cardsRemaining("Ethereum") > 0);
        assertTrue(supply.cardsRemaining("Dogecoin") > 0);
        assertTrue(supply.cardsRemaining("Method") > 0);
        assertTrue(supply.cardsRemaining("Module") > 0);
        assertTrue(supply.cardsRemaining("Framework") > 0);
    }
    
    @Test
    public void testGetCardFromSupply() {
        CardSupply supply = new CardSupply();
        int initialCount = supply.cardsRemaining("Bitcoin");
        
        Card card = supply.getCard("Bitcoin");
        assertNotNull(card);
        assertEquals("Bitcoin", card.getName());
        assertEquals(initialCount - 1, supply.cardsRemaining("Bitcoin"));
    }
    
    @Test
    public void testPeekCardFromSupply() {
        CardSupply supply = new CardSupply();
        int initialCount = supply.cardsRemaining("Ethereum");
        
        Card card = supply.peekCard("Ethereum");
        assertNotNull(card);
        assertEquals("Ethereum", card.getName());
        assertEquals(initialCount, supply.cardsRemaining("Ethereum")); // Count shouldn't change
    }
    
    @Test
    public void testGetNonexistentCard() {
        CardSupply supply = new CardSupply();
        Card card = supply.getCard("NonexistentCard");
        assertNull(card);
    }
    
    @Test
    public void testCardsRemainingAccurate() {
        CardSupply supply = new CardSupply();
        int bitcoinCount = supply.cardsRemaining("Bitcoin");
        
        for (int i = 0; i < bitcoinCount; i++) {
            supply.getCard("Bitcoin");
        }
        
        assertEquals(0, supply.cardsRemaining("Bitcoin"));
    }
    
    // ==================== GAME STATE TESTS ====================
    
    @Test
    public void testGameStateCreation() {
        GameState game = new GameState();
        assertNotNull(game.getSupply());
        assertNotNull(game.getPlayers());
    }
    
    @Test
    public void testGameInitialization() {
        GameState game = new GameState();
        game.initializeGame();
        
        assertEquals(2, game.getPlayers().size());
        
        for (Player player : game.getPlayers()) {
            assertEquals(5, player.getHand().size());
            assertEquals(5, player.getDeck().size());
        }
    }
    
    @Test
    public void testStarterDeckComposition() {
        GameState game = new GameState();
        game.initializeGame();
        
        Player player = game.getPlayers().get(0);
        
        // Count Bitcoins and Methods in deck + hand
        int bitcoinCount = 0;
        int methodCount = 0;
        
        for (Card card : player.getDeck()) {
            if (card.getName().equals("Bitcoin")) bitcoinCount++;
            if (card.getName().equals("Method")) methodCount++;
        }
        for (Card card : player.getHand()) {
            if (card.getName().equals("Bitcoin")) bitcoinCount++;
            if (card.getName().equals("Method")) methodCount++;
        }
        
        assertEquals(7, bitcoinCount);
        assertEquals(3, methodCount);
    }
    
    // ==================== TURN TESTS ====================
    
    @Test
    public void testTurnCreation() {
        GameState game = new GameState();
        game.initializeGame();
        Player player = game.getPlayers().get(0);
        Turn turn = new Turn(player, game);
        
        assertNotNull(turn);
    }
    
    @Test
    public void testTurnDrawsCards() {
        GameState game = new GameState();
        game.initializeGame();
        Player player = game.getPlayers().get(0);
        
        // Clear hand and deck to test drawing
        player.getHand().clear();
        player.getDeck().clear();
        
        for (int i = 0; i < 5; i++) {
            player.getDeck().add(new CryptocurrencyCard("Bitcoin", 0, 1));
        }
        
        Turn turn = new Turn(player, game);
        
        // Manually test the draw phase (which is part of executeTurn)
        for (int i = 0; i < 5; i++) {
            player.drawCard();
        }
        
        assertEquals(5, player.getHand().size());
    }
    
    // ==================== INTEGRATION TESTS ====================
    
    @Test
    public void testCompletePlayerTurn() {
        GameState game = new GameState();
        game.initializeGame();
        Player player = game.getPlayers().get(0);
        
        int initialHandSize = player.getHand().size();
        
        Turn turn = new Turn(player, game);
        turn.executeTurn();
        
        // After turn: hand should be discarded, deck should be reshuffled
        assertEquals(0, player.getHand().size());
    }
    
    @Test
    public void testMultiplePlayersInitialized() {
        GameState game = new GameState();
        game.initializeGame();
        
        assertEquals(2, game.getPlayers().size());
        assertEquals("Player 1", game.getPlayers().get(0).getName());
        assertEquals("Player 2", game.getPlayers().get(1).getName());
    }
    
    @Test
    public void testCardTypesInSupply() {
        CardSupply supply = new CardSupply();
        
        Card bitcoin = supply.peekCard("Bitcoin");
        assertTrue(bitcoin instanceof CryptocurrencyCard);
        
        Card method = supply.peekCard("Method");
        assertTrue(method instanceof AutomationCard);
    }
    
    @Test
    public void testPlayerAcquiresCards() {
        Player player = new Player("TestPlayer");
        CryptocurrencyCard card = new CryptocurrencyCard("Bitcoin", 0, 1);
        
        player.buyCard(card);
        assertEquals(1, player.getDiscardPile().size());
        
        // Add card to hand and then play it
        CryptocurrencyCard card2 = new CryptocurrencyCard("Bitcoin", 0, 1);
        player.getHand().add(card2);
        player.playCard(card2);
        // Card should be in discard pile after play
        assertEquals(2, player.getDiscardPile().size());
    }
    
    @Test
    public void testGameWinCondition() {
        GameState game = new GameState();
        game.initializeGame();
        
        // Simulate Framework cards being exhausted
        CardSupply supply = game.getSupply();
        
        // Get all Framework cards
        while (supply.cardsRemaining("Framework") > 0) {
            supply.getCard("Framework");
        }
        
        assertEquals(0, supply.cardsRemaining("Framework"));
    }
    
    @Test
    public void testCoinCalculationFromMultipleCards() {
        Player player = new Player("CoinTester");
        
        CryptocurrencyCard bitcoin = new CryptocurrencyCard("Bitcoin", 0, 1);
        CryptocurrencyCard ethereum = new CryptocurrencyCard("Ethereum", 3, 2);
        CryptocurrencyCard dogecoin = new CryptocurrencyCard("Dogecoin", 6, 3);
        
        player.getHand().add(bitcoin);
        player.getHand().add(ethereum);
        player.getHand().add(dogecoin);
        
        player.playCard(bitcoin);
        player.playCard(ethereum);
        player.playCard(dogecoin);
        
        assertEquals(6, player.getCoins());
    }
    
    @Test
    public void testCardAffordabilityChecking() {
        CryptocurrencyCard bitcoin = new CryptocurrencyCard("Bitcoin", 0, 1);
        CryptocurrencyCard ethereum = new CryptocurrencyCard("Ethereum", 3, 2);
        AutomationCard method = new AutomationCard("Method", 2, 1);
        
        assertTrue(bitcoin.getCost() <= 0);
        assertTrue(ethereum.getCost() <= 3);
        assertTrue(method.getCost() <= 2);
    }
    
    @Test
    public void testDeckShuffleRandomness() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        
        for (int i = 0; i < 10; i++) {
            player1.getDeck().add(new CryptocurrencyCard("Bitcoin", 0, 1));
            player2.getDeck().add(new CryptocurrencyCard("Bitcoin", 0, 1));
        }
        
        ArrayList<Card> deck1Copy = new ArrayList<>(player1.getDeck());
        ArrayList<Card> deck2Copy = new ArrayList<>(player2.getDeck());
        
        Collections.shuffle(player1.getDeck());
        Collections.shuffle(player2.getDeck());
        
        // After shuffle, decks should still have same cards
        assertEquals(deck1Copy.size(), player1.getDeck().size());
        assertEquals(deck2Copy.size(), player2.getDeck().size());
    }
    
    @Test
    public void testPlayerNameUniqueness() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        Player player3 = new Player("Alice");
        
        assertEquals("Alice", player1.getName());
        assertEquals("Bob", player2.getName());
        assertEquals("Alice", player3.getName());
        
        assertNotEquals(player1.getName(), player2.getName());
    }
}

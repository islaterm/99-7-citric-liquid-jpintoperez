package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.unit.BossUnit;
import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricjuice.model.board.*;
import com.github.cc3002.citricjuice.model.norma.*;
import com.github.cc3002.citricjuice.model.unit.WildUnit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class GameController implements PropertyChangeListener {

  private List<Player> players = new ArrayList<Player>();
  private List<IPanel> panels = new ArrayList<IPanel>();
  private int chapter = 1;
  private int turn = 0;
  private boolean gameEnded = false;
  private TurnPhase turnPhase;

  public GameController() {
    setTurnPhase(new StartPhase());
  }

  /**
   * Returns a copy of the players list.
   * @return
   */
  public List<Player> getPlayers() {
    return List.copyOf(players);
  }

  /**
   * Gives the begin-turn stars corresponding to the player
   */
  public void giveBeginTurnStars() {
    Player player = getTurnOwner();
    int extraStars = (int) Math.floor((getChapter()/5)+1);
    player.increaseStarsBy(extraStars);
  }

  /**
   * Sets the turn phase to a certain state
   * @param
   */
  public void setTurnPhase(TurnPhase phase) {
    turnPhase = phase;
    phase.setController(this);
  }

  /**
   * Gives the player begin-turn stars and transitions to card picking phase.
   */
  public void beginTurn() {
    giveBeginTurnStars();
    turnPhase.cardPickPhase();
  }

  /**
   * Picks a card to use, when no card is used NullCard might be passed as argument.
   */
  public void useCard() {
    // Not really implemented yet since there are no cards on this version,
    // basically it will just skip to moving phase but it is here as a placeholder.
    turnPhase.movingPhase();
  }
  /**
   * Moves the player an amount of steps ahead, if it has to stop it will return the amount of steps remaining
   * on the movement.
   * @param steps
   */
  public int movePlayer(int steps) {

    Player player = getTurnOwner();

    while (steps > 0) {
      IPanel stepPanel = getPlayerPanel(player);
      List<IPanel> nextPanels = stepPanel.getNextPanels();

      // Has to take a decision over which panel to take first
      if (nextPanels.size() > 1) {
        return steps;
      }
      // There's only one option of nextPanel
      IPanel nextPanel = nextPanels.get(0);
      player.setCurrentPanel(nextPanel);
      steps--;

      if (steps > 0) { // Checks to stop if has to keep walking
        // Walks through/lands on their own HomePanel.
        if (player.getHomePanel().equals(player.getCurrentPanel())) {
          // Turn phase -> must go to choosing if wants to stay in Home Phase.
          turnPhase.homeStopChoosePhase(steps);
          return steps;
        }
        // Has to decide if to combat or not
        if (player.getCurrentPanel().getPlayers().size() >= 2) {
          // Turn phase -> must go to choosing if wants to engage in combat.
          turnPhase.combatChoosePhase(steps);
          return steps;
        }
      }
    }

    // Successfully did all the steps, therefore we proceed to end phase.

    turnPhase.endPhase();
    return steps;

  };

  /**
   * Stops at the current panel when prompted if to stay at homePanel or keep moving and transitions to endPhase.
   */
  public void stopAtHome() {
    turnPhase.endPhase();
  }

  /**
   * Continues moving instead of stopping at home or starting a combat.
   */
  public void continueMoving() {
    int steps = turnPhase.getSteps();
    turnPhase.movingPhase();
    movePlayer(steps);
  }

  /**
   * Engages in combat with the player sharing panel with the turn owner. Only valid if there's only one aside from the
   * turn player.
   */
  public void startCombat() {
    Player player = getTurnOwner();
    IPanel panel = player.getCurrentPanel();
    List<Player> players = panel.getPlayers();
    if (players.size() != 2) { throw new RuntimeException("startCombat() with no arguments is only valid for single possible targets in panel."); };
    Player target;
    if (!players.get(0).equals(player)) { startCombat(players.get(0)); } else {
      startCombat(players.get(1));
    }

  }

  /**
   * Engages in a combat against a target player, target will have to choose if to defend or evade after this.
   * @param target
   */
  public void startCombat(Player target) {
    Player attacker = getTurnOwner();
    int attackValue = attacker.getAttackRoll();
    turnPhase.combatResponseChoosePhase(attacker, attackValue, target);
  }

  /**
   * Replies to the combat request by defending.
   */
  public void defendAgainstCombat() {
    Player attacker = turnPhase.getAttacker();
    int attackValue = turnPhase.getAttackValue();
    Player target = turnPhase.getTarget();
    target.defendAttack(attacker, attackValue);
    turnPhase.endPhase();
  }

  /**
   * Replies to the combat request by evading.
   */
  public void evadeAgainstCombat() {
    Player attacker = turnPhase.getAttacker();
    int attackValue = turnPhase.getAttackValue();
    Player target = turnPhase.getTarget();
    target.evadeAttack(attacker, attackValue);
    turnPhase.endPhase();
  }

  /**
   * Finishes the turn by activating the panel and then calling endTurn to increase the turn count.
   */
  public void finishTurn() {
    Player player = getTurnOwner();
    IPanel panel = player.getCurrentPanel();
    panel.activatedBy(player);
    endTurn();

    turnPhase.startPhase();
  }


  /**
   * Ends the turn increasing the counter by one.
   */
  public void endTurn() {
    turn++;
    if (turn >= getPlayers().size()) {
      chapter++;
      turn = 0;
    }
  }


  /**
   * Creates a bonus panel
   * @param id
   *      desired panel ID.
   * @return
   *      returns the instance reference.
   */
  public BonusPanel createBonusPanel(int id) {
    BonusPanel newPanel = new BonusPanel(id);
    panels.add(newPanel);
    return newPanel;
  }
  public BossPanel createBossPanel(int id) {
    BossPanel newPanel = new BossPanel(id);
    panels.add(newPanel);
    return newPanel;
  }
  public DropPanel createDropPanel(int id) {
    DropPanel newPanel = new DropPanel(id);
    panels.add(newPanel);
    return newPanel;
  }

  public EncounterPanel createEncounterPanel(int id) {
    EncounterPanel newPanel = new EncounterPanel(id);
    panels.add(newPanel);
    return newPanel;
  }
  public HomePanel createHomePanel(int id) {
    HomePanel newPanel = new HomePanel(id);
    panels.add(newPanel);
    return newPanel;
  }
  public NeutralPanel createNeutralPanel(int id) {
    NeutralPanel newPanel = new NeutralPanel(id);
    panels.add(newPanel);
    return newPanel;
  }

  /***
   * Creates a player and adds it to the player list.
   * @param name
   * @param hitPoints
   * @param attack
   * @param defense
   * @param evasion
   * @param panel
   * @return
   */
  public Player createPlayer(String name, int hitPoints, int attack, int defense, int evasion, IPanel panel) {
    Player newPlayer = new Player(name, hitPoints, attack, defense, evasion);
    newPlayer.setCurrentPanel(panel);
    setNormaGoal(newPlayer, new StarsNorma(10));
    players.add(newPlayer);
    newPlayer.addObserver(this);
    return newPlayer;
  }

  public WildUnit createWildUnit(String name, int hitPoints, int attack, int defense, int evasion) {
    WildUnit newWildUnit = new WildUnit(name, hitPoints, attack, defense, evasion);
    return newWildUnit;
  }

  public BossUnit createBossUnit(String name, int hitPoints, int attack, int defense, int evasion) {
    BossUnit newBossUnit = new BossUnit(name, hitPoints, attack, defense, evasion);
    return newBossUnit;
  }

  // Asignarle a cada panel uno o más paneles siguientes
  public void setNextPanel(IPanel panel, IPanel nextPanel) {
    panel.addNextPanel(nextPanel);
  }

  // Obtener todos los paneles del tablero
  public List<IPanel> getPanels() { return panels; }

  // Saber cuando un jugador gana llegando a la norma máxima
  public Player getWinner() {
    for( Player player : players) {
      int norma = player.getNormaLevel();
      if (norma == 6) {
        return player;
      }
    }
    return null;
  }

  // Definir el objetivo para aumentar de norma para un jugador (estrellas o victorias)
  public void setNormaGoal(Player player, INormaGoal normaGoal) {
    player.setNormaGoal(normaGoal);
  }

  // Definir el home panel de un jugador
  public void setPlayerHome(Player player, HomePanel panel) {
    player.setHomePanel(panel);
  }

  // Obtener el capítulo actual del juego
  public int getChapter() {
    return this.chapter;
  }

  // Obtener el jugador que es dueño del turno
  public Player getTurnOwner() {
    return players.get(turn);
  }



  // Realizar un norma check y norma clear cuando un jugador cae en un home panel
  public boolean normaCheck(Player player) {
    return player.normaCheck();
  }

  /**
   * Rolls the current turn owner's dice and invokes movePlayer.
   * @return
   */
  public int movePlayer() {
    Player player = getTurnOwner();
    int steps = player.roll();
    return movePlayer(steps);
  }


  public IPanel getPlayerPanel(Player player) {
    return player.getCurrentPanel();
  }

  public void setCurrPlayerNormaGoal(INormaGoal goal) {
    Player turnPlayer = getTurnOwner();
    setNormaGoal(turnPlayer, goal);
    return;
  }

  public boolean getGameEnded() {
    return gameEnded;
  }

  public void setGameEnded(boolean value) {
    gameEnded = value;
  }

  /**
   * Observer pattern structure
   * @param propertyChangeEvent
   */
  @Override
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    String propertyName = propertyChangeEvent.getPropertyName();

    switch(propertyName) {
      case "normaLevel":
        int newValue = (int) propertyChangeEvent.getNewValue();
        if (newValue == 6) { setGameEnded(true); };
        break;
    }

  }

}

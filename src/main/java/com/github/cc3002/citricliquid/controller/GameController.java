package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.board.*;
import com.github.cc3002.citricjuice.model.norma.INormaGoal;
import com.github.cc3002.citricjuice.model.norma.NormaFactory;
import com.github.cc3002.citricjuice.model.norma.StarsNorma;
import com.github.cc3002.citricjuice.model.norma.WinsNorma;
import com.github.cc3002.citricjuice.model.unit.BossUnit;
import com.github.cc3002.citricjuice.model.unit.IUnit;
import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricjuice.model.unit.WildUnit;
import com.github.cc3002.citricliquid.controller.gameflowstates.TurnState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class GameController implements PropertyChangeListener {

  private List<Player> players = new ArrayList<>();
  private List<IPanel> panels = new ArrayList<>();
  private int chapter = 1;
  private int turn = 0;
  private boolean gameEnded = false;
  private TurnState turnState;

  public GameController() {
    turnState = new TurnState();
  }

  /**
   * Returns a copy of the players list.
   * @return
   *  List copied from the players list.
   */
  public List<Player> getPlayers() {
    return List.copyOf(players);
  }

  /**
   * Returns the current state.
   * @return
   *  Reference of the turnState object linked to the controller.
   */
  public TurnState getTurnState() {
    return turnState;
  }

  /**
   * Gives the begin-turn stars corresponding to the player
   */
  public void giveBeginTurnStars() {
    Player player = getTurnOwner();
    int extraStars = (getChapter()/5)+1;
    player.increaseStarsBy(extraStars);
  }

  //region Interface interaction methods
  /**
   * Should be called when the player decides to move, assumes steps as the dice roll result.
   */
  public int doMove(int steps) {
    steps=movePlayer(steps);
    Player player = getTurnOwner();


    // if there's a player:
    if (player.getCurrentPanel().getPlayers().size() > 1) {
      turnState.combatChoosePhase(steps);
      return steps;
    }

    if (steps > 0) {
      // if i'm at home panel:
      if (player.getCurrentPanel().equals(player.getHomePanel())) {
        turnState.homeStopChoosePhase(steps);
        return steps;
      }

    } else {
      turnState.endPhase();
      return steps;
    }

    return steps;
  }

  /**
   * Should be called when the player decides to move.
   * @return
   *  Amount of remaining steps after doing the movement.
   */
  public int doMove() {
    Player player = getTurnOwner();
    return doMove(player.roll());
  }

  /**
   * Should be called when the player turn starts.
   */
  public void beginTurn() {
    Player player = getTurnOwner();
    giveBeginTurnStars();

    if (player.isKOd()) {
      turnState.recoveryPhase();
    } else {
      turnState.cardPickPhase();
    }
  }

  /**
   * Should be called when the player presses the button to roll their recovery trial.
   */
  public void recoveryTrial() {

    Player player = getTurnOwner();
    int newCounter = player.recoveryTrial();
    if (newCounter == 0) {
      turnState.cardPickPhase();
    } else {
      turnState.endPhase();
    }

  }


  /**
   * Picks a card to use, when no card is used NullCard might be passed as argument, cards are not
   * yet implemented so this method is.
   */
  public void useCard() {
    // Not really implemented yet since there are no cards on this version,
    // basically it will just skip to moving phase but it is here as a placeholder.
    turnState.movingPhase();
  }

  /**
   * Sets a player on a certain panel.
   * @param panel
   *  destination panel to set the player.
   */
  public void placePlayer(IPanel panel) {
    Player player = getTurnOwner();
    player.setCurrentPanel(panel);
  }

  /**
   * Stops at the current panel when prompted if to stay at homePanel or keep moving and transitions to endPhase.
   */
  public void stopAtHome() {
    turnState.endPhase();
  }

  /**
   * Tells the game that the player picks to continue moving and it specifies which one of the next
   * panels he is picking to continue moving.
   * @param panel
   *  panel to continue moving from.
   */
  public int continueMovingThrough(IPanel panel) {
    int steps = turnState.getSteps();
    placePlayer(panel);
    steps--;
    turnState.movingPhase();
    return doMove(steps);
  }

  /**
   * Continues moving instead of stopping at home or starting a combat.
   */
  public void continueMoving() {
    int steps = turnState.getSteps();
    turnState.movingPhase();
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
    if (players.size() != 2) { throw new RuntimeException("startCombat() with no arguments is only valid for single possible targets in panel."); }
    Player target;
    if (!players.get(0).equals(player)) { startCombat(players.get(0)); } else {
      startCombat(players.get(1));
    }
  }

  /**
   * Engages in a combat against a target player, target will have to choose if to defend or evade after this.
   * @param target
   *  player to engage the combat with.
   */
  public void startCombat(Player target) {
    Player attacker = getTurnOwner();
    int attackValue = attacker.getAttackRoll();
    turnState.combatResponseChoosePhase(attacker, attackValue, target);
  }

  /**
   * Replies to the combat request by evading.
   */
  public void evadeAgainstCombat() {
    IUnit attacker = turnState.getAttacker();
    int attackValue = turnState.getAttackValue();
    IUnit target = turnState.getTarget();
    target.evadeAttack(attacker, attackValue);

    if (!target.isKOd()) {
      turnState.counterattackPhase(attacker);
    } else {
      turnState.endPhase();
    }
  }

  /**
   * Replies to the combat request by defending.
   */
  public void defendAgainstCombat() {
    IUnit attacker = turnState.getAttacker();
    int attackValue = turnState.getAttackValue();
    IUnit target = turnState.getTarget();
    target.defendAttack(attacker, attackValue);

    if (!target.isKOd()) {
      turnState.counterattackPhase(attacker);
    } else {
      turnState.endPhase();
    }
  }

  /**
   * Engages in a counterattack, target will have to choose if to defend or evade after this.
   */
  public void startCounterAttack() {
    IUnit attacker = turnState.getAttacker();
    int attackValue = attacker.getAttackRoll();
    IUnit target = getTurnOwner();
    turnState.counterattackResponseChoosePhase(attacker, attackValue, target);
  }

  /**
   * Replies to the combat request by evading.
   */
  public void evadeAgainstCounterattack() {
    IUnit attacker = turnState.getAttacker();
    int attackValue = turnState.getAttackValue();
    IUnit target = turnState.getTarget();
    target.evadeAttack(attacker, attackValue);
    turnState.endPhase();
  }

  /**
   * Replies to the combat request by defending.
   */
  public void defendAgainstCounterattack() {
    IUnit attacker = turnState.getAttacker();
    int attackValue = turnState.getAttackValue();
    IUnit target = turnState.getTarget();
    target.defendAttack(attacker, attackValue);
    turnState.endPhase();
  }



  /**
   * Finishes the turn by activating the panel and then calling endTurn to increase the turn count.
   */
  public void finishTurn() {
    Player player = getTurnOwner();
    IPanel panel = player.getCurrentPanel();
    panel.activatedBy(player);
    endTurn();
    turnState.startPhase();
  }
  //endregion Interface interaction methods

  /**
   * Rolls the current turn owner's dice and invokes movePlayer with that amount of steps.
   * @return
   *  Amount of pending steps after the movement.
   */
  public int movePlayer() {
    Player player = getTurnOwner();
    int steps = player.roll();
    return movePlayer(steps);
  }

  /**
   * Moves the player through the board assuming steps as the dice roll result.
   * @param steps
   *  Amount of steps to move the player.
   * @return
   *  Pending steps after finishing the movement.
   */
  protected int movePlayer(int steps) {
    Player player = getTurnOwner();

    while(steps>0) {
      IPanel stepPanel = getPlayerPanel(player);
      List<IPanel> nextPanels = stepPanel.getNextPanels();

      // more than one next panel: (should stop to decide which path to take)
      if (nextPanels.size() > 1) {
        return steps;
      }
      // passed this line it means there's only 1 next panel available therefore we must advance
      IPanel nextPanel = nextPanels.get(0);
      player.setCurrentPanel(nextPanel);
      steps--;

      if (steps > 0) {
        // has to stop to ask if wants to stay at home panel or keep walking
        if (nextPanel.equals(player.getHomePanel())) {
          return steps;
        }
        // has to stop to ask if wants to engage in combat
        if (nextPanel.getPlayers().size() > 1) {
          return steps;
        }
      }
    }

    player.getCurrentPanel().activatedBy(player);
    return steps;
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

  //region Panel creation methods

  /**
   * Creates and stores a bonus panel
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

  /**
   * Creates and stores a boss panel
   * @param id
   *  desired Panel ID
   * @return
   *  generated panel
   */
  public BossPanel createBossPanel(int id) {
    BossPanel newPanel = new BossPanel(id);
    panels.add(newPanel);
    return newPanel;
  }

  /**
   * Creates and stores a drop panel.
   * @param id
   *  desired Panel ID
   * @return
   *  generated panel
   */
  public DropPanel createDropPanel(int id) {
    DropPanel newPanel = new DropPanel(id);
    panels.add(newPanel);
    return newPanel;
  }

  /**
   * Creates and stores an encounter panel.
   * @param id
   *  desired Panel ID
   * @return
   *  generated panel
   */
  public EncounterPanel createEncounterPanel(int id) {
    EncounterPanel newPanel = new EncounterPanel(id);
    panels.add(newPanel);
    return newPanel;
  }

  /**
   * Creates and stores a home panel.
   * @param id
   *  desired Panel ID
   * @return
   *  generated panel
   */
  public HomePanel createHomePanel(int id) {
    HomePanel newPanel = new HomePanel(id);
    panels.add(newPanel);
    return newPanel;
  }

  /**
   * Creates and stores a neutral panel.
   * @param id
   *  desired Panel ID
   * @return
   *  generated panel
   */
  public NeutralPanel createNeutralPanel(int id) {
    NeutralPanel newPanel = new NeutralPanel(id);
    panels.add(newPanel);
    return newPanel;
  }

  //endregion Panel creation methods

  //region Unit creation methods.
  /***
   * Creates a player and adds it to the player list.
   * @param name
   *  player's name
   * @param hitPoints
   *  player's hitpoints
   * @param attack
   *  player's attack
   * @param defense
   *  player's defense
   * @param evasion
   *  player's evasion
   * @param panel
   *  player's panel to be placed after being created
   * @return
   *  generated player
   */
  public Player createPlayer(String name, int hitPoints, int attack, int defense, int evasion, IPanel panel) {
    Player newPlayer = new Player(name, hitPoints, attack, defense, evasion);
    newPlayer.setCurrentPanel(panel);
    setNormaGoal(newPlayer, new StarsNorma(10));
    players.add(newPlayer);
    newPlayer.addObserver(this);
    return newPlayer;
  }
  /***
   * Creates a wild unit.
   * @param name
   *  unit's name
   * @param hitPoints
   *  unit's hitpoints
   * @param attack
   *  unit's attack
   * @param defense
   *  unit's defense
   * @param evasion
   *  unit's evasion
   * @return
   *  generated wild unit
   */
  public WildUnit createWildUnit(String name, int hitPoints, int attack, int defense, int evasion) {
    return new WildUnit(name, hitPoints, attack, defense, evasion);
  }
  /***
   * Creates a boss unit.
   * @param name
   *  boss' name
   * @param hitPoints
   *  boss' hitpoints
   * @param attack
   *  boss' attack
   * @param defense
   *  boss' defense
   * @param evasion
   *  boss' evasion
   * @return
   *  generated boss unit
   */
  public BossUnit createBossUnit(String name, int hitPoints, int attack, int defense, int evasion) {
    return new BossUnit(name, hitPoints, attack, defense, evasion);
  }
  //endregion Unit creation methods.

  /**
   * Sets a specific panel's next panel.
   * @param panel
   *  target panel
   * @param nextPanel
   *  next panel to add
   */
  public void setNextPanel(IPanel panel, IPanel nextPanel) {
    panel.addNextPanel(nextPanel);
  }

  /**
   * Gets all the created panels listed.
   * @return
   *  list of the all created panels on the board by the controller
   */
  public List<IPanel> getPanels() { return panels; }

  /**
   * Returns the winner player. Null if the game doesn't have a winner yet.
   * @return
   *  winner player
   */
  public Player getWinner() {
    for( Player player : players) {
      int norma = player.getNormaLevel();
      if (norma == 6) {
        return player;
      }
    }
    return null;
  }

  /**
   * Sets a specific norma goal to another specific player.
   * @param player
   *  player to set their norma goal
   * @param normaGoal
   *  norma goal object to set as their norma goal
   */
  public void setNormaGoal(Player player, INormaGoal normaGoal) {
    player.setNormaGoal(normaGoal);
  }

  /**
   * Assigns the player their next norma goal as a Stars norma.
   * @param player
   *  player to set their norma goal
   */
  public void setStarsNorma(Player player) {
    int lv = player.getNormaLevel();
    StarsNorma goal = NormaFactory.getStarsNorma(lv);

    setNormaGoal(player,goal);
  }

  /**
   * Assigns the player their next norma goal as a Wins norma.
   * @param player
   *  player to set their norma goal
   */
  public void setWinsNorma(Player player) {
    int lv = player.getNormaLevel();
    WinsNorma goal = NormaFactory.getWinsNorma(lv);

    setNormaGoal(player,goal);
  }

  /**
   * Defines the home panel of a specific player.
   * @param player
   *  player to set their home panel
   * @param panel
   *  desired home panel for said player
   */
  public void setPlayerHome(Player player, HomePanel panel) {
    player.setHomePanel(panel);
  }

  /**
   * Returns the current chapter.
   */
  public int getChapter() {
    return this.chapter;
  }

  /**
   * Returns the player who's supposed to be controlling the turn.
   * @return
   *  player that is currently playing its turn
   */
  public Player getTurnOwner() {
    return players.get(turn);
  }

  /**
   * Performs a norma check on a certain player.
   * @param player
   *  player to be asked for a norma check
   * @return
   *  whether the norma check ended on norma clear or not
   */
  public boolean normaCheck(Player player) {
    return player.normaCheck();
  }

  /**
   * Gets a certain player's current panel.
   * @param player
   *  player to check
   * @return
   *  panel where the player actually is
   */
  public IPanel getPlayerPanel(Player player) {
    return player.getCurrentPanel();
  }

  /**
   * Sets the current player's (turn owner) norma goal.
   * @param goal
   *  norma goal object to set as the player's goal
   */
  public void setCurrPlayerNormaGoal(INormaGoal goal) {
    Player turnPlayer = getTurnOwner();
    setNormaGoal(turnPlayer, goal);
  }

  /**
   * Tells whether the game has already ended with a winner or if it's
   * still going on.
   * @return
   *  whether the game ended or not.
   */
  public boolean getGameEnded() {
    return gameEnded;
  }

  /**
   * Modifies the boolean value for if the game has ended
   * @param value
   *  true if the game has ended
   */
  private void setGameEnded(boolean value) {
    gameEnded = value;
  }

  /**
   * Observer pattern structure, listens to property changes on norma level.
   * @param propertyChangeEvent
   *  event received by the observable
   */
  @Override
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    String propertyName = propertyChangeEvent.getPropertyName();

    // In case of adding more listener this could be changed to a switch case.
    if (propertyName.equals("normaLevel")) {
      int newValue = (int) propertyChangeEvent.getNewValue();
      if (newValue == 6) { setGameEnded(true); }
    }


  }

}

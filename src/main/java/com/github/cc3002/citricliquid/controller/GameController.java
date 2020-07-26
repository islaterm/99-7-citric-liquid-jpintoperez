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
import com.github.cc3002.citricliquid.gui.CitricLiquid;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController implements PropertyChangeListener {
  private List<Player> players = new ArrayList<>();
  private List<IPanel> panels = new ArrayList<>();
  private int chapter = 1;
  private int turn = 0;
  private boolean gameEnded = false;
  private TurnState turnState;

  private WildUnit currentWildUnit;
  private BossUnit bossUnitCharacter;
  private BossUnit currentBossUnit;
  private boolean bossDefeated;


  private PropertyChangeSupport changes = new PropertyChangeSupport(this);
  private IPanel[][] boardMatrix;

  public GameController() {
    turnState = new TurnState();
    bossDefeated = false;
  }

  /**
   * Adds an instance of CitricLiquid as an observer.
   * @param gui
   *    instance of a GUI CitricLiquid to be added to subscribers.
   */
  public void addObserver(CitricLiquid gui) {
    changes.addPropertyChangeListener(gui);
  }

  /**
   * Returns the current Boss Unit, creating one if there is none.
   */
  public BossUnit getCurrentBossUnit() {
    if (!isCurrentBossUnitValid()) {
      clearCurrentBossUnit();
      currentBossUnit = generateBossUnit();
    }
    return currentBossUnit;
  }

  /**
   * Sets the currentBossUnit slot as null. (Useful to remove the KO'd enemies)
   */
  protected void clearCurrentBossUnit() {
    currentBossUnit = null;
  }

  /**
   * Boolean that tells if there is any valid boss unit.
   */
  public boolean isCurrentBossUnitValid() {
    if (currentBossUnit == null) { return false; };
    if (currentBossUnit.isKOd()) { return false; };
    return true;
  }

  /**
   * Tells whether the condition to spawn a boss unit is met.
   * @return
   */
  private boolean bossCondition() {
    if (bossDefeated) { return false; };
    for(Player p : getPlayers()) {
      if (p.getNormaLevel() >= 4) {
        return true;
      }
    }
    return false;

  }

  /**
   * Generates a random Boss Unit
   */
  public BossUnit generateBossUnit() {
    if (bossUnitCharacter == null) {
      Random random = new Random();
      int rInt = random.nextInt(3);
      BossUnit newBossUnit = createBossUnit("Store Manager", 8, 3, 2, -1);
      switch (rInt) {
        case 1:
          newBossUnit = createBossUnit("Shifu Robot", 7, 2, 3, -2);
          break;
        case 2:
          newBossUnit = createBossUnit("Flying Castle", 10, 2, 1, -3);
          break;
      }
      bossUnitCharacter = newBossUnit;
    }

    return bossUnitCharacter.copy();
  }

  /**
   * Returns the current Wild Unit, creating one if there is no current Wild Unit
   */
  public WildUnit getCurrentWildUnit() {
    if (!isCurrentWildUnitValid()) {
      currentWildUnit = generateWildUnit();
    }
    return currentWildUnit;
  }

  /**
   * Sets the currentWildUnit slot as null. (Useful to remove the KO'd enemies)
   */
  private void clearCurrentWildUnit() {
    currentWildUnit = null;
  }

  /**
   * Boolean that tells if there is any valid wild unit.
   */
  public boolean isCurrentWildUnitValid() {
    if (currentWildUnit == null) { return false; };
    if (currentWildUnit.isKOd()) { return false; };
    return true;
  }

  /**
   * Generates a random Wild Unit
   */
  public WildUnit generateWildUnit() {
    Random random = new Random();
    int rInt = random.nextInt(3);
    WildUnit newWildUnit = createWildUnit("Chicken", 3, -1, -1, 1);
    switch (rInt) {
      case 1:
        newWildUnit = createWildUnit("Robo Ball", 3, -1, 1, -1);
        break;
      case 2:
        newWildUnit = createWildUnit("Seagull", 3, 1, -1, -1);
        break;
    }
    return newWildUnit;
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
   * Should be called when the player decides to pick a starsNorma.
   */
  public void selectWinsNorma() {
    Player player = getTurnOwner();
    if (turnState.isNormaPickPhase()) {
      setWinsNorma(player);
      turnState.endPhase();
      notifyStateChanged();
    }
  }

  /**
   * Should be called when the player decides to pick a starsNorma.
   */
  public void selectStarsNorma() {
    Player player = getTurnOwner();
    if (turnState.isNormaPickPhase()) {
      setStarsNorma(player);
      turnState.endPhase();
      notifyStateChanged();
    }
  }


  /**
   * Should be called when the player decides to move, assumes steps as the dice roll result.
   */
  public int doMove(int steps) {
    int preSteps = steps;
    steps=movePlayer(steps);
    int movement = preSteps-steps;
    notifyPlayerMoved();
    Player player = getTurnOwner();

    if (getTurnState().isMovingPhase()) {
      // if there's a player:
      if ((player.getCurrentPanel().getPlayers().size() > 1) && (movement > 0)) {
        turnState.combatChoosePhase(steps);
        notifyStateChanged();
        return steps;
      }

      if (steps > 0) {
        // if i'm at home panel:
        if (player.getCurrentPanel().equals(player.getHomePanel()) && (movement > 0)) {
          turnState.homeStopChoosePhase(steps);
          notifyStateChanged();
          return steps;
        }
        if (player.getCurrentPanel().getNextPanels().size() > 1) {
          turnState.pathChoosePhase(steps);
          notifyStateChanged();
          return steps;
        }

      } else {
        activatePanel();
        if (turnState.isMovingPhase()) {
          turnState.endPhase();
          notifyStateChanged();
        }
        return steps;
      }
    }
    return steps;
  }

  /**
   * Should be called when the player decides to move.
   * @return
   *  Amount of remaining steps after doing the movement.
   */
  public int doMove() {
    int steps = turnState.getSteps();
    Player player = getTurnOwner();
    if (steps == -1) { steps = player.roll(); };
    return doMove(steps);
  }

  /**
   * Should be called when the player turn starts.
   */
  public void beginTurn() {
    Player player = getTurnOwner();
    giveBeginTurnStars();

    if (player.isKOd()) {
      turnState.recoveryPhase();
      notifyStateChanged();

    } else {
      turnState.cardPickPhase();
      turnState.movingPhase(-1);
      notifyStateChanged();

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
      turnState.movingPhase(-1);
      notifyStateChanged();
    } else {
      turnState.endPhase();
      notifyStateChanged();
    }

  }


  /**
   * Picks a card to use, when no card is used NullCard might be passed as argument, cards are not
   * yet implemented so this method is.
   */
  public void useCard() {
    // Not really implemented yet since there are no cards on this version,
    // basically it will just skip to moving phase but it is here as a placeholder.
    turnState.movingPhase(-1);
    notifyStateChanged();
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
    activatePanel();
    if (turnState.isMovingPhase() || turnState.isHomeStopChoosePhase()) {
      turnState.endPhase();
      notifyStateChanged();
    }
  }

  /**
   * Tells the game that the player picks to continue moving and it specifies which one of the next
   * panels he is picking to continue moving.
   * @param panel
   *  panel to continue moving from.
   */
  public int continueMovingThrough(IPanel panel) {
    int steps = turnState.getSteps();
    steps--;
    placePlayer(panel);
    turnState.movingPhase(steps);
    return doMove();
  }

  /**
   * Continues moving instead of stopping at home or starting a combat.
   */
  public void continueMoving() {
    int preSteps = turnState.getSteps();
    turnState.movingPhase(preSteps);
    int res = movePlayer(preSteps);
    if (res == 0) {
      turnState.endPhase();
    }
    notifyStateChanged();


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
  public void startCombat(IUnit target) {
    Player attacker = getTurnOwner();
    int attackValue = attacker.getAttackRoll();
    turnState.combatResponseChoosePhase(attacker, attackValue, target);
    notifyStateChanged();
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
      turnState.counterattackPhase(target);

    } else {
      turnState.endPhase();
    }
    notifyStateChanged();

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
      turnState.counterattackPhase(target);
    } else {
      turnState.endPhase();
    }
    notifyStateChanged();
  }

  /**
   * Engages in a counterattack, target will have to choose if to defend or evade after this.
   */
  public void startCounterAttack() {
    IUnit attacker = turnState.getAttacker();
    int attackValue = attacker.getAttackRoll();
    IUnit target = getTurnOwner();
    turnState.counterattackResponseChoosePhase(attacker, attackValue, target);
    notifyStateChanged();
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
    notifyStateChanged();
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
    notifyStateChanged();
  }

  public void activatePanel() {
    Player player = getTurnOwner();
    IPanel panel = player.getCurrentPanel();
    panel.activatedBy(player);
  }

  /**
   * Finishes the turn by activating the panel and then calling endTurn to increase the turn count.
   */
  public void finishTurn() {
    endTurn();
    turnState.startPhase();
    notifyStateChanged();
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
      steps--;
      if (turnState.isMovingPhase()) {
        turnState.setSteps(steps);
      }
      player.setCurrentPanel(nextPanel);
      notifyPlayerMoved();

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
    switch (propertyName) {
      case "normaLevel":
        int newValue = (int) propertyChangeEvent.getNewValue();
        if (newValue == 6) {
          setGameEnded(true);
          notifyPlayerWon();
        } else {
          // Player needs to move to picking norma now.
          turnState.normaPickPhase();
          notifyStateChanged();
        }
        break;
      case "currentPanel":
        notifyPlayerMoved();
        break;
      case "normaGoal":
        notifyAttributeChanged();
        break;
      case "wins":
        notifyAttributeChanged();
        break;
      case "stars":
        notifyAttributeChanged();
        break;
      case "HP":
        notifyAttributeChanged();
        break;
      case "landedOnEncounter":
        landedOnEncounterHandler();
        break;
      case "landedOnBossEncounter":
        landedOnBossEncounterHandler();
        break;
      case "stumbledUponPlayer":
        stumbledUponPlayerHandler();
        break;
      case "reachedHome":
        reachedHomeHandler();
        break;
      case "reachedPathFork":
        reachedPathForkHandler();
        break;
    }
  }

  /**
   * Goes to homeStopChoosePhase so player can pick if to stop at home.
   */
  public void reachedHomeHandler() {
    if (turnState.isMovingPhase()) {
      if (turnState.getSteps() > 0) {
        turnState.homeStopChoosePhase(turnState.getSteps());
        notifyStateChanged();
      }
    }
  }

  /**
   * Goes to CombatChoosePhase so player can pick if to fight or continue.
   */
  public void stumbledUponPlayerHandler() {
    if (turnState.isMovingPhase() || turnState.isPathChoosePhase()) {
      turnState.combatChoosePhase(turnState.getSteps());
      notifyStateChanged();
    }
  }

  /**
   * Goes to CombatChoosePhase so player can pick if to fight or continue.
   */
  public void reachedPathForkHandler() {
    if (turnState.isMovingPhase() || turnState.isPathChoosePhase()) {
      if (turnState.getSteps() > 0) {
        // Only ask for path if they still have pending steps.
        turnState.pathChoosePhase(turnState.getSteps());
        notifyStateChanged();
      }
    }
  }

  /**
   * Manages the encounter automatically for the computer controlled enemies.
   */
  public void landedOnEncounterHandler() {
    WildUnit wildEnemy = getCurrentWildUnit();
    // Player landed on an encounter panel, therefore we expect this to be the MovingPhase
    // as so we can jump to combatChoosePhase
    turnState.combatChoosePhase(0); // 0 because it LANDED
    startCombat(wildEnemy);

    //enemy decides randomly if to defend or evade
    Random random = new Random();
    boolean defend = random.nextBoolean();
    if (defend) {
      defendAgainstCombat();
    } else {
      evadeAgainstCombat();
    }

    if (turnState.isCounterattackPhase()) {
      // If it is counterattackPhase right now enemy should pick randomly again, this is still
      // computer controlled so user doesn't have to do nothing yet.
      // This should lead to the CounterAttackResponseChoose Phase and therefore the game
      // is ready to continue
      startCounterAttack();
    } else {
      // If it is not counterattackPhase, it's because the enemy was defeated, therefore we must
      // clean the spot so a new Wild Unit arrives.
      clearCurrentWildUnit();
    }

    notifyAttributeChanged();

  }

  /**
   * Manages the boss encounter automatically for the computer controlled enemies.
   */
  public void landedOnBossEncounterHandler() {
    if (bossCondition()) {
      BossUnit bossEnemy = getCurrentBossUnit();
      // Player landed on an encounter panel, therefore we expect this to be the MovingPhase
      // as so we can jump to combatChoosePhase
      turnState.combatChoosePhase(0); // 0 because it LANDED
      startCombat(bossEnemy);

      //enemy decides randomly if to defend or evade
      Random random = new Random();
      boolean defend = random.nextBoolean();
      if (defend) {
        defendAgainstCombat();
      } else {
        evadeAgainstCombat();
      }

      if (turnState.isCounterattackPhase()) {
        // If it is counterattackPhase right now enemy should pick randomly again, this is still
        // computer controlled so user doesn't have to do nothing yet.
        // This should lead to the CounterAttackResponseChoose Phase and therefore the game
        // is ready to continue
        startCounterAttack();
      } else {
        // If it is not counterattackPhase, it's because the enemy was defeated, therefore we must
        // clean the spot so a new Wild Unit arrives.
        clearCurrentBossUnit();
      }

      notifyAttributeChanged();
    } else {
      landedOnEncounterHandler();
    }

  }


  // Methods to generate a game

  /**
   * Generates a 2d-array of IPanels that represent a
   * game board, also adds it to the game controller.
   * This format makes it easier to create a new game
   * with the GUI.
   * @return
   */
  public IPanel[][] generatePracticeBoard() {

    IPanel[][] matrix = new IPanel[9][9];

    // Fill the board with Nullpanels first.
    for(int x = 0; x<=8; x++) {
      for(int y=0; y<=8; y++) matrix[x][y] = NullPanel.getNullPanel();
    }

    // Hardcoded board
    // Replaces the corresponding inputs of the grid with freshly created panels.

    //region Creating panels
    matrix[3][0] = createBonusPanel(0);
    matrix[4][0] = createBossPanel(1);
    matrix[5][0] = createDropPanel(2);
    matrix[5][1] = createNeutralPanel(3);
    matrix[5][2] = createBonusPanel(4);
    matrix[6][2] = createHomePanel(5);
    matrix[6][3] = createEncounterPanel(6);
    matrix[7][3] = createNeutralPanel(7);
    matrix[8][3] = createBonusPanel(8);
    matrix[8][4] = createBossPanel(9);
    matrix[8][5] = createDropPanel(10);
    matrix[7][5] = createBonusPanel(11);
    matrix[6][5] = createNeutralPanel(10);
    matrix[6][6] = createHomePanel(11);
    matrix[5][6] = createEncounterPanel(12);
    matrix[5][7] = createNeutralPanel(13);
    matrix[5][8] = createBonusPanel(14);
    matrix[4][6] = createNeutralPanel(15);
    matrix[3][6] = createBonusPanel(16);
    matrix[2][6] = createHomePanel(17);
    matrix[2][5] = createDropPanel(18);
    matrix[1][5] = createNeutralPanel(19);
    matrix[0][5] = createBonusPanel(20);
    matrix[0][4] = createBossPanel(21);
    matrix[0][3] = createEncounterPanel(22);
    matrix[1][3] = createBonusPanel(23);
    matrix[2][3] = createNeutralPanel(24);
    matrix[2][2] = createHomePanel(25);
    matrix[3][2] = createEncounterPanel(26);
    matrix[3][1] = createNeutralPanel(27);
    matrix[4][8] = createBossPanel(28);
    matrix[3][8] = createDropPanel(29);
    matrix[3][7] = createNeutralPanel(30);
    matrix[2][4] = createBonusPanel(31);
    matrix[4][2] = createNeutralPanel(32);
    matrix[6][4] = createNeutralPanel(33);
    //endregion

    //region Connecting panels
    setNextPanel(matrix[2][2],matrix[2][3]);
    setNextPanel(matrix[2][3],matrix[1][3]);
    setNextPanel(matrix[2][3],matrix[2][4]);
    setNextPanel(matrix[2][4],matrix[2][5]);
    setNextPanel(matrix[2][5],matrix[2][6]);
    setNextPanel(matrix[2][6],matrix[3][6]);
    setNextPanel(matrix[3][6],matrix[4][6]);
    setNextPanel(matrix[3][6],matrix[3][7]);
    setNextPanel(matrix[4][6],matrix[5][6]);
    setNextPanel(matrix[5][6],matrix[6][6]);
    setNextPanel(matrix[6][6],matrix[6][5]);
    setNextPanel(matrix[6][5],matrix[7][5]);
    setNextPanel(matrix[6][5],matrix[6][4]);
    setNextPanel(matrix[6][4],matrix[6][3]);
    setNextPanel(matrix[6][3],matrix[6][2]);
    setNextPanel(matrix[6][2],matrix[5][2]);
    setNextPanel(matrix[5][2],matrix[5][1]);
    setNextPanel(matrix[5][2],matrix[4][2]);
    setNextPanel(matrix[4][2],matrix[3][2]);
    setNextPanel(matrix[3][2],matrix[2][2]);
    setNextPanel(matrix[1][3],matrix[0][3]);
    setNextPanel(matrix[0][3],matrix[0][4]);
    setNextPanel(matrix[0][4],matrix[0][5]);
    setNextPanel(matrix[0][5],matrix[1][5]);
    setNextPanel(matrix[1][5],matrix[2][5]);
    setNextPanel(matrix[3][7],matrix[3][8]);
    setNextPanel(matrix[3][8],matrix[4][8]);
    setNextPanel(matrix[4][8],matrix[5][8]);
    setNextPanel(matrix[5][8],matrix[5][7]);
    setNextPanel(matrix[5][7],matrix[5][6]);
    setNextPanel(matrix[7][5],matrix[8][5]);
    setNextPanel(matrix[8][5],matrix[8][4]);
    setNextPanel(matrix[8][4],matrix[8][3]);
    setNextPanel(matrix[8][3],matrix[7][3]);
    setNextPanel(matrix[7][3],matrix[6][3]);
    setNextPanel(matrix[5][1],matrix[5][0]);
    setNextPanel(matrix[5][0],matrix[4][0]);
    setNextPanel(matrix[4][0],matrix[3][0]);
    setNextPanel(matrix[3][0],matrix[3][1]);
    setNextPanel(matrix[3][1],matrix[3][2]);
    //endregion

    // Asigning each panel their MatrixPos
    for(int x = 0; x<=8; x++) {
      for(int y=0; y<=8; y++) matrix[x][y].setMatrixPos(x,y);
    }
    return matrix;
  }

  /**
   * Starts a new preset game on the controller.
   */
  public void newGame() {
    boardMatrix = generatePracticeBoard();
    Player player1 = createPlayer("Pikachu", 4,1,-1,2,boardMatrix[2][2]);
    Player player2 = createPlayer("Jamin", 5,1,0,0,boardMatrix[6][2]);
    Player player3 = createPlayer("Pusheen", 5,2,-1,-1,boardMatrix[6][6]);
    Player player4 = createPlayer("La Rosalia", 3,1,1,1,boardMatrix[2][6]);

    setPlayerHome(player1,(HomePanel) player1.getCurrentPanel());
    setPlayerHome(player2,(HomePanel) player2.getCurrentPanel());
    setPlayerHome(player3,(HomePanel) player3.getCurrentPanel());
    setPlayerHome(player4,(HomePanel) player4.getCurrentPanel());

  }

  /**
   * Returns the previously generated panels matrix.
   * Has to be called after newGame()
   * @return
   */
  public IPanel[][] getBoardMatrix() {
    return boardMatrix;
  }

  /**
   * Sends a property change event to all listeners indicating
   * the turn states have changed.
   */
  void notifyStateChanged() {
    changes.firePropertyChange(new PropertyChangeEvent(this, "stateChanged",null, null));
  }

  /**
   * Sends a property change event to all listeners indicating
   * a player has changed it's position.
   * (Helps GUI to redraw players on screen)
   */
  void notifyPlayerMoved() {
    changes.firePropertyChange(new PropertyChangeEvent(this, "playerMoved",null, null));
  }

  /**
   * Sends a property change event to all listeners indicating
   * a player has won the match.
   */
  void notifyPlayerWon() {
    changes.firePropertyChange(new PropertyChangeEvent(this, "playerWon",null, null));
  }

  /**
   * Sends a property change event to all listeners indicating
   * an attribute of the player has changed. (Useful for GUI updating the sidebar that shows
   * stars, HP and Wins)
   */
  void notifyAttributeChanged() {
    changes.firePropertyChange(new PropertyChangeEvent(this, "playerAttributeChanged",null,null));
  }


}

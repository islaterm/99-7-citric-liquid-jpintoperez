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
import java.util.Set;

public class GameController implements PropertyChangeListener {

  private List<Player> players = new ArrayList<Player>();
  private List<IPanel> panels = new ArrayList<IPanel>();
  private int chapter = 1;
  private int turn = 0;
  private boolean gameEnded = false;

  /**
   * Returns a copy of the players list.
   * @return
   */
  public List<Player> getPlayers() {
    return List.copyOf(players);
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

  // Terminar el turno del jugador actual
  public void endTurn() {
    turn++;
    if (turn >= players.size()) {
      chapter++;
      turn = 0;
    }
  }

  // Realizar un norma check y norma clear cuando un jugador cae en un home panel
  public boolean normaCheck(Player player) {
    return player.normaCheck();
  }

  public void movePlayer() {
    Player player = getTurnOwner();
    IPanel stepPanel = getPlayerPanel(player);
    List<IPanel> nextPanels = stepPanel.getNextPanels();
    IPanel nextPanel = nextPanels.get(0);
    player.setCurrentPanel(nextPanel);
    nextPanel.activatedBy(player);

    return; };

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

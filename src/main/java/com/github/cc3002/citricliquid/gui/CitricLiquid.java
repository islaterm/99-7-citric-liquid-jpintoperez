package com.github.cc3002.citricliquid.gui;

import com.github.cc3002.citricjuice.model.board.IPanel;
import com.github.cc3002.citricjuice.model.board.NullPanel;
import com.github.cc3002.citricjuice.model.norma.NormaFactory;
import com.github.cc3002.citricjuice.model.unit.BossUnit;
import com.github.cc3002.citricjuice.model.unit.IUnit;
import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricjuice.model.unit.WildUnit;
import com.github.cc3002.citricliquid.controller.GameController;
import com.github.cc3002.citricliquid.controller.gameflowstates.TurnState;
import com.github.cc3002.citricliquid.gui.nodes.BoardPanel;
import com.github.cc3002.citricliquid.gui.nodes.BoardPlayer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author Ignacio Slater Muñoz.
 * @version 1.0.6-rc.1
 * @since 1.0
 */
public class CitricLiquid extends Application implements PropertyChangeListener {
  public static final String RESOURCE_PATH = "src/main/resources/";
  public static final int TILE_SIZE = 64;
  private GameController gameController;

  // Resources
  public static Image connectedUp;
  public static Image connectedDown;
  public static Image connectedLeft;
  public static Image connectedRight;
  public static Image neutralPanel;
  public static Image bonusPanel;
  public static Image dropPanel;
  public static Image homePanel;
  public static Image encounterPanel;
  public static Image bossPanel;

  public static Image wildUnitIcon;
  public static Image bossIcon;
  public static Image player1Icon;
  public static Image player2Icon;
  public static Image player3Icon;
  public static Image player4Icon;

  // GUI components
  private static BorderPane border;
  private static Text bottomText;
  private static VBox optionsBar;
  private static BoardPlayer player1;
  private static BoardPlayer player2;
  private static BoardPlayer player3;
  private static BoardPlayer player4;

  /**
   * Loads image resources for panel sprites.
   *
   * @throws FileNotFoundException
   */
  public void loadPanelResources() throws FileNotFoundException {
    // Multipath icon sprites
    connectedUp = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "connectedUp.png"));
    connectedDown = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "connectedDown.png"));
    connectedLeft = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "connectedLeft.png"));
    connectedRight = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "connectedRight.png"));

    // Panel sprites
    neutralPanel = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "panel_neutral.png"));
    bonusPanel = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "panel_bonus.png"));
    dropPanel = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "panel_drop.png"));
    homePanel = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "panel_home.png"));
    encounterPanel = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "panel_encounter.png"));
    bossPanel = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "panel_boss.png"));


  }

  /**
   * Loads players, wildUnits and boss icons sprites.
   *
   * @throws FileNotFoundException
   */
  public void loadIconResources() throws FileNotFoundException {
    // WildUnits and Boss Sprites
    wildUnitIcon = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "wildUnitIcon.png"));
    bossIcon = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "bossIcon.png"));

    player1Icon = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "player1.png"));
    player2Icon = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "player2.png"));
    player3Icon = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "player3.png"));
    player4Icon = new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + "player4.png"));

  }

  @Override
  public void start(@NotNull Stage stage) throws FileNotFoundException {
    stage.setTitle("99.7% Citric Liquid");

    loadPanelResources();
    loadIconResources();


    gameController = new GameController();
    gameController.newGame();
    gameController.addObserver(this);

    player1 = new BoardPlayer(gameController.getPlayers().get(0), "player1.png");
    player2 = new BoardPlayer(gameController.getPlayers().get(1), "player2.png");
    player3 = new BoardPlayer(gameController.getPlayers().get(2), "player3.png");
    player4 = new BoardPlayer(gameController.getPlayers().get(3), "player4.png");

    border = new BorderPane();


    int width = 1280;
    int height = 720;
    Scene scene = new Scene(border, width, height);


    // Sección que mostrará el tablero:
    // Group boardView:
    Group boardView = new Group();

    GridPane boardGrid = makeBoardGrid(gameController);
    boardView.getChildren().add(boardGrid);
    boardView.getChildren().add(player1.getImageView());
    boardView.getChildren().add(player2.getImageView());
    boardView.getChildren().add(player3.getImageView());
    boardView.getChildren().add(player4.getImageView());


    // Barra al costado que mostrará info sobre los jugadores:
    // VBox playerBar
    VBox playerBar = new VBox();
    playerBar.setMinWidth(256);

    // Barra al costado derecho que mostrará opciones
    // VBox optionsBar
    optionsBar = new VBox();
    optionsBar.setMinWidth(256);


    // Barra superior que mostrará opciones
    // HBox topBar
    HBox topBar = new HBox();

    // Barra inferior que mostrará información
    // HBox topBar
    HBox bottomBar = new HBox();
    bottomText = new Text("Welcome to 100% Citric Liquid!");

    topBar.setAlignment(Pos.CENTER_LEFT);

    bottomBar.getChildren().add(bottomText);
    bottomBar.setAlignment(Pos.CENTER);

    BackgroundImage bgImage = new BackgroundImage(new Image(new FileInputStream(RESOURCE_PATH + "background.png")),
      BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    Background bg = new Background(bgImage);
    border.setBackground(bg);
    border.setTop(topBar);
    border.setCenter(boardView);
    border.setLeft(playerBar);
    border.setRight(optionsBar);
    border.setBottom(bottomBar);


    updateControls();
    updateInfoSidebar();

    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  /**
   * Creates a gridPane that shows the board of the game of a controller.
   *
   * @return
   */
  GridPane makeBoardGrid(GameController controller) throws FileNotFoundException {
    GridPane boardGrid = new GridPane();
    boardGrid.setHgap(0);
    boardGrid.setVgap(0);

    IPanel[][] matrix = controller.getBoardMatrix();
    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        if (!matrix[x][y].equals(NullPanel.getNullPanel())) {

          matrix[x][y].setMatrixPos(x, y);
          BoardPanel thisPanel = new BoardPanel(matrix[x][y]);
          thisPanel.setGui(this);
          Group thisGroup = thisPanel.getGroup();

          boardGrid.add(thisGroup, x, y);

        }
      }
    }


    return boardGrid;
  }

  /**
   * Event handler to change the bottom bar text on the window.
   * Used to display information about the panels or the characters.
   * @param Text
   */
  public static void setBottomText(String Text) {
    bottomText.setText(Text);
  }

  /**
   * Observer handler that receives notifications from the controller.
   * @param propertyChangeEvent
   */
  @Override
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    String eventString = propertyChangeEvent.getPropertyName();

    switch (eventString) {
      case "stateChanged":
        // cambió el state, debo redibujar los controles de la ventana
        updateControls();
        updateInfoSidebar();
        break;
      case "playerMoved":
        updatePlayers();
        break;
      case "playerAttributeChanged":
        updateInfoSidebar();
        break;
      case "playerWon":
        updateInfoSidebar();
        border.setRight(makeGameResultsControls());
        break;

    }
  }

  /**
   * Makes the BoardPlayer objects to update their positions.
   */
  public void updatePlayers() {
    player1.updatePosition();
    player2.updatePosition();
    player3.updatePosition();
    player4.updatePosition();
  }

  /**
   * Calls and replaces the controls of the borderpane's right side
   * with appropiate controls using make...control methods.
   */
  public void updateControls() {
    TurnState state = gameController.getTurnState();

    optionsBar.getChildren().clear();

    if (state.isStartPhase()) {
      optionsBar = makeStartPhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isMovingPhase()) {
      optionsBar = makeMovingPhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isHomeStopChoosePhase()) {
      optionsBar = makeHomeStopChoosePhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isEndPhase()) {
      optionsBar = makeEndPhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isPathChoosePhase()) {
      optionsBar = makePathChoosePhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isCombatChoosePhase()) {
      optionsBar = makeCombatChoosePhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isCombatResponseChoosePhase()) {
      optionsBar = makeCombatResponseChoosePhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isCounterattackPhase()) {
      optionsBar = makeCounterattackPhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isCounterattackResponseChoosePhase()) {
      optionsBar = makeCounterattackResponseChoosePhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isRecoveryPhase()) {
      optionsBar = makeRecoveryPhaseControls();
      border.setRight(optionsBar);
    }

    if (state.isNormaPickPhase()) {
      optionsBar = makeNormaPickPhaseControls();
      border.setRight(optionsBar);
    }

    optionsBar.setMinWidth(256);
    optionsBar.setMaxWidth(256);

    // FIX for text Wrapping
    for (Node n: optionsBar.getChildren()) {
      if (n instanceof Text) {
        Text t = (Text) n;
        t.setWrappingWidth(256);
        t.setTextAlignment(TextAlignment.CENTER);
      }
    }

  }

  /**
   * Calls a method to remake the objects on the left side of the
   * borderpane and to update info.
   */
  public void updateInfoSidebar() {
    optionsBar = makeUnitsInfoSidebar();
    border.setLeft(optionsBar);
    optionsBar.setAlignment(Pos.CENTER);
    optionsBar.setMinWidth(256);
    optionsBar.setMaxWidth(256);
  }

  //region Make Controls methods

  /**
   * Creates a VBox containing StartPhase options.
   * @return
   */
  VBox makeStartPhaseControls() {
    Player owner = gameController.getTurnOwner();
    int chapter = gameController.getChapter();
    String name = owner.getName();
    boolean alive = !(owner.isKOd());
    Text chapterText = new Text("Chapter: " + chapter);
    Text text = new Text("It's " + name + "'s turn!");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(chapterText);
    newVBox.getChildren().add(text);
    if (!alive) {
      Text textStatus = new Text(name+" is KO'd...");
      newVBox.getChildren().add(textStatus);
    }

    Button button = new Button("Start turn!");
    newVBox.getChildren().add(button);
    button.setOnAction(event -> gameController.beginTurn());

    return newVBox;
  }

  /**
   * Creates a VBox containing MovingPhase options.
   * @return
   */
  VBox makeMovingPhaseControls() {
    Player owner = gameController.getTurnOwner();
    int chapter = gameController.getChapter();
    String name = owner.getName();
    boolean alive = !(owner.isKOd());
    Text chapterText = new Text("Chapter: " + chapter);
    Text text = new Text("It's " + name + "'s turn!");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(chapterText);
    newVBox.getChildren().add(text);
    Text textStatus = new Text("It's moving phase.");
    newVBox.getChildren().add(textStatus);


    Button button = new Button("Roll dice!");
    newVBox.getChildren().add(button);
    button.setOnAction(event -> gameController.doMove());

    return newVBox;
  }

  /**
   * Creates a VBox containing HomeStopChoosePhase options.
   * @return
   */
  VBox makeHomeStopChoosePhaseControls() {
    Player owner = gameController.getTurnOwner();
    int chapter = gameController.getChapter();
    String name = owner.getName();
    boolean alive = !(owner.isKOd());
    Text chapterText = new Text("Chapter: " + chapter);
    Text text = new Text("It's " + name + "'s turn!");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(chapterText);
    newVBox.getChildren().add(text);
    Text textStatus = new Text("Do you want to stop at home?");
    newVBox.getChildren().add(textStatus);


    Button button = new Button("Yes");
    newVBox.getChildren().add(button);
    button.setOnAction(event -> gameController.stopAtHome());
    Button button2 = new Button("No");
    newVBox.getChildren().add(button2);
    button2.setOnAction(event -> gameController.continueMoving());

    return newVBox;
  }

  /**
   * Creates a VBox containing EndPhase options.
   * @return
   */
  VBox makeEndPhaseControls() {
    Player owner = gameController.getTurnOwner();
    int chapter = gameController.getChapter();
    String name = owner.getName();
    boolean alive = !(owner.isKOd());
    Text chapterText = new Text("Chapter: " + chapter);
    Text text = new Text("It's the end of " + name + "'s turn!");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(chapterText);
    newVBox.getChildren().add(text);

    if (!alive) {
      int recoveryLeft = owner.getRecoveryLeft();
      Text recoveryText = new Text("They will need to roll a "+recoveryLeft+"\nto get back to the game.");
      newVBox.getChildren().add(recoveryText);
    }

    Button button = new Button("Finish turn");
    newVBox.getChildren().add(button);
    button.setOnAction(event -> gameController.finishTurn());

    return newVBox;
  }

  /**
   * Creates a VBox containing CombatChoosePhase options.
   * @return
   */
  VBox makeCombatChoosePhaseControls() {
    Player owner = gameController.getTurnOwner();
    int chapter = gameController.getChapter();
    String name = owner.getName();
    boolean alive = !(owner.isKOd());
    Text chapterText = new Text("Chapter: " + chapter);
    Text text = new Text("You stumbled upon one or more players!\nDo you want to fight?");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(chapterText);
    newVBox.getChildren().add(text);

    // We proceed to look for possible opponents to fight.
    // To do this we'll iterate through the current panel
    // and generate a button to trigger the fight against it
    // only if that player is not the turn owner.
    IPanel panel = owner.getCurrentPanel();
    for (Player p : panel.getPlayers()) {
      if (!p.equals(owner) && !p.isKOd()) {
        var fightPlayerButton = new Button("Fight against " + p.getName());
        newVBox.getChildren().add(fightPlayerButton);
        fightPlayerButton.setOnAction(event -> gameController.startCombat(p));
      }
    }
    var dontFightButton = new Button("Don't fight anyone");
    newVBox.getChildren().add(dontFightButton);
    dontFightButton.setOnAction(event -> gameController.continueMoving());

    return newVBox;
  }

  /**
   * Creates a VBox containing combatResponseChoosePhase options.
   * @return
   */
  VBox makeCombatResponseChoosePhaseControls() {
    IUnit attacker = gameController.getTurnState().getAttacker();
    IUnit target = gameController.getTurnState().getTarget();
    int incomingDamage = gameController.getTurnState().getAttackValue();
    Text attackDescriptionText = new Text(attacker.getName()+" is attacking "+target.getName()+".");
    Text attackValueText = new Text(attacker.getName() + " got " + incomingDamage + " on their attack roll.");
    Text questionText = new Text(target.getName()+" do you want to defend or evade?");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(attackDescriptionText);
    newVBox.getChildren().add(attackValueText);
    newVBox.getChildren().add(questionText);


    var defendButton = new Button("Defend");
    newVBox.getChildren().add(defendButton);
    defendButton.setOnAction(event -> gameController.defendAgainstCombat());

    var evadeButton = new Button("Evade");
    newVBox.getChildren().add(evadeButton);
    evadeButton.setOnAction(event -> gameController.evadeAgainstCombat());

    return newVBox;
  }

  /**
   * Creates a VBox containing CounterattackPhase options.
   * @return
   */
  VBox makeCounterattackPhaseControls() {
    IUnit counterattacker = gameController.getTurnState().getAttacker();
    Player turnOwner = gameController.getTurnOwner();
    String name = counterattacker.getName();
    Text counterattackText = new Text("It's "+counterattacker.getName()+" time to counterattack!");
    Text hp1Text = new Text("["+name+"] HP: "+counterattacker.getCurrentHP()+"/"+counterattacker.getMaxHP());
    Text hp2Text = new Text("["+turnOwner.getName()+"] HP: "+turnOwner.getCurrentHP()+"/"+turnOwner.getMaxHP());

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(counterattackText);
    newVBox.getChildren().add(hp1Text);
    newVBox.getChildren().add(hp2Text);

    var counterattackButton = new Button("Counterattack!");
    newVBox.getChildren().add(counterattackButton);
    counterattackButton.setOnAction(event -> gameController.startCounterAttack());

    return newVBox;
  }

  /**
   * Creates a VBox containing counterattackResponseChoosePhase options.
   * @return
   */
  VBox makeCounterattackResponseChoosePhaseControls() {
    IUnit attacker = gameController.getTurnState().getAttacker();
    IUnit target = gameController.getTurnState().getTarget();
    int incomingDamage = gameController.getTurnState().getAttackValue();
    Text attackDescriptionText = new Text(attacker.getName()+" is counterattacking at "+target.getName()+".");
    Text attackValueText = new Text(attacker.getName() + " got " + incomingDamage + " on their counterattack roll.");
    Text questionText = new Text(target.getName()+" do you want to defend or evade?");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(attackDescriptionText);
    newVBox.getChildren().add(attackValueText);
    newVBox.getChildren().add(questionText);


    var defendButton = new Button("Defend");
    newVBox.getChildren().add(defendButton);
    defendButton.setOnAction(event -> gameController.defendAgainstCounterattack());

    var evadeButton = new Button("Evade");
    newVBox.getChildren().add(evadeButton);
    evadeButton.setOnAction(event -> gameController.evadeAgainstCounterattack());

    return newVBox;
  }

  /**
   * Creates a VBox containing pathChoosePhase options.
   * @return
   */
  VBox makePathChoosePhaseControls() {
    Player owner = gameController.getTurnOwner();
    int chapter = gameController.getChapter();
    String name = owner.getName();
    boolean alive = !(owner.isKOd());
    Text chapterText = new Text("Chapter: " + chapter);
    Text text = new Text("It's " + name + "'s turn!");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(chapterText);
    newVBox.getChildren().add(text);
    Text textStatus = new Text("You have to choose a path to continue moving.\nPlease click on the board the panel you wish to choose.");
    newVBox.getChildren().add(textStatus);

    /*
    IPanel panel = owner.getCurrentPanel();
    for(IPanel o : panel.getNextPanels()) {
      Button button = new Button(o.toString());
      newVBox.getChildren().add(button);
      button.setOnAction(event -> gameController.continueMovingThrough(o));
    } */


    return newVBox;
  }

  /**
   * Creates a VBox containing recoveryPhase options.
   * @return
   */
  VBox makeRecoveryPhaseControls() {
    Player owner = gameController.getTurnOwner();
    int recoveryLeft = owner.getRecoveryLeft();
    String name = owner.getName();
    Text recoveryText = new Text("Recovery Phase!");
    Text text = new Text(name+" needs to roll a "+recoveryLeft+"\nto get back in the game.");

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(recoveryText);
    newVBox.getChildren().add(text);

    Button button = new Button("Roll recovery trial!");
    newVBox.getChildren().add(button);
    button.setOnAction(event -> gameController.recoveryTrial());



    return newVBox;
  }

  /**
   * Creates a VBox containing NormaPickPhase options.
   * @return
   */
  VBox makeNormaPickPhaseControls() {
    Player owner = gameController.getTurnOwner();
    String name = owner.getName();
    int level = owner.getNormaLevel();
    Text normaClearText = new Text("Norma Clear!\n" + (level-1)+" -> "+(level));
    normaClearText.setTextAlignment(TextAlignment.CENTER);
    Text normaPickHintText = new Text(name + ", please pick your next Norma goal!");
    normaPickHintText.setTextAlignment(TextAlignment.CENTER);

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(normaClearText);
    newVBox.getChildren().add(normaPickHintText);

    int wins = NormaFactory.getWinsNorma(level).getRequirement();
    int stars = NormaFactory.getStarsNorma(level).getRequirement();
    Button pickWinsNormaButton = new Button("Reach "+wins+" wins!");
    newVBox.getChildren().add(pickWinsNormaButton);
    pickWinsNormaButton.setOnAction(event -> gameController.selectWinsNorma());

    Button pickStarsNormaButton = new Button("Reach "+stars+" stars!");
    newVBox.getChildren().add(pickStarsNormaButton);
    pickStarsNormaButton.setOnAction(event -> gameController.selectStarsNorma());



    return newVBox;
  }

  /**
   * Creates a VBox containing GameResults options.
   * These are not actual options because when the game ends
   * there's nothing left to do aside from closing the game and starting again.
   * @return
   */
  VBox makeGameResultsControls() {
    Player owner = gameController.getWinner();
    String name = owner.getName();
    int chapters = gameController.getChapter();
    var finishText = new Text("Game has ended!\nCongratulations "+name+"!");
    finishText.setTextAlignment(TextAlignment.CENTER);
    var chaptersInfoText = new Text("This game lasted for "+chapters+" chapters.");
    chaptersInfoText.setTextAlignment(TextAlignment.CENTER);
    var restartHintText = new Text("To start a new match please close the game window and execute it again!");
    restartHintText.setTextAlignment(TextAlignment.CENTER);

    VBox newVBox = new VBox();
    newVBox.setAlignment(Pos.CENTER);

    newVBox.getChildren().add(finishText);
    newVBox.getChildren().add(chaptersInfoText);
    newVBox.getChildren().add(restartHintText);

    return newVBox;
  }

  //endregion

  /**
   * Creates a VBox containing the sidebar elements that display
   * info about the units.
   * @return
   */
  public VBox makeUnitsInfoSidebar() {
    // Constant for avatar display size
    int AVATAR_SIZE = 112;

    // We will return this VBox with the corresponding elements
    // for the sidebar display
    VBox unitsSidebar = new VBox();
    for(BoardPlayer p : getBoardPlayers()) {

      // Gather Player info first
      Player player = p.getPlayer();
      Image sprite = p.getImageView().getImage();
      String name = ""+player.getName()+"";
      String HPinfo = "HP: " + player.getCurrentHP() + "/" + player.getMaxHP();
      String StarsInfo = "Stars: " + player.getStars();
      String WinsInfo = "Wins: " + player.getWins();
      String NormaInfo = "Norma: " + player.getNormaLevel();
      String NormaGoalInfo = "Goal: " + player.getNormaGoal().getRequirement()
                          + " " + player.getNormaGoal().getRequirementNoun();

      // Create the player band
      HBox playerBand = new HBox();
      ImageView playerAvatar = new ImageView(sprite);
      playerAvatar.setFitHeight(AVATAR_SIZE);
      playerAvatar.setFitWidth(AVATAR_SIZE);
      if (player.isKOd()) {
        playerAvatar.setOpacity(0.65);
      }
      playerBand.getChildren().add(playerAvatar);
      VBox playerBandTextInfo = new VBox();
      var nameText = new Text(name);
      var texts = List.of(nameText, new Text(HPinfo), new Text(StarsInfo), new Text(WinsInfo),
        new Text(NormaInfo), new Text(NormaGoalInfo));
      nameText.setStyle("-fx-font-size: 20px;");
      nameText.setTextAlignment(TextAlignment.LEFT);
      playerBandTextInfo.getChildren().addAll(texts);
      playerBand.getChildren().add(playerBandTextInfo);
      unitsSidebar.getChildren().add(playerBand);
      playerBand.setAlignment(Pos.CENTER_LEFT);
      playerBandTextInfo.setAlignment(Pos.CENTER_LEFT);
      // Resize avatars to match text info height


    }

    // Special case now for Wild Units
    if (gameController.isCurrentWildUnitValid()) {

      // Gather unit info first
      WildUnit wildUnit = gameController.getCurrentWildUnit();
      Image sprite = wildUnitIcon;
      String name = wildUnit.getName();
      String HPinfo = "HP: " + wildUnit.getCurrentHP() + "/" + wildUnit.getMaxHP();
      String StarsInfo = "Stars: " + wildUnit.getStars();
      String WinsInfo = "Wins: " + wildUnit.getWins();
      String StatsInfo = "ATK: "+ wildUnit.getAtk() +"| DEF: " + wildUnit.getDef() +" | EVD: "+wildUnit.getEvd();

      // Create the player band
      HBox wildUnitBand = new HBox();
      ImageView wildUnitAvatar = new ImageView(sprite);
      wildUnitAvatar.setFitHeight(AVATAR_SIZE);
      wildUnitAvatar.setFitWidth(AVATAR_SIZE);
      wildUnitBand.getChildren().add(wildUnitAvatar);
      VBox wildUnitBandInfo = new VBox();
      var nameText = new Text(name);
      var texts = List.of(nameText, new Text(HPinfo), new Text(StarsInfo), new Text(WinsInfo),
        new Text(StatsInfo));
      nameText.setStyle("-fx-font-size: 20px;");
      nameText.setTextAlignment(TextAlignment.LEFT);
      wildUnitBandInfo.getChildren().addAll(texts);
      wildUnitBand.getChildren().add(wildUnitBandInfo);
      unitsSidebar.getChildren().add(wildUnitBand);
      wildUnitBand.setAlignment(Pos.CENTER_LEFT);
      wildUnitBandInfo.setAlignment(Pos.CENTER_LEFT);

    }

    // Special case now for Boss Units
    if (gameController.isCurrentBossUnitValid()) {

      // Gather unit info first
      BossUnit boss = gameController.getCurrentBossUnit();
      Image sprite = bossIcon;
      String name = boss.getName();
      String HPinfo = "HP: " + boss.getCurrentHP() + "/" + boss.getMaxHP();
      String StarsInfo = "Stars: " + boss.getStars();
      String WinsInfo = "Wins: " + boss.getWins();
      String StatsInfo = "ATK: "+ boss.getAtk() +"| DEF: " + boss.getDef() +" | EVD: "+boss.getEvd();

      // Create the player band
      HBox bossUnitBand = new HBox();
      ImageView bossUnitAvatar = new ImageView(sprite);
      bossUnitAvatar.setFitHeight(AVATAR_SIZE);
      bossUnitAvatar.setFitWidth(AVATAR_SIZE);
      bossUnitBand.getChildren().add(bossUnitAvatar);
      VBox bossUnitBandInfo = new VBox();
      var nameText = new Text(name);
      var texts = List.of(nameText, new Text(HPinfo), new Text(StarsInfo), new Text(WinsInfo),
        new Text(StatsInfo));
      nameText.setStyle("-fx-font-size: 20px;");
      nameText.setTextAlignment(TextAlignment.LEFT);
      bossUnitBandInfo.getChildren().addAll(texts);
      bossUnitBand.getChildren().add(bossUnitBandInfo);
      unitsSidebar.getChildren().add(bossUnitBand);
      bossUnitBand.setAlignment(Pos.CENTER_LEFT);
      bossUnitBandInfo.setAlignment(Pos.CENTER_LEFT);

    }

    return unitsSidebar;
  }


  /**
   * Handler for the mouse press on a panel.
   * This is so when a player has to choose a path between more than one
   * next panels it is possible to click on the panel.
   * @return
   */
  public void panelSelected(IPanel panel) {
    // This should only work when on
    // pathChoosePhase, so:
    if (gameController.getTurnState().isPathChoosePhase()) {
      // Being on the phase is not enough, we need to check that
      // the panel selected is effectively one available panel
      // from the current turn's owner panel.
      IPanel actualPanel = gameController.getTurnOwner().getCurrentPanel();
      if (actualPanel.getNextPanels().contains(panel)) {
        gameController.continueMovingThrough(panel);
      } else {
        setBottomText("This panel cannot be selected, you have to choose from the adjacent ones to "+gameController.getTurnOwner().getName()+"'s current panel.");
      }
    }


  }

  /**
   * This returns a list of the BoardPlayers.
   * (Useful for iteration purposes)
   * @return
   */
  public List<BoardPlayer> getBoardPlayers() {
    return List.of(player1,player2,player3,player4);
  }

  /**
   * This returns the Image corresponding to a Panel Sprite
   * with a given string.
   * @param str
   * @return
   */
  public static Image getPanelSprite(String str) {
    Image ret = neutralPanel;
    switch (str) {
      case "HOME":
        return homePanel;
      case "BOSS":
        return bossPanel;
      case "BONUS":
        return bonusPanel;
      case "DROP":
        return dropPanel;
      case "ENCOUNTER":
        return encounterPanel;
    }
    return ret;
  }

}

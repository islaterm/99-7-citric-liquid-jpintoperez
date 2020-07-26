# 99.7% Citric Liquid  


This project (99.7% Citric Liquid) is a java-written simplified clone of **100% Orange Juice** developed originally by [Orange Juice](http://daidai.moo.jp/) and distributed by [Fruitbat Factory](https://fruitbatfactory.com/).  

On the current state of this project: **basic gameplay functionality is implemented using a Model-View-Controller design strcuture. Game's core components like *panels, player movement and interactions, game flow's turn phases and user interaction* are built inside the project. The project is missing some features from the original game like *cards*, although, due to this project being developed following OOP and SOLID principle guidelines, I expect that implementing those shouldn't be that complicated.

**[!] Note:** *Board background and panels graphic assets were drawn specifically for this, however, character images are just placeholders used for testing and visualization and don't belong to me nor the class' assignment responsible team.*
**[!] Note 2:** *Jamin's (a classmate) picture is used with their total authorization and consent.*

## Executing (Application)

The game can be executed making use of the CitricLiquid app that is included on the **src/main/java/com.github.cc3002.citricliquid.gui** package.

## Playing Instructions

After executing the main application the GUI will start up right away and create a new game on the practice board that is included. (More boards can be added as methods on the controller.)

1. The game will start for 4 players (Pikachu, Jamin, Pusheen and La Rosalia)
2. The left sidebar shows information about the units that are currently in-game.
3. The right sidebar shows the game's flow and offers controls to take decisions and make actions.
4. All dice-rolling and RNG dependant actions are automatic. (i.e. movement, attack, defense and evade rolls are done automatically, you only have to choose if to defend, evade, engage in combat or to stop on a certain panel)
5. Once a Player reaches Norma level 6 the game is over and to start again the Application needs to be relaunched.

Some extra features to point:

* Hovering the mouse over a panel will give a short description of what does that panel do.
* Hovering the mouse over a unit in the board will show their current status and their stats.


## Executing (Tests)  

Tests can be executed by running the test packages included at _**src/test/java/com.github.cc3002.citricjuice.model**_ (JUnit is required).  

## About the Structure  


Currently the project contains three major/main class structures:  


* Model


  * Units
  	
  	>Units represent players, monsters (wild units) and bosses, they can interact with each other by fighting.
  	
   * Panels  

      >Panels are the atomic unit of the game's board. Each panel is a cell that the players can cross and step on, there are different kinds of panels with different kind of effects.

* Controller
	>The controller class handles all other components together, it's the bridge that makes the flow of the game to occur. Also, there are methods to act as user interaction like the beginTurn(), useCard(), doMove(), startCombat(), continueMoving(), stopAtHome(), finishTurn(), etc, methods. This can actually get a game match simulation running.
  

* View

  > The view consists of a window divided in four regions, the left bar that shows information about units all the time, the right bar that shows the controls and actions the players can use to interact in the game, the bottom footer that gives a hint of information when the mouse hovers on elements in the board and the center region, where the board and characters are displayed.

These classes are implemented following an Object Oriented Programming paradigm in which inheritance and polymorphism is used to take advantage of code reutilization and responsibility delegation through the different kind of components present in the game. Externally to that, Model-View-Controller structure is used to link each one of the components to give a graphical display to the players so they can actually play the game.

___
The previously mentioned classes interact with each other in different ways:

### Between units  


> There are combat features implemented like defending or evading from an incoming attack, though, since user interaction is not ready yet it is assumed that the input requires the result of the attacker's attack roll to be entered on the call to the combat methods.  
>
> * Victory after KOing another unit is currently implemented, giving the corresponding reward stars and wins to each type of unit (player, wild, boss).  


### Units, panels and controller 

> Units can interact with panels and apply their effects, each time something relevant happens to the player, the player notifies the controller that their state has changed. This causes the controller to perform certain actions according to what happens.

### Controller
> The controller class can handle the game's setup logic by creating panels and units without the need of having to create each instance separatedly and managing it manually. 
> Also, the controller is capable to run through the game flow turn phases doing different actions, e.g. officially starting a turn, resolving player decisions like picking whether to start a combat or keep moving through the board.
> The controller also notifies the View each time that there are changes in the current flow of the game, this way the View can update the controls according to what options/decisions the players can take on the moment.

### View

> The view is constantly updating the window to show the status of characters in real time, to keep a clean interface it was decided to keep the controls, information and board graphics on the same side all the time, this helps to understand how the GUI works quite quickly.

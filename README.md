  
    
  
# 99.7% Citric Liquid  
  
    
This project (99.7% Citric Liquid) is a java-written simplified clone of **100% Orange Juice** developed originally by [Orange Juice](http://daidai.moo.jp/) and distributed by [Fruitbat Factory](https://fruitbatfactory.com/).  
  
    
  
On the current state of this project: ***gameplay functionality is implemented by parts***, therefore it is ***not*** on a playable state. However, many of the game core components like *panels, player movement and interactions, game flow's turn phases and preliminar user interaction* are already built inside the project module. The project as it is right now is missing some features from the original game like *encounters, cards and a graphical interface.*
  
  
## Executing (Tests)  
    
Tests can be executed by running the test packages included at _**src/test/java/com.github.cc3002.citricjuice.model**_ (JUnit is required).  
  
## About the Structure  
  
    
Currently the project contains three major/main class structures:  
  
  
* Units
	>Units represent players, monsters (wild units) and bosses, they can interact with each other by fighting.
 * Panels  
	 >Panels are the atomic unit of the game's board. Each panel is a cell that the players can cross and step on, there are different kinds of panels with different kind of effects.
* Controller
	>The controller class handles all other components together, it's the bridge that makes the flow of the game to occur. Also, there are preliminar methods to act as user interaction like the beginTurn(), useCard(), doMove(), startCombat(), continueMoving(), stopAtHome(), finishTurn(), etc, methods. This can actually get a game match simulation running.
  
These classes are implemented following an Object Oriented Programming paradigm in which inheritance and polymorphism is used to take advantage of code reutilization and responsibility delegation through the different kind of components present in the game.
___  
The previously mentioned classes interact with each other in different ways:
  
### Between units  
  
    
> There are combat features implemented like defending or evading from an incoming attack, though, since user interaction is not ready yet it is assumed that the input requires the result of the attacker's attack roll to be entered on the call to the combat methods.  
> * Victory after KOing another unit is currently implemented, giving the corresponding reward stars and wins to each type of unit (player, wild, boss).  
  
    
### Units and panels  
  
> Units can interact with panels and apply their effects, however there are currently 2 unimplemented panels due to lack of user interaction and game flow implementation which are required for these panels to actually work, these panels are:  
>*  _**Encounter Panel**_  
> *  _**Boss Panel**_

### Controller
> The controller class can handle the game's setup logic by creating panels and units without the need of having to create each instance separatedly and managing it manually. 
> Also, the controller is capable to run through the game flow turn phases doing different actions, e.g. officially starting a turn, resolving player decisions like picking whether to start a combat or keep moving through the board.
> These "user interaction"-like methods can easily be adapted to be used by a graphical interface in which the user could pick the desired options.

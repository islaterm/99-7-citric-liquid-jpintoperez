
  

# 99.7% Citric Liquid

  

This project (99.7% Citric Liquid) is a java-written simplified clone of **100% Orange Juice** developed originally by [Orange Juice](http://daidai.moo.jp/) and distributed by [Fruitbat Factory](https://fruitbatfactory.com/).

  

On the current state of this project: ***gameplay functionality is not implemented yet***, therefore it is ***not*** on a playable state. However, many of the game components like *board panels, players, wild enemies and boss units* are already built inside the project module.

  

## Executing (Tests)

  

Tests can be executed by running the test package included at _**src/test/java/com.github.cc3002.citricjuice.model**_ (JUnit is required).

  

## About the Structure

  

Currently the project contains two main class archetypes:

  

* Units

  

* Panels

  

They're actually implemented with different kinds for each one through sub-classes with their respective methods according to what they do in-game.

  

These classes interact with each other through different mechanisms:

  

### Between units

  

> There are combat features implemented like defending or evading from an incoming attack, though, since user interaction is not ready yet it is assumed that the input requires the result of the attacker's attack roll to be entered on the call to the combat methods.
> * Victory after KOing another unit is currently implemented, giving the corresponding reward stars and wins to each type of unit (player, wild, boss).

  

### Units and panels

  

> Units can interact with panels and apply their effects, however there are currently 2 unimplemented panels due to lack of user interaction and game flow implementation which are required for these panels to actually work, these panels are:
>*  _**Encounter Panel**_
> *  _**Boss Panel**_
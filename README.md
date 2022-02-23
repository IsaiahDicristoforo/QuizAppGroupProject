# Quiz App

## Introduction

Our application will be similar to Wordle and Kahoot, two popular online word/quiz-based games. Wordle is a word guessing game where people have six attempts to correctly guess a word. Kahoot is a multiplayer quiz game where educators typically create a quiz that students take in real-time attempting to move up the leaderboard. Our game will allow teachers to create custom wordles that will be presented to students in a multiplayer format.

## Storyboard

[Storyboard](https://projects.invisionapp.com/prototype/ckyyt0m59003sb301po5ikef8/play)

## Functional Requirements

### Requirement 01: Create a Wordle Quiz

#### Scenario

As an educator, I want to create a custom Wordle quiz for my students, so that I can quiz them and test their knowledge about spcifics words and terms disucssed in class.

#### Dependencies

The Wordle quiz is stored in a MySql database and is associated with the creator's account.

#### Assumptions

The user creating the wordle quiz has an account to save, share, and start the Wordle quiz.

#### Examples

1.1\
Given that a educator adds a Wordle question to their quiz titled English 1001 Vocabulary,\
When the educator adds the word *intermittent* to their quiz,\
Then the word should have a list of 0 or more hints, time limit in seconds, and total number of allowed guesses.

1.2\
Given that a educator adds a Wordle question to their quiz titled English 1001 Vocabulary,\
When the educator add the word *abc123* to their quiz,\
Then the invalid word should not be added to the wordle list, and the user should be notified that they need to enter a valid word.

### Requirement 02: Start a Wordle Quiz Game

#### Scenario

As a host of a game, I want to start an existing Wordle game so that people can join and play.

#### Assumptions

The host of the Wordle can view a list of their own, and other user created Wordle lists.

#### Examples

1.1\
Given that a logged in user starts their Wordle quiz, titled Countries Around the World,\
When the user starts the game\
Then a game code such as 19583ab should generated so that players can play the game.

1.2\
Given that a user enter a game code such as 19583ab to join a game, and an open game with that code exists,\
When the user is directed to the game queue, they will be prompted to enter a game username, ie: Isaiah123\
Then they should be entered into the game and see a list of other waiting players and their usernames.

1.3\
Given that a user enter a game code such as 19583ab to join a game,\
When the game code does not belong to a started game, \
Then they should receive a messagge saying "Invalid game code. Game does not exist".

### Requirement 03: Play

#### Scenario

As a player of a Wordle game, I want to be able to guess a specific word to earn points, and compete against other players.

#### Assumptions

The player of the game can view some information about the word, such as the length, and any hints associated with the word.

#### Examples

1.1\
Given that the wordle solution is the word *house*,\
When a user guesses the word *homes*"\
Then the information available to the player should be that the letter h and o are in the word and in the right location, e and s are in the word but in the wrong location, and that m is not in the word.

1.2\
Given that a user correctly enters the word *house* and the word *house* is the solution,\
When the user has entered the word within the allowed number of guesses, 5, and within the time limit, 60 seconds,\
Then they should be given a minimum of 1000 points, plus any bonus points given for guessing the word before other players.

1.3\
Given that a user does not guess the word *house*,\
When the has failed to guess the word within a sixty second time limit and/or have used up their total guesses,\
Then they should be given zero points.

## Class Diagram

![dto](https://user-images.githubusercontent.com/37581557/151727563-0c1a89e0-cd50-4e15-bace-f62af457ec50.PNG)
![dao](https://user-images.githubusercontent.com/37581557/151719041-3ae7727f-0d31-4e7f-886b-f1ef2a286850.PNG)

## Json Schema

```json
{
   "type" : "object",
   "properties" : {
    "hints" : {
      "type" : "object"
    },
    "questionTimeLimit" : {
      "type" : "integer"
    },
    "allowedGuesses" : {
      "type" : "integer"
    },
    "totalCharactersInWord" : {
      "type" : "integer"
    },
    "answer" : {
      "type" : "string"
    }
  }
}
```

## Scrum Roles

- Isaiah Dicristoforo Project Owner and Scrum Master
- Eric Davin UI
- Ian Hegarty UI
- Christian Turner BL + Persistence
- Mahesh Gowda BL + Persistence

## Project Link

[Our GitHub Project](https://github.com/IsaiahDicristoforo/QuizAppGroupProject)

## Scrum/Kanban Board

Our milestones, issues, and project boards are all listed here on GitHub.

## Weekly meeting

We plan to meet weekly on Discord Tuesdays at 5 P.M

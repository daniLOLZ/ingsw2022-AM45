
# Prova Finale Ingegneria del Software 2022
## Gruppo AM45

- ###   Daniele Dente ([@daniPolii](https://github.com/daniPolii)) <br> daniele.dente@mail.polimi.it
- ###   Daniele Di Santi ([@DanieleDiSanti](https://github.com/DanieleDiSanti)) <br> daniele.disanti@mail.polimi.it
- ###   Valerio Donno ([@Zeekuri](https://github.com/Zeekuri)) <br> valerio.donno@mail.polimi.it

🔴
🟢
🟡

<H1>Starting the application</H1>
<b>Server:</b>

    java -jar Eriantys_AM45.jar --server --host <hostname> --port <port number>
<b>Client - CLI:</b>

    java -jar Eriantys_AM45.jar --cli --host <hostname> --port <port number>
<b>Client - GUI:</b>

    java -jar Eriantys_AM45.jar --gui --host <hostname> --port <port number>

<H1> Requirements </H1>

<h3>Installing</h3>
You will need to install the java jdk version 17.0.2 or higher to run the game


<h3> CLI: </h3>
To make sure that the screen is cleared properly when
it needs to be, the terminal on which the application is launched
must support ANSI escape codes.

The terminal must have a width of at least *`145`* characters to
properly show the game interface.

<h3>GUI:</h3>


<h1>Game instructions:</h1>
<h6>To play the game on the CLI, you must follow some 
rules to input the commands</h6>

While in a game, to input a command ,you must type 
in exactly the name of the command, including the
underscores. This command is case-insensitive 

`ex: SELECT_STUDENT`

When selecting elements identified by their id, the 
parameter to write will be the id

When selecting elements identified by their position (students at the 
entrance of your board and on character cards), 
the parameter to write will be their position, starting from 1

When using commands that require multiple elements to be
selected at once, the parameter will be written enclosed 
in square brackets, separated by commas and without
any whitespaces in them <br>
`ex: [value1,value2,...,valueN]`

Such commands are the ones used to select the components 
required to play a character card
`ex: SELECT_STUDENTS_ON_CARD [2,3]`

The colors of the students have to be in capital letters <br>
Available student colors: Red, Green, Blue, Pink, Yellow
`ex: RED`


<H1> Coverage </H1>

| Package |   Class %    |    Method %    |      Line %      |
|:-------:|:------------:|:--------------:|:----------------:|
|  Model  | 100% (61/61) | 86% (452/525)  | 87% (2068/2364)  |


<H1>Implemented Components</H1>

| Functionality                      | State |
|:-----------------------------------|:-----:|
| Basic rules                        |  🟢   |
| Complete rules                     |  🟢   |
| Controller                         |  🟢   |
| Network                            |  🟢   |
| CLI                                |  🟢   |
| GUI                                |  🟢   |
| <b>AF</b> Character cards          |  🟢   |
| <b>AF</b> 4 player game            |  🟢   |
| <b>AF</b> Multiple games           |  🟢   |
| <b>AF</b> Persistence              |  🔴   |
| <b>AF</b> Disconnection resilience |  🔴   |


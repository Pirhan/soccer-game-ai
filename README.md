# java-soccer-game-ai
An expandable 5 v 5 soccer game simulation that puts an AI against another AI in java. 
![giphy](https://user-images.githubusercontent.com/64742446/138037100-6c63e2f0-6729-42f3-aa14-5fe4a4707b01.gif)

**Pison** is the name of the implemented AI, and some of the features of the AI are listed down below:
- Smart dribbling
- Smart shooting
- Smart passing
- Different formations that the players can take after analyzing the state of the game
- Ease of changing the difficulty of the AI by tuning some of the present parameters
- Randomization (with a small probability)


Here, you can see the simplified version of the implemented AI's decision making process;
![image](https://user-images.githubusercontent.com/64742446/138036953-faa06b75-a7a6-44e7-a0cc-2ae0d1cea5fa.png)


Here are 3 simple steps to start a match:
- Initialize the project structure to include the following libs (they are provided under **libs** folder)
  - **_lwjgl_** (included from slick2d)
  - **_javaGeom_**
  - **_jgrapht-core_**
  - **_slick2d_** (unzip the zip file, and don't forget to also provide the native library locations inside the zip file)
- Run SoccerDemo.java
- Sit back, relax, and grab yourself a beverage!

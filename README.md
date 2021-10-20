# java-soccer-game-ai
An expandable 5 v 5 soccer game simulation that puts an AI against another AI in java. 
![giphy](https://user-images.githubusercontent.com/64742446/138037100-6c63e2f0-6729-42f3-aa14-5fe4a4707b01.gif)

There exists an implemented AI in the projects' folder. Its' name is **Pison**, and some of the features of the AI are listed down below:
- Smart dribbling
- Smart shooting
- Smart passing
- Different formations that the players can take after analyzing the state of the game
- Ease of changing the difficulty of the AI by tuning some of the present parameters
- Randomization (with a small probability)


Here, you can see the simplified version of the **Pison**'s decision making process;
![image](https://user-images.githubusercontent.com/64742446/138036953-faa06b75-a7a6-44e7-a0cc-2ae0d1cea5fa.png)


There are 3 steps you need to take to start a match:
- Initialize the project structure to include the following libs (they are provided under **libs** folder)
  - **_javaGeom_**
  - **_jgrapht-core_**
  - **_slick_** (unzip the zip file, add both lwjgl.jar and slick.jar from the **libs** folder into **Libraries** inside your **Project Structure**, and also don't forget to provide **lwjgl.dll** as the **Native Library Locations** if you are working on Windows.)
  - **_lwjgl_** (mentioned above)
- Run SoccerDemo.java
- Sit back, relax, and grab yourself a beverage!

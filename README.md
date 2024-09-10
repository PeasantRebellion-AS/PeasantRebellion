# Peasant Rebellion


## Description of game concept
Peasant Rebellion is a spin-off of the classic Atari game Space Invaders. There are starving peasants approaching in waves trying to attack you by shooting arrows. The goal of the game is to stay alive as long as possible, killing peasants and getting the highest possible score. The game ends when the player loses all their lives. The player is then prompted if they would like to save their score to the global leaderboard, inspired by old arcade machines, which is accessible from the main menu.

## Trailer 
[![Trailer](https://github.com/user-attachments/assets/cf67dd2c-2abd-49fd-a26d-7d81f51ff790)
](http://www.youtube.com/watch?v=UHAvuxEaq4o "TDT4240 Software Architecture project group 01 - Peasant Rebellion")

## Screenshots
<div align="center" display="block">
    <img src="https://github.com/user-attachments/assets/23f8d68f-538f-477e-89f6-42b3af7ccb29" width="50%"/>
    <img src="https://github.com/user-attachments/assets/79d0fd1a-6a27-4f4a-8b14-6b0e602edc3c" width="50%"/>
    <img src="https://github.com/user-attachments/assets/66e5227e-ef51-4c3a-8f94-2e0f040fe87a" width="50%"/>
</div>


## How to compile and run the project
In a command line, navigate to the root of the project and enter these commands

```
./gradlew assembleDebug
```
```
./gradlew installDebug
```

There should now be a .apk file located under /android/build/outputs/apk/debug

Create an Android Virtual Device (AVD) by following these [guidelines](https://developer.android.com/studio/run/managing-avds#createavd), or [pair up your Android phone](https://developer.android.com/studio/run/device)

Navigate to Android/Sdk/emulator and run the following command

```
./emulator -avd avd-name
```

Where *avd-name* is the name of your virtual device, you can list all AVDs by entering ```./emulator -list-avds```

Finally, navigate to Android/Sdk/platform-tools and run


```
./adb install path/to/debug-apk.apk
```

Where *path/to/debug-apk.apk* is the path to the .apk file that was created by running ```./gradlew installDebug```

The app should now be available to run on either the AVD or your Android phone

## How to run with Android Studio

This assumes that you have already created a virtual device or paired up your Android phone

Download and open the latest version of Android Studio. Import the project by going to File → Import project

Lastly press the green run button on the top to run the project


## Project Structure
Below is the tree of our project. Showcasing our folder structure and files
```bash
├───android
│   │   AndroidManifest.xml
│   │   build.gradle
│   │   google-services.json
│   │   ic_launcher-web.png
│   │   proguard-rules.pro
│   │   project.properties
│   │
│   └───src
│       └───main
│           └───kotlin
│               └───com
│                   └───peasantrebellion
│                       └───android
│                               AndroidLauncher.kt
│                               Firebase.kt
│
├───assets
│   │   copper_coin.png
│   │   game_icon_background.png
│   │   peasant_rebellion_music.mp3
│   │   Peralta-Regular.ttf
│   │
│   ├───hearts
│   │       heart_empty.png
│   │       heart_full.png
│   │
│   ├───menu
│   │       back_button.png
│   │       back_button_small.png
│   │       crown.png
│   │       double_damage_icon.png
│   │       double_shot_icon.png
│   │       game_background.png
│   │       in_game_settings.png
│   │       knob.png
│   │       large_button.png
│   │       leaderboard_background.png
│   │       main_menu_background.png
│   │       piercing_shot_icon.png
│   │       placeholder_copper_coin.png
│   │       plank.png
│   │       settings_background.png
│   │       settings_icon.png
│   │       shop_menu.png
│   │       side_menu.png
│   │       slider.png
│   │       splash_text.png
│   │       sword.png
│   │       top_bar.png
│   │       triple_damage_icon.png
│   │       triple_shot_icon.png
│   │       upgrade_shop_icon.png
│   │
│   ├───peasant
│   │   ├───easy
│   │   │       easy_peasant1.png
│   │   │       easy_peasant2.png
│   │   │       easy_peasant3.png
│   │   │       easy_peasant4.png
│   │   │       easy_peasant5.png
│   │   │
│   │   ├───hard
│   │   │       hard_peasant1.png
│   │   │       hard_peasant2.png
│   │   │       hard_peasant3.png
│   │   │       hard_peasant4.png
│   │   │       hard_peasant5.png
│   │   │
│   │   └───medium
│   │           medium_peasant1.png
│   │           medium_peasant2.png
│   │           medium_peasant3.png
│   │           medium_peasant4.png
│   │           medium_peasant5.png
│   │
│   ├───player
│   │       player1.png
│   │       player2.png
│   │       player3.png
│   │       player4.png
│   │       player5.png
│   │       player6.png
│   │       player7.png
│   │
│   ├───projectiles
│   │       arrow.png
│   │
│   ├───sfx
│   │       bow-shooting.wav
│   │       game-over.wav
│   │       player-hit.wav
│   │
│   └───tutorial
│           tutorial_1.png
│           tutorial_2.png
│           tutorial_3.png
│           tutorial_4.png
│           tutorial_5.png
│           tutorial_6.png
│           tutorial_7.png
│
├───core
│   │   build.gradle
│   │
│   └───src
│       └───main
│           └───kotlin
│               └───com
│                   └───peasantrebellion
│                       │   PeasantRebellion.kt
│                       │   Screen.kt
│                       │
│                       ├───controller
│                       │   │   Controller.kt
│                       │   │   GameController.kt
│                       │   │   GameEndController.kt
│                       │   │   LeaderboardController.kt
│                       │   │   MainMenuController.kt
│                       │   │   SettingsController.kt
│                       │   │   TutorialController.kt
│                       │   │
│                       │   └───utility
│                       │           Input.kt
│                       │
│                       ├───model
│                       │   │   Game.kt
│                       │   │   Leaderboard.kt
│                       │   │
│                       │   ├───components
│                       │   │       AnimationComponent.kt
│                       │   │       BodyComponent.kt
│                       │   │       CopperDropperComponent.kt
│                       │   │       HealthComponent.kt
│                       │   │       ProjectileComponent.kt
│                       │   │       ScoreValueComponent.kt
│                       │   │       ShooterComponent.kt
│                       │   │       TextureComponent.kt
│                       │   │       UserControlledComponent.kt
│                       │   │
│                       │   ├───entities
│                       │   │       Arrow.kt
│                       │   │       Peasant.kt
│                       │   │       Player.kt
│                       │   │
│                       │   └───systems
│                       │           AnimationSystem.kt
│                       │           CoinSystem.kt
│                       │           CollisionSystem.kt
│                       │           EnemyMovementSystem.kt
│                       │           EnemyWaveSystem.kt
│                       │           HealthSystem.kt
│                       │           PlayerControlSystem.kt
│                       │           ProjectileMovementSystem.kt
│                       │           ScoreSystem.kt
│                       │           ShootingSystem.kt
│                       │           UpgradeSystem.kt
│                       │
│                       └───view
│                           │   GameEndView.kt
│                           │   GameView.kt
│                           │   LeaderboardView.kt
│                           │   MainMenuView.kt
│                           │   SettingsView.kt
│                           │   TutorialView.kt
│                           │   View.kt
│                           │
│                           └───utility
│                                   Button.kt
│                                   MenuFont.kt
│
├───gradle
│   └───wrapper
│           gradle-wrapper.jar
│           gradle-wrapper.properties
│
└───lwjgl3
    │   build.gradle
    │   nativeimage.gradle
    │
    └───src
        └───main
            ├───kotlin
            │   └───com
            │       └───peasantrebellion
            │           └───lwjgl3
            │                   Lwjgl3Launcher.kt
            │                   StartupHelper.kt
            │
            └───resources
                    libgdx128.png
                    libgdx16.png
                    libgdx32.png
                    libgdx64.png

```

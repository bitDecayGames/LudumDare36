# Ludum Dare 36
# Sporg's Mishap

## Initialization

### Install Animagic
1. ```git clone git@github.com:Kenoshen/animagic.git```
2. ```cd animagic/```
3. ```mvn install```

### Install Jump
1. ```git clone git@github.com:bitDecayGames/Jump.git jump```
2. ```cd jump/```
3. ```mvn install```

### Setup this project
1. ```git clone git@github.com:bitDecayGames/LudumDare36.git ludumdare36```
2. ```cd ludumdare36/```
3. ```mvn compile```
4. Go into the project in your IDE, right click the Launcher.java file and run as a main class. (This should throw an error)
5. Edit the launch configuration and add `dev` as a program argument in order to get it to build the image atlas


## Package to Distribute

```mvn package```

Find the file in ```target/LudumDare36-<version>-packaged.zip```

Unzip the file, double click the jar.

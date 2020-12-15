# PLD - AGILE
PD AGILE (4IF - 2020) is an application finding best tour given lists of pickups and deliveries

## Requirements
- Java JDK 13
- Maven

## Setup the project / Installation
First you have to clone the project to your local repository :
```
$ git init
$ git clone URL
```

Then you have to build and run the project with the following commands :
```
$ mvn clean install
$ mvn exec:java
```
## Project architecture
- **src/main** : Contains .java files organised in packages.
    - **main/java/**
        - **command** : Classes concerning memento pattern
        - **config** : Classes encapsulating global variables
        - **controller** : Classes concerning the controller in the MVC pattern
        - **dijkstra** : Classes to execute Dijktra's algorithm
        - **model** : Classes concerning the model in the MVC pattern            
        - **tsp** : Classes to find TSP solution (complete graph, iterator, branch and bound)
        - **view** : Classes concerning the view in the MVC pattern
        - **xml** : Classes to parse XML data into intelligble structures 

- **src/test** : Contains .java files of the test classes.
- **pom.xml** : Contains maven dependencies (JUnit)


## Authors
- BEL Corentin
- SIERRA Camilo
- GALARZA Javier
- OUVRARD Mathieu
- VERSMEE Erwan
- GIRARD Andrieu
- KERMANI Benjamin

## License
[INSA Lyon](https://www.insa-lyon.fr/)

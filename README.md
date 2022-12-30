# Orthographic Viewer

## Building the project

The project uses maven and can be built using:

```
mvn clean package
```
This will generate a standalone jar with dependencies in the `/target` directory.

## Running the project

Run the `orthographic_viewer.jar` using
```
java -jar orthographic_viewer.jar
```

Additionaly you can run the project through maven using:
```
mvn clean install exec:java
```

You may also use the `generate_jar.sh` script to generate a fresh jar of the project.

## Opening files
Once you have the application running you can open a file by going to File -> Open.

## Noteable Issue withe Graphics in JavaFX

- Application lags when drawing size on the canvas is large. This seems to be a common issue with the library that this project uses:
  - https://bugs.openjdk.org/browse/JDK-8090755
  - https://community.oracle.com/tech/developers/discussion/3755802/rendering-a-canvas-on-a-background-thread-is-very-slow-when-updateing-the-scene-graph
  The source suggests that this is not an issue on Windows and MacoS.
- An overall better solution would be to do this in OpenGL using C/C++.

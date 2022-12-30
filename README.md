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

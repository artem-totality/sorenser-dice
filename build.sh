#!/bin/bash

echo "Compiling Java sources..."
javac --enable-preview --release 23 -d bin src/ie/atu/sw/*.java

if [ $? -eq 0 ]; then
    echo "Creating JAR file..."
    jar cvfm dice.jar MANIFEST.MF -C bin .
    echo "Build successful! JAR file created: dice.jar"
else
    echo "Compilation failed!"
    exit 1
fi
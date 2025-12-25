#!/bin/bash

echo "Compiling Java sources..."
javac --enable-preview --release 23 -d bin src/ie/atu/sw/*.java

if [ $? -eq 0 ]; then
    echo "Creating JAR file..."
    jar cvfm dice.jar MANIFEST.MF -C bin .
    echo "Generating Javadoc..."
    javadoc --enable-preview --release 23 -d docs -sourcepath src ie.atu.sw
    echo "Build successful! JAR file: dice.jar, Docs: docs/"
else
    echo "Compilation failed!"
    exit 1
fi
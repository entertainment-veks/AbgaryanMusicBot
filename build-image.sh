#!/bin/bash
mvn package
mkdir -p target/dependency
cd target/dependency
jar -xf ../*.jar
cd ../../
docker build --tag abgaryan-team/music-bot:0.0.1-ALPHA .

#!/bin/sh

mvn clean versions:set -DnewVersion=$NEW_VERSION -f pom.xml
mvn clean install -Dmaven.test.skip=true -f pom.xml
mvn clean versions:revert -f pom.xml

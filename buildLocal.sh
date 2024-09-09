#!/usr/bin/env bash

# Собрать приложение в локальный регистри
mvn clean package -DskipTests jib:dockerBuild
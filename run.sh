#!/bin/bash

JAVA_OPTS="$JAVA_OPTS \
        -Dspring.profiles.active="${SPRING_PROFILE}" \
        -server \
        "${JVM_MEMORY}" \
"

set -- java $JAVA_OPTS -jar app.jar
exec "$@"


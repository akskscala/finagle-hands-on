#!/bin/sh
java -Xmx2048M -XX:MaxPermSize=512M -jar `dirname $0`/lib/sbt-launch.jar "$@"


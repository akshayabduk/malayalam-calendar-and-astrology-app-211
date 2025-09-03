#!/bin/bash
cd /home/kavia/workspace/code-generation/malayalam-calendar-and-astrology-app-211/malyalam_calender_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi


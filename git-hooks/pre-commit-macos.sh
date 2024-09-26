#!/bin/sh

######## KTLINT-GRADLE HOOK START ########

CHANGED_FILES="$(git --no-pager diff --name-status --no-color --cached | awk '$1 != "D" && $2 ~ /\.kts|\.kt/ { print $2}')"

if [ -z "$CHANGED_FILES" ]; then
    echo "No Kotlin staged files."
    exit 0
fi;

echo "Running ktlint over these files:"
echo "$CHANGED_FILES"

./gradlew --quiet formatKotlin -PinternalKtlintGitFilter="$CHANGED_FILES"

echo "Completed ktlint run."

echo "$CHANGED_FILES" | while read -r file; do
    if [ -f $file ]; then
        git add $file
    fi
done

echo "Completed ktlint hook."
######## KTLINT-GRADLE HOOK END ########
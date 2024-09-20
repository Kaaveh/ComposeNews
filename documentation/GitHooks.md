# Git Hooks

This project has some Git hooks included inside the [git-hooks](/git-hooks) folder. These hooks can be installed automatically via the [setup.sh](/git-hooks/setup.sh) (just windows machine currently, support others OS comming soon).

## Pre-Commit

There is a [pre-commit](/git-hooks/pre-commit-windows.sh) hook that will automatically run Ktlint formatting over any modified Kotlin files. This way you can just commit your code and trust that formatting happens behind the scenes, without having to consciously worry about it.

## Pre-Push

There is a [pre-push](/git-hooks/pre-push-windows.sh) hook that will run static analysis checks before any code is pushed up to the remote repository. This way, if you have any code smells, you can be alerted right away without waiting for the GitHub Actions to fail. 

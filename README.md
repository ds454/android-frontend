# Android Frontend

Repo for the frontend of Locomotive, built for Android.

## Getting Started

Before doing anything, this repo needs to be cloned recursively. Make sure git can use your github username and token without prompts, and run the following command.
```
git clone --recurse-submodules https://github.com/ds454/android-frontend.git
```
This pulls the repository and the submodule `local-dev` which is needed to make sure everyone has the same versions of gRPC protos, the docker compose stack, and the Bruno project.

the git submodule is a reference to another repo inside a parent repo. In this situation, `android-frontend` is the parent repo, and `local-dev` is the submodule. By default submodules aren't pulled, so we need to specifically tell git to pull `local-dev` with the `--recurse-submodules` flag.

You have a couple different options for credentials.
- Enter username and token each time you open the container
- Store your credentials in git

If you want to do the second option, you will need to enter your username and token the first time the container is run. After that, it should cache your credentials so you won't need to enter them again. You should still save your token somewhere in case something weird happens.

#### Automatically updating `local-dev`
A way to automate updating the `local-dev` submodule needs to be implemented. Here is the command that needs to be run each time the project is opened.
```
git submodule update --remote --merge
```
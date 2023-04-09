# Contributing guide

Contributions of any kind are welcome, including small documentation fixes. If the change is large in scope, you should consult the maintainers in an issue first. Before you get started on your contribution, please fully read this page. 

## Git Authorship

Before you commit, make sure that you have Git authorship correctly set up on your machine:
```
git config --global user.name "Your Name"
git config --global user.email your.email@example.com
```

## Pull Request Etiquette

Contributions should be performed in your own fork of this repository, and should be submitted by opening a pull request. Before you submit a pull request though, please enable GitHub Actions in your fork and ensure that any code changes build in CI.

Pull requests should have a single focus. If you want to contribute multiple things (e.g. fix "this" and implement "that" feature), please submit multiple pull requests. Additionally, please squash your commits before submitting them in a pull request, and be sure to use meaningful messages in commits that are part of submitted pull requests.

## Building and Installing Locally

DisQuark requires Java 11 or later to build.

To build DisQuark, run the Maven wrapper by invoking `./mvnw` on your machine from the root directory. As part of the build, code is automatically formatted. If you want to skip formatting, append `-DskipFormatting` to your Maven command. However, please be sure to format your code in a commit by running `./mvnw process-sources` before submitting a pull request, otherwise your code will not build in CI.

Our integration tests against the Discord API are not run in forks, so before you submit your changes in a PR, you should first test them in another project by importing the `io.disquark:disquark-rest:999-SNAPSHOT` dependency using your preferred build tool. In Gradle, this means adding `mavenLocal()` to your list of repositories. Be sure that you're importing your local changes and not a real snapshot!
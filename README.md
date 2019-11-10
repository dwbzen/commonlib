#commonlib
Data models and utilities to support:
* linear algebra, metric spaces, complex numbers, linear transformations
* fractals such as Iterated Function Systems and Apophysis flame files
* Collector-Producer pattern
* General Relations
* Json support

commonlib supports [music-framework](https://github.com/dwbzen/music-framework) and [text-processing](https://github.com/dwbzen/text-processing) projects.

## Build instructions
* gradlew build
* gradlew uploadArchives
* gradlew sonarqube (optional)

## eclipse project setup
* Clone the latest [commonlib](https://github.com/dwbzen/commonlib) repo from Github
    * Recommend cloning in C:\Compile along with text-processing and music-framework projects
* Download and install the version of the JDK referenced in build.gradle (Java 13)
* Download and install latest eclipse Java IDE (2019-09)
* Spin up eclipse and add the JDK for Java 13 under Installed JREs, and make it the default
* Import the commonlib gradle project

The project should build cleanly with 21 warnings. Use build.gradle.sonarqube to integrate with SonarQube static analysis.


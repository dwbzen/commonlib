pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                 git 'https://github.com/dwbzen/commonlib.git'
            }
        }
        stage('Build') {
			echo 'Building'
            steps {
                bat 'gradlew.bat -b build.gradle.sonarqube clean'
                bat 'gradlew.bat -b build.gradle.sonarqube build'
                bat 'gradlew.bat -b build.gradle.sonarqube test'
            }
        }
        stage('SonarQube analysis') {
			echo 'Running code analysis'
            // requires SonarQube Scanner for Gradle 2.1+
            // It's important to add --info because of SONARJNKNS-281
            steps {
                bat 'gradlew.bat -b build.gradle.sonarqube sonarqube'
            }
        }
        stage('Deploy') {
			echo 'Deploying'
            steps {
                bat 'gradlew.bat uploadArchives'
            }
        }
    }
}

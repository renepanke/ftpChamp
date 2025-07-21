pipeline {
    agent any
    stages {
        stage("Build") {
            agent {
                docker {
                    image "maven:3.9.11-eclipse-temurin-24"
                    args "-u root"
                }
            }
            steps {
                sh "mvn clean compile"

            }
        }
    }
}
pipeline {
    agent any
    stages {
        stage("Build") {
            agent {
                docker {
                    image "maven:3.9.11-eclipse-temurin-24"
                }
            }
            steps {
                sh "mvn clean compile"
            }
        }
    }
}
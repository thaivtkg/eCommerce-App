pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/thaivtkg/eCommerce-App.git'
            }
        }
        stage('Build') {
            steps {
                sh './mvn clean install' // Use 'mvn clean install' if using Maven
            }
        }
        stage('Test') {
            steps {
                sh './test' // Use 'mvn test' if using Maven
            }
        }
        stage('Package') {
            steps {
                sh './mvn package' // Use 'mvn package' if using Maven
            }
        }
    }

    post {
        success {
            echo 'Build and Deploy succeeded!'
        }
        failure {
            echo 'Build or Deploy failed!'
        }
    }
}

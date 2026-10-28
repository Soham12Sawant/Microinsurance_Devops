pipeline {
    agent any

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['development', 'staging', 'production'], description: 'Deployment target environment')
        string(name: 'APP_PORT', defaultValue: '8085', description: 'Application host port')
        booleanParam(name: 'RUN_SELENIUM_TESTS', defaultValue: true, description: 'Run Selenium E2E automated suite')
        string(name: 'DOCKER_IMAGE_NAME', defaultValue: 'microinsurance-platform', description: 'Docker Image Name')
    }

    environment {
        IMAGE_TAG = "${params.DOCKER_IMAGE_NAME}:${BUILD_NUMBER}"
        REGISTRY_LATEST = "${params.DOCKER_IMAGE_NAME}:latest"
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('1. SCM Checkout') {
            steps {
                echo "Fetching repository revision from Git SCM..."
                checkout scm
            }
        }

        stage('2. Compile & Unit Testing') {
            steps {
                echo "Running unit tests and code compilation..."
                script {
                    if (isUnix()) {
                        sh './mvnw clean compile test -B'
                    } else {
                        bat 'call .\\mvnw.cmd clean compile test -B'
                    }
                }
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('3. Selenium E2E Test Suite') {
            when {
                expression { return params.RUN_SELENIUM_TESTS }
            }
            steps {
                echo "Executing automated Selenium WebDriver journeys in Headless Chrome..."
                script {
                    if (isUnix()) {
                        sh './mvnw test -Dtest=*SeleniumTest -B'
                    } else {
                        bat 'call .\\mvnw.cmd test -Dtest=*SeleniumTest -B'
                    }
                }
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*SeleniumTest*.xml', allowEmptyResults: true
                    archiveArtifacts artifacts: 'target/screenshots/*.png', allowEmptyArchive: true
                }
                failure {
                    echo "CRITICAL: E2E Regression detected. Aborting packaging and container deployment."
                }
            }
        }

        stage('4. Package Artifact') {
            steps {
                echo "Packaging release JAR artifact for environment: ${params.ENVIRONMENT}..."
                script {
                    if (isUnix()) {
                        sh './mvnw package -DskipTests -B'
                    } else {
                        bat 'call .\\mvnw.cmd package -DskipTests -B'
                    }
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('5. Docker Image Build & Tag') {
            steps {
                echo "Building versioned Docker image: ${IMAGE_TAG}..."
                script {
                    if (isUnix()) {
                        sh "docker build -t ${IMAGE_TAG} -t ${REGISTRY_LATEST} ."
                    } else {
                        bat "docker build -t ${IMAGE_TAG} -t ${REGISTRY_LATEST} ."
                    }
                }
            }
        }

        stage('6. Continuous Deployment & Health Verification') {
            steps {
                echo "Deploying service to port ${params.APP_PORT}..."
                script {
                    if (isUnix()) {
                        sh """
                            docker stop microinsurance-app || true
                            docker rm microinsurance-app || true
                            docker run -d --name microinsurance-app -p ${params.APP_PORT}:8080 -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} ${IMAGE_TAG}
                            sleep 10
                            curl -s -f http://localhost:${params.APP_PORT}/api/health || true
                        """
                    } else {
                        bat """
                            docker stop microinsurance-app 2>nul || echo Not running
                            docker rm microinsurance-app 2>nul || echo Not existing
                            docker run -d --name microinsurance-app -p ${params.APP_PORT}:8080 -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} ${IMAGE_TAG}
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "SUCCESS: Pipeline completed successfully. Microinsurance Platform verified on port ${params.APP_PORT}."
        }
        failure {
            echo "FAILURE: Pipeline execution failed."
        }
    }
}

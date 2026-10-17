pipeline {
    agent any

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['development', 'staging', 'production'], description: 'Deployment target environment')
        string(name: 'APP_PORT', defaultValue: '8080', description: 'Application port')
        booleanParam(name: 'RUN_SELENIUM_TESTS', defaultValue: true, description: 'Run Selenium E2E automated suite')
        string(name: 'DOCKER_IMAGE_NAME', defaultValue: 'microinsurance-platform', description: 'Docker Image Name')
    }

    environment {
        JAVA_HOME = tool 'JDK-21'
        MAVEN_HOME = tool 'Maven-3.9'
        IMAGE_TAG = "${params.DOCKER_IMAGE_NAME}:${BUILD_NUMBER}"
        REGISTRY_LATEST = "${params.DOCKER_IMAGE_NAME}:latest"
        PATH = "${env.MAVEN_HOME}/bin:${env.JAVA_HOME}/bin:${env.PATH}"
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        ansiColor('xterm')
    }

    stages {
        stage('1. SCM Checkout') {
            steps {
                echo "Fetching repository revision from Git SCM..."
                checkout scm
                sh 'git log -1 --pretty=format:"Commit: %h - %s by %an (%cd)"'
            }
        }

        stage('2. Compile & Unit Testing') {
            steps {
                echo "Running unit tests and code compilation via Maven..."
                sh 'mvn clean compile test -B'
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
                sh 'mvn test -Dtest=*SeleniumTest -B'
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
                sh 'mvn package -DskipTests -B'
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
                sh """
                    docker build -t ${IMAGE_TAG} -t ${REGISTRY_LATEST} .
                    docker images | grep ${params.DOCKER_IMAGE_NAME}
                """
            }
        }

        stage('6. Continuous Deployment & Health Verification') {
            steps {
                echo "Deploying containerized service to port ${params.APP_PORT}..."
                sh """
                    # Stop and remove existing container if running
                    docker stop microinsurance-app || true
                    docker rm microinsurance-app || true

                    # Run new container version
                    docker run -d --name microinsurance-app \
                        -p ${params.APP_PORT}:8080 \
                        -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} \
                        ${IMAGE_TAG}

                    # Wait and verify health
                    echo "Waiting for application startup probe..."
                    sleep 15
                    curl -s -f http://localhost:${params.APP_PORT}/api/health || (echo "Health check failed!" && exit 1)
                """
            }
        }
    }

    post {
        success {
            echo "SUCCESS: Pipeline completed successfully. Microinsurance Platform deployed and verified on port ${params.APP_PORT}."
        }
        failure {
            echo "FAILURE: Pipeline execution failed. Please inspect build console and test reports."
        }
    }
}

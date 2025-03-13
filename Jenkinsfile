pipeline {
    agent any

    parameters {
        choice(name: 'PARALLELISM', choices: ['1', '2', '4'], description: 'Parallel thread count')
        string(name: 'TEST_TAG', defaultValue: '@smoke', description: 'Cucumber tags')
    }

    stages {
        stage('Run Appium Tests') {
            steps {
                sh "docker exec appium-tests mvn clean test -Dcucumber.filter.tags='${params.TEST_TAG}' -Dparallel.threads=${params.PARALLELISM}"
            }
        }

        stage('Copy Reports from Container') {
            steps {
                sh '''
                    mkdir -p jenkins-reports
                    docker cp appium-tests:/usr/appium-tests/target/report.html ./jenkins-reports/report.html
                '''
            }
        }

        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'jenkins-reports/report.html'
            }
        }
    }

    post {
        always {
            emailext (
                subject: "[Jenkins] Appium Test Execution Results",
                body: "Test execution report attached (Parallelism: ${params.PARALLELISM}, Tags: ${params.TEST_TAG}).",
                attachmentsPattern: 'jenkins-reports/report.html',
                recipients: 'berk@example.com'
            )
        }
    }
}

pipeline {
    agent any

    parameters {
        booleanParam(name: 'PARALLEL_ENABLED', defaultValue: 'true', description: 'Parallel execution is enabled?')
        choice(name: 'PARALLEL_MODE', choices: ['concurrent', 'same_thread'], description: 'Parallel default mode')
        choice(name: 'PARALLEL_MODE_CLASSES', choices: ['concurrent', 'same_thread'], description: 'Parallel mode based on classes')
        choice(name: 'THREAD_COUNT', choices: ['1', '2', '4', '8'], description: 'Fixed Parallelism Level')
        string(name: 'TAG', defaultValue: '@wip', description: 'Tag to filter tests')
        string(name: 'API_KEY', defaultValue: 'V3zq3UUuHJSevSjL6TqdR40I5RXVdxgA', description: 'API KEY for the AccuWeather API (normally it can stored in gitlab secrets etc)')
        booleanParam(name: 'RUN_LOCAL', defaultValue: 'false', description: 'Are test will run on local or docker (This execution parameter will affect the appium js path and devices will set as config)')
    }

    stages {
        stage('Log Configs') {
            steps {
                echo "[Pipeline-INFO]-Parallel Execution Enabled: ${params.PARALLEL_ENABLED}"
                echo "[Pipeline-INFO]-Parallel Mode Default: ${params.PARALLEL_MODE}"
                echo "[Pipeline-INFO]-Parallel Mode Classes: ${params.PARALLEL_MODE_CLASSES}"
                echo "[Pipeline-INFO]-Parallelism Level: ${params.PARALLELISM_LEVEL}"
                echo "[Pipeline-INFO]-Tags: ${params.TAG}"
                echo "[Pipeline-INFO]-RUN_LOCAL: ${params.RUN_LOCAL}"
            }
        }

        stage('Run Appium Tests') {
            steps {
                script {
                    def exitCode = sh(script: """
                        docker exec appium-tests mvn clean test \
                        -Djunit.jupiter.execution.parallel.enabled=${params.PARALLEL_ENABLED} \
                        -Djunit.jupiter.execution.parallel.mode.default=${params.PARALLEL_MODE} \
                        -Djunit.jupiter.execution.parallel.mode.classes.default=${params.PARALLEL_MODE_CLASSES} \
                        -Djunit.jupiter.execution.parallel.config.fixed.parallelism=${params.PARALLELISM_LEVEL} \
                        -Djunit.jupiter.tags=${params.TAG} \
                        -DapiKey=${params.API_KEY} \
                        -DrunOnLocal=${params.RUN_LOCAL} 
                    """, returnStatus: true)

                    if (exitCode != 0) {
                        echo "Some tests failed, marking build as UNSTABLE."
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
    }

    post {
        always {

            echo "Copying test reports"

            sh '''
            mkdir -p jenkins-reports
            docker cp appium-tests:/workspace/test-output ./jenkins-reports/test-output || echo "⚠ Test output folder not found!"
        '''

            archiveArtifacts artifacts: 'jenkins-reports/test-output/**'

            echo "Pipeline completed. Reports moved to jenkins-reports folder."

            script {
                try {
                    emailext(
                            subject: "[Jenkins] Appium Test Execution Results",
                            body: """
                    <html>
                    <body>
                        <h2 style="color:#1E88E5;">Appium Test Execution Report</h2>
                        <p><strong>Date:</strong> ${new Date().format("yyyy-MM-dd HH:mm:ss")}</p>
                        <p><strong>Parallelism:</strong> ${params.PARALLELISM_LEVEL}</p>
                        <p><strong>Tags:</strong> ${params.JUNIT_TAGS}</p>
                        <p><strong>Report Path:</strong> <a href="${BUILD_URL}artifact/jenkins-reports/test-output/Mobile-UI-Automation-Report.html">Click to view report</a></p>
                        <br>
                        <p>Check the full report for more details.</p>
                        <hr>
                        <p style="color:gray; font-size:12px;">This email was sent automatically by Jenkins.</p>
                    </body>
                    </html>
                    """,
                            mimeType: "text/html",
                            attachLog: true,
                            attachmentsPattern: 'jenkins-reports/test-output/**',
                            to: 'berkados@gmail.com',
                            from: 'developerberkdev@gmail.com',
                            replyTo: 'developerberkdev@gmail.com'
                    )

                } catch (Exception e) {
                    echo "Email sending failed: ${e.message}"
                }
            }
        }
    }

}

pipeline {
    agent any

    environment {
        TOKEN = credentials('TOKEN')
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_EXEC', defaultValue: 'POEI2-1190')
        string(name: 'URL_GRID', defaultValue: 'http://192.168.1.30:4444/wd/hub')
        string(name: 'EXEC_NAME', defaultValue: 'Execution Jenkins')
    }

    stages {

        stage('Export features, test and import results') {
            steps {
                script {
                    def tickets = params.TEST_EXEC.tokenize(',')
                    tickets.each { ticket ->
                        def cleanTicket = ticket.trim()

                        bat "curl -H \"Content-Type: application/json\" -X GET -H \"Authorization: Bearer %TOKEN%\" \"https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=${cleanTicket}\" --output features.zip"
                        bat 'if exist "src/test/resources/features" rd /s /q "src/test/resources/features"'
                        bat 'mkdir "src/test/resources/features"'
                        bat 'tar -xf features.zip -C src/test/resources/features'
                        bat 'del features.zip'

                        bat "mvn clean test -DurlGrid=%URL_GRID%"

                        bat "curl -H \"Content-Type: application/json\" -X POST -H \"Authorization: Bearer %TOKEN%\" --data @target/cucumber.json \"https://xray.cloud.getxray.app/api/v1/import/execution/cucumber?testExecKey=${cleanTicket}\""
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Tests exécutés avec succès 🎉'
        }

        failure {
            echo 'Des tests ont échoué ❌'
        }
    }
}

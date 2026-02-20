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

        stage('Export features') {
            steps {
                script {
                            def tickets = params.TEST_EXEC.tokenize(',')
                            tickets.each { ticket ->
                                def cleanTicket = ticket.trim()

                                // Export des features pour ce ticket uniquement
                                bat "curl -H \"Content-Type: application/json\" -X GET -H \"Authorization: Bearer %TOKEN%\" \"https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=${cleanTicket}\" --output features.zip"
                                bat 'if exist "src/test/resources/features" rd /s /q "src/test/resources/features"'
                                bat 'mkdir "src/test/resources/features"'
                                bat 'tar -xf features.zip -C src/test/resources/features'
                                bat 'del features.zip'

                                // Exécution des tests
                                bat "mvn clean test -DurlGrid=%URL_GRID%"

                                // Import des résultats
                                bat "curl -H \"Content-Type: application/json\" -X POST -H \"Authorization: Bearer %TOKEN%\" --data @target/cucumber.json \"https://xray.cloud.getxray.app/api/v1/import/execution/cucumber?testExecKey=${cleanTicket}\""
                            }
                        }
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Execution des tests Cucumber via Maven...'
                bat "mvn clean test -DurlGrid=%URL_GRID%"
            }
        }
    }

    post {
        always {
            script {
                if (fileExists('target/cucumber.json')) {
                    // On transforme la chaîne "ID1, ID2" en une liste Groovy [ID1, ID2]
                    def tickets = params.TEST_EXEC.split(',')

                    tickets.each { ticket ->
                        def cleanTicket = ticket.trim() // On enlève les espaces éventuels
                        echo "Importation des résultats vers le ticket : ${cleanTicket}"

                        bat """
                        curl -H "Content-Type: application/json" -X POST -H "Authorization: Bearer %TOKEN%" --data @target/cucumber.json "https://xray.cloud.getxray.app/api/v1/import/execution/cucumber?testExecKey=${cleanTicket}"
                        """
                    }
                }
            }
        }


        success {
            echo 'Tests exécutés avec succès 🎉'
        }

        failure {
            echo 'Des tests ont échoué ❌'
        }
    }
}

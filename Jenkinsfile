pipeline {
    agent any

    environment {
        TOKEN = credentials('TOKEN')
        JAVA_TOOL_OPTIONS = '-Dfile.encoding=UTF-8'
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_EXEC', defaultValue: 'POEI2-1190,POEI2-1191')
        string(name: 'URL_GRID', defaultValue: 'http://192.168.1.30:4444/wd/hub')
        string(name: 'EXEC_NAME', defaultValue: 'Execution Jenkins')
    }

    stages {

        stage('Configuration encodage') {
            steps {
                bat 'chcp 65001'
            }
        }


        stage('Export features, test and import results') {
            steps {
                script {
                    def tickets = params.TEST_EXEC.tokenize(',')
                    tickets.each { ticket ->
                        try {
                            echo "=== Traitement du ticket : ${ticket} ==="

                            echo "Export des features..."
                            bat "curl -H \"Content-Type: application/json\" -X GET -H \"Authorization: Bearer %TOKEN%\" \"https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=${ticket}\" --output features.zip"
                            bat 'if exist "src/test/resources/features" rd /s /q "src/test/resources/features"'
                            bat 'mkdir "src/test/resources/features"'
                            bat 'tar -xf features.zip -C src/test/resources/features'
                            bat 'del features.zip'

                            echo "Exécution des tests..."
                            bat "mvn clean test -DurlGrid=%URL_GRID%"

                            echo "Import des résultats..."
                            bat "curl -H \"Content-Type: application/json\" -X POST -H \"Authorization: Bearer %TOKEN%\" --data @target/cucumber.json \"https://xray.cloud.getxray.app/api/v1/import/execution/cucumber?testExecKey=${ticket}\""

                            echo "=== Ticket ${ticket} traité avec succès ✅ ==="

                        } catch (Exception e) {
                            echo "=== Erreur sur le ticket ${ticket} : ${e.message} ❌ ==="
                            currentBuild.result = 'UNSTABLE'
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Tous les tests ont été exécutés avec succès 🎉'
        }

        unstable {
            echo 'Certains tickets ont rencontré des erreurs ⚠️'
        }

        failure {
            echo 'Le pipeline a échoué ❌'
        }
    }
}
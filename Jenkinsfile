pipeline {
    agent any

    environment {
        TOKEN = credentials('TOKEN')
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_PLAN',        defaultValue: 'POEI2-989')
        string(name: 'TEST_EXEC',        defaultValue: 'POEI2-1176')  // ← Ticket Test Execution cible
        string(name: 'URL_GRID',         defaultValue: 'http://172.16.14.164:4449/wd/hub')
        string(name: 'EXEC_NAME',        defaultValue: 'Execution Jenkins')
    }

    stages {

        stage('Export features') {
            steps {
                echo 'Exportation des features depuis Xray...'
                bat 'curl -H "Content-Type: application/json" -X GET -H "Authorization: Bearer %TOKEN%" "https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=%TEST_PLAN%" --output features.zip'
                bat 'if not exist "src/test/resources/features" mkdir "src/test/resources/features"'
                bat 'tar -xf features.zip -C src/test/resources/features'
                bat 'del features.zip'
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
            echo 'Importation des résultats d\'exécution vers Xray...'

            script {
                def xrayUrl = "https://xray.cloud.getxray.app/api/v2/import/execution/cucumber?testExecIssueKey=${params.TEST_EXEC}"
                echo "Mise à jour de la Test Execution : ${params.TEST_EXEC}"

                bat """curl -X POST ^
                    -H "Content-Type: application/json" ^
                    -H "Authorization: Bearer %TOKEN%" ^
                    --data "@target/cucumber.json" ^
                    "${xrayUrl}" """
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
pipeline {
    agent any

    environment {
        TOKEN = credentials('TOKEN')
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_PLAN',        defaultValue: 'POEI2-989')
        string(name: 'TEST_EXEC',        defaultValue: 'POEI2-1176')
        string(name: 'URL_GRID',         defaultValue: 'http://172.16.14.164:4449/wd/hub')
        string(name: 'EXEC_NAME',        defaultValue: 'Execution Jenkins')
    }

    stages {

        stage('Export features') {
            steps {
                echo 'Exportation des features depuis Xray...'
                // ✅ %TEST_EXEC% et non plus %TEST_PLAN%
                bat 'curl -H "Content-Type: application/json" -X GET -H "Authorization: Bearer %TOKEN%" "https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=%TEST_EXEC%" --output features.zip'
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
            echo 'Importation des résultats vers Xray...'
            script {
                // Plus besoin du query param, le tag dans le .feature fait le travail
                bat """curl -X POST ^
                    -H "Content-Type: application/json" ^
                    -H "Authorization: Bearer %TOKEN%" ^
                    --data "@target/cucumber.json" ^
                    "https://xray.cloud.getxray.app/api/v2/import/execution/cucumber" """
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
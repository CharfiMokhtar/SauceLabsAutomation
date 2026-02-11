pipeline {
    agent any

    environment {
        TOKEN = credentials('TOKEN')
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_PLAN', defaultValue: 'POEI2-989')
        string(name: 'URL_GRID', defaultValue: 'http://172.16.14.164:4449/wd/hub')
    }

    stages {

        stage('Export features') {
            steps {
                echo 'Exportation des features depuis Xray...'
                bat 'curl -H "Content-Type: application/json" -X GET -H "Authorization: Bearer %TOKEN%"  "https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=%TEST_PLAN%" --output features.zip'
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

            def metadata = """
                {
                    "info": {
                    "summary": "${params.EXEC_NAME} - ${params.TEST_PLAN}",
                    "description": "Exécution automatique générée par Jenkins",
                    "testPlanKey": "${params.TEST_PLAN}"
                    }
                }
            """
            writeFile file: 'info.json', text: metadata


            bat """
                curl -H "Authorization: Bearer %TOKEN%" ^
                -F "info=@info.json" ^
                -F "result=@target/cucumber.json" ^
                "https://xray.cloud.getxray.app/api/v1/import/execution/cucumber/multipart"
                """
        }

        success {
            echo 'Tests exécutés avec succès 🎉'
        }

        failure {
            echo 'Des tests ont échoué ❌'
        }
    }
}

pipeline {
    agent any

    environment {
        TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnQiOiJiNmNhZGQwNS1lMzQxLTNmMTctYjU1Zi00OTM0MTI4MWQ4MmEiLCJhY2NvdW50SWQiOiI3MTIwMjA6MGVkNjY4NzAtM2Y2ZC00NzM3LTljMmYtZDQyMTVmM2MyOWRmIiwiaXNYZWEiOmZhbHNlLCJpYXQiOjE3NzA2MzI1ODcsImV4cCI6MTc3MDcxODk4NywiYXVkIjoiNTY3NTJEQUJBMENDNERBQ0IzQUQ1RTcyMEZGRDJDN0UiLCJpc3MiOiJjb20ueHBhbmRpdC5wbHVnaW5zLnhyYXkiLCJzdWIiOiI1Njc1MkRBQkEwQ0M0REFDQjNBRDVFNzIwRkZEMkM3RSJ9.7BeWIRxz8e4HsGuhrm9XmU6eWUDYMSaxwsf5ADM63as"
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_PLAN')
    }

    stages {

        stage('Export features') {
            steps {
                echo 'Exportation des features depuis Xray...'
                bat 'curl -H "Content-Type: application/json" -X GET -H "Authorization: Bearer %TOKEN%"  "https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=%TEST_PLAN%" --output features.zip'
                bat 'tar -xf features.zip -C src/test/resources/features'
                bat 'del features.zip'
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Execution des tests Cucumber via Maven...'
                bat 'mvn clean test -Dbrowser=%SELENIUM_BROWSER%'
            }
        }

        stage('Import execution') {
            steps {
                echo 'Importation des résultats d\'exécution vers Xray...'
                bat 'curl -H "Content-Type: application/json" -X POST -H "Authorization: Bearer %TOKEN%"  --data @"target/cucumber.json" https://xray.cloud.getxray.app/api/v1/import/execution/cucumber'
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
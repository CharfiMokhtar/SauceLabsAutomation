pipeline {
    agent any

    environment {
        TOKEN = credentials('TOKEN')
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_PLAN', defaultValue: 'POEI2-989')
        string(name: 'URL_GRID', defaultValue: 'http://172.16.14.164:4449/wd/hub')
        string(name: 'EXEC_NAME', defaultValue: 'Execution Jenkins')
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

            script {
                def metadataMap = [
                    fields: [
                        project: [key: "POEI2"],
                        summary: "${params.EXEC_NAME} - ${params.TEST_PLAN}".toString(),
                        description: "Execution automatique generee par Jenkins",
                        issuetype: [name: "Test Execution"],
                        labels: ["Mokhtar"],
                        reporter: [accountId: "712020:0ed66870-3f6d-4737-9c2f-d4215f3c29df?cloudId=44b031c3-be43-4b09-ba30-f624689be584"]
                    ],
                    xrayFields: [
                        testPlanKey: params.TEST_PLAN
                    ]
                ]
                def metadataJson = groovy.json.JsonOutput.toJson(metadataMap)

                writeFile file: 'info.json', text: metadataJson

                bat 'type info.json'
                bat 'curl -H "Content-Type: multipart/form-data" -X POST -F "info=@info.json;type=application/json" -F "results=@target/cucumber.json" -H "Authorization: Bearer %TOKEN%" https://xray.cloud.getxray.app/api/v2/import/execution/cucumber/multipart'
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

pipeline {
    agent any

    environment {
        TOKEN = credentials('TOKEN')
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_PLAN',        defaultValue: 'POEI2-989')
        string(name: 'TNR_LABEL',        defaultValue: 'Mokhtar')
        string(name: 'URL_GRID',         defaultValue: 'http://192.168.1.30:4444/wd/hub')
        string(name: 'EXEC_NAME',        defaultValue: 'Execution Jenkins')
    }

    stages {

        stage('Récupération des Test Executions TNR') {
            steps {
                echo "Recherche des Test Executions [${params.TNR_LABEL}] du plan ${params.TEST_PLAN}..."

                script {

                    // ── ÉTAPE 1 : récupérer l'issueId interne du Test Plan ──────────
                    def graphqlStep1 = """{"query":"{ getTestPlans(jql: \\"key = ${params.TEST_PLAN}\\", limit: 1) { results { issueId } } }"}"""
                    writeFile file: 'gql_step1.json', text: graphqlStep1

                    bat """curl -s -X POST ^
                        -H "Authorization: Bearer %TOKEN%" ^
                        -H "Content-Type: application/json" ^
                        --data "@gql_step1.json" ^
                        "https://xray.cloud.getxray.app/api/v2/graphql" ^
                        -o step1_response.json"""

                    writeFile file: 'extract_id.ps1', text: '''
                        $json = Get-Content step1_response.json | ConvertFrom-Json
                        $json.data.getTestPlans.results[0].issueId
                    '''
                    def issueId = bat(
                        returnStdout: true,
                        script: '@powershell -NoProfile -ExecutionPolicy Bypass -File extract_id.ps1'
                    ).trim()

                    echo "✅ issueId du Test Plan : ${issueId}"

                    // ── ÉTAPE 2 : récupérer les Test Executions avec leurs labels ───
                    def graphqlStep2 = """{"query":"{ getTestPlan(issueId: \\"${issueId}\\") { testExecutions(limit: 50) { results { issueId jira(fields: [\\"key\\", \\"labels\\"]) } } } }"}"""
                    writeFile file: 'gql_step2.json', text: graphqlStep2

                    bat """curl -s -X POST ^
                        -H "Authorization: Bearer %TOKEN%" ^
                        -H "Content-Type: application/json" ^
                        --data "@gql_step2.json" ^
                        "https://xray.cloud.getxray.app/api/v2/graphql" ^
                        -o step2_response.json"""

                    bat 'type step2_response.json'

                    // ── ÉTAPE 3 : filtrer par label ─────────────────────────────────
                    def tnrLabel = params.TNR_LABEL
                    writeFile file: 'filter_exec.ps1', text: """
                        \$json = Get-Content step2_response.json | ConvertFrom-Json
                        \$executions = \$json.data.getTestPlan.testExecutions.results
                        \$filtered = \$executions | Where-Object {
                            \$_.jira.labels -contains '${tnrLabel}'
                        }
                        if (-not \$filtered) {
                            Write-Error 'Aucune Test Execution avec le label ${tnrLabel}'
                            exit 1
                        }
                        (\$filtered | ForEach-Object { \$_.jira.key }) -join ';'
                    """
                    def execKeys = bat(
                        returnStdout: true,
                        script: '@powershell -NoProfile -ExecutionPolicy Bypass -File filter_exec.ps1'
                    ).trim()

                    echo "✅ Test Executions [${params.TNR_LABEL}] trouvées : ${execKeys}"
                    env.TNR_EXEC_KEYS = execKeys
                }
            }
        }

        stage('Export features') {
            steps {
                echo "Export des features depuis : ${env.TNR_EXEC_KEYS}"
                script {
                    bat 'if exist "src\\test\\resources\\features" rmdir /s /q "src\\test\\resources\\features"'
                    bat 'mkdir "src\\test\\resources\\features"'

                    bat """curl -s -X GET ^
                        -H "Content-Type: application/json" ^
                        -H "Authorization: Bearer %TOKEN%" ^
                        "https://xray.cloud.getxray.app/api/v1/export/cucumber?keys=${env.TNR_EXEC_KEYS}" ^
                        --output features.zip"""

                    bat 'tar -xf features.zip -C src/test/resources/features'
                    bat 'del features.zip'
                    echo "✅ Features exportées avec succès"
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
            echo 'Importation des résultats vers Xray...'
            script {
                // Importer séparément vers chaque Test Execution trouvée
                def keys = env.TNR_EXEC_KEYS.split(';')
                keys.each { execKey ->
                    def infoJson = """{"fields": {"project": {"key": "POEI2"}, "issuetype": {"name": "Test Execution"}}, "xrayFields": {"testExecKey": "${execKey}"}}"""
                    writeFile file: "info_${execKey}.json", text: infoJson

                    bat """curl -X POST ^
                        -H "Authorization: Bearer %TOKEN%" ^
                        -F "info=@info_${execKey}.json;type=application/json" ^
                        -F "results=@target/cucumber.json;type=application/json" ^
                        "https://xray.cloud.getxray.app/api/v2/import/execution/cucumber/multipart" """
                }
            }
        }
        success { echo 'Tests exécutés avec succès 🎉' }
        failure { echo 'Des tests ont échoué ❌' }
    }
}
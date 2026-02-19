pipeline {
    agent any

    environment {
        TOKEN        = credentials('TOKEN')
        JIRA_BASE_URL = 'https://team-1612820401992.atlassian.net'
    }

    parameters {
        string(name: 'SELENIUM_BROWSER', defaultValue: 'CHROME')
        string(name: 'TEST_PLAN',        defaultValue: 'POEI2-989')
        string(name: 'TNR_LABEL',        defaultValue: 'Mokhtar')        // ← tag à filtrer
        string(name: 'URL_GRID',         defaultValue: 'http://192.168.1.30:4444/wd/hub')
        string(name: 'EXEC_NAME',        defaultValue: 'Execution Jenkins')
    }

    stages {

        stage('Récupération des Test Executions TNR') {
            steps {
                echo "Recherche des Test Executions du plan ${params.TEST_PLAN} avec le label ${params.TNR_LABEL}..."

                script {
                    // JQL : Test Executions liées au Test Plan ET ayant le label TNR
                    def jql = """issuetype = \"Test Execution\" AND \"Test Plan\" = ${params.TEST_PLAN} AND labels = \"${params.TNR_LABEL}\""""
                    def encodedJql = jql.replace(' ', '%20').replace('"', '%22').replace('=', '%3D')

                    bat """curl -s -X GET ^
                        -H "Authorization: Bearer %TOKEN%" ^
                        -H "Content-Type: application/json" ^
                        "%JIRA_BASE_URL%/rest/api/2/search?jql=issuetype%%3D%%22Test%%20Execution%%22%%20AND%%20%%22Test%%20Plan%%22%%3D%%20${params.TEST_PLAN}%%20AND%%20labels%%3D%%22${params.TNR_LABEL}%%22&fields=key,summary,labels&maxResults=50" ^
                        -o testexec_tnr.json"""

                    bat 'type testexec_tnr.json'

                    // Extraire les clés avec PowerShell
                    bat '''powershell -Command "
                        $json = Get-Content testexec_tnr.json | ConvertFrom-Json;
                        $total = $json.total;
                        Write-Host ('Total Test Executions TNR trouvees : ' + $total);
                        if ($total -eq 0) { Write-Error 'Aucune Test Execution TNR trouvee !'; exit 1 }
                        $keys = ($json.issues | ForEach-Object { $_.key }) -join ';';
                        Write-Host ('Cles : ' + $keys);
                        Set-Content -Path tnr_exec_keys.txt -Value $keys
                    "'''

                    def execKeys = readFile('tnr_exec_keys.txt').trim()
                    echo "✅ Test Executions TNR trouvées : ${execKeys}"
                    env.TNR_EXEC_KEYS = execKeys
                }
            }
        }

        stage('Export features') {
            steps {
                echo "Export des features depuis les Test Executions TNR : ${env.TNR_EXEC_KEYS}"

                script {
                    bat 'if not exist "src\\test\\resources\\features" mkdir "src\\test\\resources\\features"'

                    // Export de toutes les executions TNR en une seule requête (clés séparées par ;)
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
                // Les tags @POEI2-XXXX dans les .feature guident l'import
                // vers chaque Test Execution concernée automatiquement
                bat """curl -X POST ^
                    -H "Content-Type: application/json" ^
                    -H "Authorization: Bearer %TOKEN%" ^
                    --data "@target/cucumber.json" ^
                    "https://xray.cloud.getxray.app/api/v2/import/execution/cucumber" """
            }
        }

        success { echo 'Tests exécutés avec succès 🎉' }
        failure { echo 'Des tests ont échoué ❌' }
    }
}
```

---

## 📊 Flux complet
```
TEST_PLAN (POEI2-989)
        │
        ▼
JQL Jira API
issuetype="Test Execution"
AND "Test Plan" = POEI2-989
AND labels = "TNR"
        │
        ▼
[ POEI2-1176, POEI2-1200, ... ]  ← seulement les TNR
        │
        ▼
GET /export/cucumber?keys=POEI2-1176;POEI2-1200
        │  (features contiennent @POEI2-1176 et @POEI2-1200)
        ▼
mvn clean test
        │
        ▼
POST /import/execution/cucumber
        │  (Xray lit les tags → met à jour chaque exec automatiquement)
        ▼
POEI2-1176 ✅ mis à jour
POEI2-1200 ✅ mis à jour
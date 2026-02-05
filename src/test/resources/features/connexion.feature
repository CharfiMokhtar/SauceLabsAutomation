@login
Feature: Connexion utilisateur

  @successfulLogin
  Scenario Outline: Connexion avec identifiants valides
    Given l'utilisateur est sur la page de connexion
    When il saisit le login "<username>" et le mot de passe "<password>"
    And il valide la saisie
    Then il est redirigé vers la page d'accueil
    Examples:
      | username      | password     |
      | standard_user | secret_sauce |
      | problem_user  | secret_sauce |
      | visual_user   | secret_sauce |


  @failedLogin
  Scenario: Tentative de connexion avec identifiants invalides
    Given l'utilisateur est sur la page de connexion
    When il saisit le login "standard_user" et le mot de passe "invalid_password"
    And il valide la saisie
    Then le message d'erreur s'affiche
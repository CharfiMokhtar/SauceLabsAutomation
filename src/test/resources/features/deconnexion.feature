@logout
Feature: Déconnexion utilisateur

  Background: Connexion
    Given l'utilisateur est sur la page de connexion
    When il saisit le login "standard_user" et le mot de passe "secret_sauce"
    And il valide la saisie

  Scenario: Déconnexion
    Given l'utilisateur ouvre la sidebar
    When il se déconnecte
    Then il est redirigé vers la page de connexion
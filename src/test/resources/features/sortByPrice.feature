Feature: Tri des produits par prix

  Background: Connexion
    Given l'utilisateur est sur la page de connexion
    When il saisit le login "standard_user" et le mot de passe "secret_sauce"
    And il valide la saisie


  Scenario: Tri par prix croissant
    Given l'utilisateur trie les produits par prix "croissant"
    Then les produits sont triés par prix "croissant"


  Scenario: Tri par prix décroissant
    Given l'utilisateur trie les produits par prix "décroissant"
    Then les produits sont triés par prix "décroissant"
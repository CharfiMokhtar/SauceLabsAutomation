Feature: Achat d'un produit

  Background: Connexion et ajout d'un produit au panier
    Given l'utilisateur est sur la page de connexion
    When il saisit le login "standard_user" et le mot de passe "secret_sauce"
    And il valide la saisie
    And l'utilisateur ajoute le produit "Sauce Labs Backpack" au panier
    And il accede au panier

  Scenario Outline: Ajout d'un produit au panier avec différents employés
    Given l'utilisateur il accede au checkout
    When il saisit ses informations:
      | firstname   | lastname   | postalcode   |
      | <firstname> | <lastname> | <postalcode> |
    And il valide la saisie
    And il termine le checkout
    Then le message thank you for your order s'affiche

    Examples:
      | firstname | lastname | postalcode |
      | Mokhtar   | Charfi   | 92600      |
      | Alice     | Dupont   | 75001      |
      | Jean      | Martin   | 69002      |
      | Sophie    | Bernard  | 33000      |

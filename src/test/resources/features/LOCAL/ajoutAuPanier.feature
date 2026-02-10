@addToCart
Feature: Ajout de produits au panier

  Background: Connexion
    Given l'utilisateur est sur la page de connexion
    When il saisit le login "standard_user" et le mot de passe "secret_sauce"
    And il valide la saisie

  Scenario: Ajout d'un produit au panier
    Given l'utilisateur ajoute le produit "Sauce Labs Backpack" au panier
    When il accede au panier
    Then le produit "Sauce Labs Backpack" est dans le panier

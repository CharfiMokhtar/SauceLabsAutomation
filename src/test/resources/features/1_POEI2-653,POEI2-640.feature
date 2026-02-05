@POEI2-813 @Jira
Feature: [MOKHTAR] testPlan

	Background:
		#@POEI2-648
		Given l'utilisateur est sur la page de connexion
		When il saisit le login "standard_user" et le mot de passe "secret_sauce"
		And il valide la saisie

	@POEI2-653 @POEI2-700 @Mokhtar
	Scenario: [MOKHTAR] Ajout au panier [KO]
		Given l'utilisateur ajoute le produit "Sauce Labz Backpack" au panier
		When il accede au panier
		Then le produit "Sauce Labs Backpack" est dans le panier
		
	@POEI2-640 @POEI2-700 @Mokhtar
	Scenario: [MOKHTAR] Déconnexion [OK]
		Given l'utilisateur ouvre la sidebar
		When il se déconnecte
		Then il est redirigé vers la page de connexion
		

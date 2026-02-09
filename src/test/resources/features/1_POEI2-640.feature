Feature: [MOKHTAR] Déconnexion [OK]

	Background:
		#@POEI2-648
		Given l'utilisateur est sur la page de connexion
		When il saisit le login "standard_user" et le mot de passe "secret_sauce"
		And il valide la saisie

	@POEI2-640 @POEI2-700 @Mokhtar
	Scenario: [MOKHTAR] Déconnexion [OK]
		Given l'utilisateur ouvre la sidebar
		When il se déconnecte
		Then il est redirigé vers la page de connexion
		

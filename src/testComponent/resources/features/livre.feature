Feature: L'utilisateur peut créer, récupèrer des livres et en réserver.
  Scenario: L'utilisateur crée quatres livres et récupère les quatres.
    When L'utilisateur crée le livre "Gasly, le magnifique" écrit par "Pierre Gasly" et "false".
    And L'utilisateur crée le livre "Charles Leclerc, le prodige" écrit par "Charles Leclerc" et "false".
    And L'utilisateur crée le livre "La mort en face" écrit par "Romain Grosjean" et "false".
    And L'utilisateur crée le livre "Ma vie sans gravité" écrit par "Thomas Pesquet" et "false".
    And L'utilisateur récupère tous les livres.
    Then La liste doit contenir les livres suivants dans le même ordre.
      | name | author | reserved |
      | Charles Leclerc, le prodige | Charles Leclerc | false |
      | Gasly, le magnifique | Pierre Gasly | false |
      | La mort en face | Romain Grosjean | false |
      | Ma vie sans gravité | Thomas Pesquet | false |

  Scenario: L'utilisateur crée deux livres, réserve le premier et récupère ses informations.
    When L'utilisateur crée le livre "Ferrari" écrit par "Saverio Villa" et "false".
    And L'utilisateur crée le livre "Formule 1 2023" écrit par "Marabout" et "false".
    And L'utilisateur réserve le livre intitulé "Ferrari".
    And L'utilisateur récupère le livre intitulé "Ferrari".
    Then La réservation du livre doit être "true".
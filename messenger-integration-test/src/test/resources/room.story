Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: SetUp
Given a new user <nameOfUser> with role <nameOfRoom>
Examples:
|nameOfUser|nameOfRoom
|Ed|NORMAL
|Edd|NORMAL
|Eddy|ADMIN
|Bruna|ADMIN
|Amanda|NORMAL
|Liza|NORMAL
|Carol|NORMAL

Scenario:
Given Ed creates a new room Dudes with Edd, Eddy
Then Ed sees the room Dudes have 2 participants

Scenario:
Given Bruna creates a new room Gals with Amanda, Liza, Carol
When Bruna removes Amanda from the room Gals
Then Bruna sees the room Gals have 4 participants

Scenario:
Given a new user Auditor with role AUDITOR
Then the Auditor is able to fetche all rooms

Scenario:
Given Eddy updates name of the room Dudes to Pals
Then Edd sees the room Dudes has a new name Pals

Scenario:
When Bruna deletes the room Gals
When Ed deletes the room Dudes
Then the user Bruna sees the room Gals does not exists anymore
Then the user Ed sees the room Dudes does not exists anymore
Meta: @room

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: SetUp
Given a new user <nameOfUser> with role <nameOfRole>
Examples:
|nameOfUser |nameOfRole |
|Ed         |NORMAL     |
|Edd        |NORMAL     |
|Eddy       |ADMIN      |
|Bruna      |ADMIN      |
|Amanda     |NORMAL     |
|Liza       |NORMAL     |
|Carol      |NORMAL     |

Scenario: An user creates room containing other users
When Ed creates a new room Dudes with Edd, Eddy
Then Ed sees the room Dudes have 3 participants
Then the user Ed can fetch his rooms

Scenario: An user creates room containing other users and removes one user
When Bruna creates a new room Gals with Amanda, Liza, Carol
When Bruna removes Amanda from the room Gals
Then Bruna sees the room Gals have 3 participants
Then the user Bruna can fetch his rooms

Scenario: An user has a room and sends a message to other users in the room
When Ed has room Dudes with Ed, Edd, Eddy
When Ed sends a message Hello to Edd, Eddy in room Dudes
Then Ed message Hello has 2 receivers
Then Edd, Eddy is able to see the message Hello Ed sent to them in room Dudes

Scenario: An user with role AUDITOR can list and search rooms and also check messages
Given a new user Auditor with role AUDITOR
Then Auditor fetches all rooms
Then Auditor searches rooms by SENDER Ed
Then Auditor searches rooms by RECEIVER Eddy
Then Auditor searches rooms by CONTENT Hello
Then Auditor fetches all messages
Then Auditor fetches message Hello
Then Auditor sees Ed sent message Hello
Then Auditor sees Edd, Eddy received message Hello

Scenario: An user updates the name of a room containing other users
When Eddy updates name of the room Dudes to Pals
Then Edd sees the room Dudes has a new name Pals
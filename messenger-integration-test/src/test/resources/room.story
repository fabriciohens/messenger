Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal


Scenario: when an user creates a user the api would respond with the new user containing a new id
Given a new user with role normal containing an id
Examples:
|user
|bruna
|rodrigo
|liza
|carol

Scenario: when an user creates a room the api would respond with the new room containing a new id
Given an user and a new room with participants containing an id
Examples:
|user|room|participants
|bruna|mists|rodrigo,liza,carol
|rodrigo|gals|liza,carol
|liza|nanmm|bruna,rodrigo,carol
|carol|lony|bruna

Scenario: when an user fetches his room by id using his credentials the api responds with the room
When the user fetches his room by id using his credentials the api responds with the room
Examples:
|user|room
|bruna|mists
|rodrigo|gals
|liza|nanmm
|carol|lony

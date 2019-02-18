Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal


Scenario: when an user creates a user the api would respond with the new user containing a new id
Given a new user with role admin containing an id
When the user fetches his user by id the api responds with detailed user
Examples:
|user
|bruna
|rodrigo

Scenario: when an user updates his user the api would respond with the updated user
Given an updated user with role admin and his id
When the user fetches his user by id using new credentials the api responds with the user
Examples:
|user
|bruna
|rodrigo

Scenario: when an user lists all users the api would respond with a list of all users
When an user lists all users the api responds with a list of all users
Examples:
|user
|bruna
|rodrigo

Scenario: when an user deletes his user the api would respond with unauthorized for new requests
Given a deleted user with role admin and his id
When the user fetches his user by id using his credentials the api responds with unauthorized
Examples:
|user
|bruna
|rodrigo
Meta:

Narrative:
As a user
I want to create a new user
So that I can have a user with an id

Scenario: when BRUNA creates a user the api would respond with the new user containing a new id

Given a new user BRUNA with role ADMIN containing an id
When BRUNA fetches herself by id the api responds with detailed BRUNA
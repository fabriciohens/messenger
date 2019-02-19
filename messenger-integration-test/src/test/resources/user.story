Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: SetUp
Given a new user Rodrigo with role ADMIN

Scenario:
Given Rodrigo creates a new user Mari with role ADMIN
Given Rodrigo creates a new user Gabi with role AUDITOR
Given Rodrigo creates a new user Fabricio with role NORMAL
Then Mari is able to fetch all users
Then Gabi is unable to fetch all users
Then Fabricio is unable to fetch all users

Scenario:
Given Rodrigo updates role of Mari to NORMAL
Given Rodrigo updates role of Fabricio to ADMIN
Then Mari is unable to fetch all users
Then Fabricio is able to fetch all users
Then Fabricio is able to fetch user Mari

Scenario:
Given Rodrigo updates role of Gabi to ADMIN
Then Gabi is able to delete user Mari

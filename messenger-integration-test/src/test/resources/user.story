Meta: @user

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: SetUp
Given a new user Rodrigo with role ADMIN

Scenario: An user creates others users with roles and these users test their roles
When Rodrigo creates a new user Mari with role ADMIN
When Rodrigo creates a new user Gabi with role AUDITOR
When Rodrigo creates a new user Fabricio with role NORMAL
Then Mari is able to fetch all users
Then Gabi is unable to fetch all users
Then Fabricio is unable to fetch all users

Scenario: An user updates other user's roles and these users test their roles
When Rodrigo updates role of Mari to NORMAL
When Rodrigo updates role of Fabricio to ADMIN
Then Mari is unable to fetch all users
Then Fabricio is able to fetch all users
Then Fabricio is able to fetch user Mari

Scenario: An user updates another user's role to ADMIN and this user test his role
When Rodrigo updates role of Gabi to ADMIN
Then Gabi is able to delete user Mari

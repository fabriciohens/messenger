Meta: @user

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: SetUp
Given a new user Rodrigo with role ADMIN

Scenario: An user creates an user with role ADMIN and can do some admin task
When Rodrigo creates a new user Mari with role ADMIN
Then Mari is able to fetch all users

Scenario: An user creates others users with non ADMIN roles and these users cannot do admin tasks
When Rodrigo creates a new user Gabi with role AUDITOR
When Rodrigo creates a new user Fabricio with role NORMAL
Then Gabi is unable to fetch all users
Then Fabricio is unable to fetch all users

Scenario: An user updates another user's role to NORMAL so this user cannot do admin task
When Rodrigo updates role of Mari to NORMAL
Then Mari is unable to fetch all users

Scenario: An user updates another user's role to ADMIN so this user can do some admin tasks
When Rodrigo updates role of Fabricio to ADMIN
Then Fabricio is able to fetch all users
Then Fabricio is able to fetch user Mari

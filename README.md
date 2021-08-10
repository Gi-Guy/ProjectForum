# ProjectForum
 This is my final project in the university - A Java forum web application.

Last Update:


-----------------------------

This update represent a whole new REST web services update including:

**CRUD actions for entitys:**
*Users entities
- Forums entities
- Topics entities
- Posts entities
* Might add a private messages entities in the future
*CRUD = Create, Read, Update, Delete

**New services are:**
Users: 
- Get all users list
- Find User by: username, ID
- Create new user
- Update an exists user - Change email, FirstName, LastName, Password && Role.
- Delete an exists user
- System also provides examples of: Creating a new User, Update/Delete an exists user.

Forums:
- Get all Forums list
- Find Forum by: ID, Priority
- Create new Forum
- Update an exists Forum - Change Name && Description.
- Delete an exists Forum
- System also provides examples of: Creating a new Forum, Update/Delete an exists Forum.

Topics:
- Get all Topics list
- Find Topic by: ID
- Delete an exists Topic by ID

Posts:
- Get all Posts list
- Find Post by: ID
- Delete an exists Post by ID

**Also:**
Fixed many bugs and improve some methods.


-----------------------------

New Update: Private Messages
Users now has the ability to send private messages between them and other users.

**USER features**
- Users can send private messages between them and other users.
- Users can responde in a private Conversation.
- Each user can delete it own answers.
- Each user can delete the conversation itself.

**ADMIN features**
- Admins can read users private messages.
- Admins can delete users private messages.
- Admins can respone in other users private messages.
- When Admin delete an exists user, All user's private messages will be deleted as well.

**Missing features**
- There is no limit to a number of conversations that user can have.
- There is no time limit for a conversation

##################################################################################################


This update has a servel majors updates:

**YOU MUST TO CREATE A WHOLE NEW DATABASE AT THIS POINT**

*I'll update the cloud database soon.

**New Roles System:**

I've removed the old roles system and created a new one which now works in all leves of the application, including via thymeleaf pages.

**Services:**

I've created new services files which are working on the business layer,

There are services for editing entitys and delete entitys.

**When deleting a user, all user's activity will be not deleted but will be attached to a dummy user.

**ADMIN USER CAN'T BE DELETED via control panel nor via application services.

**DUMMY USER CAN'T BE DELETED via control panel nor via application services.


**Control Panel:**

I've finally created the control panel files and an temporary controller page.

**I've only finished the forums section.


**Initializing:**

When first initializing the application, The application will automatically create:

*All Entity's tables, including joinTables

*Admin user to access the application control panel.

*Unknown Dummy user to keep deleted users activity.


**Bugs:**

Fixed alot of bugs, the application is running smoothly.


**Known bugs:**

*Sometimes login page randomly move users to incorrect target page.


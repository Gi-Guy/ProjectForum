# ProjectForum
 This is my final project in the university - A Java forum web application.

Last Update: 14/8/21

**This update will include the following:**

### _**Exceptions and Exploits Handlers**_
There are a whole new Exceptions Handlers that will handle most of the errors and exploits of the application.
- All controllers has been updated to use services only and will not access data via Repository's files.
- All services has been updated to give more services and catch as many errors they can.
- All controllers and Security files has been updated so they will be able to catch exploiters.
- Exceptions and Exploits Handlers will print reports in console when they catch an exploits or exceptions.

### _**Errors 404/403**_
- Exceptions Handlers will take USER to page error 404 when something went wrong.
- Exploits Handlers will take USER to page error 403 when user is trying to exploit the application.
- pages 403 and 404 has been updated with new content.

### _**Configurations**_
User can now set an default configurations via application.properties file including:
- Forum's official Name
- Forum's official Description
- Forum's official Time until delete data.

In first initialize an configurations class will create a new table of _**ForumInformation**_ and will put all values from application.properties file.

After the first initialize this class will not be activated again, However if table will be deleted it will create the table again and update it with default values.

### _**Control Panel**_
- Admins can now search users with upper and lower letters.
- Admins can now update the Forum's name. 
- Admins can now update the Forum's Description.
- Admins can now update the Forum's Time until delete data.
**_### CONTROL PANEL IS COMLETED_**

### _**Web Services (REST)**_
- Admins can now update the Forum's name via web services.
- Admins can now update the Forum's Description via web services.
- Admins can now update the Forum's Time until delete data via web services.
**_### WEB SERVICES IS COMPLETED_**

### _**Login**_
- There is a new limitation that prevent logged users to access the login page.
- Users can now click on 'Remember me' option. 
NOTE: 'Remember me' options isn't working after reseting application, users have to click on it again.

### _**Home Page**_
- I've updated the homepage so it will display an information from the forum's configurations

### _**Bugs**_
- i've fixed many major and minor bugs.

### _**Known Bugs**_
There are some known bugs that will be not fixed.
- for example Web services exploits.



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


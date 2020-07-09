#Wording

# Import logfiles
* The commandline command ``spica import logs `` you find all logs in the current folder and all subdirectories. 
*  This command shows all files, which contain exceptions as warning (yellow) and all other files with default color (green). 
*  If you want to import a specific file append this filename. If you want to import all files with a stacktrace call ``spica import logs stack``. 
*  If you want to import all logs call ``spica import logs all``. 
 
# Copy & paste 
* Global key Ctrl+S inserts current clipboard entity to spica clipboard
* Ctrl+C / Ctrl+V inserts copied entity to spica clipboard
* DragAnd Drop inserts something to spica clipboard
* Screenshots can be inserted to spica clipboard by 


# Tasks
## Add task 
* Create a todo when pressing ENTER in Textfield, clipboard is added as file or link   
* Add files / links to the task 
* Entering @ shows users as auto suggestion
* Entering # shows tags 
* Todo is created in priority 3 by default, + sets the prio 2, ++ sets prio1, - sets prio4
* Entering -> sets planning date e.g ->today, ->tomorrow, ->01.12.,)
* Entering () sets duration, e.g. (1h) (30min) (5min)
* Create a voicemessage
* Command ``spica t create`` creates a new task

## Show tasks 
* Command ``spica t list`` shows all tasks 
* Command ``spica t show [STRING]`` shows details of tasks containing STRING

## Finish task
* In the todo overview click on button *finish* or type f
* Task can be finished with commandline ``spica t finish``
## Remove task 
* In the todo overview click on button *remove* or type -. This removes a jira issue if associated
* Task can be removed with commandline ``spica t remove``. This removes a jira issue if associated

## Tasks - Show new tasks in header
## Tasks - Fileserver Upload / Download / Link
## Tasks - Attach file / screenshot / content to task
## Tasks - Jira integration
* A task can be created in spica only, optionally be created in 
jira and linked
* Tasks can be imported from external system from commandline with ``spica t import`` or button in fxclient
 
# Booking
## Booking - Start working at a task 
## Booking - Stop working on a task
## Booking - Plan all tasks be done on day/week 
## Booking - Pause
## Booking - Finish day
## Message - Attach file / content to booking

# Message
## Message - New message (chat/mail/...)
## Message - Recieve new messages from server 
## Message - Show number of unread message
## Message - Attach file / content to messagecontainer / message
## Message - Create task from message
## Message - Create note from message
## Message - Create event from message
## Message - Mail integration
## Message - Chat integration

# Planning
## Planning - Create event from task
## Planning - Plan the day

# Notes
## Notes - Create note
## Notes - Attach file / content to node
## Notes - Remove note
## Notes - Tag notes
## Notes - Evernote integration
https://github.com/evernote/evernote-sdk-java


# User
## User - LDAP integration 
## User - UserGroup (=Department)
## User - Security Profile
## User - Visibility Scope for entities 
## User - Roles (User/Admin/Technical Lead...)
## User - SecurityScheme
User has a certain role. Every role is connected with a security scheme. 
Also a user security scheme can override the security scheme of the role (by admin).
## User - Notify when user getting online
 

# Search
## Search in entities (tasks,message,user,planning)








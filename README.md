# Splitty - Team 32 

## How to execute our program
In your prefered IDE, open the folder of our app. Firstly, run server/src/main/java/server/Main.java
While the server is running, run client/src/main/java/client/Main.java

### Acessing features
After the client has been started, you will be shown the Start Page.
- The Start Page contains:
  - Invite Code field + Join Event Button, directs us to the Overview page of the Event
  - Event Name field + Create Event Button, directs us to the Overview page of the Event
  - A language switch option
  - A list of all events, from which you can choose an event you want to join by clicking on it, directs us to the Overview page of the Event
  - A security button that directs us to the Admin Login page.
- The Admin Login Page contains:
  - A password field that admins should use to be directed to the Admin Dashboard
  - The password is randomly generated when the server is started and it can be found in the server console as: "Using generated security password: *password*"
- The Admin Dashboard contains:
  - A language switch option
  - A list of all events
  - A Sort By button which lets the Admin to sort the events in ascending order by:
    - Title
    - Creation Date
    - Last Activity Date
  - By right-clicking on an event you will see the options to:
    - Permanently delete an event from the database
    - Download an event to save it as a JSON backup file
    - Upload a JSON file as an Event.
  - Go back button that directs to Start Page
- Overview Page contains:
  - Edit button next to the event name for editing the event name
  - Statistics button directs to the Statistics Page
  - Settle Debts button directs to the Settle Debts Page
  - Tags button directs to the Tags Page
  - Test Email tests if the email is correct.
  - Event Code: the code for joining an event
  - Participants: current participants list + Add Participant button directs to the Add Participant Page
  - A list of expenses with options to filter by payer, paid for users and description
  - Plus button directs to the Add Expense page
  - A language switch option
  - Edit button for each expense
  - Send invites Button directs to the Send invites button
- Add participant page contains:
  - Fields for all the information about the user
  - Automated check if the email has correct format
- Send Invites Page
  - A field to enter all emails of the people we wish to invite to the team
  - Creates participants based on the name and email of the people we have invited
- Add expense Page
  - Fields for all the information about the expense
  - Options about how the payer wants to split the expense with the other users
- Add Tag Page
  - Shows all existing tags
  - Create tag button that redirects to a page with 2 fields: choose name and colour
- Statistics Page
  - Shows all statistics about the expenses and debts
- Settle Debts Page
  - Shows payment instruction such that all debts are cleared.
  - Marked as received button removes the payment instruction

### Shortcuts
- For undo: CTRL+Z
- For navigation: TAB + Space


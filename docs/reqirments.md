# Design Decision

## Spec


## Terms
- **Organiser** - The person responsible for organising the meal  
- **Meal** - A collection of orders belonging to an organising  
- **Order** - A Participants requested food and drink for a meal  
- **Participants** - A person attending a mean who wants a specific order
- **Venue Menu** - A collection of Menu Item available at a specific Venue  
- **Venue Menu Item** - An item available at a specific menu.


## Elevator pitch
This application would allow large groups to easily order a meal from a restaurant.    
Participants would be able to add their orders for a one or more specific event in one place and see all available option on a menu.  
Participants would also be able to see details about the meal e.g. time, location, deadline for orders  
An Organiser would then be able to place a group order to the restaurant from the orders submitted by participants  

## Requirements
- **Organisers** must authenticate through Google.
- **Organisers** should be able to view and edit a **Meal** after it has been created.
- **Organisers** should be able to add **Participants** to a **Meal** at any point before the order deadline.
- **Participants** should have sensible authorization control. For example **Participants** should not be able to edit the **Meal** details or edit/view other **Participants** orders.
- **Participants** should be able to use the application anonymously or authenticate through Google.
- **Participants** should be able to place/edit and view their orders related to a meal at any point before the deadline.



## User journey

### **Organiser** user journey for creating a meal
The **Organiser** authenticates and lands on home page
The **Organiser** is then able to navigate to the new meal page
The **Organiser** is presented with and fills in a form to create a **Meal**
The **Organiser** is then given a Meal link to invite the **Participants** to the **Meal**

### **Unauthenticated Participants** user journey for adding an order to a meal
The **Unauthenticated Participants** opens **Meal** link provided by an **Organiser** 
The **Unauthenticated Participants** is presented a screen asking if they would like to sign in to sign in. If they log in the **Authenticated Participants** user journey is triggered.
The **Unauthenticated Participants** is presented with and fills in a form with **Venue Menu Items** from the provided **Venue Menu** to add a **Order** to the **Meal**
The **Unauthenticated Participants** is then given a link to edit and view their order at a later date if needed.

### **Authenticated Participant** user journey for adding an order to a meal
The **Authenticated Participant** opens **Meal** link provided by an **Organiser**
The **Meal** that the Meal link belongs to is then associated with the **Authenticated Participant** so that they can view it at a later date.
The **Unauthenticated Participants** is presented with and fills in a form with **Venue Menu Items** from the provided **Venue Menu** to add a **Order** to the **Meal**

### **Participants** user journey for viewing a **Meal** once the **Meals** order deadline has passed
The **Participant** either uses the **Meal** link or uses the list of **Meals** associated to their account to accesses a specif **Meal** page.
The **Participant** is then able to view but not edit their **Order** for the given Meal along with the **Meal** details 

### **Organiser** user journey for viewing a **Meal** once the **Meals** order deadline has passed
The **Organiser** uses the list of **Meals** associated to their account to accesses a specif **Meal** page.
The **Organiser** can then see everyone's **Orders** and is able to export them in a csv format or tab separated format to allow them to easily add to a table in a external application.


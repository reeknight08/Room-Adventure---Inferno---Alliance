import java.util.Scanner;                                               // Import scanner for reading user input

public class RoomAdventure {                                           // Main class containing game logic

    // class variables
    private static Room currentRoom;                                    // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status;                                       // Message to display after each selection 
    private static long Time(){
        return System.currentTimeMillis() / 1000 / 60 ;                              // Returns the current time in milliseconds
    }
    private static long endTime = Time() + 5;                               // Timer to track game time
    
    // constants
    final private static String DEFAULT_STATUS =                        
    // Default error message
    "Sorry, I do not understand, Try [verb] [noun]. Valid verbs include  'go', 'look', and 'take'.";

    public static int findElement(String[] list, String element){
        if(list == null){
            return -1;
        }
        
        for (int i = 0; i < list.length; i++) {                       // Loop through the list
            if (list[i] != null && list[i].equals(element)) {          // Check if element matches
                return i;                                              // Return index if found
            }
        }
        return -1;                                                     // Return -1 if not found
    }

    private static void handleGo(String noun) {
        String[] exitDirections = currentRoom.getExitDirections();      // Get available exit directions
        Room[] exitDestinations = currentRoom.getExitDestinations();    // Get corresponding room objects  
        status = "I don't see that room.";                              // Default if direction not found
        for (int i = 0; i < exitDirections.length; i++) {               // Loop through exit directions
            if (noun.equals(exitDirections[i])) {                    // if direction matches
                if(exitDestinations[i].getName() == "Power Core" && findElement(inventory, "ID-Card") == -1){ // If trying to enter the power core without the ID card then they are locked out
                    status = "You do not have the ID card Required to enter the Power Core.";
                    break;
                }
                currentRoom = exitDestinations[i];                      // Move to the new room
                status = "Changed Room";                                // Update status 
            }
        }
    }

    private static void handleLook(String noun) {                       // Handles inspecting items
        String[] items = currentRoom.getItems();                        // Visible Items in the room
        String[] itemDescriptions = currentRoom.getItemDescriptions();  // Descriptions of items in the room
        status = "I don't see that item.";                              // Default if item not found
        for (int i = 0; i < items.length; i++) {                        // Loop through items
            if (noun.equals(items[i])) {                                // if user noun matches an item
                status = itemDescriptions[i];                           // Update status with item description
            }
        }
    }

    private static void handleTake(String noun) {                       // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables();              // Items that can be picked up
        status = "I can't grab that.";                                  // Default if not grabbable
        for (String item : grabbables) {                                // Loop through grabbables
            if (noun.equals(item)) {                                    // If user noun matches a grabbable item
                if(item == "Death-Star-Plans" && findElement(inventory, "Maintenance-Tools") == -1){ // If trying to take the death star plans without the maintenance tools then they are blocked
                    status = "You do not have the Tools Required to take the Death Star Plans.";
                    break;
                }
                for (int j = 0; j < inventory.length; j++) {            // Loop through inventory
                    if (inventory[j] == null) {                         // If empty slot found
                        inventory[j] = item;                            // Add item to inventory
                        status = "Item added to inventory.";            // Update status
                        break;                                          // Exit loop
                    }
                }
            }
        }
    }
    private static void handleUse(String noun) {
    if (noun.equals("blaster") && findElement(inventory, "blaster") != -1) {
        if (currentRoom.getName().equals("Power Core")) {
            System.out.println("\nYou raise your blaster and fire directly into the Power Core...");
            System.out.println("The core destabilizes. Warning sirens blare.");
            System.out.println("As explosions erupt around you, you smile knowing the Death Star will fall.");
            System.out.println("\nCUTSCENE: You sacrificed your life to destroy the Death Star.");
            System.out.println("The rebels pick up the debris and cheer your bravery.\n");
            System.out.println("GAME OVER - SACRIFICE ENDING");
            System.exit(0); // Ends the game
        } else {
            status = "You can only use the blaster in the Power Core.";
        }
    } else {
        status = "You don't have that item or can't use it here.";
        }
    }


    private static void setupGame(){
        // Hanger Bay
        Room hangerBay = new Room("Hanger Bay");                            // Create hanger bay
        Room controlroom = new Room("Control Room");                            // Create control room
        Room powercore = new Room("Power Core");                            // Create power core
        Room maintenance = new Room("Maintenance");                       // Create maintenance room
        Room armory = new Room("Armory");                                 // Create armory room

        String[] hangerBayExitDirections = {"north"} ;                        // Set exit directions for hanger bay
        Room[] hangerBayExitDestinations = {controlroom};                          // Set exit directions and destinations for hanger bay
        String[] hangerBayItems = {"Tie-Fighter", "Mouse-Droid"};                         // Set items in hanger bay
        String[] hangerBayItemDescriptions = {                               // Set item descriptions for hanger bay
            "An Imperial Tie Fighter, might be good on the way out.", "A small mouse droid."
        }; 
        String[] hangerBayGrabbables = {"blaster"};                             // Set grabbables for hanger bay
        hangerBay.setExitDirections(hangerBayExitDirections);                   // Set exit directions for hanger bay
        hangerBay.setExitDestinations(hangerBayExitDestinations);               // Set exit destinations for hanger bay
        hangerBay.setItems(hangerBayItems);                                     // Set items for hanger bay
        hangerBay.setItemDescriptions(hangerBayItemDescriptions);               // Set item descriptions for hanger bay
        hangerBay.setGrabbables(hangerBayGrabbables);                           // Set grabbables for hanger bay

        // Control Room
        String[] controlroomExitDirections = {"north", "east", "south", "west"};                        // Set exit directions for control room
        Room[] controlroomExitDestinations = {powercore, maintenance, hangerBay, armory};                         // Set exit directions and destinations for control room
        String[] controlroomItems = {"Computer-Terminals", "ID-Card", "Targeting-Screens"};             // Set items in control room
        String[] controlroomItemDescriptions = {                              // Set item descriptions for control room
            "A large terminal that controls the Death Star.", "An ID card that gets you into the Powercore", "A targeting screen that shows the Death Star's target."
        };
        String[] controlroomGrabbables = {"ID-Card"};                               // Set grabbables for control room
        controlroom.setExitDirections(controlroomExitDirections);                   // Set exit directions for control room
        controlroom.setExitDestinations(controlroomExitDestinations);               // Set exit destinations for control room
        controlroom.setItems(controlroomItems);                                     // Set items for control room
        controlroom.setItemDescriptions(controlroomItemDescriptions);               // Set item descriptions for control room
        controlroom.setGrabbables(controlroomGrabbables);                           // Set grabbables for control room

        // Power Core
        String[] powercoreExitDirections = {"south"};                        // Set exit directions for power core
        Room[] powercoreExitDestinations = {controlroom};                         // Set exit directions and destinations for power core
        String[] powercoreItems = {"Power-Core", "Death-Star-Plans"};             // Set items in power core       
        String[] powercoreItemDescriptions = {                              // Set item descriptions for power core
            "The power core of the Death Star.", "The plans to destroy the Death Star. can be stolen with the proper tools"
        };

        String[] powercoreGrabbables = {"Death-Star-Plans"};                    // Set grabbables for power core
        powercore.setExitDirections(powercoreExitDirections);                   // Set exit directions for power core
        powercore.setExitDestinations(powercoreExitDestinations);               // Set exit destinations for power core
        powercore.setItems(powercoreItems);                                     // Set items for power core
        powercore.setItemDescriptions(powercoreItemDescriptions);               // Set item descriptions for power core
        powercore.setGrabbables(powercoreGrabbables);                           // Set grabbables for power core
        
        // Maintenance
        String[] maintenanceExitDirections = {"west"};                          // Set exit directions for maintenance
        Room[] maintenanceExitDestinations = {controlroom};                     // Set exit directions and destinations for maintenance
        String[] maintenanceItems = {"Maintenance-Droids", "Maintenance-Tools"};// Set items in maintenance
        String[] maintenanceItemDescriptions = {                                // Set item descriptions for maintenance
            "A group of maintenance droids.", "A set of tools for repairing or destroying the Death Star."
        };
        String[] maintenanceGrabbables = {"Maintenance-Tools"};                 // Set grabbables for maintenance
        maintenance.setExitDirections(maintenanceExitDirections);               // Set exit directions for maintenance
        maintenance.setExitDestinations(maintenanceExitDestinations);           // Set exit destinations for maintenance
        maintenance.setItems(maintenanceItems);                                 // Set items for maintenance
        maintenance.setItemDescriptions(maintenanceItemDescriptions);           // Set item descriptions for maintenance
        maintenance.setGrabbables(maintenanceGrabbables);                       // Set grabbables for maintenance

        // Armory
        String[] armoryExitDirections = {"east"};
        Room[] armoryExitDestinations = {controlroom};
        String[] armoryItems = {"blaster"};
        String[] armoryItemDescriptions = {"A powerful blaster capable of destroying vital systems."};
        String[] armoryGrabbables = {"blaster"};

        armory.setExitDirections(armoryExitDirections);
        armory.setExitDestinations(armoryExitDestinations);
        armory.setItems(armoryItems);
        armory.setItemDescriptions(armoryItemDescriptions);
        armory.setGrabbables(armoryGrabbables);

        // Set up the game
        currentRoom = hangerBay;                                          
    }

    @SuppressWarnings("java:S2189")
    public static void main(String[] args) {
        setupGame(); // Initialize game setup
        System.out.println("Welcome to the Death Star! \nYou have 5 minutes to escape the Death Star with the plans before it blows up your planet. \nYou can go [north], [south], [east], or [west]. \nYou can look at items in the room with 'look [item]'. \nYou can take items with 'take [item]'. \nWould you like to play with a countdown timer? (y/n)"); // Print welcome message
        Scanner ts = new Scanner(System.in);
        String tstring = ts.nextLine();



        while (true) { 
            if(findElement(inventory, "Death-Star-Plans") != -1 && currentRoom.getName() == "Hanger Bay"){     // If the player has the death star plans are are in the Hanger Bay then they win
                System.out.println("\nYou have the Death Star Plans! \nYou fly out of the Death Star in a Tie-Fighter with the plans bringing them to the rebels. \nThey band together and go blow up the Death star, defeating the Empire!");
                break;
            }
            if (tstring.equals("y")) { // If the user wants to play with a countdown timer
                System.out.println((endTime - Time()) + " Minutes remaining"); // Print the current time in minutes
                if (endTime - Time() <= 0) {                               // If time is up
                    System.out.println("You have run out of time! The Death Star is ready to blow up your planet.");       // Print game over message
                    break;                                                  // Exit game loop
                }
            }

            System.out.print(currentRoom.toString());                   // Print current room
            System.out.print("\nInventory: ");                        // Print inventory
            for (int i = 0; i < inventory.length; i++) {                // Loop through inventory slots
                System.out.print(inventory[i] + " ");                   // Print each item in inventory
            }
            System.out.println("\nWhat would you like to do? ");      // Prompt for user input

            Scanner s = new Scanner(System.in);                         // Create scanner for user input
            String input = s.nextLine();                                // Read entire line of input
            String[] words = input.split(" ");                    // Split input into words

            if (words.length != 2) {                                    // Check for proper two-word command
                status = DEFAULT_STATUS;                                // Set default status if input is not two words
                continue;
            }
            String verb = words[0];                                     // First word is the verb
            String noun = words[1];                                     // Second word is the noun

            switch (verb) {                                             // Handle different verbs
                case "go":                                              // If verb is "go"
                    handleGo(noun);                                     // Call method to handle movement
                    break;
                case "look":                                            // If verb is "look"
                    handleLook(noun);                                   // Call method to handle looking at items
                    break;
                case "take":                                            // If verb is "take"
                    handleTake(noun);                                   // Call method to handle taking items
                    break;
                case "use":                                             // If verb is "use"
                    handleUse(noun);                                    // Call method to handle using items
                    break;
                default:                                                // Invalid command
                    status = DEFAULT_STATUS;                            // Set default status for invalid command
            }

            System.out.println("\n" + status);                                 // Print status message
        }
    }
}


class Room {                            // Represents a game room
    private String name;                // Name of the room
    private String[] exitDirections;    // Directions to other rooms
    private Room[] exitDestinations;    // Rooms corresponding to the exit directions
    private String[] items;             // Items visible in the room
    private String[] itemDescriptions;  // Descriptions of the items
    private String[] grabbables;        // Items that can be picked up

    public Room (String name) {         // Constructor
        this.name = name;               // Set the room name
    }
    public String getName() {
        return name;                   // Getter for room name
    }

    public void setExitDirections(String[] exitDirections) {    // Setter for exits
        this.exitDirections = exitDirections; 
    }

    public String[] getExitDirections() {                       // Getter for exits
        return exitDirections; 
    }

    public void setExitDestinations(Room[] exitDestinations) {  // Setter for exit destinations
        this.exitDestinations = exitDestinations; 
    }

    public Room[] getExitDestinations() {                       // Getter for exit destinations
        return exitDestinations; 
    }

    public void setItems(String[] items) {                      // Setter for items
        this.items = items; 
    }

    public String[] getItems() {                                // Getter for items
        return items; 
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for item descriptions
        this.itemDescriptions = itemDescriptions; 
    }

    public String[] getItemDescriptions() {                     // Getter for item descriptions
        return itemDescriptions; 
    }

    public void setGrabbables(String[] grabbables) {            // Setter for grabbables
        this.grabbables = grabbables; 
    }

    public String[] getGrabbables() {                           // Getter for grabbables
        return grabbables; 
    }

    @Override
    public String toString() {                      // Custom print for the room
        String result = "\nLocation: " + name;      // Show room name
        result += "\nYou See: ";                    // List items
        for (String item : items) {                 // Loop items
            result += "\n" + item;                  // Append each item
        }
        result += "\nExits: ";                      // List exits
        for (String direction : exitDirections) {   // Loop exits
            result += direction + " ";              // Append each exit direction

        }
        return result + "\n";                       // Return full description
    }
}
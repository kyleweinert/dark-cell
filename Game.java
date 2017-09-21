/**
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 */
import java.lang.Object;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.Vector;
import java.util.Stack;
import java.util.ArrayList;

public class Game
{
    private Object game;
    private Parser parser;
    private Item sword, healthp, blueKey, redKey, mCookie;
    private Room cell, armory, health, key, red, blue, boss, locked;
    private Stack<Room> lastRoom;
    private Player steve;
    private ArrayList allItems;
    private int i;
    private Item newItem;

    public static void main(String [ ] args){
        Game game = new Game();
        game.play();
    }

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createItems();
        createRooms();
        createPlayer();
        parser = new Parser();
        lastRoom = new Stack();
    }

    private void createItems(){
        // create items
        sword = new Item("Sword", "a rusty sword", 10, true);
        healthp = new Item("Health-Potion", "heals battle wounds", 2, true);
        blueKey = new Item("Blue-Key", "opens a blue door", 1, true);
        redKey = new Item("Red-Key", "opens a red door", 1, true);
        mCookie = new Item("Magic-Cookie", "increases max carry weight", 0, true);
    }

    private void createPlayer(){
        steve = new Player(cell, "Steve", 0);
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {

        // create the rooms
        cell = new Room("You are in your cell.");
        armory = new Room("You have entered a small, unguarded room with a rusty sword in the corner.");
        health = new Room("You have entered room with a with a beast guarding a small vile.");
        key = new Room("You have entered a room with a beast guarding a key which you expect you will need later.");
        red = new Room("You have entered a room with a beast guarding a red door." + ".\n" + "Use a red key to UNLOCK the door.");
        blue = new Room("You have entered a room with a beast guarding a blue door." + ".\n" + "Use a blue key to UNLOCK the door.");
        boss = new Room("You have entered the bosses lair.");
		locked = new Room("You should not be here.");

        // set exits
        cell.setExit("east", armory);
        armory.setExit("north", health);
        armory.setExit("east", key);
        armory.setExit("west", cell);
        health.setExit("south", armory);
        key.setExit("north", red);
        key.setExit("east", blue);
        key.setExit("west", armory);
        red.setExit("south", key);
        blue.setExit("west", key);
        boss.setExit("south", blue);
        boss.setExit("west", red);
        blue.setExit("north", locked);
        red.setExit("east", locked);

        // set items
        armory.addItem(sword);
        armory.addItem(healthp);
        health.addItem(healthp);
        key.addItem(blueKey);
        red.addItem(healthp);
        blue.addItem(healthp);
        cell.addItem(mCookie);
    }

    private void printLocationInfo(){
        System.out.println(steve.getCurrentRoom().getLongDescription());
    }

    private void look(){
        System.out.println(steve.getCurrentRoom().getLongDescription());
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    private void play()
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {

            Command command = parser.getCommand();
            /*if(command.getCommandWord().equals("hello"))
            {
            System.out.println("Go Away!!!");
            } else{
            if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            }
            }*/

            finished = processCommand(command);
        }
        System.out.println("Farewell Traveler.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("You awaken trapped in a cold cell alone.");
        System.out.println("Welcome to Dark Cell.");
        System.out.println("Type 'help' for a list of Commands.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
            System.out.println("I don't know what you mean...");
            break;

            case HELP:
            printHelp();
            break;

            case GO:
            goRoom(command);
            break;

            case LOOK:
            look();
            break;

            case EAT:
            eat();
            break;

            case BACK:
            back();
            break;

            case TAKE:
            take(command);
            break;

            case DROP:
            drop(command);
            break;

            case ITEMS:
            items();
            break;

            case UNLOCK:
            unlock();
            break;

            case QUIT:
            wantToQuit = quit(command);
            break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    private void items(){
        System.out.println(steve.getItemString());
    }

    private void back(){
        if(lastRoom.empty()){
            System.out.println("You can't go back.");
        } else{
            steve.setCurrentRoom(lastRoom.pop());
        }
        look();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        lastRoom.push(steve.getCurrentRoom());
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = steve.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            if(nextRoom != locked){
                steve.setCurrentRoom(nextRoom);
                printLocationInfo();
            }else{
				System.out.println("The door is locked. Use UNLOCK to enter.");
            }
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    private void take(Command command){
        String thisItem = command.getSecondWord();
        if(command.hasSecondWord()) {
            if(steve.getCurrentRoom().hasRItem(thisItem)){
                if(steve.canPickUpItem(thisItem)){
                    steve.pickUpItem(thisItem);
                    steve.getCurrentRoom().removeItem(thisItem);
                    System.out.println(thisItem + " taken");
                }else{
                    System.out.println(thisItem + " is too heavy.");
                }
            }else{
                System.out.println("Can't find item: " + thisItem);
            }
        }
        else {
            System.out.println("Take what?");
        }
    }

    private void drop(Command command){
        if(command.hasSecondWord()) {
            if(steve.hasItem(command.getSecondWord())){
                steve.dropItem(steve.getItem(command.getSecondWord()));
                System.out.println(command.getSecondWord() + " dropped");
            }else{
                System.out.println("Can't drop the item: " + command.getSecondWord());
            }
        }
        else {
            System.out.println("Drop what?");
        }
    }

    private void eat(){
        if(steve.hasItem("Magic-Cookie")){
            steve.setMaxWeight(20);
            steve.removeItem("Magic-Cookie");
            System.out.println("You feel yourself becoming stronger.");
        } else{
            System.out.println("You don't have anything to eat!");
        }
    }

    private void unlock(){
        Room location = steve.getCurrentRoom();
        boolean hasBKey = steve.hasItem("Blue-Key");
        boolean hasRKey = steve.hasItem("Red-Key");
        if(hasBKey && location == blue){
            steve.setCurrentRoom(boss);
        }else if(hasRKey && location == red){
            steve.setCurrentRoom(boss);
        }else{
            System.out.println("You do not have the correct key to open the door in this room.");
        }
		look();
    }
}

package zork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static zork.Constants.*; //import all from Constants

public class World {
    private String file;
    Room currentRoom;
    private String start;
    private Map<String, Room> mapOfRooms = new HashMap<>();
    private List<String> inventory = new LinkedList<>();

    public World(String file) {
        this.file = file;
    }

    public RoomDescription start() {
        System.out.println("Starting room ID: " + start);
        currentRoom = mapOfRooms.get(start); // to load the current room. given the start variable, find it in map of rooms
        if (currentRoom == null) {
            System.out.println("Error: Start room is null!");
        }
        currentRoom.printItems();
        currentRoom.printDirections();
        return new RoomDescription(currentRoom);
    }

    public RoomDescription move(String direction) {
    System.out.println("Attempting to move " + direction);
    Optional<String> nextRoomId = currentRoom.getRoomInDirection(direction);

    if (nextRoomId.isPresent()) {
        System.out.println("Moving to room: " + nextRoomId.get());
        Room nextRoom = mapOfRooms.get(nextRoomId.get());
        if (nextRoom != null) {
            currentRoom = nextRoom;
        } else {
            System.out.println("Error: Room with ID '" + nextRoomId.get());
        }
    } else {
        System.out.println("You can't go that way.");
    }
    currentRoom.printItems();
    currentRoom.printDirections();
    return new RoomDescription(currentRoom);
}

    public RoomDescription evaluate(String line) {
        String[] input = line.trim().split(" ");
        if ((GO).equals(input[0]) && input.length > 1) {
            return move(input[1]);

        } else if ((EXIT).equals(input[0])) {
            System.out.println("Exiting the game. Bye!");

        } else if ((TAKE).equals(input[0])) {
            Optional<String> item = currentRoom.takeItem(input[1]);
            if (!item.equals(Optional.empty())) {
                inventory.add(item.get()); // call .get() on optional
            }
            System.out.println("Inventory :" + inventory);
            return new RoomDescription(currentRoom);
        }
        return null;
    }

    public void parseFile() throws IOException {
        File f = new File(file);
        try (FileReader fr = new FileReader(f)) {
            BufferedReader br = new BufferedReader(fr);
            Room room = null;
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }
                String[] lineArr = line.split(": ");
                switch (lineArr[0]) {
                    case ROOM:
                        if (room != null) {
                            saveRoom(room); // Save previous room before creating a new one
                        }
                        room = new Room(lineArr[1]);
                        break;

                    case START:
                        if (room != null) {
                            saveRoom(room); // Save last room
                        }
                        start = lineArr[1];
                        break;

                    case NAME:
                        room.setName(lineArr[1]);
                        break;

                    case DESCRIPTION:
                        
                        room.setDescription(lineArr[1]);
                        break;

                    case ITEMS:
                        String[] items = lineArr[1].split(",");
                        for (String item : items) {
                            room.putItem(item.trim());
                        }
                        break;

                        case DIRECTION:
                        // System.out.println("Parsing direction for room " + room.getId() + ": " + lineArr[1]); // Debug print
                        String[] dirParts = lineArr[1].trim().split(" ");
                        if (dirParts.length >= 2) {
                            String dir = dirParts[0];
                            String roomId = dirParts[1];
                            room.setDirection(dir, roomId);
                        } else {
                            System.out.println("Invalid direction format: " + lineArr[1]);
                        }
                        break;
                    

                    default:

                }

            }
        }
    }

    public void saveRoom(Room room) {
        mapOfRooms.put(room.getId(), room);
        // System.out.println(room.getId() + " has been saved");
    }
}

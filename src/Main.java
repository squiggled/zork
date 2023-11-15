package zork;

import java.io.Console;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //open the file
        if (args.length < 1){
            System.out.println("please enter the file name");
        }
        String filePath = args[0];
        File file = new File(filePath);
        System.out.println("Trying to access file at: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }
        World world = new World(args[0]);  //pass in file name for world to construct
        world.parseFile();

        RoomDescription desc = world.start();

        Console cons = System.console(); //open console for user input
        while (true){
            System.out.println(world.currentRoom.getDescription());
            String line = cons.readLine("> ");
            if (("exit").equals(line)){
                break;
            }

            desc=world.evaluate(line);
        }
    }
}

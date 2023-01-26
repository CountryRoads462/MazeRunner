package maze;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean mazeCreated = false;
        boolean currentMazeIsLoaded = false;
        Maze maze = new Maze(0, 0);
        List<String> mazeDisplay = new ArrayList<>();
        while (true) {
            if (mazeCreated) {
                System.out.println("=== Menu ===\n" +
                        "1. Generate a new maze\n" +
                        "2. Load a maze\n" +
                        "3. Save the maze\n" +
                        "4. Display the maze\n" +
                        "5. Find the escape\n" +
                        "0. Exit");
                switch (scanner.nextLine()) {
                    case "1":
                        currentMazeIsLoaded = false;
                        System.out.println("Enter the size of a new maze");
                        int size = Integer.parseInt(scanner.nextLine());
                        maze = new Maze(size, size);
                        System.out.println(maze);
                        break;
                    case "2":
                        currentMazeIsLoaded = true;
                        File loadFile = new File(scanner.nextLine());
                        try (Scanner fileScanner = new Scanner(loadFile)) {
                            while (fileScanner.hasNextLine()) {
                                mazeDisplay.add(fileScanner.nextLine());
                            }
                        } catch (IOException ignored) {
                        }
                        break;
                    case "3":
                        File saveFile = new File(scanner.nextLine());
                        try (PrintWriter printWriter = new PrintWriter(saveFile)) {
                            if (mazeDisplay.size() != 0) {
                                for (String line :
                                        mazeDisplay) {
                                    printWriter.println(line);
                                }
                            } else {
                                printWriter.println(maze);
                            }
                        } catch (IOException ignored) {
                        }
                        break;
                    case "4":
                        if (currentMazeIsLoaded) {
                            mazeDisplay.forEach(System.out::println);
                        } else {
                            System.out.println(maze);
                        }
                        break;
                    case "5":
                        if (currentMazeIsLoaded) {
                            maze = new Maze(mazeDisplay);
                        }
                        maze.findTheEscape();
                        System.out.println(maze);
                        break;
                    case "0":
                        exit(0);
                    default:
                        System.out.println("Incorrect option. Please try again");
                        break;
                }
                System.out.println();
            } else {
                System.out.println("=== Menu ===\n" +
                        "1. Generate a new maze\n" +
                        "2. Load a maze\n" +
                        "0. Exit");
                switch (scanner.nextLine()) {
                    case "1":
                        mazeCreated = true;
                        System.out.println("Enter the size of a new maze");
                        int size = Integer.parseInt(scanner.nextLine());
                        maze = new Maze(size, size);
                        System.out.println(maze);
                        break;
                    case "2":
                        currentMazeIsLoaded = true;
                        mazeCreated = true;
                        File loadFile = new File(scanner.nextLine());
                        try (Scanner fileScanner = new Scanner(loadFile)) {
                            while (fileScanner.hasNextLine()) {
                                mazeDisplay.add(fileScanner.nextLine());
                            }
                        } catch (IOException ignored) {
                        }
                        break;
                    case "0":
                        exit(0);
                    default:
                        System.out.println("Incorrect option. Please try again");
                        break;
                }
                System.out.println();
            }
        }
    }
}

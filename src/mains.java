
import java.io.*;
import java.nio.file.*;
import java.sql.SQLOutput;
import java.util.*;


public class mains {

    private static final Scanner scanner = new Scanner(System.in);
    private static List<String> itemList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFilename = null;


    public static void main(String[] args) {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("A - Add an item");
            System.out.println("D - Delete an item");
            System.out.println("I - Insert an item");
            System.out.println("M - move an item");
            System.out.println("V - view the list");
            System.out.println("C - Clear the list");
            System.out.println("O - Open a list file");
            System.out.println("S - Save the list file");
            System.out.println("Q - Quit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            try {
                switch (choice) {
                    case "A" -> addItem();
                    case "D" -> deleteItem();
                    case "I" -> insertItem();
                    case "M" -> moveItem();
                    case "V" -> viewList();
                    case "C" -> clearList();
                    case "O" -> openFile();
                    case "S" -> saveFile();
                    case "Q" -> quit();
                    default -> System.out.println("invalid option. try again.");
                }
            } catch (IOException e) {
                System.out.println("an error occurred: " + e.getMessage());
            }
        }

    }

    private static void addItem() {
        System.out.println("enter the item to add: ");
        String item = scanner.nextLine();
        itemList.add(item);
        needsToBeSaved = true;
        System.out.println("item added.");
    }


    private static void deleteItem() {
        viewList();
        System.out.println("enter the index of the item to delete: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < itemList.size()) {
            itemList.remove(index);
            needsToBeSaved = true;
            System.out.println("item deleted.");
        } else {
            System.out.println("invalid index.");
        }
    }


    private static void insertItem() {
        System.out.println("enter the item to insert: ");
        String item = scanner.nextLine();
        System.out.print("Enter the position to insert at (O to " + itemList.size() + "): ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index <= itemList.size()) {
            itemList.add(index, item);
            needsToBeSaved = true;
            System.out.println("item inserted.");
        } else {
            System.out.println("invalid index.");
        }
    }

    private static void moveItem() {
        viewList();
        System.out.println("enter the index of the item to move: ");
        int fromIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("enter the new position: ");
        int toIndex = Integer.parseInt(scanner.nextLine());
        if (fromIndex >= 0 && fromIndex < itemList.size() && toIndex >= 0 && toIndex <= itemList.size()) {
            String item = itemList.remove(fromIndex);
            itemList.add(toIndex, item);
            needsToBeSaved = true;
            System.out.println("item moved.");
        } else {
            System.out.println("invalid indices.");
        }
    }

    private static void viewList() {
        if (itemList.isEmpty()) {
            System.out.println("the list is empty.");
            for (int i = 0; i < itemList.size(); i++) {
                System.out.println(i + ": " + itemList.get(i));
            }
        }
    }


    private static void clearList() {
        System.out.print("are you sure that you cant to clear the list? (Y/N): ");
        String confirmation = scanner.nextLine().toUpperCase();
        if (confirmation.equals("Y")) {
            itemList.clear();
            needsToBeSaved = true;
            System.out.println("list cleared.");
        } else {
            System.out.println("clear operation canceled");
        }
    }

    private static void openFile() throws IOException {
        if (needsToBeSaved) {
            promptToSave();
        }
        System.out.print("enter the filename to open: ");
        String filename = scanner.nextLine();
        Path path = Paths.get(filename);
        if (Files.exists(path)) {
            itemList = Files.readAllLines(path);
            currentFilename = filename;
            needsToBeSaved = false;
            System.out.println("file loaded.");
        } else {
            System.out.println("file not found.");
        }
    }

    private static void saveFile() throws IOException {
        if (currentFilename == null) {
            System.out.print("enter the filename to save as: ");
            currentFilename = scanner.nextLine();
        }
        Files.write(Paths.get(currentFilename), itemList);
        needsToBeSaved = false;
        System.out.println("file saved.");
    }

    private static void quit() throws IOException {
        if (needsToBeSaved) {
            promptToSave();
        }
        System.out.println("bye!");
        System.exit(0);
    }

    private static void promptToSave() throws IOException {
        System.out.print("you have unsaved changes. save now? (Y/N): ");
        String response = scanner.nextLine().toUpperCase();
        if (response.equals("y")) {
            saveFile();
        }
    }


}

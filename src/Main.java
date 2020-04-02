import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        play();
    }

    private static void play() {
        Scanner sc = new Scanner(System.in);
        String input;
        String[] inSplit;
        String playlistType;

        input = sc.nextLine();
        do {
            inSplit = input.split(" ");
            playlistType = "";
            if (inSplit.length > 1) {
                playlistType = inSplit[1];
                input = inSplit[0];
            }
            switch (input) {
                case "new":
                    newMusic();
                    input = sc.nextLine();
                    break;
                case "featured":
                    featuredMusic();
                    input = sc.nextLine();
                    break;
                case "categories":
                    categoriesMusic();
                    input = sc.nextLine();
                    break;
                case "playlists":
                    playlistsMusic(playlistType);
                    input = sc.nextLine();
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Unknown command. Try again.");
            }

        } while (!input.equals("exit"));
        System.out.println("---GOODBYE!---");
    }

    private static void newMusic() {
        System.out.println("---NEW RELEASES---");
        System.out.printf("%s [%s, %s, %s]\n", "Mountains", "Sia", "Diplo", "Labrinth");
        System.out.printf("%s [%s]\n", "Runaway", "Lil Peep");
        System.out.printf("%s [%s]\n", "The Greatest Show", "Panic! At The Disco");
        System.out.printf("%s [%s]\n", "All Out Life", "Slipknot");
    }

    private static void featuredMusic() {
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }

    private static void categoriesMusic() {
        System.out.println("---CATEGORIES---");
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("Latin");
    }

    private static void playlistsMusic(String genre) {
        System.out.printf("---%S PLAYLISTS---\n", genre);
        System.out.println("Walk Like A Badass");
        System.out.println("Rage Beats");
        System.out.println("Arab Mood Booster");
        System.out.println("Sunday Stroll");
    }
}

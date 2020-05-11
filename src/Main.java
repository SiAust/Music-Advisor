import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    private static boolean auth = false;

    public static void main(String[] args) {
        if (args.length > 1 && args[0].contains("-access")) {
            HttpUtils.setUri(args[1]);
        }
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
                case "auth":
                    auth();
                    input = sc.nextLine();
                    break;
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
//                    HttpUtils.stopHttpServer();
                    break;
                default:
                    System.out.println("Unknown command. Try again.");
                    input = sc.nextLine();
            }

        } while (!input.equals("exit"));
        System.out.println("---GOODBYE!---");
    }

    private static void auth() {
        System.out.println("Use this link to request the access code:");
        System.out.println("https://accounts.spotify.com/authorize?client_id=6edb9b1ac21042abacc6daaf0fbc4c4d&redirect_uri=http://localhost:8080&response_type=code");
        HttpUtils.startHttpServer();
        System.out.println("waiting for code...");
        HttpUtils.waitForCode();
        System.out.println("code received");
        System.out.println("making http request for access_token...\nresponse:");
        HttpUtils.getToken();
        System.out.println("---SUCCESS---");
        auth = true;
    }

    private static boolean checkAuth() {
        if (!auth) {
            System.out.println("Please, provide access for application.");
        }
        return auth;
    }

    private static void newMusic() {
        if (!checkAuth()) {
            return;
        }
        System.out.println("---NEW RELEASES---");
        System.out.printf("%s [%s, %s, %s]\n", "Mountains", "Sia", "Diplo", "Labrinth");
        System.out.printf("%s [%s]\n", "Runaway", "Lil Peep");
        System.out.printf("%s [%s]\n", "The Greatest Show", "Panic! At The Disco");
        System.out.printf("%s [%s]\n", "All Out Life", "Slipknot");
    }

    private static void featuredMusic() {
        if (!checkAuth()) {
            return;
        }
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }

    private static void categoriesMusic() {
        if (!checkAuth()) {
            return;
        }
        System.out.println("---CATEGORIES---");
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("Latin");
    }

    private static void playlistsMusic(String genre) {
        if (!checkAuth()) {
            return;
        }
        System.out.printf("---%S PLAYLISTS---\n", genre);
        System.out.println("Walk Like A Badass");
        System.out.println("Rage Beats");
        System.out.println("Arab Mood Booster");
        System.out.println("Sunday Stroll");
    }
}

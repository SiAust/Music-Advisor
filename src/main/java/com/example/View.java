package com.example;

public class View {

    MusicGUI musicGUI = new MusicGUI();

    private static final View view = new View();
    public SpotifyCollection<SpotifyObject> content;

    public int numberOfItems = 5;
    private int currentPage;
    private int totalPages;

    private View() {
    }

    public static View getInstance() {
        return view;
    }

    public void setContent(SpotifyCollection<SpotifyObject> content) {
        this.content = content;

    }

    public void update() {
        if (content != null) {
            totalPages = content.size() / numberOfItems + ((content.size() % numberOfItems) > 0 ? 1 : 0); // if there is a remainder add a page
            content.printItems(numberOfItems,0);

            musicGUI.setTextArea(content.printItems());

            currentPage = 1;
            printCurrentPage();
        }
    }

    public void next() {
        if (content != null) {
            if (currentPage == totalPages) {
                System.out.println("No more pages.");
                return;
            }
            content.printItems(numberOfItems, currentPage * numberOfItems);

            musicGUI.setTextArea(content.printItems());

            currentPage++;
            printCurrentPage();
            return;
        }
        System.out.println("No items available.");
    }

    public void prev() {
        if (content != null) {
            if (currentPage == 1) {
                System.out.println("No more pages.");
                return;
            }
            content.printItems(numberOfItems, (currentPage * numberOfItems) - (numberOfItems * 2));

            musicGUI.setTextArea(content.printItems());

            currentPage--;
            printCurrentPage();
            return;
        }
        System.out.println("No items available.");
    }

    private void printCurrentPage() {
        System.out.printf("=== Page %d of %d ===\n", currentPage, totalPages);
    }

}

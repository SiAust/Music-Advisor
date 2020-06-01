package com.example;

import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName()); // todo: worth doing?

    public static void main(String[] args) {

        if (args.length > 1) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].contains("-access")) {
                    HttpUtils.setAccessUri(args[i + 1]);
                }
                if (args[i].contains("-resource")) {
                    HttpUtils.setResourceUri(args[i + 1]);
                }
                if (args[i].contains("-page")) {
                    View view = View.getInstance();
                    view.numberOfItems = Integer.parseInt(args[i + 1]);
                }
            }
        }
        Controller.mainMenu();
    }


}

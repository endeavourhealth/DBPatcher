package org.endeavourhealth.dbpatcher.helpers;

public class ConsoleHelper {

    public static boolean readYesNoFromConsole() {
        while (true) {

            System.out.print("       > (yes, no) > ");

            String line = System.console().readLine().trim().toLowerCase();

            if (line.equals("y") || line.equals("yes"))
                return true;
            else if (line.equals("n") || line.equals("no"))
                return false;
        }
    }
}

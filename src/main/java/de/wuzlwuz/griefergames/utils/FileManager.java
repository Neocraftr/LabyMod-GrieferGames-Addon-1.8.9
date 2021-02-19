package de.wuzlwuz.griefergames.utils;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.addon.AddonLoader;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager {

    private File dataDirectory;
    private File transactionsLogFile;
    private BufferedWriter transactionsLogWriter;

    public FileManager() {
        try {
            dataDirectory = new File(AddonLoader.getConfigDirectory(), "GrieferGames");
            if(!dataDirectory.exists()) {
                dataDirectory.mkdirs();
            }

            transactionsLogFile = new File(dataDirectory, "transactions.log");
            if(!transactionsLogFile.exists()) {
                transactionsLogFile.createNewFile();
            }

            transactionsLogWriter = new BufferedWriter(new FileWriter(transactionsLogFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logTransaction(String player, double amount, boolean received) {
        if(!GrieferGames.getGriefergames().getSettings().isLogTransactions()) return;

        try {
            final String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
            if(received) {
                transactionsLogWriter.write("["+date+"] Received $"+amount+" from "+player+"\n");
            } else {
                transactionsLogWriter.write("["+date+"] Payed $"+amount+" to "+player+"\n");
            }
            transactionsLogWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openTransactionsFile() {
        try {
            Desktop.getDesktop().open(transactionsLogFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

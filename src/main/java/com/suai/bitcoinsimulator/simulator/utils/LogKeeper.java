package com.suai.bitcoinsimulator.simulator.utils;

import java.io.File;
import java.util.logging.*;
import com.suai.bitcoinsimulator.view.SimulatorGUI;

public class LogKeeper {
    private static final Logger log = Logger.getGlobal();
    private static boolean loggerCreated = false;
    private static SimulatorGUI gui;

    public static void createLogger(SimulatorGUI g) {
        if (loggerCreated) {
            return;
        }
        loggerCreated = true;
        gui = g;
        FileHandler mFileHandler = null;
        final Logger parentLogger = Logger.getAnonymousLogger().getParent();
        final Handler[] handlers = parentLogger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            final Handler handler = handlers[i];
            if (handler instanceof ConsoleHandler) {
                parentLogger.removeHandler(handler);
            }
        }
        try {
            final String logDirectoryPath = getLogDirectory();
            final File logDirectory = new File(logDirectoryPath);
            logDirectory.mkdirs();
            if (logDirectory.isDirectory()) {
                final String pathPattern = logDirectoryPath + File.separatorChar + "log";
                mFileHandler = new FileHandler(pathPattern, 1400000, 5, false);
                parentLogger.addHandler(mFileHandler);
            }
            final ConsoleHandler stdConsoleHandler = new ConsoleHandler();
            if (System.getProperty("java.util.logging.config.file", null) == null) {
                mFileHandler.setLevel(Level.INFO);
                stdConsoleHandler.setLevel(Level.INFO);
            }
            parentLogger.addHandler(stdConsoleHandler);
        } catch (final Exception e) {
            LogKeeper.info("Error creating logging File Handler", 0);
        }
    }

    public static String getLogDirectory() {
        final String logDirectory = ".";
        return logDirectory;
    }

    public static synchronized void info(final String string, int curTime) {
        String message = "Program time: " + curTime + ". " + string;
        System.out.println(message);
        //log.log(Level.INFO, string);
        if(gui != null)
            gui.addLogMessage(message);
    }
}

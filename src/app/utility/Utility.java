package app.utility;

import java.util.Scanner;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.main.controller.GameController;

public class Utility {
  private static Scanner s = new Scanner(System.in);

  public static String nextLine() {
    return s.nextLine();
  }

  public static void debug(Object text) {
    GameController controller = GameController.getInstance();
    if (controller.isDebug()) {
      System.out.println(text);
    }
  }

  public static void delayAction(int millis, TimerTask action) {
//    Timer timer = new Timer();
//    timer.schedule(action, millis);
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    executor.schedule(action, millis, TimeUnit.MILLISECONDS);
    executor.shutdown();
  }

  public static void pressEnter() {
    System.out.println("Press ENTER to continue...");
    s.nextLine();
  }

  public static int getInt(String text, int min, int max) {
    int output = min - 1;
    do {
      System.out.print(text);
      try {
        output = Integer.parseInt(s.nextLine());
      } catch (Exception e) {
      }
    } while (output < min || output > max);
    return output;
  }

  public static int random(int min, int max) {
    return (int) (Math.random() * (max - min + 1) + min);
  }

  public static double frand(double min, double max) {
    return Math.random() * (max - min + 1) + min;
  }

  public static void cls() {
    for (int i = 0; i < 50; i++) {
      System.out.println("");
    }
  }

  public static boolean chance(int percent) {
    return (int) (Math.random() * 100) + 1 <= percent;
  }

  public static boolean validateEmail(String input) {
    String[] split = input.split("@");

    if (split.length != 2)
      return false;
    if (split[1].equals(".com"))
      return false;
    if (!split[1].endsWith(".com"))
      return false;

    return true;
  }

  public static boolean isAlphaNumeric(String input) {
    boolean alphabet = false, digit = false, other = false;
    for (char c : input.toCharArray()) {
      if (Character.isAlphabetic(c))
        alphabet = true;
      else if (Character.isDigit(c))
        digit = true;
      else
        other = true;
    }
    return alphabet && digit && !other;
  }

  public static void inProgress(boolean dumpStack) {
    System.out.println("Work in progres...");
    if (dumpStack) {
      Thread.dumpStack();
      System.exit(0);
    } else {
      s.nextLine();
    }
  }

  public static void printClosing() {
    System.out.println("                         .                        \r\n"
        + "                        ...                       \r\n"
        + "                       ..*..                      \r\n"
        + "                     ...***..                     \r\n"
        + "                    ..**,***,..                   \r\n"
        + "                  .****,***.****.                 \r\n"
        + " .......... ..,.. ...*,..*...*... ...,............\r\n"
        + "   ..***.**.****.   ...*****../   .****.**.***... \r\n"
        + "    ...*,*,*,,......,*,*,, &**,......*,*,*,*...   \r\n"
        + "      ....**.***,****,*&&#***,,*,*****.**....     \r\n"
        + "       ,,***..**,,* &&&(,,***,**/&/**..*,***      \r\n"
        + "        ...  ..*,,&&&&,,***&&&&&&/**..  ....      \r\n"
        + "        ...  ..*,,&&&&,*****,&&&(,**..  ...       \r\n"
        + "      .,****..,* &&(,****,,&&&&(,,**...****.      \r\n"
        + "      ....**.***,**,,**,,&&&#*,,,****..*.....     \r\n"
        + "    ...******.....*****& *,,****,.....*****...    \r\n"
        + "  ...,,,.,,.,,,,.   /**,,,,,,..  ..,,,..,,.,,,..  \r\n"
        + " ............**.. ...,,.**...,.. ...*,........... \r\n"
        + "                 ..*****,**,****.                 \r\n"
        + "                    ..******...                   \r\n"
        + "                     ...,*,..                     \r\n"
        + "                       ..*..                      \r\n"
        + "                        ...                       \r\n"
        + "                         .                        \r\n\r\n");
    System.out.println("                       22-2");
    System.out.println(" Wonderful things can be achieved when there is a");
    System.out.println("       teamwork, hardwork, and perseverance");
    System.out.println();
  }

  public static int clamp(int num, int min, int max) {
    return Math.min(Math.max(num, min), max);
  }

  public static double clamp(double num, double min, double max) {
    return Math.min(Math.max(num, min), max);
  }
}

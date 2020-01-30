package ch.romix.ivk.resultarchiver;

public class RESTURI {

  private static final String BASE = "https://korbball.turnverband.ch/ws/";
  private static final String GROUPS = "groupsV1.php";
  private static final String TABLE = "tableV2.php?group=%s";
  private static final String RANKINGS = "rankingV1.php?group=%s";
  private static final String GAMES = "gamesV1.php?group=%s";
  private static final String ANNOTATIONS = "annotationsV1.php?group=%s";
  private static final String SEASON = "seasonV1.php";

  public static String getGroupURI() {
    return appendToBase(GROUPS);
  }

  public static String getTableURI(int id) {
    return appendToBase(String.format(TABLE, id));
  }

  public static String getRankingsURI(int id) {
    return appendToBase(String.format(RANKINGS, id));
  }

  public static String getGamesURI(int groupId) {
    return appendToBase(String.format(GAMES, groupId));
  }

  public static String getAnnotationsURI(int groupId) {
    return appendToBase(String.format(ANNOTATIONS, groupId));
  }

  public static String getSeasonURI() {
    return appendToBase(SEASON);
  }

  private static String appendToBase(String path) {
    String base = System.getProperty("BASE_PATH", BASE);
    return base + path;
  }
}

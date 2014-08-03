package ch.romix.ivk.resultarchiver;

public class RESTURI {

  private static final String BASE = "http://korbball.turnverband.ch/ws/";
  // private static final String BASE = "http://localhost/www.turnverband.ch/korbball/ws/";
  private static final String GROUPS = "groupsV1.php";
  private static final String TABLE = "tableV2.php?group=%s";

  public static String getGroupURI() {
    return BASE + GROUPS;
  }

  public static String getTableURI(int id) {
    return BASE + String.format(TABLE, id);
  }
}

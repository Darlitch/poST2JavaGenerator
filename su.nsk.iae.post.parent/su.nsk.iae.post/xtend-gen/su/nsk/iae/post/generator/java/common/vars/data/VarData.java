package su.nsk.iae.post.generator.java.common.vars.data;

@SuppressWarnings("all")
public class VarData {
  private String name;

  private String type;

  private String initValue;

  public VarData(final String name, final String type, final String initValue) {
    this.name = name;
    this.type = type;
    this.initValue = initValue;
  }
}

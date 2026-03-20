package su.nsk.iae.post.generator.java.common.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class GenerationContext {
  private final Map<String, String> varTypes = new HashMap<String, String>();

  private final Map<String, Object> constValues = new HashMap<String, Object>();

  public void registerVar(final String memoryName, final String postType) {
    this.varTypes.put(memoryName, postType);
  }

  public boolean hasType(final String memoryName) {
    return this.varTypes.containsKey(memoryName);
  }

  public String resolveVarType(final String name) {
    String _xblockexpression = null;
    {
      final String resolved = this.resolveAlias(name);
      final String type = this.varTypes.get(resolved);
      if ((type == null)) {
        throw new IllegalStateException(
          ("Type not registered for variable: " + resolved));
      }
      _xblockexpression = type;
    }
    return _xblockexpression;
  }

  public String resolveVarName(final String name) {
    String _xblockexpression = null;
    {
      final String resolved = this.resolveAlias(name);
      boolean _containsKey = this.varTypes.containsKey(resolved);
      boolean _not = (!_containsKey);
      if (_not) {
        throw new IllegalStateException(
          ("Unknown variable: " + resolved));
      }
      _xblockexpression = resolved;
    }
    return _xblockexpression;
  }

  public void registerConst(final String name, final Object value) {
    this.constValues.put(name, value);
  }

  public Object getConst(final String name) {
    return this.constValues.get(name);
  }

  public boolean hasConst(final String name) {
    return this.constValues.containsKey(name);
  }

  private final Set<String> inputVars = new HashSet<String>();

  private final Set<String> outputVars = new HashSet<String>();

  private final Set<String> globalVars = new HashSet<String>();

  private final Set<String> localVars = new HashSet<String>();

  public void registerInputVar(final String name) {
    this.inputVars.add(name);
  }

  public void registerOutputVar(final String name) {
    this.outputVars.add(name);
  }

  public void registerGlobalVar(final String name) {
    this.globalVars.add(name);
  }

  public void registerLocalVar(final String name) {
    this.localVars.add(name);
  }

  public Set<String> getInputVars() {
    return this.inputVars;
  }

  public Set<String> getOutputVars() {
    return this.outputVars;
  }

  public Set<String> getGlobalVars() {
    return this.globalVars;
  }

  public Set<String> getLocalVars() {
    return this.localVars;
  }

  private final Map<String, Integer> arrayStarts = new HashMap<String, Integer>();

  private final Map<String, String> arrayElementTypes = new HashMap<String, String>();

  public void registerArrayStart(final String arrayName, final int start) {
    this.arrayStarts.put(arrayName, Integer.valueOf(start));
    System.out.println(((("REGISTER ARRAY START: " + arrayName) + " = ") + Integer.valueOf(start)));
  }

  public int getArrayStart(final String arrayName) {
    Integer _xblockexpression = null;
    {
      boolean _containsKey = this.arrayStarts.containsKey(arrayName);
      boolean _not = (!_containsKey);
      if (_not) {
        throw new IllegalStateException(
          ("Array start not registered: " + arrayName));
      }
      _xblockexpression = this.arrayStarts.get(arrayName);
    }
    return (_xblockexpression).intValue();
  }

  public boolean hasArrayStart(final String arrayName) {
    return this.arrayStarts.containsKey(arrayName);
  }

  public void registerArrayType(final String arrayName, final String elementType) {
    this.arrayElementTypes.put(arrayName, elementType);
  }

  public String getArrayElementType(final String arrayName) {
    String _xblockexpression = null;
    {
      final String type = this.arrayElementTypes.get(arrayName);
      if ((type == null)) {
        throw new IllegalStateException(
          ("Array element type not registered: " + arrayName));
      }
      _xblockexpression = type;
    }
    return _xblockexpression;
  }

  public boolean hasArrayElementType(final String arrayName) {
    return this.arrayElementTypes.containsKey(arrayName);
  }

  private final Map<String, String> aliases = new HashMap<String, String>();

  public void registerAlias(final String alias, final String target) {
    this.aliases.put(alias, target);
  }

  public boolean hasAlias(final String name) {
    return this.aliases.containsKey(name);
  }

  public String getAliasTarget(final String name) {
    return this.aliases.get(name);
  }

  public String resolveAlias(final String name) {
    String _xblockexpression = null;
    {
      String current = name;
      final HashSet<String> visited = new HashSet<String>();
      while (this.aliases.containsKey(current)) {
        {
          boolean _add = visited.add(current);
          boolean _not = (!_add);
          if (_not) {
            throw new IllegalStateException((((("Alias cycle detected while resolving \'" + name) + "\' at \'") + current) + "\'"));
          }
          current = this.aliases.get(current);
        }
      }
      _xblockexpression = current;
    }
    return _xblockexpression;
  }

  private final Map<String, String> processRefs = new HashMap<String, String>();

  private final Map<String, String> processTypes = new HashMap<String, String>();

  public void registerProcess(final String postName, final String javaFieldName, final String processType) {
    this.processRefs.put(postName, javaFieldName);
    this.processTypes.put(javaFieldName, processType);
  }

  public boolean hasProcess(final String name) {
    return this.processRefs.containsKey(name);
  }

  public String resolveProcess(final String name) {
    String _xblockexpression = null;
    {
      final String resolved = this.processRefs.get(name);
      if ((resolved == null)) {
        throw new IllegalStateException(
          ("Process not registered: " + name));
      }
      _xblockexpression = resolved;
    }
    return _xblockexpression;
  }

  public String getProcessTypeByFieldName(final String name) {
    String _xblockexpression = null;
    {
      final String type = this.processTypes.get(name);
      if ((type == null)) {
        throw new IllegalStateException(
          ("Process type not registered: " + name));
      }
      _xblockexpression = type;
    }
    return _xblockexpression;
  }
}

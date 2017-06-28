package org.slizaa.scanner.jtype.model.internal.bytecode;

import static com.google.common.base.Preconditions.checkNotNull;

import org.objectweb.asm.Type;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.jtype.model.internal.primitvedatatypes.IPrimitiveDatatypeNodeProvider;

public class Utils {

  /**
   * <p>
   * </p>
   */
  public static Type resolveArrayType(Type type) {
    switch (checkNotNull(type).getSort()) {
    case Type.ARRAY:
      return resolveArrayType(type.getElementType());
    default:
      return type;
    }
  }

  public static boolean isVoid(Type type) {

    type = resolveArrayType(type);

    return type.getSort() == Type.VOID;
  }

  public static boolean isPrimitive(Type type) {

    type = resolveArrayType(type);

    return type.getSort() == Type.BOOLEAN | type.getSort() == Type.BYTE | type.getSort() == Type.CHAR
        | type.getSort() == Type.DOUBLE | type.getSort() == Type.FLOAT | type.getSort() == Type.INT
        | type.getSort() == Type.LONG | type.getSort() == Type.SHORT;
  }

  public static INode getPrimitiveDatatypeNode(Type type, IPrimitiveDatatypeNodeProvider primitiveDatatypeNodeProvider) {

    type = resolveArrayType(type);

    switch (type.getSort()) {
    case Type.BOOLEAN: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeBoolean();
    }
    case Type.BYTE: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeByte();
    }
    case Type.CHAR: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeChar();
    }
    case Type.DOUBLE: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeDouble();
    }
    case Type.FLOAT: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeFloat();
    }
    case Type.INT: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeInt();
    }
    case Type.LONG: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeLong();
    }
    case Type.SHORT: {
      return primitiveDatatypeNodeProvider.getPrimitiveDatatypeShort();
    }
    default:
      // TODO
      throw new RuntimeException();
    }
  }

  // /**
  // * Returns the Java type name corresponding to the given internal name.
  // *
  // * @param desc
  // * The internal name.
  // * @return The type name.
  // */
  // public static String getObjectType(String desc) {
  // return getType(Type.getObjectType(desc));
  // }
  //
  // /**
  // * Returns the Java type name type corresponding to the given type descriptor.
  // *
  // * @param desc
  // * The type descriptor.
  // * @return The type name.
  // */
  // public static String getType(String desc) {
  // return getType(Type.getType(desc));
  // }
  //
  // /**
  // * Return the type name of the given ASM type.
  // *
  // * @param t
  // * The ASM type.
  // * @return The type name.
  // */
  // public static String getType(final Type t) {
  // switch (t.getSort()) {
  // case Type.ARRAY:
  // return getType(t.getElementType());
  // default:
  // return t.getClassName();
  // }
  // }

  /**
   * @param type
   * @return
   */
  public static String getFullyQualifiedTypeName(Type type) {
    return resolveArrayType(type).getClassName();
  }

  /**
   * Return a method signature.
   * 
   * @param name
   *          The method name.
   * @param rawSignature
   *          The signature containing parameter, return and exception values.
   * @return The method signature.
   */
  public static String getMethodSignature(String name, String rawSignature) {
    StringBuffer signature = new StringBuffer();
    String returnType = org.objectweb.asm.Type.getReturnType(rawSignature).getClassName();
    if (returnType != null) {
      signature.append(returnType);
      signature.append(' ');
    }
    signature.append(name);
    signature.append('(');
    org.objectweb.asm.Type[] types = org.objectweb.asm.Type.getArgumentTypes(rawSignature);
    for (int i = 0; i < types.length; i++) {
      if (i > 0) {
        signature.append(',');
      }
      signature.append(types[i].getClassName());
    }
    signature.append(')');
    return signature.toString();
  }

  /**
   * Return a field signature.
   * 
   * @param name
   *          The field name.
   * @param rawSignature
   *          The signature containing the type value.
   * @return The field signature.
   */
  public static String getFieldSignature(String name, String rawSignature) {
    StringBuffer signature = new StringBuffer();
    String returnType = org.objectweb.asm.Type.getReturnType(rawSignature).getClassName();
    signature.append(returnType);
    signature.append(' ');
    signature.append(name);
    return signature.toString();
  }
}

package org.slizaa.scanner.simpledefinition;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entry {

  @Expose
  @SerializedName("name")
  private String       _name;

  @Expose
  @SerializedName("version")
  private String       _version;

  @Expose
  @SerializedName("binary-paths")
  private List<String> _binaryPaths;

  @Expose
  @SerializedName("source-paths")
  private List<String> _sourcePaths;

  @Expose
  @SerializedName("analyse")
  private String       _analyse;

  public Entry(String name, String version, String analyse) {
    super();
    _name = name;
    _version = version;
    _analyse = analyse;
    _binaryPaths = new LinkedList<>();
    _sourcePaths = new LinkedList<>();
    
    _binaryPaths.add("bumm");
  }

  public static void main(String[] args) {

    Gson gson = new GsonBuilder().create();
    gson.toJson(new Entry("name", "version", "BINARY_ONLY"), System.out);
  }
}

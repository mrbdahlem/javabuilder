package org.code.protocol;

/** Manages content files that are created during a Javabuilder session */
public interface JavabuilderFileManager {
  /**
   * Write the specified file to a storage location
   *
   * @param filename name of the file to be written
   * @param inputBytes contents of the file
   * @param contentType content type of the file
   * @return a URL pointing to the written file
   * @throws JavabuilderException if there is an issue writing the file
   */
  String writeToFile(String filename, byte[] inputBytes, String contentType)
      throws JavabuilderException;

  /**
   * Get an upload URL for a specified file name that can be used to upload the file to a storage
   * location.
   *
   * @param filename name of the file to upload
   * @return the upload URL
   * @throws JavabuilderException if there is an issue generating the URL
   */
  String getUploadUrl(String filename) throws JavabuilderException;
}

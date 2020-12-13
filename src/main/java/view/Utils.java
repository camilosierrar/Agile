package view;

import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 * This class is used to check the correctness of xml files
 */
public class Utils {

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    /**
     * Returns the extension of the file
     * @param f the actual file
     * @return the extension as a string
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    /**
     * The extension filter class
     */
    static class ExtensionFileFilter extends FileFilter {
        String description;
        String type;

        String extensions[];

        public ExtensionFileFilter(String description, String extension, String type) {
            this(description, new String[] { extension },type);
        }
        /**
         * Its constructor
         */
        public ExtensionFileFilter(String description, String extensions[], String type) {
            if (description == null) {
                this.description = extensions[0];
            } else {
                this.description = description;
            }
            this.extensions = (String[]) extensions.clone();
            toLower(this.extensions);
            this.type = type;
        }
        /**
         * Puts an array of strings into lower case
         */
        private void toLower(String array[]) {
            for (int i = 0, n = array.length; i < n; i++) {
                array[i] = array[i].toLowerCase();
            }
        }

        public String getDescription() {
            return description;
        }
        /**
         * Returns the extension of the file
         * @param file the actual file
         * @return a boolean checking if the xml file is correctly written
         */
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else {
                String path = file.getAbsolutePath().toLowerCase();
                for (int i = 0, n = extensions.length; i < n; i++) {
                    String extension = extensions[i];
                    if (( path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.'
                            && (path.charAt(path.length() - file.getName().length() )) != '.'
                    && file.getName().contains(type))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
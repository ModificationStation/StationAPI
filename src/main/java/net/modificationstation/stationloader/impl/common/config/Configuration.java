package net.modificationstation.stationloader.impl.common.config;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Configuration {

    public Configuration(File configFile) {
        this.configFile = configFile;
    }

    public Category getCategory(String name) {
        for (Category category : categories)
            if (name.equals(category.name))
                return category;
        Category category = new Category(name);
        categories.add(category);
        return category;
    }

    public void load() {
        try {
            if ((!configFile.getParentFile().exists() && !configFile.getParentFile().mkdirs()) || (!configFile.exists() && !configFile.createNewFile()))
                throw new RuntimeException("Failed to load config at " + configFile.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (configFile.canRead()) {
            FileInputStream fileInputStream;
            BufferedReader buffer;
            try {
                fileInputStream = new FileInputStream(configFile);
                buffer = new BufferedReader(new InputStreamReader(fileInputStream, "8859_1"));
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            Category currentCategory = null;
            String line;
            while (true) {
                try {
                    line = buffer.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (line == null) {
                    if (currentCategory != null)
                        throw new RuntimeException("Category \"" + currentCategory.name + "\" isn't closed!");
                    break;
                }
                boolean doneOrComment = false;
                for (int i = 0; i < line.length() && !doneOrComment; i++) {
                    if (!Character.isLetterOrDigit(line.charAt(i)) && line.charAt(i) != '.' && !Character.isWhitespace(line.charAt(i))) {
                        switch (line.charAt(i)) {
                            case '#':
                                doneOrComment = true;
                                break;
                            case '=':
                                if (currentCategory == null)
                                    throw new RuntimeException("Property outside of a category!");
                                currentCategory.getProperty(line.split("=")[0].substring(4)).setValue(line.substring(i + 1));
                                doneOrComment = true;
                                break;
                            case '{':
                                if (currentCategory != null)
                                    throw new RuntimeException("Category inside of \"" + currentCategory.name + "\" category!");
                                currentCategory = getCategory(line.substring(0, i - 1));
                                doneOrComment = true;
                                break;
                            case '}':
                                if (currentCategory == null)
                                    throw new RuntimeException("Closing a category while the one isn't present!");
                                currentCategory = null;
                                doneOrComment = true;
                                break;
                            default:
                                throw new RuntimeException("Unknown character " + line.charAt(i));
                        }
                    }
                }
            }
            try {
                buffer.close();
                fileInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void save() {
        try {
            if ((!configFile.getParentFile().exists() && !configFile.getParentFile().mkdirs()) || (!configFile.exists() && !configFile.createNewFile()))
                throw new RuntimeException("Failed to save config at " + configFile.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (configFile.canWrite()) {
            FileOutputStream fileOutputStream = null;
            BufferedWriter buffer = null;
            try {
                fileOutputStream = new FileOutputStream(configFile);
                buffer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "8859_1"));
                buffer.write("# Saved on " + DateFormat.getInstance().format(new Date()) + "\r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (Category category : categories) {
                try {
                    buffer.write("\r\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                category.save(buffer);
            }
            try {
                buffer.close();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final File configFile;
    public final Set<Category> categories = new TreeSet<>();
}

package ext;


/**
 * Basic groovy server page extension for markdown
 * 
 * Usage: ${"**Hello** *world*".markdown().raw()}
 * 
 * @author olivier refalo
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.text.ParseException;

import markdown.Markdown;


import play.Play;
import play.templates.BaseTemplate;
import play.templates.JavaExtensions;

public class MarkdownExtensions extends JavaExtensions {
	private static String explodePath(String path) {
		String name = path;
		if (name.startsWith("./")) {
            String ct = BaseTemplate.currentTemplate.get().name;
            if (ct.matches("^/lib/[^/]+/app/views/.*")) {
                ct = ct.substring(ct.indexOf("/", 5));
            }
            ct = ct.substring(0, ct.lastIndexOf("/"));
            name = ct + name.substring(1);
        }
		return name;
	}
	public static String markdownText(Object mdString) {

		try {
			return Markdown.transformMarkdown(mdString.toString());
		} catch (ParseException e) {
			return e.toString();
		}

	}

	public static String markdownFile(Object mdFile) {
		try {
			File f = new File(Play.applicationPath,explodePath(mdFile.toString()));
			if (f.exists()) {
				Reader pageReader;
				
				pageReader = new FileReader(f);
				
				return Markdown.transformMarkdown(pageReader);
			}
		} catch (FileNotFoundException e) {
			return e.toString();
		} catch (ParseException e) {
			return e.toString();
		}
		
		return null;
	}

	public static String markdown(Object md) {
		File f = new File(Play.applicationPath,explodePath(md.toString()));
		if (f.exists()) {
			return markdownFile(md);
		}
		
		return markdownText(md);
	}
}

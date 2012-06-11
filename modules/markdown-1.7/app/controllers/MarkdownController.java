package controllers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import markdown.Markdown;
import play.Play;
import play.mvc.Controller;

public class MarkdownController extends Controller {

	private static final String IMAGE_LOCATION = "public/mddocs/images/";
	private static final String DOCUMENT_LOCATION = "public/mddocs/";

	public static void transform(String page) throws Exception {

		// Just a little validation to make sure the path is not forged
		if (page == null || page.indexOf('/') > 0 || page.indexOf('\\') > 0
				|| page.indexOf('.') > 0)
			throw new IOException("Invalid path:"+page);

		File f = new File(Play.applicationPath, DOCUMENT_LOCATION + page
				+ ".md");
		if (!f.exists()) {
			notFound("Markdown page for " + page + " not found");
		}

		Reader pageReader = new FileReader(f);
		String html = Markdown.transformMarkdown(pageReader);
		render(html);
	}

	public static void image(String imageName, String ext) throws Exception {
		// Just a little validation to make sure the path is not forged
		if (imageName == null || imageName.indexOf('/') > 0
				|| imageName.indexOf('\\') > 0 || imageName.indexOf('.') > 0)
			throw new IOException("Invalid path:"+imageName);
		if (ext == null || ext.indexOf('/') > 0 || ext.indexOf('\\') > 0
				|| ext.indexOf('.') > 0)
			throw new IOException("Invalid path:"+ext);

		File image = new File(Play.applicationPath, IMAGE_LOCATION + imageName
				+ '.' + ext);

		if (!image.exists()) {
			notFound();
		}
		renderBinary(image);
	}
}

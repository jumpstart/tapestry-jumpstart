package jumpstart.web.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;

@Import(stylesheet = "css/sourcecodedisplay.css")
public class SourceCodeDisplay2 {
	static private String LINE_SEPARATOR = System.getProperty("line.separator");

	static private String STYLE_SOURCECODEDISPLAY = "sourcecodedisplay";
	static private String STYLE_BOX = "box";
	static private String STYLE_TITLE = "title";
	static private String STYLE_SOURCE = "source";
	static private String STYLE_NOT_FOUND = "not-found";
	
	static private int TAB_STOPS_WIDTH = 4;

	// The source file path from the project root eg. "/web/src/main/jumpstart/web/pages/Start.java"
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String src;

	@Inject
	private Context context;

	boolean beginRender(MarkupWriter writer) {

		// Print start of the source block

		writer.write(LINE_SEPARATOR);
		writer.writeRaw("<!-- Start of source code inserted by SourceCodeDisplay2 component. -->");
		writer.write(LINE_SEPARATOR);
		writer.write(LINE_SEPARATOR);

		// Print a div with style info to make a pretty block

		writer.element("div", "class", STYLE_SOURCECODEDISPLAY);
//		writer.element("div", "class", STYLE_BOX);
//		writer.write(LINE_SEPARATOR);
//		{
//			writer.element("div", "class", STYLE_TITLE);
//			writer.write(LINE_SEPARATOR);
//			{
//				writer.write(extractSimpleName(src));
//				writer.write(LINE_SEPARATOR);
//			}
//			writer.end();
//		}
//		// writer.element("hr", "style", "color: #ccc; background-color: #ccc;");
//		writer.element("hr", "style", "color: #ddd; background-color: #ddd;");
//		writer.end();

		// Print the source

		if (src != null) {
			printSourceFromInputStream(writer, src, "/WEB-INF/sourcecode" + src);
		}

		// Print end of div

//		writer.write(LINE_SEPARATOR);
//		writer.end();
		writer.end();

		// Print end of source block

		writer.write(LINE_SEPARATOR);
		writer.write(LINE_SEPARATOR);
		writer.writeRaw("<!-- End of source code inserted by SourceCodeDisplay component. -->");
		writer.write(LINE_SEPARATOR);

		return true;
	}

	private String extractSimpleName(String path) {
		String simpleName = path;

		int i = path.lastIndexOf("/");
		simpleName = path.substring(i + 1);

		return simpleName;
	}

	private void printSourceFromInputStream(MarkupWriter writer, String title, String givenPath) {
		if (givenPath != null) {
			URL url = context.getResource(givenPath);
			try {
				if (url != null) {
					InputStream templateStream = url.openStream();
					if (templateStream != null) {
						BufferedReader templateReader = new BufferedReader(new InputStreamReader(templateStream));
						printSource(writer, templateReader);
					}
					else {
						printResourceNotFound(writer, givenPath);
					}
				}
				else {
					printResourceNotFound(writer, givenPath);
				}
			}
			catch (IOException e) {
				printResourceNotFound(writer, givenPath);
			}
		}
	}

	private void printSource(MarkupWriter writer, BufferedReader sourceReader) {
		writer.element("div", "class", STYLE_SOURCE);
		{
			writer.element("pre", "style", "margin: 0;");
			{
				writer.write(LINE_SEPARATOR);
				writer.element("code");
				{
					writer.write(LINE_SEPARATOR);

					String s;
					try {
						while ((s = sourceReader.readLine()) != null) {
							s = replaceTabsWithSpaces(s);
							writer.write(s);
							writer.write(LINE_SEPARATOR);
						}
					}
					catch (IOException e) {
						writer.write("Error reading .....?");
						e.printStackTrace();
					}

					writer.end();
				}
				writer.write(LINE_SEPARATOR);
			}
			writer.end();
		}
		writer.end();
	}

	private void printResourceNotFound(MarkupWriter writer, String resourcePath) {
		writer.element("div", "class", STYLE_NOT_FOUND);
		{
			writer.write("The file was not found. Path given was " + resourcePath);
		}
		writer.end();
	}
	
	private String replaceTabsWithSpaces(String s) {
		StringBuilder sb = new StringBuilder();
		char c;
		int column = 1;
		
		for (int i = 0; i < s.length(); i++, column++) {
			if ((c = s.charAt(i)) == '\t') {
				sb.append(' ');
				while (column % TAB_STOPS_WIDTH != 0) {
					sb.append(' ');
					column++;
				}
			}
			else {
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
}

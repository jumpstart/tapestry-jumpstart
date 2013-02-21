package jumpstart.web.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;

public class SourceCodeDisplay {
	static private String LINE_SEPARATOR = System.getProperty("line.separator");

	static private String STYLE_BOX = "margin: 10px 0px 0px 0px; background: #adffd6; padding: 8px; "
			+ "border: 1px solid #ddd; border-radius: 8px; -webkit-border-radius: 8px; -moz-border-radius: 8px;";

	static private String STYLE_TITLE = "margin: -2px 0 0 0; text-align: left; font-family: Arial, Helvetica, sans-serif; "
			+ "font-size: 12px; font-weight: normal; color: #444; line-height: 14px; ";

	static private String STYLE_SOURCE = "text-align: left; tab-stops: 5px; "
			+ "font-size: 12px; font-weight: normal; color: #444; line-height: 14px; ";

	static private String STYLE_NOT_FOUND = "margin: 10px 0; font-family: Arial, Helvetica, sans-serif; "
			+ "font-size: 12px; font-weight: normal; text-align: left; color: red;";
	
	static private int TAB_STOPS_WIDTH = 4;

	// The source file path from the project root eg. "/web/src/main/jumpstart/web/pages/Start.java"
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String src;

	@Inject
	private Context context;

	boolean beginRender(MarkupWriter writer) {

		// Print start of the source block

		writer.write(LINE_SEPARATOR);
		writer.writeRaw("<!-- Start of source code inserted by SourceCodeDisplay component. -->");
		writer.write(LINE_SEPARATOR);
		writer.write(LINE_SEPARATOR);

		// Print a div with style info to make a pretty block

		writer.element("div", "style", STYLE_BOX);
		writer.write(LINE_SEPARATOR);
		{
			writer.element("div", "style", STYLE_TITLE);
			writer.write(LINE_SEPARATOR);
			{
				writer.write(extractSimpleName(src));
				writer.write(LINE_SEPARATOR);
			}
			writer.end();
		}
		// writer.element("hr", "style", "color: #ccc; background-color: #ccc;");
		writer.element("hr", "style", "color: #ddd; background-color: #ddd;");
		writer.end();

		// Print the source

		if (src != null) {
			printSourceFromInputStream(writer, src, "/WEB-INF/sourcecode" + src);
		}

		// Print end of div

		writer.write(LINE_SEPARATOR);
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
		writer.element("div", "style", STYLE_SOURCE);
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
		writer.element("div", "style", STYLE_NOT_FOUND);
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

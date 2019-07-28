package co.com.smartcode.common.template.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Scanner;

import com.google.common.base.Charsets;

import co.com.smartcode.common.template.TemplateManager;
import co.com.smartcode.common.template.exceptions.TemplateException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * Wrapper to facilitate the freemarker use
 *
 * @author Julianccalle
 */
public abstract class AbstractTemplateManager implements TemplateManager {

	protected Configuration configuration;

	public AbstractTemplateManager() {

	}

	/**
	 * method that must be overwritten to return the path of the folder or package
	 * containing the templates
	 *
	 * @return relative template path
	 */
	protected abstract String getTemplateFolder();

	/**
	 * Method that initializes the freemarker configuration
	 *
	 * @param useClassPathLoader if true the templates will be inside a package,
	 *                           otherwise in an external folder
	 */
	protected void init(boolean useClassPathLoader) {
		try {
			configuration = new Configuration(Configuration.VERSION_2_3_28);
			if (useClassPathLoader) {
				TemplateLoader templateLoader = new ClassTemplateLoader(getClass(), getTemplateFolder());
				configuration.setTemplateLoader(templateLoader);
			} else {
				configuration.setDirectoryForTemplateLoading(new File(getTemplateFolder()));
			}
			configuration.setDefaultEncoding(Charsets.UTF_8.name());
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * process the templatePath inside the templates folder and generate the text
	 * with the parameters
	 *
	 * @param params  variables to process the template
	 * @param templatePath relative path within the templates folder
	 * @return processed text of the template with the parameters
	 */
	@Override
	public String process(Map<String, Object> params, String templatePath) {
		try {
			Template temp = configuration.getTemplate(templatePath);
			return processTemplate(params, temp);
		} catch (NullPointerException e) {
			throw new TemplateException("Must invoke the init method after you create the service", e);
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * process template from a String and generate the text with the parameters
	 * 
	 * @param params  variables to process the template
	 * @param content template text
	 * @return processed text of the template with the parameters
	 */
	@Override
	public String processText(Map<String, Object> params, String content) {
		try {
			Template temp = new Template("template", content, configuration);
			return processTemplate(params, temp);
		} catch (NullPointerException e) {
			throw new TemplateException("Must invoke the init method after you create the service", e);
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * process template from a File and generate the text with the parameters
	 *
	 * @param params variables to process the template
	 * @param file   template file
	 * @return processed text of the template with the parameters
	 */
	@Override
	public String processFile(Map<String, Object> params, File file) {
		try(Scanner scanner = new Scanner(file, Charsets.UTF_8.name())) {
			StringBuilder builder = new StringBuilder();
			while (scanner.hasNextLine()) {
				if (builder.length() > 0) {
					builder.append(System.lineSeparator());
				}
				builder.append(scanner.nextLine());
			}
			return processText(params, builder.toString());
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	protected String processTemplate(Map<String, Object> params, Template temp)
			throws IOException, freemarker.template.TemplateException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(baos, Charsets.UTF_8.name());
		temp.process(params, out);
		baos.close();
		return new String(baos.toByteArray(), Charsets.UTF_8.name());
	}

}

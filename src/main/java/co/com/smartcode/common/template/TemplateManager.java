package co.com.smartcode.common.template;

import java.io.File;
import java.util.Map;

/**
 * Define the contracts for the template manager
 * @author Julianccalle
 */
public interface TemplateManager {

	/**
	 * process the templatePath inside the templates folder and generate the text
	 * with the parameters
	 *
	 * @param params  variables to process the template
	 * @param templatePath relative path within the templates folder
	 * @return processed text of the template with the parameters
	 */
    public String process(Map<String, Object> params, String templatePath);

	/**
	 * process template from a String and generate the text with the parameters
	 * 
	 * @param params  variables to process the template
	 * @param content template text
	 * @return processed text of the template with the parameters
	 */
    public String processText(Map<String, Object> params, String content);

	/**
	 * process template from a File and generate the text with the parameters
	 *
	 * @param params variables to process the template
	 * @param file   template file
	 * @return processed text of the template with the parameters
	 */
    public String processFile(Map<String, Object> params, File file);

}

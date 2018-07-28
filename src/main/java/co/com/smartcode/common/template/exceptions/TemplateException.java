package co.com.smartcode.common.template.exceptions;

/**
 * Excepción que envuelve cualquier otra lanzada en el procesamiento del template
 * @author Julianccalle
 */
public class TemplateException extends RuntimeException{

	private static final long serialVersionUID = -2324023568324984997L;

	public TemplateException() {
    }

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateException(Throwable cause) {
        super(cause);
    }
    
}

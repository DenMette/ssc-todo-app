package ordina.jworks.security.todo.web.in.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResource {

    private HttpStatus status;
    private String code;
    private String message;
    private String exceptionType;

    public ErrorResource(HttpStatus status, String message) {
        this(status, message, null);
    }

    public ErrorResource(HttpStatus status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
        if ((this.code == null || this.code.isBlank()) && this.status != null) {
            this.code = this.status.toString();
        }
    }

    public ErrorResource(HttpStatus status, Throwable exception, String code) {
        this(status, exception.getMessage(), code);
        this.exceptionType = exception.getClass().getTypeName();
    }

    public ErrorResource(HttpStatus status, Throwable exception, String message, String code) {
        this(status, exception, code);
        if (message != null && !message.isBlank()) {
            this.message = message;
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResource that = (ErrorResource) o;
        return getStatus() == that.getStatus() && Objects.equals(getCode(), that.getCode()) && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getExceptionType(), that.getExceptionType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getCode(), getMessage(), getExceptionType());
    }

    @Override
    public String toString() {
        return "ErrorResource{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", exceptionType='" + exceptionType + '\'' +
                '}';
    }
}

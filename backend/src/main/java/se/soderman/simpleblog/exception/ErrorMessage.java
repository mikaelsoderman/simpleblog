package se.soderman.simpleblog.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ErrorMessage implements Serializable {
    private Integer httpStatus;
    private List<String> messages;

    private ErrorMessage(Builder builder) {
        this.httpStatus = builder.httpStatus;
        this.messages = builder.messages;
    }

    public static class Builder {
        private Integer httpStatus;
        private List<String> messages = new ArrayList<>();

        public Builder setHttpStatus(Integer httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder setMessages(List<String> messages) {
            this.messages = messages;
            return this;
        }

        public Builder addMessage(String message) {
            this.messages.add(message);
            return this;
        }

        public ErrorMessage build() {
            return new ErrorMessage(this);
        }
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public List<String> getMessages() {
        return messages;
    }
}
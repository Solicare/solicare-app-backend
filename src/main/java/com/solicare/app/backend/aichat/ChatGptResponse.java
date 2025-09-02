package com.solicare.app.backend.aichat;

import java.util.List;

public record ChatGptResponse(List<Choice> choices) {
    public record Choice(Message message) {}

    public record Message(String role, String content) {}
}

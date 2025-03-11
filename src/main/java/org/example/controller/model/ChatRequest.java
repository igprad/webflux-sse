/* igprad - (C) 2025 */
package org.example.controller.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "Message can not be blank.") String message,
        @NotBlank(message = "Username can not be blank.") String username) {}

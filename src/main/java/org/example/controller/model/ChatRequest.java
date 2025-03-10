/* igprad - (C) 2025 */
package org.example.controller.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank String message, @NotBlank String username) {}

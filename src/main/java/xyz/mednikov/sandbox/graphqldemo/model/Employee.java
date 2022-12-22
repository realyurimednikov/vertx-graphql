package xyz.mednikov.sandbox.graphqldemo.model;

import java.util.UUID;

public record Employee(UUID id, String firstName, String lastName) {
}

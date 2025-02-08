package dev.professional_fullstack_developer.tutorial.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DemoExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private int testCount;

    public DemoExtension() {
        this.testCount = 0;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println("[DEMO] Initializing demo test configurations");
        this.testCount = 0;
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        System.out.println("[DEMO] Tearing down demo test configurations");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.printf("[DEMO] Setting up demo test %s%n", ++testCount);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.printf("[DEMO] Cleaning up demo test %s%n", testCount);
    }

}

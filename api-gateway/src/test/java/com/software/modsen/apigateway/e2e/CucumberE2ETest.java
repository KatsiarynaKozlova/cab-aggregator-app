package com.software.modsen.apigateway.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/",
        glue = "com.software.modsen.apigateway.e2e"
)
public class CucumberE2ETest {
}

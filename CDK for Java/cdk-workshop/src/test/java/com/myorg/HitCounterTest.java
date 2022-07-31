package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.assertions.Template;
import software.amazon.awscdk.assertions.Capture;
import java.io.IOException;

import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

import java.util.Map;

// Assertions is a collection of utility methods that support asserting conditions in tests.
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class HitCounterTest {

    // @Test注解声明这是一个测试方法，使用Junit测试框架
    @Test
    // 测试方法通常不返回任何值，因为测试方法有抛出异常的情况，throws Exception是为了帮助程序通过编译
    public void testDynamoDBTable() throws Exception {
        Stack stack = new Stack();

        Function hello = Function.Builder.create(stack, "HelloHandler")
                .runtime(Runtime.NODEJS_14_X)
                .code(Code.fromAsset("lambda"))
                .handler("hello.handler")
                .build();
        // 实例化我们自定义的HitCounter这个Construct
        HitCounter helloWithCounter = new HitCounter(stack, "HelloHitCounter", HitCounterProps.builder()
                .downstream(hello)
                .build());

        // synthesize the stack to a CloudFormation template
        // Template: Suite of assertions that can be run on a CDK stack.
        // Typically used, as part of unit tests, to validate that the rendered
        // CloudFormation template has expected resources and properties.
        Template template = Template.fromStack(stack);
        // resourceCountIs: Assert that the given number of resources of the given type
        // exist in the template.
        template.resourceCountIs("AWS::DynamoDB::Table", 1);
    }

    @Test
    public void testLambdaEnvVars() throws Exception {
        Stack stack = new Stack();

        Function hello = Function.Builder.create(stack, "HelloHandler")
                .runtime(Runtime.NODEJS_14_X)
                .code(Code.fromAsset("lambda"))
                .handler("hello.handler")
                .build();

        HitCounter helloWithCounter = new HitCounter(stack, "HelloHitCounter", HitCounterProps.builder()
                .downstream(hello)
                .build());

        // synthesize the stack to a CloudFormation template
        Template template = Template.fromStack(stack);

        // Capture values while matching templates.
        // Using an instance of this class within a Matcher will capture the matching value.
        // The as*() APIs on the instance can be used to get the captured value.
        Capture envCapture = new Capture();
        // Class Object is the root of the class hierarchy. Every class has Object as a superclass父类.
        Map<String, Object> expected = Map.of(
                "Handler", "hitcounter.handler",
                "Environment", envCapture);
        // Assert that a resource of the given type and properties exists in the CloudFormation template.
        template.hasResourceProperties("AWS::Lambda::Function", expected);

        // Cloudformation内部函数Ref返回指定参数或资源的值。https://docs.aws.amazon.com/zh_cn/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-ref.html
        Map<String, Object> expectedEnv = Map.of(
                "Variables", Map.of(
                        "DOWNSTREAM_FUNCTION_NAME", Map.of("Ref", "HelloHandler2E4FBA4D"),
                        "HITS_TABLE_NAME", Map.of("Ref", "HelloHitCounterHits7AAEBF80")));
        
        System.out.println("---------------------------");  
        System.out.println(envCapture.asObject());  
        System.out.println("---------------------------");  

        // System.out.println(expectedEnv);

        // The assertThat() method takes the actual value or object as a method parameter.
        // envCapture.asObject()是实际值，expectedEnv是期待值
        // asObject(): Retrieve the captured value as a JSON object.
        // An error is generated if no value is captured or if the value is not an object.
        // isEqualTo: Verifies that the actual value is equal to the given one.
        // https://joel-costigliola.github.io/assertj/core/api/org/assertj/core/api/Assert.html#:~:text=Method%20Detail-,isEqualTo,-SELF%C2%A0isEqualTo(
        assertThat(envCapture.asObject()).isEqualTo(expectedEnv);
    }

    @Test
    public void testDynamoDBEncryption() throws Exception {
        Stack stack = new Stack();

        Function hello = Function.Builder.create(stack, "HelloHandler")
                .runtime(Runtime.NODEJS_14_X)
                .code(Code.fromAsset("lambda"))
                .handler("hello.handler")
                .build();

        HitCounter helloWithCounter = new HitCounter(stack, "HelloHitCounter", HitCounterProps.builder()
                .downstream(hello)
                .build());

        // synthesize the stack to a CloudFormation template
        Template template = Template.fromStack(stack);
        Capture envCapture = new Capture();
        Map<String, Object> expected = Map.of(
                "SSESpecification", Map.of("SSEEnabled", true));

        template.hasResourceProperties("AWS::DynamoDB::Table", expected);
    }
}

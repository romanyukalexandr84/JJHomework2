package org.example;

public class TestRunnerDemo {
    @Test(order = 3)
    void test1() {
        System.out.println("test1");
    }

    @Test(order = 4)
    private void test2() { //не запустится, т.к. приватный
        System.out.println("test2");
    }

    @Test(order = 2)
    void test3() {
        System.out.println("test3");
    }

    @Test(order = 1)
    void test4() {
        System.out.println("test4");
    }

    @BeforeAll
    void firstMethod() {
        System.out.println("Это самый первый метод");
    }

    @BeforeAll
    void firstMethod2() {
        System.out.println("Это тоже самый первый метод");
    }

    @BeforeEach
    void beginningMethod() {
        System.out.println("Это метод перед тестом");
    }

    @BeforeEach
    void beginningMethod2() {
        System.out.println("Это ещё один метод перед тестом");
    }

    @AfterEach
    void endingMethod() {
        System.out.println("Это метод после теста");
    }

    @AfterEach
    void endingMethod2() {
        System.out.println("Это ещё один метод после теста");
    }

    @AfterAll
    void lastMethod() {
        System.out.println("Это самый последний метод");
    }

    @AfterAll
    void lastMethod2() {
        System.out.println("Это тоже самый последний метод");
    }

    public static void main(String[] args) {
        TestRunner.run(TestRunnerDemo.class);
    }
}

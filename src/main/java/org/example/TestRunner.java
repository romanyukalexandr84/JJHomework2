package org.example;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    public static void run(Class<?> testClass) {
        final Object testObj = initTestObj(testClass);
        Method[] allMethods = testClass.getDeclaredMethods();
        List<Method> beginMethods = new ArrayList<>();
        List<Method> endMethods = new ArrayList<>();
        List<Method> lastMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method someMethod : allMethods) {
            if (someMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                continue;
            }
            if (someMethod.getAnnotation(BeforeAll.class) != null) {
                try {
                    someMethod.invoke(testObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else if (someMethod.getAnnotation(BeforeEach.class) != null) {
                beginMethods.add(someMethod);
            } else if (someMethod.getAnnotation(AfterEach.class) != null) {
                endMethods.add(someMethod);
            } else if (someMethod.getAnnotation(AfterAll.class) != null) {
                lastMethods.add(someMethod);
            } else if (someMethod.getAnnotation(Test.class) != null) {
                testMethods.add(someMethod);
            }
        }

        testMethods.sort(myComparator);
        for (Method someMethod : testMethods) {
            try {
                for (Method beginMethod : beginMethods) {
                    beginMethod.invoke(testObj);
                }
                someMethod.invoke(testObj);
                for (Method endMethod : endMethods) {
                    endMethod.invoke(testObj);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        for (Method someMethod : lastMethods) {
            try {
                someMethod.invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Object initTestObj(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект тест класса");
        }
    }

    public static Comparator<Method> myComparator = new Comparator<Method>() {
        @Override
        public int compare(Method m1, Method m2) {
            return m1.getAnnotation(Test.class).order() - m2.getAnnotation(Test.class).order();
        }
    };
}

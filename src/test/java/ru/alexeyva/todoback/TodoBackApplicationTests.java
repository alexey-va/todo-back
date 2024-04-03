package ru.alexeyva.todoback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class TodoBackApplicationTests {

    @AllArgsConstructor
    @Getter @ToString
    static class Car{
        LocalDate localDate;
    }

    @Test
    void contextLoads() {

        List<Car> cars = new ArrayList<>();
        cars.add(new Car(LocalDate.now()));
        cars.add(new Car(LocalDate.now().minusMonths(10)));
        cars.add(new Car(LocalDate.now().minusMonths(5)));
        cars.add(new Car(LocalDate.now().minusMonths(2)));
        cars.add(new Car(LocalDate.now().minusMonths(1)));

        var car = cars.stream()
                .max(Comparator.comparing(Car::getLocalDate))
                .orElse(null);
        System.out.println(car);

    }

}

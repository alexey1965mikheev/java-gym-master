package ru.yandex.practicum.gym;

import java.util.Comparator;

public class TrainingCountDescComparator implements Comparator<CounterOfTrainings> {
    @Override
    public int compare(CounterOfTrainings o1, CounterOfTrainings o2) {
        return Integer.compare(o2.getCount(), o1.getCount());
    }
}

package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;
    private Map<Coach, Integer> coachesCounter;

    public Timetable() {
        timetable = new HashMap<>();
        coachesCounter = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);
        if (dayMap == null) {
            dayMap = new TreeMap<>();
            timetable.put(day, dayMap);
        }
        List<TrainingSession> sessions = dayMap.get(time);
        if (sessions == null) {
            sessions = new ArrayList<>();
            dayMap.put(time, sessions);
        }
        sessions.add(trainingSession);

        // Обновление счетчика занятий для тренера
        Coach coach = trainingSession.getCoach();
        coachesCounter.put(coach, coachesCounter.getOrDefault(coach, 0) + 1);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) {
            return new TreeMap<>();
        }
        return dayMap;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessions = dayMap.get(timeOfDay);
        if (sessions == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(sessions);
        }
    }

    public List<CounterOfTrainings> getCountByCoaches() {

        // Преобразование Map в список объектов CounterOfTraining
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachesCounter.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        // Сортировка списка по убыванию количества занятий
        TrainingCountDescComparator comparator = new TrainingCountDescComparator();
        result.sort(comparator);
        return result;
    }
}
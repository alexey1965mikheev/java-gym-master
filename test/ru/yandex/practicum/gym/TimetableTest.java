package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {
    // Тест получения занятий за день - одно занятие
    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.get(0));

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    // Тест получения занятий за день - несколько занятий
    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(mondayChildTrainingSession, mondaySessions.get(0));
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0));
        assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1));
        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    // Тест получения занятий за конкретный день и время
    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySessionAt13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, mondaySessionAt13.size());
        assertEquals(singleTrainingSession, mondaySessionAt13.get(0));
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> mondaySessionAt14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(mondaySessionAt14.isEmpty());
    }

    // Тест подсчета тренировок - пустое расписание
    @Test
    void testGetCountByCoachesEmpty() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertTrue(result.isEmpty());
    }

    // Тест подсчета тренировок - один тренер
    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession mondayChildTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession wednesdayChildTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        TrainingSession fridayChildTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(10, 0));
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(wednesdayChildTrainingSession);
        timetable.addNewTrainingSession(fridayChildTrainingSession);

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertEquals(1, result.size());
        assertEquals(coach, result.get(0).getCoach());
        assertEquals(3, result.get(0).getCount());
    }

    // Тест подсчета тренировок - несколько тренеров с сортировкой по убыванию количества занятий
    @Test
    void testGetCountByCoachesMultipleCoachesSorted() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coachA = new Coach("Иванов", "Иван", "Иванович");
        Coach coachB = new Coach("Петров", "Петр", "Петрович");
        Coach coachC = new Coach("Сергеев", "Сергей", "Сергеевич");

        // CoachA - 2 тренировки
        TrainingSession TrainingSession1 = new TrainingSession(group, coachA,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession TrainingSession2 = new TrainingSession(group, coachA,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 0));
        timetable.addNewTrainingSession(TrainingSession1);
        timetable.addNewTrainingSession(TrainingSession2);

        // CoachB - 4 тренировки
        TrainingSession TrainingSession3 = new TrainingSession(group, coachB,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0));
        TrainingSession TrainingSession4 = new TrainingSession(group, coachB,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        TrainingSession TrainingSession5 = new TrainingSession(group, coachB,
                DayOfWeek.SATURDAY, new TimeOfDay(8, 30));
        TrainingSession TrainingSession6 = new TrainingSession(group, coachB,
                DayOfWeek.SATURDAY, new TimeOfDay(18, 30));
        timetable.addNewTrainingSession(TrainingSession3);
        timetable.addNewTrainingSession(TrainingSession4);
        timetable.addNewTrainingSession(TrainingSession5);
        timetable.addNewTrainingSession(TrainingSession6);

        // CoachC - 1 тренировка
        TrainingSession TrainingSession7 = new TrainingSession(group, coachC,
                DayOfWeek.FRIDAY, new TimeOfDay(14, 45));
        timetable.addNewTrainingSession(TrainingSession7);

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertEquals(3, result.size());
        assertEquals(coachB, result.get(0).getCoach());
        assertEquals(4, result.get(0).getCount());
        assertEquals(coachA, result.get(1).getCoach());
        assertEquals(2, result.get(1).getCount());
        assertEquals(coachC, result.get(2).getCoach());
        assertEquals(1, result.get(2).getCount());
    }

    /* Тест подсчета тренировок - у двух тренеров одинаковое количество занятий
       проверяем только количество занятий, так как порядок не регламентирован */
    @Test
    void testGetCountByCoachesSameCount() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coachA = new Coach("Иванов", "Иван", "Иванович");
        Coach coachB = new Coach("Петров", "Петр", "Петрович");

        TrainingSession TrainingSession1 = new TrainingSession(group, coachA,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));
        TrainingSession TrainingSession2 = new TrainingSession(group, coachA,
                DayOfWeek.THURSDAY, new TimeOfDay(11, 0));
        timetable.addNewTrainingSession(TrainingSession1);
        timetable.addNewTrainingSession(TrainingSession2);

        TrainingSession TrainingSession3 = new TrainingSession(group, coachB,
                DayOfWeek.TUESDAY, new TimeOfDay(11, 0));
        TrainingSession TrainingSession4 = new TrainingSession(group, coachB,
                DayOfWeek.SATURDAY, new TimeOfDay(11, 0));
        timetable.addNewTrainingSession(TrainingSession3);
        timetable.addNewTrainingSession(TrainingSession4);

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getCount());
        assertEquals(2, result.get(1).getCount());
    }
}

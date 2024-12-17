import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<GlobalTask> globalTasks = new ArrayList<>();
        globalTasks.add(new GlobalTask(Status.TO_DO, "Переезд"));
        globalTasks.add(new GlobalTask(Status.TO_DO, "Помыть машину"));
        globalTasks.add(new GlobalTask(Status.IN_PROGRESS, "Сходить в магазин"));
        globalTasks.add(new GlobalTask(Status.DONE, "Купить подарок"));

        printTasks(globalTasks);

       GlobalTask task = globalTasks.get(1); // Получаем вторую задачу (индекс начинается с 0)
        task.subTask.add(new SubTask(Status.IN_PROGRESS, "Доехать до автомойки")); // Добавляем подзадачу "Доехать до автомойки" ко второй задаче
        task.subTask.add(new SubTask(Status.IN_PROGRESS, "Оплатить комплекс"));
        System.out.println("Добавили подзадачи в Епик Помыть машину");
        System.out.println();
        // Это чтобы не возиться со сканером и быстро затестить добавление. А сканнер и метод для добавления закоментить

        chekSubStatusAndChangeEpicStatus(globalTasks);

/*
        System.out.println("В какую задачу, Вы хотите добавить подзадачу");
        String epic = scanner.nextLine();
        System.out.println("Название подзадачи (статус по умолчанию TO_DO)");
        String subTask = scanner.nextLine();

        addSubTask(globalTasks, epic, subTask);

        System.out.println("Введите задачу у которой у хотите изменить статус");
        String tasks = scanner.nextLine();
        changeStatus(globalTasks, tasks);

        Можно раскоментить, здесь добавляем субзадачи и меняем статус задачи если хотим
 */
        printTasks(globalTasks);
    }

        public static void changeStatus(ArrayList<GlobalTask> globalTasks, String tasks) {
            for (GlobalTask task : globalTasks) {
                if (task.description.equals(tasks)) {
                    task.status = Status.IN_PROGRESS;  // Изменение статуса задачи
                    System.out.println("Статус задачи изменен");
                    return;
                }
                for (SubTask subTask : task.subTask) {
                    if (subTask.description.equals(tasks)) {
                        subTask.status = Status.IN_PROGRESS;
                        System.out.println("Статус задачи изменен");
                        return;
                    }
                }
            }
            System.out.println("Такой задачи нет");
            System.out.println("----------");
        }

        public static void printTasks (ArrayList<GlobalTask> globalTasks) {
            System.out.println("Задачи, их статус и id");
            for (GlobalTask globalTask : globalTasks) {
                System.out.printf(globalTask.description + " " + globalTask.status + " " + globalTask.getId() + "%n");
                if (!globalTask.subTask.isEmpty()) {
                    System.out.println("-----------------------");
                    System.out.println("Найдены подзадачи у Эпик задания: " + globalTask.description);
                    for (SubTask subTask : globalTask.subTask) {
                        System.out.println(subTask.description + " " + subTask.status + " " + subTask.taskId);
                    }
                    System.out.println("-----------------------");

                }
            }
            System.out.println();
        }

        public static void addSubTask (ArrayList<GlobalTask> globalTasks, String epic, String subTask) {
            for (int i = 0; i < globalTasks.size(); i++) {
                if (globalTasks.get(i).description.equals(epic)) {
                    SubTask newSubTask = new SubTask(Status.TO_DO, subTask);
                    globalTasks.get(i).subTask.add(newSubTask);
                    return;
                } else {
                    break;
                }
            }
            System.out.println("Задача не найдена");
        }

        public static void chekSubStatusAndChangeEpicStatus (ArrayList<GlobalTask> globalTasks) {
            for (int i = 0; i < globalTasks.size(); i++) { // весь список задач
                GlobalTask tasks = globalTasks.get(i);
                if (!tasks.subTask.isEmpty()) { // есть ли суб задачи
                    for (Status status : Status.values()) { // проходим по все статусам
                        boolean sameStatus = true;
                        for (int j = 0; j < tasks.subTask.size(); j++) {
                            if (!tasks.subTask.get(j).status.equals(status)) {
                                sameStatus = false;
                                break;
                            }
                        }
                        if (sameStatus) {
                            tasks.status = status;
                            System.out.println("Все подзадачи у Епик выполнены. Смена статуса у Епика: " +
                                               tasks + ", новый статус: " + status );
                        }
                    }
                }
            }
        }
}

/* Я скидываю только пока узнать я вообще не зря это делаю? может я не правильно понял структуру и вообще по другому нужно делать
Епик задача у меня это ГлобалТаск. Субзадачи это SubTask. А обычные задачи это ГлобалТаск у которых просто нет подзадач. И айди я сделал, но цифры совпадут с Таском,
просто то что по другому называется, может так они будут разделяться, этим названием Айди. Отправляю это чтобы просто понять я делаю то что нужно или нет. Вдруг тут все нужно снести и делать по новой.
Все поля у меня Протектед, если нужно переделаю в приват. Методы оставил пока так. Потом если все корректно и нужно, перенесу в ГлобалТаск.
Еквалс и ХешКод не переопределял. И без них какбудто все корректно находит. Но если что через генератор сделаю, просто не понимаю зачем это нужно.
Также если не сложно буду рад получить советы, рекомендации. Если честно все что тут написано я только с помощью ЯндексГПТ смог написать. Очень очень сложно.
Спасибо за обратную связь, и Хорошего Дня!
 */
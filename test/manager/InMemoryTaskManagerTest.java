package manager;


class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    void setManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        manager = new InMemoryTaskManager(historyManager);
    }
}
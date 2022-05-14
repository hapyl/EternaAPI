package kz.hapyl.spigotutils.module.reflect.npc;

public class NPCResponse {

    private String questGIFinish;
    private String questGINeedMore;
    private String questGIInvalidItem;
    private String cannotInteract;

    public NPCResponse() {
        this.cannotInteract = "";
        this.questGIFinish = "That's exactly what I needed, thanks!";
        this.questGINeedMore = "Thanks! But I needed more.";
        this.questGIInvalidItem = "What is this? I asked for something completely different!";
    }

    public String getCannotInteract() {
        return cannotInteract;
    }

    public void setCannotInteract(String cannotInteract) {
        this.cannotInteract = cannotInteract;
    }

    public String getQuestGiveItemsFinish() {
        return questGIFinish;
    }

    public void setQuestGiveItemsFinish(String questGIFinish) {
        this.questGIFinish = questGIFinish;
    }

    public String getQuestGiveItemsNeedMore() {
        return questGINeedMore;
    }

    public void setQuestGiveItemsNeedMore(String questGINeedMore) {
        this.questGINeedMore = questGINeedMore;
    }

    public String getQuestGiveItemsInvalidItem() {
        return questGIInvalidItem;
    }

    public void setQuestGiveItemsInvalidItem(String questGIInvalidItem) {
        this.questGIInvalidItem = questGIInvalidItem;
    }
}

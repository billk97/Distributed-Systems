public class Publisher {
    private String myHash=null;
    public Brocker hashTopic(Topic topic){
        Md5 md5 = new Md5();
        myHash=md5.HASH(topic.getBusLine());

    }
    public String getMyHash(){
        return myHash;
    }


}

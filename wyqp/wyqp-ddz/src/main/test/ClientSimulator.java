public class ClientSimulator {

    private static final int waitTime = 100;


    public static void main(String[] args) throws Exception {
        Client clientOwner = new Client();
        clientOwner.init();
        clientOwner.entryHall();
        clientOwner.createRoom();
        clientOwner.playerReady();
        Thread.sleep(waitTime);

        Client client1 = new Client();
        client1.init();
        client1.entryHall();
        client1.playerIn(clientOwner.getRoomId());
        client1.playerReady();

        Client client2 = new Client();
        client2.init();
        client2.entryHall();
        client2.playerIn(clientOwner.getRoomId());
        client2.playerReady();


        Thread.sleep(waitTime);
        client2.callLandlord(1);
        Thread.sleep(waitTime);
        clientOwner.callLandlord(0);
        Thread.sleep(waitTime);
        client1.callLandlord(2);


        Client[] clients = new Client[3];
        clients[0] = client1;
        clients[1] = client2;
        clients[2] = clientOwner;
        for (int i=0;i<100;i++){
            if (clients[i%3].getGameOver()){
                break;
            }
            System.out.println("第" + i + "轮");
            Thread.sleep(waitTime);
            clients[i%3].cue();
            Thread.sleep(waitTime);
            clients[i%3].playerCard();
        }


    }
}

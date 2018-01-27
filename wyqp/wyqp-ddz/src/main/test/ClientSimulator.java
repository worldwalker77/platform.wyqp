public class ClientSimulator {


    public static void main(String[] args) throws Exception {
        Client clientOwner = new Client();
        clientOwner.init();
        clientOwner.entryHall();
        clientOwner.createRoom();
        clientOwner.playerReady();
        Thread.sleep(1000);

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

        Thread.sleep(1000);
        client2.cue();
        Thread.sleep(1000);
        client2.playerCard();

    }
}

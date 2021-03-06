package server.handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Constants;
import model.PRTData;
import model.Vertex;

/**
 *
 * @author Bennet
 */
public class AndroidMobileHandler extends StaticObjectHandler implements Runnable, Constants {

    private final Socket clientSocket;
    private PodcarHandlerOUT podcarHandler;

    public AndroidMobileHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.podcarHandler = new PodcarHandlerOUT();
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            DataInputStream in = new DataInputStream(inputStream);

            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(outputStream);

            int hello = in.readInt();
            System.out.println(hello);
            if (hello == HELLO_INT) {
                byte functionNumber = in.readByte();
                switch (functionNumber) {
                    case 000:
                        System.out.println("Functie 000 is aangeroepen");
                        String[] stationNamesArray = PRTData.getPRTData().getStationNamesArray();
                        ous.writeObject(stationNamesArray);
                        ous.flush();

                        if (in.readInt() == 250) {
                            System.out.println("Sending array complete!");
                        }

                        break;

                    case 001:

                        System.out.println("Functie 001 is aangeroepen");

                        int idNodeFrom = in.readInt();
                        int idNodeTo = in.readInt();
                        System.out.println(idNodeFrom + "--" + idNodeTo);


                      
                        this.allocateRide(idNodeFrom, idNodeTo);
                        break;
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(AndroidStationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

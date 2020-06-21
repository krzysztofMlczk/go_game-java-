package com.tearsvalley.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.tearsvalley.bot.Bot;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Command;
import com.tearsvalley.data.Gamemode;
import com.tearsvalley.data.Point;
import com.tearsvalley.logic.BoardService;
import com.tearsvalley.serializable.*;

public class Player implements Runnable {

    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private Socket client = null;
    private volatile Gamemode gamemode = Gamemode.NOTCHOSENYET;

    private Color playerColor = Color.NAC;
    private Player opponent;
    private boolean passed = false;
    private BoardService boardService;
    private Bot bot = null;

    // private MessageHandler messageHandler; // each player will have his OWN
    // message handler (buecause we would have to make it synchronized which is not
    // good idea)

    // player constructor
    public Player(Socket client, Color playerColor) throws IOException {
        this.client = client;
        this.playerColor = playerColor;

        // opening output and input to send initial message (which will inform client
        // about his color)
        try {
            outputStream = new ObjectOutputStream(client.getOutputStream());
            inputStream = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setClient();
    }

    // not yet implemented
    @Override
    public void run() {
        try {
            if(gamemode == Gamemode.MULTIPLAYER)
                procesCommandMulti();
            else if(gamemode == Gamemode.SINGLEPLAYER) {
                bot = new Bot(boardService);
                procesCommandSingle();
            }
            } catch(IOException ex) {
                ex.printStackTrace();
                close();
            } catch(ClassNotFoundException ex) {
                ex.printStackTrace();
                close();
            }
    }


    private void procesCommandMulti() throws IOException, ClassNotFoundException {
        //we are reading instructions until both players pass (pass has to be set to false every queue if both haven't passed)
        flag:
        while (true) { 
            //do metody handle wysylamy liste komend
            MessageInterface message;

            message = (MessageInterface) inputStream.readObject();

            if(message.getCommand() == Command.QUIT) {
                MessageInterface closec = new CloseConnection();
                opponent.sendMessage(message);
                sendMessage(closec);
                close();
            } else if(message.getCommand() == Command.MOVE) {
                MoveMessage move = (MoveMessage) message;

                int px = move.getPoint().getX();
                int py = move.getPoint().getY();
                Color pcolor = move.getStoneColor();

                boolean canBePlaced = boardService.setStoneOnBoard(px,py,pcolor);
                System.out.println(canBePlaced);
                if(canBePlaced) {
                    MessageInterface pStone = new PlaceStoneMessage(new Point(px, py), pcolor);
                    
                    sendMessage(pStone);
                    outputStream.flush();
                    opponent.sendMessage(pStone);
                    opponent.outputStream.flush();


                    ArrayList<Point> fieldsToDelete = boardService.getStonesToDelete(px,py,pcolor);

                    MessageInterface dMessage = new DeleteStonesMessage(fieldsToDelete, this.playerColor);

                    opponent.passed = false;

                    sendMessage(dMessage);
                    opponent.sendMessage(dMessage);
                } else {
                    MessageInterface badMove = new NotValidMoveMessage();
                    sendMessage(badMove);
                }
            } else if(message.getCommand() == Command.PASSED) {
                passed = true;
                if(opponent.passed == true) {
                    Color winnerColor;
                    if(boardService.whitePoints>boardService.blackPoints)
                        winnerColor = Color.WHITE;
                    else
                        winnerColor = Color.BLACK;

                    MessageInterface end = new EndMessage(winnerColor, boardService.getTerritoryCounted(playerColor), boardService.getScore(playerColor));
                    MessageInterface opponentEnd = new EndMessage(winnerColor, boardService.getTerritoryCounted(opponent.playerColor), boardService.getScore(opponent.playerColor));
                    sendMessage(end);
                    opponent.sendMessage(opponentEnd);
                    break flag;
                } else {
                    opponent.sendMessage(message);
                }
 
            }
        }
    }
    
    private void procesCommandSingle() throws IOException, ClassNotFoundException {

        while(true) {
            MessageInterface message;

            message = (MessageInterface) inputStream.readObject();

            if(message.getCommand() == Command.QUIT) {
                MessageInterface closec = new CloseConnection();
                sendMessage(closec);
                close();
            } else if(message.getCommand() == Command.MOVE) {
                MoveMessage move = (MoveMessage) message;

                int px = move.getPoint().getX();
                int py = move.getPoint().getY();
                Color pcolor = move.getStoneColor();

                boolean canBePlaced = boardService.setStoneOnBoard(px,py,pcolor);
                System.out.println(canBePlaced);

                if(canBePlaced) {
                    MessageInterface pStone = new PlaceStoneMessage(new Point(px, py), pcolor);
                    
                    sendMessage(pStone);
                    outputStream.flush();


                    ArrayList<Point> fieldsToDelete = boardService.getStonesToDelete(px,py,pcolor);

                    MessageInterface dMessage = new DeleteStonesMessage(fieldsToDelete, this.playerColor);

                    sendMessage(dMessage);

                    Point botPoint = bot.botMove();

                    MessageInterface pbstone = new PlaceStoneMessage(botPoint, Color.WHITE);

                    sendMessage(pbstone);

                    ArrayList<Point> bFieldsToDelete = boardService.getStonesToDelete(botPoint.getX() ,botPoint.getY(), Color.WHITE);

                    MessageInterface dbMessage = new DeleteStonesMessage(bFieldsToDelete, Color.WHITE);
                    sendMessage(dbMessage);
                } else {
                    MessageInterface badMove = new NotValidMoveMessage();
                    sendMessage(badMove);
                }

            }

        }

    }


    //method which invokes sending initial message to the client
    private void setClient() {
        try {
        sendInitalMessage();
        } catch(IOException ex) {
            ex.printStackTrace();
            close();
        } catch(ClassNotFoundException ex) {
            ex.printStackTrace();
            close();
        }
    }

    public void setOpponent (Player opponent) {
        this.opponent = opponent;
    }
    
    public Player getOpponent () {
        return this.opponent;
    }

    public Gamemode getGamemode() {
        return this.gamemode;
    }

    public Color getPlayerColor () {
        return playerColor;
    }

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }





    //method sending initial message
    private void sendInitalMessage() throws IOException, ClassNotFoundException {
        //sending to the client amount of connected players and color to set
        outputStream.writeObject(new InitialMessage(this.playerColor));
        outputStream.flush();
        //server reading from the input stream to get the game mode from the client
        InitialMessage im = (InitialMessage) inputStream.readObject();
        gamemode = im.getGamemode();
        if(Server.getGamemode() == Gamemode.NOTCHOSENYET){
            Server.setGamemode(im.getGamemode());
        }
    }

    public void sendStartGameMessage() throws IOException{
        outputStream.writeObject(new StartGameMessage());
    }


    public void sendMessage(MessageInterface mi) throws IOException {
        outputStream.writeObject(mi);
    }

    public MessageInterface readMessage() throws ClassNotFoundException, IOException {
        return (MessageInterface) inputStream.readObject();
    }
    
    public void setPlayerColor(Color color) {
    	this.playerColor = color;
    }



    private void close() {
        try {
            inputStream.close();
            outputStream.close();
            client.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }   

}
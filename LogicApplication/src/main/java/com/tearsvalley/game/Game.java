package com.tearsvalley.game;

import com.tearsvalley.data.Board;
import com.tearsvalley.logic.BoardService;
import com.tearsvalley.server.Player;

public abstract class Game {
    private Player currentPlayer;
    private Board board;
    protected BoardService boardService;

    public Game() {
        board = new Board();
        boardService = new BoardService(board);
    }
    
    public abstract void runGame();

    public abstract void setPlayers();

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
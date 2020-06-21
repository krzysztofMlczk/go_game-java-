package com.tearsvalley.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.tearsvalley.data.Board;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Point;
import com.tearsvalley.logic.BoardService;
import com.tearsvalley.server.Player;


public class GameRulesTest {
	
	Board board;
	BoardService boardService;
	
	@Before
	public void setUp() {
		board = new Board();
		boardService = new BoardService(board);
		
		//setting up stones on the board to check breaths on
		//White rectangle surrounded by black stones
		for(int i = 10; i <= 16; i++)
			for(int j = 10; j <= 16; j++) {
				board.field[i][j].setStoneColor(Color.BLACK);				
			}
		for(int i = 11; i <= 15; i++)
			for(int j = 11; j <= 15; j++) {
				board.field[i][j].setStoneColor(Color.WHITE);				
			}
	}
	
	@Test
	public void checkBreathsTest() {
		//I suppose that getNeighbors is tested (it actually is)
		//firstly we check if blacks have breaths - expected = true
		assertTrue(boardService.getGameRules().checkBreaths(board.field[10][16])); //we expect to have breaths
		
		//than we check if whites have breaths - expected = false
		assertFalse(boardService.getGameRules().checkBreaths(board.field[11][15])); //we expect no to have breaths
	}
	
	@Test
	public void fieldToDeleteTest() {
		//should return all white stones from the inside of the rectangle j = 11 
		ArrayList<Point> fieldToDelete = boardService.getGameRules().fieldToDelete(board.field[11][10]); //we invoke this on the one of the black stones
		//should return arraylist of a size that is same as whites amount
		int counter = 0;
		
		for(Point point : fieldToDelete)
			for(int i = 11; i <= 15; i++)
				for(int j = 11; j <= 15; j++) {
					if(point.getX() == i && point.getY() == j)
						counter++; 
//we increase counter, only when we meet point with the same coordiantes, then when counter == size of returned array, then test is passed
				}
		assertEquals(counter, fieldToDelete.size());
	}
	
	@Test
	public void checkDeleteTest() {
		//its one of the white stones, so should return false, cuz it doesn't have any breaths
		assertFalse(boardService.getGameRules().checkDelete(board.field[13][13])); 
	}
	
	@Test
	public void isKillingTest() {
		assertTrue(boardService.getGameRules().isKilling(board.field[16][15])); //because we know that this particular stone could kill inner white rectangle
	}
	
	@Test
	public void countInnerPoints() {
		
		boardService.getGameRules().toCount = true;
		
		boardService.getGameRules().countInnerPoints(board.field[13][13]);
		
		assertFalse(boardService.getGameRules().toCount);
		assertEquals(boardService.getGameRules().fieldToCount.size(), 1);
	}
	
	@Test
	public void countTerritoryTest() {
		Player player = mock(Player.class); //we have to mock the player object, because we will need player color
		when(player.getPlayerColor()).thenReturn(Color.WHITE);
		
		assertEquals(boardService.getGameRules().countTerritory(player.getPlayerColor()), 0); //count territory should be zero, because white rectangle doesn't have any territory
	}
	
}

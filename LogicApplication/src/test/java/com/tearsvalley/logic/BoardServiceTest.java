package com.tearsvalley.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tearsvalley.data.Board;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Field;
import com.tearsvalley.data.Point;
import com.tearsvalley.logic.BoardService;
import com.tearsvalley.logic.GameRules;
import com.tearsvalley.server.Player;

public class BoardServiceTest {
	
	Board board;
	BoardService boardService;
	
	@Before //executes before every test
	public void setUpBoardService() {
		board = new Board();
		boardService = new BoardService(board);
		assertNotNull(board);
	}
	@After
	public void cleanUpBoardService() {
		board = null;
		boardService = null;
	}
	
	@Test 
	public void clearCheckFlagsTest() {
		boardService.clearCheckFlags();
		
		for(int i = 0; i < 19; i++)
			for(int j = 0; j < 19; j++) {
				assertFalse(board.field[i][j].checked);
			}
	}
	
	@Test
	public void setStoneOnBoardTest() {
		assertTrue(boardService.setStoneOnBoard(10, 10, Color.WHITE)); //when setStoneOnBoard returns true it means that placing the stone went succesfully
		assertEquals(Color.WHITE, board.field[10][10].getStoneColor()); //additionally checking the stone color
	}
	
	@Test
	public void setStoneOnOccupiedFieldTest() {
		//Try to set stone on the occupied field
		boardService.setStoneOnBoard(15, 15, Color.BLACK);
		//setStoneOnBoard method should return false when placing the stone on the specified field failed - that's why we check if it returns FALSE
		assertFalse(boardService.setStoneOnBoard(15, 15, Color.WHITE));
	}
	
	@Test
	public void setStoneAfterKoMoveTest() {
		//Try to set the stone on the board after ko move
		boardService.setKo(6,6);
		assertFalse(boardService.setStoneOnBoard(6, 6, Color.WHITE)); //We try to set stone on the "blocked field" so we have to check if SetStone return false
		assertEquals(Color.NAC, board.field[6][6].getStoneColor()); //additionally checking the color - field was empty so it has to maintain this state
	}
		
	@Test
	public void setStoneWhenSuicidalOrKillingTest() { 
		//we DO NOT WANT GAMERULES CLASS TO INFLUENCE THE OUTCOME OF THIS TEST - SO WE HAVE TO MOCK IT
		GameRules gameRules = mock(GameRules.class); //we have to mock GameRules object and define its behaviour
		boardService.setGameRules(gameRules); //setting mocked object to the boardService
		
		//mocked GameRules behaviour defined for the (7,7) field - Suicidal and NOT killing move (Color doesn't matter in this case)
		when(gameRules.checkBreaths(board.field[7][7])).thenReturn(false);
		when(gameRules.isKilling(board.field[7][7])).thenReturn(false);
		
		Color expected77 = Color.NAC; //shouldn't agree for this move
		assertFalse(boardService.setStoneOnBoard(7, 7, Color.BLACK));
		assertEquals(expected77, board.field[7][7].getStoneColor());
		
		//mocked GameRules behaviour defined for the (8,8) field - Not suicidal and killing move
		when(gameRules.checkBreaths(board.field[8][8])).thenReturn(false);
		when(gameRules.isKilling(board.field[8][8])).thenReturn(true);
		
		Color expected88 = Color.BLACK; //should agree for this move
		assertTrue(boardService.setStoneOnBoard(8, 8, Color.BLACK));
		assertEquals(expected88, board.field[8][8].getStoneColor());
		
		//mocked GameRules behaviour defined for the (9,9) field - Not suicidal and not killing move
		when(gameRules.checkBreaths(board.field[9][9])).thenReturn(true);
		when(gameRules.isKilling(board.field[9][9])).thenReturn(false);
		
		Color expected99 = Color.WHITE; //should agree for this move
		assertTrue(boardService.setStoneOnBoard(9, 9, Color.WHITE));
		assertEquals(expected99, board.field[9][9].getStoneColor());
		
		//mocked GameRules behaviour defined for the (10,10) field - Not suicidal and killing move
		when(gameRules.checkBreaths(board.field[10][10])).thenReturn(true);
		when(gameRules.isKilling(board.field[10][10])).thenReturn(true);
		
		Color expected1010 = Color.WHITE; //should agree for this move
		boardService.setStoneOnBoard(10, 10, Color.WHITE);
		assertEquals(expected1010, board.field[10][10].getStoneColor());
	}
	
	@Test
	public void getStonesToDeleteTest() {
		GameRules gameRules = mock(GameRules.class); //we have to mock GameRules object and define its behaviour
		boardService.setGameRules(gameRules); //setting mocked object to the boardService
		
		ArrayList<Point> toDelete = new ArrayList<>(); //will be useful to define mocked gameRules behaviour
		//creating fake list of points to delete, to define gameRules behaviour
		toDelete.add(new Point(11, 10));
		toDelete.add(new Point(12, 10));
		toDelete.add(new Point(13, 10));
		toDelete.add(new Point(14, 10));
		//setting stones on the board on mentioned points
		boardService.setStoneOnBoard(11, 10, Color.WHITE);
		boardService.setStoneOnBoard(12, 10, Color.WHITE);
		boardService.setStoneOnBoard(13, 10, Color.WHITE);
		boardService.setStoneOnBoard(14, 10, Color.WHITE);
		
		when(gameRules.fieldToDelete(board.field[10][10])).thenReturn(toDelete);
		boardService.getStonesToDelete(10, 10, Color.BLACK);
		
		Color expectedFieldColor = Color.NAC; //we expect all the mentioned fields to be deleted
		
		for(int i = 11; i < 15; i++) {
			//we have to check if every field from toDelete was set to NAC - because getStonesToDelete should delete them from the board
			assertEquals(expectedFieldColor, board.field[i][10].getStoneColor()); 
		}
	}
		
	@Test
	public void getCornerNeighborsTest() {
		//Corners
		int expectedNeighborsAmount = 2;
		
		ArrayList<Field> neighbors = boardService.getNeighbors(0, 0);
		assertEquals(expectedNeighborsAmount,neighbors.size());
		
		ArrayList<Field> neighbors1 = boardService.getNeighbors(0, 18);
		assertEquals(expectedNeighborsAmount,neighbors1.size());
		
		ArrayList<Field> neighbors2 = boardService.getNeighbors(18, 0);
		assertEquals(expectedNeighborsAmount,neighbors2.size());
		
		ArrayList<Field> neighbors3 = boardService.getNeighbors(18, 18);
		assertEquals(expectedNeighborsAmount,neighbors3.size());
	}
	
	@Test
	public void getMiddleNeighborsTest() {
		//Middle of the map
		int expectedNeighborsAmount = 4;
		
		ArrayList<Field> neighbors = boardService.getNeighbors(10, 10);
		assertEquals(expectedNeighborsAmount,neighbors.size());
	}
	
	@Test
	public void getBorderNeighborsTest() {
		//Borders
		int expectedNeighborsAmount = 3;
		
		ArrayList<Field> neighbors = boardService.getNeighbors(0, 10);
		assertEquals(expectedNeighborsAmount,neighbors.size());
		
		ArrayList<Field> neighbors1 = boardService.getNeighbors(10, 0);
		assertEquals(expectedNeighborsAmount,neighbors1.size());
		
		ArrayList<Field> neighbors2 = boardService.getNeighbors(18, 10);
		assertEquals(expectedNeighborsAmount,neighbors2.size());
		
		ArrayList<Field> neighbors3 = boardService.getNeighbors(10, 18);
		assertEquals(expectedNeighborsAmount,neighbors3.size());
	}
	
	@Test
	public void getScoreTest() {
		Player player = mock(Player.class); //we have to mock the player object, because we will need player color
		when(player.getPlayerColor()).thenReturn(Color.WHITE); //we have to mock this method behavior, because we need player color to proceed with the test
		
		
		GameRules gameRules = mock(GameRules.class); //we have to mock the gameRules object to get the access to the method which counts territory
		boardService.setGameRules(gameRules); //we have to set the mocked gameRules
		
		int mockedTerritory = 11;
		when(gameRules.countTerritory(player.getPlayerColor())).thenReturn(mockedTerritory); //mocking the behavior of the mocked gameRules's method countTerritory
		
		boardService.whitePoints = 3;
		
		int expectedResult = mockedTerritory + boardService.whitePoints;
		
		assertEquals(expectedResult, boardService.getScore(player.getPlayerColor()));
	}
}
	
	


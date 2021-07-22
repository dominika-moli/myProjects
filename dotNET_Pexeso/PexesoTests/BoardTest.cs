using PexesoGame.Core;
using Xunit;
using System.Collections;

namespace PexesoGameTests
{
    public class BoardTest
    {
        Board board = new Board(4, 5, 2);

        [Fact]
        public void TestRowCount()
        {
            var expected = 5;
            var actual = board.RowCount;

            Assert.Equal(expected, actual);               
        }

        [Fact]  
        public void TestColumnCount()
        {
            var expected = 4;
            var actual = board.ColumnCount;

            Assert.Equal(expected, actual);  
        }

        [Fact]  
        public void TestCardCount()
        {
            var expected = 20;
            var actual = board.CardCount;

            Assert.Equal(expected, actual);  
        }

        [Fact]  
        public void TestPairCount()
        {
            var expected = 10;
            var actual = board.PairCount;

            Assert.Equal(expected, actual);  
        }

        [Fact]  
        public void TestGenerateListOfPairs()
        {
            var expected = new ArrayList(){1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10};
            board.GenerateListOfPairs();
            var actual = board.listOfPairs;

            Assert.Equal(expected, actual);  
        }



        [Theory]  
        [InlineData(0, 0, CardState.OPENED)]
        [InlineData(0, 1, CardState.OPENED)]
        [InlineData(0, 2, CardState.OPENED)]
        [InlineData(0, 3, CardState.OPENED)]
        [InlineData(1, 0, CardState.OPENED)]
        [InlineData(1, 1, CardState.OPENED)]
        [InlineData(1, 2, CardState.OPENED)]
        [InlineData(1, 3, CardState.OPENED)]
        [InlineData(2, 0, CardState.OPENED)]
        [InlineData(2, 1, CardState.OPENED)]
        [InlineData(2, 2, CardState.OPENED)]
        [InlineData(2, 3, CardState.OPENED)]
        [InlineData(3, 0, CardState.OPENED)]
        [InlineData(3, 1, CardState.OPENED)]
        [InlineData(3, 2, CardState.OPENED)]
        [InlineData(3, 3, CardState.OPENED)]
        [InlineData(4, 0, CardState.OPENED)]
        [InlineData(4, 1, CardState.OPENED)]
        [InlineData(4, 2, CardState.OPENED)]
        [InlineData(4, 3, CardState.OPENED)]

        public void TestFlipCard(int row, int column, CardState expected)
        {
            board.FlipCard(row, column);
            CardState actual = Board.cardsArray[row,column].MyCardState;
            Assert.Equal(expected, actual);  
        }

        [Theory]  
        [InlineData(0, 0, CardState.CLOSED)]
        [InlineData(0, 1, CardState.CLOSED)]
        [InlineData(0, 2, CardState.CLOSED)]
        [InlineData(0, 3, CardState.CLOSED)]
        [InlineData(1, 0, CardState.CLOSED)]
        [InlineData(1, 1, CardState.CLOSED)]
        [InlineData(1, 2, CardState.CLOSED)]
        [InlineData(1, 3, CardState.CLOSED)]
        [InlineData(2, 0, CardState.CLOSED)]
        [InlineData(2, 1, CardState.CLOSED)]
        [InlineData(2, 2, CardState.CLOSED)]
        [InlineData(2, 3, CardState.CLOSED)]
        [InlineData(3, 0, CardState.CLOSED)]
        [InlineData(3, 1, CardState.CLOSED)]
        [InlineData(3, 2, CardState.CLOSED)]
        [InlineData(3, 3, CardState.CLOSED)]
        [InlineData(4, 0, CardState.CLOSED)]
        [InlineData(4, 1, CardState.CLOSED)]
        [InlineData(4, 2, CardState.CLOSED)]
        [InlineData(4, 3, CardState.CLOSED)]
        public void TestCloseCards(int row, int column, CardState expected)
        {            
            board.CloseFlippedCards();
            CardState actual = Board.cardsArray[row,column].MyCardState;
            Assert.Equal(expected, actual); 
        }

        [Theory]  
        [InlineData(0, 0, CardState.PAIRED, 0, 1, CardState.PAIRED)]
        [InlineData(1, 0, CardState.PAIRED, 1, 1, CardState.PAIRED)]
        [InlineData(2, 0, CardState.PAIRED, 2, 1, CardState.PAIRED)]
        [InlineData(3, 0, CardState.PAIRED, 3, 1, CardState.PAIRED)]
        [InlineData(4, 0, CardState.PAIRED, 4, 1, CardState.PAIRED)]
        [InlineData(0, 2, CardState.PAIRED, 0, 3, CardState.PAIRED)]
        [InlineData(1, 2, CardState.PAIRED, 1, 3, CardState.PAIRED)]
        [InlineData(2, 2, CardState.PAIRED, 2, 3, CardState.PAIRED)]
        [InlineData(3, 2, CardState.PAIRED, 3, 3, CardState.PAIRED)]
        [InlineData(4, 2, CardState.PAIRED, 4, 3, CardState.PAIRED)]

        public void TestPairedCards(int row1, int column1, CardState expected1, int row2, int column2, CardState expected2)
        {
            System.Console.SetCursorPosition(0,0);
            Card card1 = Board.cardsArray[row1,column1];        
            Card card2 = Board.cardsArray[row2,column2];
            card1.CardValue = card2.CardValue;
            board.FirstPexesoCard = card1;
            board.SecondPexesoCard = card2;

            board.AreCardsPaired();

            CardState actual1 = Board.cardsArray[row1,column1].MyCardState;
            CardState actual2 = Board.cardsArray[row2,column2].MyCardState;

            Assert.Equal(expected1, actual1); 
            Assert.Equal(expected2, actual2); 

        }

        [Fact]
        public void TestGameStateSolved()
        {
            for (var row = 0; row < board.RowCount; row++)
            {
                for (var column = 0; column < board.ColumnCount; column++)
                {
                    Board.cardsArray[row, column].MyCardState = CardState.PAIRED;
                }
            }

            board.IsPexesoSolved();

            var expected = GameState.SOLVED;
            var actual = board.MyGameState;

            Assert.Equal(expected, actual); 
        }

        [Fact]
        public void TestGameStatePlaying()
        {
            board.FlipCard(0, 2);
                        
            board.IsPexesoSolved();

            var expected = GameState.PLAYING;
            var actual = board.MyGameState;

            Assert.Equal(expected, actual); 
        }

        [Fact]
        public void TestGameStateInitiating()
        {
            var expected = GameState.INITIATING;

            board.GenerateListOfPairs();

            var actual = board.MyGameState;
           
            Assert.Equal(expected, actual); 
       }

       [Theory]
       [InlineData(3,3,2)]
       [InlineData(3,1,2)]
       [InlineData(5,5,2)]
       [InlineData(1,9,2)]
       public void TestGameStateIncorrect(int row, int column, int players)
       {
           Board board1 = new Board(row, column, players);
           
           var expected = GameState.INCORRECT;
           var actual = board1.MyGameState;

           Assert.Equal(expected, actual); 
       }
       

    }         
}
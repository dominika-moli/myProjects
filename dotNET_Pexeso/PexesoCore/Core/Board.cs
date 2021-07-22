using System;
using System.Collections;

namespace PexesoGame.Core
{
    public enum GameState { INITIATING, PLAYING, SOLVED, INCORRECT }

    [Serializable]
    public class Board
    {
        public static Card[,] cardsArray { get; set; }
        public virtual Card[,] CardsArray => cardsArray;
        public ArrayList listOfPairs { get; private set; } = new ArrayList();
        public int RowCount { get; private set; }
        public int ColumnCount { get; private set; }
        public int PlayerCount { get; private set; }
        public int CardCount { get; private set; }
        public int PairCount { get; private set; }
        public Card FirstPexesoCard { get; set; }
        public Card SecondPexesoCard { get; set; }
        public GameState MyGameState { get; set; }
        private DateTime startTime;
        public int Hints { get; set; }
        public int Score { get; set; }
        public string Level { get; set; }
        public string Name { get; set; }

        public Board(int columnCount, int rowCount, int playerCount)
        {
            MyGameState = GameState.INITIATING;
            RowCount = rowCount;
            ColumnCount = columnCount;
            PlayerCount = playerCount;
            CardCount = RowCount * ColumnCount;
            PairCount = CardCount / 2;
            cardsArray = new Card[RowCount, ColumnCount];
            FirstPexesoCard = null;
            SecondPexesoCard = null;
            GenerateListOfPairs();
            InitializeGame();
            startTime = DateTime.Now;
            Score = 1;
            Hints = 3;

        }

        public void InitializeGame()
        {
            if (MyGameState == GameState.INCORRECT)
            {
                return;
            }
            else
            {
                ShuffleCards();
            }
        }

        public void GenerateListOfPairs()
        {
            MyGameState = GameState.INITIATING;

            if (CardCount % 2 != 0 || RowCount == 0 || ColumnCount == 0)
            {
                MyGameState = GameState.INCORRECT;
                return;
            }

            for (var i = 1; i <= PairCount; i++)
            {
                listOfPairs.Add(i);
                listOfPairs.Add(i);

            }
        }

        public void ShuffleCards()
        {
            int Number;

            MyGameState = GameState.PLAYING;

            Random random = new Random();
            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {
                    if (listOfPairs.Count != 0)
                    {
                        Number = random.Next(listOfPairs.Count);
                        cardsArray[row, column] = new Card();
                        cardsArray[row, column].CardValue = ((int)listOfPairs[Number]);
                        listOfPairs.RemoveAt(Number);
                    }
                }
            }
        }

        public void FlipCard(int row, int column)
        {

            Card Card = cardsArray[row, column];

            if (row < RowCount && column < ColumnCount)
            {
                if (Card.MyCardState == CardState.OPENED || Card.MyCardState == CardState.PAIRED)
                {
                    Console.ForegroundColor = ConsoleColor.Magenta;
                    System.Console.WriteLine("This card is already opened/paired!");
                    Console.ForegroundColor = ConsoleColor.White;

                    return;
                }
                if (Card.MyCardState == CardState.CLOSED)
                {
                    if (FirstPexesoCard == null)
                    {
                        Card.MyCardState = CardState.OPENED;
                        FirstPexesoCard = Card;
                        System.Console.WriteLine("First flipped card is: {0}", FirstPexesoCard.CardValue);
                    }
                    else if (SecondPexesoCard == null && Card != FirstPexesoCard)
                    {
                        Card.MyCardState = CardState.OPENED;
                        SecondPexesoCard = Card;
                        System.Console.WriteLine("Second flipped card is: {0}", SecondPexesoCard.CardValue);

                    }
                }
            }

        }

        public void CloseFlippedCards()
        {
            if (FirstPexesoCard == null || SecondPexesoCard == null || FirstPexesoCard.MyCardState == CardState.PAIRED || SecondPexesoCard.MyCardState == CardState.PAIRED)
            {
                return;
            }
            else if (FirstPexesoCard.CardValue == SecondPexesoCard.CardValue)
            {
                FirstPexesoCard.MyCardState = CardState.PAIRED;
                SecondPexesoCard.MyCardState = CardState.PAIRED;
                Console.ForegroundColor = ConsoleColor.Magenta;
                System.Console.WriteLine();
                System.Console.WriteLine("You found a pair! ");
                Console.ForegroundColor = ConsoleColor.White;
                System.Console.WriteLine();

                FirstPexesoCard = null;
                SecondPexesoCard = null;
            }
            else
            {
                FirstPexesoCard.MyCardState = CardState.CLOSED;
                SecondPexesoCard.MyCardState = CardState.CLOSED;
                System.Console.WriteLine("First flipped card is: {0}", FirstPexesoCard.CardValue);
                System.Console.WriteLine(FirstPexesoCard.MyCardState);
                System.Console.WriteLine("Second flipped card is: {0}", SecondPexesoCard.CardValue);
                System.Console.WriteLine(SecondPexesoCard.MyCardState);

                Console.ForegroundColor = ConsoleColor.Magenta;
                System.Console.WriteLine();
                System.Console.WriteLine("You did not find a pair :( ");
                Console.ForegroundColor = ConsoleColor.White;
                System.Console.WriteLine();

                FirstPexesoCard = null;
                SecondPexesoCard = null;
            }
            IsPexesoSolved();
        }

        public bool IsPexesoSolved()
        {
            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {
                    if (cardsArray[row, column] != null && cardsArray[row, column].MyCardState != CardState.PAIRED)
                    {
                        return false;
                    }
                }
            }
            MyGameState = GameState.SOLVED;
            return true;
        }

        public bool IsSolved()
        {
            return (MyGameState == GameState.SOLVED);
        }

        public int GetScore()
        {
            if (Score != 0)
            {
                Score = RowCount * ColumnCount * 100 - (DateTime.Now - startTime).Seconds;
            }
            if (Score <= 0)
            {
                Score = 0;
            }
            return Score;
        }

        public void ResetScore()
        {
            Score = 0;
        }

        public void Solution()
        {
            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {
                    var card = cardsArray[row, column];
                    card.MyCardState = CardState.PAIRED;
                }
            }
            ResetScore();
        }

        public int Hint1()
        {
            Card hint1 = null;

            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {
                    if (cardsArray[row, column].MyCardState != CardState.PAIRED && hint1 == null)
                    {
                        hint1 = cardsArray[row, column];
                        System.Console.WriteLine("First hint card is: {0}", hint1.CardValue);
                        hint1.MyCardState = CardState.PAIRED;
                    }
                }
            }
            return hint1.CardValue;
        }

        public void Hint2()
        {
            if (IsSolved())
            {
                return;
            }
            if (Hints > 0)
            {
                CloseOpenedCard();
                FirstPexesoCard = null;
                SecondPexesoCard = null;

                var searching = Hint1();
                Card hint2 = null;
                for (var row = RowCount - 1; row >= 0; row--)
                {
                    for (var column = ColumnCount - 1; column >= 0; column--)
                    {
                        if (cardsArray[row, column].MyCardState != CardState.PAIRED)
                        {
                            if (hint2 == null && searching == cardsArray[row, column].CardValue)
                            {
                                hint2 = cardsArray[row, column];
                                System.Console.WriteLine("Second hint card is: {0}", hint2.CardValue);
                                hint2.MyCardState = CardState.PAIRED;
                            }
                        }
                    }
                }
                Hints = Hints - 1;

            }
            else
            {
                return;
            }

        }

        public void CloseOpenedCard()
        {
            Card card = null;

            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {
                    if (cardsArray[row, column].MyCardState == CardState.OPENED)
                    {
                        card = cardsArray[row, column];
                        card.MyCardState = CardState.CLOSED;
                    }
                }
            }
        }

        //webik , aby vyhral hru 

        public bool LastPair()
        {
            Card card = null;
            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {
                    if (cardsArray[row, column].MyCardState == CardState.CLOSED)
                    {
                        card = cardsArray[row, column];
                        return false;

                    }
                }
            }
            return true;

        }

        //na webik 
        public void CompareCard()
        {
            Card card1 = null;
            Card card2 = null;

            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {
                    if (cardsArray[row, column].MyCardState == CardState.OPENED)
                    {
                        card1 = cardsArray[row, column];

                    }
                }
            }


            for (var row = RowCount - 1; row >= 0; row--)
            {
                for (var column = ColumnCount - 1; column >= 0; column--)
                {
                    if (cardsArray[row, column].MyCardState == CardState.OPENED)
                    {
                        card2 = cardsArray[row, column];

                    }
                }
            }

            if (card1 == null || card2 == null || card1 == card2 || card1.MyCardState == CardState.PAIRED || card2.MyCardState == CardState.PAIRED)
            {
                return;
            }
            else if (card1.CardValue == card2.CardValue)
            {
                card1.MyCardState = CardState.PAIRED;
                card2.MyCardState = CardState.PAIRED;
                Console.ForegroundColor = ConsoleColor.Magenta;
                System.Console.WriteLine();
                System.Console.WriteLine("You found a pair! ");
                Console.ForegroundColor = ConsoleColor.White;
                System.Console.WriteLine();

                card1 = null;
                card2 = null;
            }
            else
            {
                card1.MyCardState = CardState.CLOSED;
                card2.MyCardState = CardState.CLOSED;
                System.Console.WriteLine("First flipped card is: {0}", card1.CardValue);
                System.Console.WriteLine(card1.MyCardState);
                System.Console.WriteLine("Second flipped card is: {0}", card2.CardValue);
                System.Console.WriteLine(card2.MyCardState);

                Console.ForegroundColor = ConsoleColor.Magenta;
                System.Console.WriteLine();
                System.Console.WriteLine("You did not find a pair :( ");
                Console.ForegroundColor = ConsoleColor.White;
                System.Console.WriteLine();

                card1 = null;
                card2 = null;
            }
            FirstPexesoCard = null;
            SecondPexesoCard = null;
            IsPexesoSolved();
        }


        // zjednodusena metoda bez vypisu kvoli boardtestom na overenie zmeny stavu sparovanych kariet
        public void AreCardsPaired()
        {
            if (FirstPexesoCard == null || SecondPexesoCard == null)
            {
                return;
            }
            else if (FirstPexesoCard.CardValue == SecondPexesoCard.CardValue)
            {
                FirstPexesoCard.MyCardState = CardState.PAIRED;
                SecondPexesoCard.MyCardState = CardState.PAIRED;
                FirstPexesoCard = null;
                SecondPexesoCard = null;
            }
            else
            {
                FirstPexesoCard.MyCardState = CardState.CLOSED;
                SecondPexesoCard.MyCardState = CardState.CLOSED;
                FirstPexesoCard = null;
                SecondPexesoCard = null;
            }
        }

        public void VypisStavyKariet()
        {
            Card card = null;

            for (var row = 0; row < RowCount; row++)
            {
                for (var column = 0; column < ColumnCount; column++)
                {

                    card = cardsArray[row, column];
                    System.Console.WriteLine("Stav karty je : {0}", card.MyCardState);

                }
            }


        }
    }
}
using PexesoGame.Core;
using PexesoGame.Service;
using System;
using PexesoGame.Entity;


namespace PexesoGame.CUI
{
    public class ConsoleUI
    {
       private readonly Board board;
      // private readonly IScoreService scoreService = new ScoreServiceFile();
      private readonly IScoreService scoreService = new ScoreServiceEF();

      // private readonly ICommentService commentService = new CommentServiceFile();
      private readonly ICommentService commentService = new CommentServiceEF();
       //private readonly IRatingService ratingService = new RatingServiceFile();
       private readonly IRatingService ratingService = new RatingServiceEF();
       private string player;
       
        public ConsoleUI()
        {
            //scoreService.ClearScores();
            //commentService.ClearComments();
            //ratingService.ClearRatings();
            Console.Clear();            
            string welcome = "WELCOME TO PEXESO GAME!";
            Console.SetCursorPosition(Console.WindowWidth/2 - welcome.Length/2, Console.CursorTop);
            Console.BackgroundColor = ConsoleColor.DarkBlue; 
            System.Console.Write(welcome);
            Console.ResetColor();
            System.Console.WriteLine();

            System.Console.Write("Player name:  ");
            var player = Console.ReadLine().Trim();
            this.player = player;

            System.Console.Write("HOW MANY ROWS should your pexeso have?  ");
            var input1 = Console.ReadLine().Trim().ToUpper();
            System.Console.Write("HOW MANY COLUMNS should your pexeso have?  ");
            var input2 = Console.ReadLine().Trim().ToUpper();

            System.Console.WriteLine();

            Board board1 = new Board(Int32.Parse(input1),Int32.Parse(input2), 1);
            this.board = board1;
            board.Hints = 1; 
           // var level = 1;           
        }


        public void Play()
        {
            //PrintScores();
            //PrintComments();
            //PrintRatings();

            System.Console.WriteLine();
            if (board.MyGameState == GameState.INCORRECT)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                System.Console.WriteLine("Incorrect number of rows and columns!");
                System.Console.WriteLine();
                Console.ForegroundColor = ConsoleColor.White;
                return;
            }
            else 
            {
                 string play = "PLAY!";
                 Console.SetCursorPosition(Console.WindowWidth/2 - play.Length/2, Console.CursorTop);
                 Console.BackgroundColor = ConsoleColor.DarkBlue; 
                 System.Console.Write(play);
                 Console.ResetColor();
                 System.Console.WriteLine(); 
                 System.Console.WriteLine();

                do
                {
                    PrintField();
                    ProcessInput();                                                                          
                } while (!board.IsSolved());

                 scoreService.AddScore(new Score{Player = player, Points = board.GetScore()});          
           
                PrintField();
                System.Console.WriteLine();

            string winner = "YOU WON!";
            Console.SetCursorPosition(Console.WindowWidth/2 - winner.Length/2, Console.CursorTop);
            Console.BackgroundColor = ConsoleColor.DarkBlue; 
            System.Console.Write(winner);
            Console.ResetColor();
            System.Console.WriteLine(); 

            PrintScores();
            LeaveComment();
            PrintComments();
            LeaveRating();
            PrintRatings();                                
          }
        }

        public void LeaveComment()
        {
            Console.BackgroundColor = ConsoleColor.DarkBlue;
            System.Console.Write("Leave a comment: ");
            Console.ResetColor();
            System.Console.WriteLine();
            var inputComment = Console.ReadLine();
            if  (inputComment.Length == 0)
             {
                 Console.ForegroundColor = ConsoleColor.Red;
                 Console.WriteLine("Bad input!");
                 Console.ForegroundColor = ConsoleColor.White;
                 System.Console.WriteLine();
                 LeaveComment();
             }
             else
             {
                 commentService.AddComment(new Comment{Author = player, Message = inputComment});

             }
        }

        public void LeaveRating()
        {
            Console.BackgroundColor = ConsoleColor.DarkBlue;
            System.Console.Write("Rate the game: ");
            Console.ResetColor();
            System.Console.WriteLine();
            var inputRating = Console.ReadLine();
            if  (inputRating.Length == 0)
             {
                 Console.ForegroundColor = ConsoleColor.Red;
                 Console.WriteLine("Bad input!");
                 Console.ForegroundColor = ConsoleColor.White;
                 System.Console.WriteLine();
                 LeaveRating();
             }
             else
             {
                 var numberOfStars = 0;
                 numberOfStars = Int32.Parse(inputRating);
                 if (numberOfStars > 5 || numberOfStars < 1 )
                 {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine("Bad input!");
                    Console.ForegroundColor = ConsoleColor.White;
                    System.Console.WriteLine();
                    LeaveRating();
                 } else
                 {
                      ratingService.AddRating(new Rating{Player = player, NumberOfStars = numberOfStars});
                 }
            }
            System.Console.WriteLine();
        }

         private void ProcessInput()
        {       
            if (board.IsPexesoSolved() == true) 
            {
                return;
            }    
            Console.Write("Enter a ROW of a card you wish to open, starting from zero: ");
            var input1 = Console.ReadLine().Trim().ToUpper();

             if(input1 == "S" || input1 == "s")
            {                
                ShowSolution();
                return;
            } 

            if(input1 == "H" || input1 == "h") 
                {                   
                    FindPair();
                    return;
                }   

            Console.Write("Enter a COLUMN of a card you wish to open, starting from zero: ");
            var input2 = Console.ReadLine().Trim().ToUpper();
            System.Console.WriteLine();

            if(input1 == "X" || input2 == "X") 
                {
                    Environment.Exit(0);
                }
           
            try
            {
                board.FlipCard(Int32.Parse(input1),Int32.Parse(input2)); 
                if (board.FirstPexesoCard != null && board.SecondPexesoCard != null)
                {
                     PrintField();
                     board.CloseFlippedCards();                     
                }               
                board.CloseFlippedCards(); 
                
            }   
            catch (Exception)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Bad input!");
                Console.ForegroundColor = ConsoleColor.White;
                System.Console.WriteLine();
            }            
        }
       
        private void PrintField()
        {
            for (var row = 0; row < board.RowCount; row++)
            {
                Console.SetCursorPosition(Console.WindowWidth/2 - board.ColumnCount/2, Console.CursorTop);
                for (var column = 0; column < board.ColumnCount; column++)
                {                   
                   Console.Write(" ");

                    if (Board.cardsArray[row,column].MyCardState == CardState.PAIRED)
                       {
                           Console.ForegroundColor = ConsoleColor.DarkGreen;
                           Console.Write("{0,4}","P");
                           Console.ForegroundColor = ConsoleColor.White;
                       } 
                    else if (Board.cardsArray[row,column].MyCardState == CardState.OPENED) 
                    {
                        Console.ForegroundColor = ConsoleColor.Cyan;
                        Console.Write("{0,4}", Board.cardsArray[row,column].CardValue);
                        Console.ForegroundColor = ConsoleColor.White;
                    }
                    else {                    
                        Console.Write("{0,4}","C");                        
                    }
                }
                Console.WriteLine();
            }
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.DarkGray;
            Console.WriteLine("------------------------------------------------------------------------------------------------------------");    
            Console.ForegroundColor = ConsoleColor.White;           
        }
       
        private void PrintScores()
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("\tTOP SCORES:");
            Console.WriteLine("---------------------------");
            Console.WriteLine("Player     | Points");
            Console.WriteLine("---------------------------");
            Console.ForegroundColor = ConsoleColor.White;
            foreach (var score in scoreService.GetTopScores())
            {
                Console.WriteLine(String.Format("{0,-10} | {1,6}", score.Player, score.Points));              
               
            }
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("---------------------------");
            System.Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.White;
        }

        private void PrintComments()
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("\tCOMMENTS:");
            Console.WriteLine("------------------------------------");
            Console.WriteLine("Author     | Comment");
            Console.WriteLine("-----------------------------------");
            Console.ForegroundColor = ConsoleColor.White;
            foreach (var comment in commentService.GetComments())
            {
                Console.WriteLine(String.Format("{0,-10} | {1,20}", comment.Author, comment.Message));              
               
            }
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("-----------------------------------");
            System.Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.White;
        }

        private void PrintRatings()
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("\tRATINGS:");
            Console.WriteLine("---------------------------");
            Console.WriteLine("Player     | Rating");
            Console.WriteLine("---------------------------");
            Console.ForegroundColor = ConsoleColor.White;
            foreach (var rating in ratingService.GetRatings())
            {
                var stars = "";
                var pocet = rating.NumberOfStars;
                
            switch (pocet)
            {
                case 1:
                    stars ="*";
                    break;
                case 2:
                    stars ="**";
                    break;
                case 3:
                    stars = "***";
                    break;
                case 4:
                    stars ="****";
                    break;
                default:
                    stars ="*****"; 
                    break;            

            }
                Console.WriteLine(String.Format("{0,-10} | {1,6}", rating.Player, stars));            
                    
            }
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("---------------------------");
            System.Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Magenta;
            System.Console.WriteLine("Average rating is:  {0:N2}", ratingService.GetAverageRating());
            Console.ForegroundColor = ConsoleColor.White;
            System.Console.WriteLine();
        }
        public void ShowSolution()
        {
            System.Console.WriteLine();
            Console.BackgroundColor = ConsoleColor.DarkGray;
            System.Console.Write("Solution ");
            Console.ResetColor();          
            board.Solution();
            PrintSolution();
        }   

        public void PrintSolution()
        {
            for (var row = 0; row < board.RowCount; row++)
            {
                Console.SetCursorPosition(Console.WindowWidth/2 - board.ColumnCount/2, Console.CursorTop);
                for (var column = 0; column < board.ColumnCount; column++)
                {
                   var cardVal = Board.cardsArray[row,column].CardValue;                  
                   Console.Write(" ");                   
                   Console.Write("{0,4}", cardVal);                    
                }
                Console.WriteLine();
            } 
             Console.ForegroundColor = ConsoleColor.DarkGray;
             Console.WriteLine("------------------------------------------------------------------------------------------------------------");    
             Console.ForegroundColor = ConsoleColor.White;            
        }  

        public void FindPair()
        {   
            if (board.Hints >0 && board.MyGameState == GameState.PLAYING) 
            {   
                Console.BackgroundColor = ConsoleColor.DarkGray;
                System.Console.Write("Hint!");
                Console.ResetColor();      
                 System.Console.WriteLine(); 
                board.Hint2();             
                board.Hints = board.Hints-1;
            }
            else 
            {                
                Console.ForegroundColor = ConsoleColor.Magenta;
                System.Console.WriteLine("You have already used all your hints");
                Console.ForegroundColor = ConsoleColor.White;                
            }      
        }
    }
}
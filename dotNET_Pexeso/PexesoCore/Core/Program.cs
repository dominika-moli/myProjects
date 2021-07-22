﻿using System;
using PexesoGame.Core;

namespace PexesoGame.Core
{
    class Program
    {
        static void Main(string[] args)
        {
            Board board = new Board(4,4,3);
            board.FlipCard(0,0);            
            board.FlipCard(0,1);
            
            board.FlipCard(1,0); 
            board.FlipCard(1,1); 

            board.FlipCard(0,0); 
            board.FlipCard(1,0); 

            board.FlipCard(0,0); 
            board.FlipCard(1,1); 


            System.Console.WriteLine(board.MyGameState);  
                                                     
        }
    }
}

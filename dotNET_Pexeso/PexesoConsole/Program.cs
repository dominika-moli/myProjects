﻿﻿using System;
using PexesoGame.Core;
using PexesoGame.CUI;

namespace PexesoGame.CUI
{
    class Program
    {
        static void Main(string[] args)
        {
            var ui = new ConsoleUI();
            ui.Play();
            Console.ReadKey();
                                                     
        }
    }
}
using System;

namespace PexesoGame.Core 
{
    public enum CardState { OPENED, CLOSED, PAIRED }

    [Serializable]
    public class Card 
    {    
        public CardState MyCardState { get; set; }
        public int CardValue{ get; set; }

        public Card() 
        {
            MyCardState = CardState.CLOSED;
            CardValue = 0;
                       
        }
    }
}